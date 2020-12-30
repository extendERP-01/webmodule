package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.ActivitylogModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemRequisitionModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryRequisitionModel;

/*
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryRequisitionController {

	Logger logger = LoggerFactory.getLogger(InventoryRequisitionController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("view-requisition")
	public String generateInventoryStockReport(Model model, HttpSession session) {

		logger.info("Method : generateInventoryStockReport starts");

		/**
		 * get DropDown value for Requisition Type
		 *
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getInventoryUrl() + "get-requisition-type",
					DropDownModel[].class);
			List<DropDownModel> requisitionTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("requisitionTypeList", requisitionTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(env.getInventoryUrl() + "get-requisition-priority", DropDownModel[].class);
			List<DropDownModel> requisitionPrioList = Arrays.asList(dropDownModel);
			model.addAttribute("requisitionPrioList", requisitionPrioList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			InventoryRequisitionModel[] inventoryStockModel = restTemplate.getForObject(
					env.getInventoryUrl() + "get-requisition-item-list", InventoryRequisitionModel[].class);
			List<InventoryRequisitionModel> productList = Arrays.asList(inventoryStockModel);
			model.addAttribute("productList", productList);
			System.out.println("productList " + productList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateInventoryStockReport ends");
		return "inventory/view-requisition";

	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@GetMapping(value = { "view-requisition-item-trough-ajax" })
	public @ResponseBody List<InventoryRequisitionModel> viewStockThroughAjax(Model model, HttpServletRequest request,
			HttpSession session) {
		logger.info("Method : viewStockThroughAjax starts");
		JsonResponse<List<InventoryRequisitionModel>> jsonResponse = new JsonResponse<List<InventoryRequisitionModel>>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {

			InventoryRequisitionModel[] inventoryStockModel = restTemplate.getForObject(
					env.getInventoryUrl() + "get-requisition-item-list", InventoryRequisitionModel[].class);
			List<InventoryRequisitionModel> productList = Arrays.asList(inventoryStockModel);

			jsonResponse.setBody(productList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewStockThroughAjax ends");
		return jsonResponse.getBody();
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@GetMapping(value = { "view-requisition-activity-log" })
	public @ResponseBody List<ActivitylogModel> getActivityLog(@RequestParam String id) {
		logger.info("Method : viewStockThroughAjax starts");
		JsonResponse<List<ActivitylogModel>> jsonResponse = new JsonResponse<List<ActivitylogModel>>();

		try {

			ActivitylogModel[] activityLog = restTemplate
					.getForObject(env.getInventoryUrl() + "get-activity-log?id=" + id, ActivitylogModel[].class);
			List<ActivitylogModel> activityLogList = Arrays.asList(activityLog);

			jsonResponse.setBody(activityLogList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewStockThroughAjax ends");
		return jsonResponse.getBody();
	}

	/*
	 * post Mapping for add ItemRequisition
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-requisition-save-th-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveItemRequisition(
			@RequestBody List<InventoryRequisitionModel> inventoryItemRequisitionModel,  
			HttpSession session) {
		logger.info("Method : saveItemRequisition function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		} 
		/*
		 * for (InventoryRequisitionModel m : inventoryItemRequisitionModel) {
		 * m.setCreatedBy(userId); }
		 */
		try {

			res = restTemplate.postForObject(env.getInventoryUrl() + "rest-requisition",
					inventoryItemRequisitionModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveItemRequisition function Ends");
		return res;
	}
}
