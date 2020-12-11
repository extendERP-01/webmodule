package nirmalya.aathithya.webmodule.account.controller;

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
import nirmalya.aathithya.webmodule.account.model.TdsMasterModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account/")
public class TdsMasterController {
	Logger logger = LoggerFactory.getLogger(TdsMasterController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Add TDS
	 */
	@GetMapping("/add-tds-master")
	public String addTdsMaster(Model model, HttpSession session) {
		logger.info("Method : addTdsMaster start");
		TdsMasterModel tdsMasterModel = new TdsMasterModel();
		TdsMasterModel form = (TdsMasterModel) session.getAttribute("sTdsMasterModel");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("tdsMasterModel", form);
			session.setAttribute("sTdsMasterModel", null);

		} else {
			model.addAttribute("tdsMasterModel", tdsMasterModel);
		}

		logger.info("Method : addTdsMaster end");
		return "account/addTdsMaster";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-tds-master")
	public String postTdsMaster(@ModelAttribute TdsMasterModel tdsMasterModel, Model model, HttpSession session) {
		logger.info("Method : postTdsMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = null;
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			tdsMasterModel.setCreatedBy(userId);
			resp = restClient.postForObject(env.getAccountUrl() + "addTdsMaster", tdsMasterModel, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sTdsMasterModel", tdsMasterModel);
			return "redirect:/account/add-tds-master";
		}
		session.setAttribute("sTdsMasterModel", null);
		logger.info("Method : postTdsMaster end");
		return "redirect:/account/view-tds-master";
	}

	@GetMapping("/view-tds-master")
	public String viewTdsMaster(Model model) {
		logger.info("Method : viewTdsMaster starts");

		logger.info("Method : viewTdsMaster end");
		return "account/viewTdsMaster";

	}

	/**
	 * get mapping for view finance YEAr through ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-tds-master-throughajax")
	public @ResponseBody DataTableResponse viewTdsMasterthroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewTdsMasterthroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<TdsMasterModel>> jsonResponse = new JsonResponse<List<TdsMasterModel>>();
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTdsMaster", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<TdsMasterModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<TdsMasterModel>>() {
					});

			String s = "";
			for (TdsMasterModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getTdsId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId)
						+ "\")'><i class='fa fa-search ' style='font-size:14px;color:#0095c6;'></i></a>";
				s = s + " &nbsp;<a href='view-tds-master-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style='font-size:14px;color:#0095c6;'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getTdsStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
			// System.out.println(x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewTdsMasterthroughAjax end");
		return response;
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-tds-master-edit")
	public String editTdsMaster(Model model,  @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editTdsMaster starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		TdsMasterModel tdsMasterModel = new TdsMasterModel();
		JsonResponse<TdsMasterModel> jsonResponse = new JsonResponse<TdsMasterModel>();

		try {
			jsonResponse = restClient.getForObject(env.getAccountUrl() + "getTdsById?id=" + id + "&Action=editTds", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		tdsMasterModel = mapper.convertValue(jsonResponse.getBody(), TdsMasterModel.class);
		session.setAttribute("message", "");
		
		model.addAttribute("tdsMasterModel", tdsMasterModel);

		logger.info("Method : editTdsMaster end");
		return "account/addTdsMaster";
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-tds-master-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(
					env.getAccountUrl() + "getTdsById?id=" + id + "&Action=" + "ModelViewTds",
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
