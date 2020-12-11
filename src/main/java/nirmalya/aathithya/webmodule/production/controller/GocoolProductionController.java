package nirmalya.aathithya.webmodule.production.controller;

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
import nirmalya.aathithya.webmodule.common.utils.ResponseConstands;
import nirmalya.aathithya.webmodule.common.utils.StringConstands;
import nirmalya.aathithya.webmodule.production.model.ProductionGoCoolModel;

@Controller
@RequestMapping(value = "production")
public class GocoolProductionController {
	Logger logger = LoggerFactory.getLogger(GocoolProductionController.class);

	private static final String MESSAGE = "message";
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee dependent
	 */

	@GetMapping("/add-production")
	public String addProduction(Model model, HttpSession session) {

		logger.info("Method : addProduction starts");

		ProductionGoCoolModel productionModel = new ProductionGoCoolModel();
		ProductionGoCoolModel sessionProductionModel = (ProductionGoCoolModel) session
				.getAttribute("sessionProductionModel");

		String message = (String) session.getAttribute(MESSAGE);

		if (message != null && !message.isEmpty()) {
			model.addAttribute(MESSAGE, message);

		}

		session.setAttribute(MESSAGE, "");

		if (sessionProductionModel != null) {
			model.addAttribute("productionModel", sessionProductionModel);
			session.setAttribute("productionModel", null);
		} else {
			model.addAttribute("productionModel", productionModel);
		}

		// Drop Down for planning list
		try {
			DropDownModel[] plannings = restClient.getForObject(env.getProduction() + "getProdPlanning",
					DropDownModel[].class);
			List<DropDownModel> planningList = Arrays.asList(plannings);

			model.addAttribute("planningList", planningList);
		} catch (RestClientException e) {
			logger.error(e.getMessage());
		}

		/**
		 * Get DropDown Value Store List
		 *
		 */
		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addProduction ends");
		return "production/add-new-gocool-production";
	}

	/**
	 * DROP DOWN DATA FOR PRODUCTION ITEM ON PLAN ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-production-get-prod-details" })
	public @ResponseBody JsonResponse<ProductionGoCoolModel> getProductionItems(@RequestParam String planId) {
		logger.info("Method : getProductionItems starts");

		JsonResponse<ProductionGoCoolModel> res = new JsonResponse<ProductionGoCoolModel>();

		try {
			res = restClient.getForObject(env.getProduction() + "rest-get-production-items?planId=" + planId,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}

		logger.info("Method : getProductionItems ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-production-get-batch-details" })
	public @ResponseBody JsonResponse<ProductionGoCoolModel> getBatchDetails(@RequestParam String planId,
			@RequestParam String storeId) {
		logger.info("Method : getBatchDetails starts");

		JsonResponse<ProductionGoCoolModel> res = new JsonResponse<ProductionGoCoolModel>();

		try {
			res = restClient.getForObject(
					env.getProduction() + "rest-get-batch-details?planId=" + planId + "&storeId=" + storeId,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}

		logger.info("Method : getBatchDetails ends");
		return res;
	}

	/**
	 * Web controller add new deliveryChalanModel data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-production-ajax")
	public @ResponseBody JsonResponse<Object> addProduction(
			@RequestBody List<ProductionGoCoolModel> productionGoCoolModelList, HttpSession session) {

		logger.info("Method : addProduction starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			String userId = (String) session.getAttribute(StringConstands.USERID);
			for (ProductionGoCoolModel r : productionGoCoolModelList) {
				r.setCreatedBy(userId);

			}

			resp = restClient.postForObject(env.getProduction() + "add-production", productionGoCoolModelList,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {
			resp.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			resp.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : addProduction function Ends");
		return resp;
	}

	/*
	 * Get Mapping view employee dependent
	 */
	@GetMapping("/view-production-details")
	public String viewProductionMix(Model model, HttpSession session) {

		logger.info("Method : viewProductionMix   starts");

		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] items = restClient.getForObject(env.getProduction() + "getProdItemList",
					DropDownModel[].class);
			List<DropDownModel> itemList = Arrays.asList(items);

			model.addAttribute("itemList", itemList);
		} catch (RestClientException e) {
			logger.error(e.getMessage());
		}
		logger.info("Method : viewProductionMix  ends");

		return "production/view-production-details-page";
	}

	/*
	 * For view mother-coil-slit throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-production-details-ThroughAjax")
	public @ResponseBody DataTableResponse viewMotherCoilSlitAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,  @RequestParam String param3) {

		logger.info("Method : viewMotherCoilSlitAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1); 
			tableRequest.setParam3(param3);
			JsonResponse<List<ProductionGoCoolModel>> jsonResponse;

			jsonResponse = restClient.postForObject(env.getProduction() + "view-production-details", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionGoCoolModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionGoCoolModel>>() {
					});

			String s = "";

			for (ProductionGoCoolModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getPlanId().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.getProdId().getBytes());

				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId2)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a> &nbsp; &nbsp; ";

				/*
				 * if (m.getStatus() == 0) { s = s +
				 * "<a href='view-mother-coil-slit-edit?batchid=" + new String(encodeId) +
				 * "&mthick=" + new String(encodeId2) + "&slitbatch=" + new String(encodeId3) +
				 * "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>"; }
				 */
				m.setAction(s);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignEdu);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewMotherCoilSlitAjax ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/view-production-details-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalPipeProductionbMixProduction(Model model,
			@RequestBody ProductionGoCoolModel index, BindingResult result) {

		logger.info("Method :modalPipeProductionMix starts");
		byte[] planId = Base64.getDecoder().decode(index.getPlanId().getBytes());
		byte[] batchId = Base64.getDecoder().decode(index.getBatchId().getBytes());
		String mplanId = (new String(planId));
		String mBatchId = (new String(batchId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(
					env.getProduction() + "view-production-details-by-id?mplanId=" + mplanId + "&prodId=" + mBatchId, // +"pipeSzId="+pipeSzId,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modalPipeProductionMix ends ");
		return response;
	}

	/**
	 * DROP DOWN DATA FOR PRODUCTION ITEM ON PLAN ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-production-get-plannings" })
	public @ResponseBody JsonResponse<DropDownModel> getPlannings(@RequestParam String storeId) {
		logger.info("Method : getPlannings starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getProduction() + "rest-get-plannings?storeId=" + storeId,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}

		logger.info("Method : getPlannings ends");
		return res;
	}
	
	
	/**
	 * DROP DOWN DATA FOR PRODUCTION ITEM ON PLAN ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-production-get-all-plannings" })
	public @ResponseBody JsonResponse<DropDownModel> getPlanningsAll(@RequestParam String storeId) {
		logger.info("Method : getPlannings starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getProduction() + "rest-get-plannings-all?storeId=" + storeId,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}

		logger.info("Method : getPlannings ends");
		return res;
	}
	
}
