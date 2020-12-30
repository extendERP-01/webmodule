package nirmalya.aathithya.webmodule.account.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.FinancialModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account/")
public class FinancialController {
	Logger logger = LoggerFactory.getLogger(FinancialController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Add	Financial Year
	 */
	@GetMapping("/add-financial-master")
	public String addFinancialMaster(Model model, HttpSession session) {
		logger.info("Method : addFinancialMaster start");
		FinancialModel financialModel = new FinancialModel();
		FinancialModel form = (FinancialModel) session.getAttribute("sFinancialModel");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("financialModel", form);
			session.setAttribute("sFinancialModel", null);

		} else {
			model.addAttribute("financialModel", financialModel);
		}
		
		logger.info("Method : addFinancialMaster end");
		return "account/addFinancialMaster";
	}
	/*
	 * post mapping for financial year
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-financial-master")
	public String postFinanceYear(@ModelAttribute FinancialModel financialModel, Model model, HttpSession session) {
		logger.info("Method : postFinanceYear starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			financialModel.setCreatedBy(userId);
			resp = restClient.postForObject(env.getAccountUrl() + "addFiancialYear", financialModel, JsonResponse.class);
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sFinancialModel", financialModel);
			return "redirect:/account/add-financial-master";
		}
		session.setAttribute("sFinancialModel", null);
		logger.info("Method : postFinanceYear end");
		return "redirect:/account/view-financial-master";
	}
	@GetMapping("/view-financial-master")
	public String viewFinanceYear(Model model) {
		logger.info("Method : viewFinanceYear starts");


		logger.info("Method : viewFinanceYear end");
		return "account/viewFinanceYear";

	}
	/**
	 * get mapping for view finance YEAr through ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-financial-master-throughajax")
	public @ResponseBody DataTableResponse viewFinanceYearhroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewFinanceYearhroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<FinancialModel>> jsonResponse = new JsonResponse<List<FinancialModel>>();
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllFinance", tableRequest,
					JsonResponse.class);
		
			ObjectMapper mapper = new ObjectMapper();

			List<FinancialModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<FinancialModel>>() {
					});

			String s = "";
			for (FinancialModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getFinancialYearId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search ' style='font-size:14px;color:#0095c6;'></i></a>";
				s = s + " &nbsp;<a href='view-financial-master-edit?id=" +  new String(pId)
						+ "' ><i class=\"fa fa-edit\" style='font-size:14px;color:#0095c6;'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getFinancialStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
				//System.out.println(x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewFinanceYearhroughAjax end");
		return response;
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-financial-master-edit")
	public String editFinanceYear(Model model,  @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editFinanceYear starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		FinancialModel financialModel = new FinancialModel();
		JsonResponse<FinancialModel> jsonResponse = new JsonResponse<FinancialModel>();

		try {
			jsonResponse = restClient.getForObject(env.getAccountUrl() + "getFinanceYearById?id=" + id + "&Action=editFinance", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		financialModel = mapper.convertValue(jsonResponse.getBody(), FinancialModel.class);
		session.setAttribute("message", "");
		
		model.addAttribute("financialModel", financialModel);

		logger.info("Method : editFinanceYear end");
		return "account/addFinancialMaster";
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-financial-master-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(
					env.getAccountUrl() + "getFinanceYearById?id=" + id + "&Action=" + "ModelViewFinance",
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

		logger.info("Method : modelView end");
		return res;
	}
}
