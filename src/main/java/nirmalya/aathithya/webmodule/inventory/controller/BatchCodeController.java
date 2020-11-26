package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.BatchCodeModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class BatchCodeController {

Logger logger = LoggerFactory.getLogger(BatchCodeController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	/**
	 * View Default 'Batch Code' page
	 *
	 */
	@GetMapping("/add-batch-code")
	public String defaultBatchCode(Model model, HttpSession session) {
		logger.info("Method : defaultBatchCode starts");
		
		logger.info("Method : defaultBatchCode ends");
		return "inventory/add-batch-code";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-batch-code-get-grn" })
	public @ResponseBody JsonResponse<DropDownModel> getGRNAutoSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGRNAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getGRNForBatchCode?id=" + searchValue,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getGRNAutoSearch ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-batch-code-get-grn-details" })
	public @ResponseBody JsonResponse<BatchCodeModel> getGRNDetails(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGRNDetails starts");

		JsonResponse<BatchCodeModel> res = new JsonResponse<BatchCodeModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getGRNDetailsForBatchCode?id=" + searchValue,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getGRNDetails ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-batch-code-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addNewBatchCode(@RequestBody List<BatchCodeModel> batchCode,
			Model model, HttpSession session) {
		logger.info("Method : addNewBatchCode function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < batchCode.size(); i++) {
				batchCode.get(i).setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getInventoryUrl() + "newBatchCodeSave", batchCode, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addNewBatchCode function Ends");
		return res;
	}
	
	/**
	 * Default 'View Batch Code' page
	 *
	 */
	@GetMapping("/view-batch-code")
	public String viewBatchCode(Model model, HttpSession session) {
		logger.info("Method : viewBatchCode starts");

		logger.info("Method : viewBatchCode ends");
		return "inventory/view-batch-code";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-batch-code-through-ajax")
	public @ResponseBody DataTableResponse viewGatePassThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param4, @RequestParam String param5) {
		logger.info("Method : viewGatePassThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);

			JsonResponse<List<BatchCodeModel>> jsonResponse = new JsonResponse<List<BatchCodeModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getBatchCodeDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<BatchCodeModel> batchCode = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<BatchCodeModel>>() {
					});

			String s = "";

			for (BatchCodeModel m : batchCode) {
				byte[] pId = Base64.getEncoder().encode(m.getGrnId().getBytes());
				
				s = s + "<a href='edit-batch-code?id=" + new String(pId)
					+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;"
					+ " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
					+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(batchCode);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewGatePassThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-batch-code-modal" })
	public @ResponseBody JsonResponse<Object> modalBatchCode(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalBatchCode starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getBatchCodeById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalBatchCode ends");
		return res;
	}
	
	@GetMapping("edit-batch-code")
	public String editBatchCode(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editBatchCode starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			BatchCodeModel[] gatePass = restClient.getForObject(env.getInventoryUrl() + "editBatchCode?id=" + id,
					BatchCodeModel[].class);
			List<BatchCodeModel> batchList = Arrays.asList(gatePass);
			
			model.addAttribute("batchId", batchList.get(0).getBatchId());
			model.addAttribute("batch", batchList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editBatchCode starts");
		return "inventory/add-batch-code";
	}
}
