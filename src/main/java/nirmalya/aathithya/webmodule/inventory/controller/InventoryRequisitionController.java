package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import nirmalya.aathithya.webmodule.common.utils.ActivitylogModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
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
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Requisition Type
		 *
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getInventoryUrl() + "get-cost-center",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(dropDownModel);
			model.addAttribute("costCenterList", costCenterList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Requisition Type
		 *
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getInventoryUrl() + "get-location",
					DropDownModel[].class);
			List<DropDownModel> locationList = Arrays.asList(dropDownModel);
			model.addAttribute("locationList", locationList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Requisition Type
		 *
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getInventoryUrl() + "get-uom",
					DropDownModel[].class);
			List<DropDownModel> uomList = Arrays.asList(dropDownModel);
			model.addAttribute("uomList", uomList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateInventoryStockReport ends");
		return "inventory/view-requisition";

	}

	/*
	 * for copy
	 * 
	 * 
	 */
	@GetMapping(value = { "view-requisition-item-trough-ajax" })
	public @ResponseBody List<InventoryRequisitionModel> viewRequsitionEdit(@RequestParam String id,
			HttpSession session) {
		logger.info("Method : viewRequsitionEdit starts");
		JsonResponse<List<InventoryRequisitionModel>> jsonResponse = new JsonResponse<List<InventoryRequisitionModel>>();

		if (id != null && id != "") {
			try {
				InventoryRequisitionModel[] inventoryStockModel = restTemplate.getForObject(
						env.getInventoryUrl() + "get-requisition-edit?id=" + id, InventoryRequisitionModel[].class);
				List<InventoryRequisitionModel> productList = Arrays.asList(inventoryStockModel);

				jsonResponse.setBody(productList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			jsonResponse.setBody(new ArrayList<InventoryRequisitionModel>());
		}
		logger.info("Method : viewRequsitionEdit ends");
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
	@PostMapping(value = "view-requisition-save-th-ajax")
	public @ResponseBody JsonResponse<Object> saveItemRequisition(
			@RequestBody List<InventoryRequisitionModel> inventoryItemRequisitionModel, HttpSession session) {
		logger.info("Method : saveItemRequisition function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		for (InventoryRequisitionModel m : inventoryItemRequisitionModel) {
			m.setCreatedBy(userId);
		}
		try {

			res = restTemplate.postForObject(env.getInventoryUrl() + "rest-add-requisition",
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

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-requisition-get-product-list" })
	public @ResponseBody JsonResponse<InventoryRequisitionModel> getItemAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<InventoryRequisitionModel> res = new JsonResponse<InventoryRequisitionModel>();

		try {
			res = restTemplate.getForObject(env.getInventoryUrl() + "getProductListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getItemAutoSearchList ends");
		return res;
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@GetMapping(value = { "view-requisition-trough-ajax" })
	public @ResponseBody List<InventoryRequisitionModel> viewRequisitionThroughAjax(HttpSession session) {
		logger.info("Method : viewRequisitionThroughAjax starts");
		JsonResponse<List<InventoryRequisitionModel>> jsonResponse = new JsonResponse<List<InventoryRequisitionModel>>();

		try {

			InventoryRequisitionModel[] inventoryStockModel = restTemplate.getForObject(
					env.getInventoryUrl() + "get-requisition-view-list", InventoryRequisitionModel[].class);
			List<InventoryRequisitionModel> productList = Arrays.asList(inventoryStockModel);

			jsonResponse.setBody(productList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewRequisitionThroughAjax ends");
		return jsonResponse.getBody();
	}

	/*
	 * post Mapping for delete ItemRequisition
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "view-requisition-delete-th-ajax")
	public @ResponseBody JsonResponse<Object> deleteItemRequisition(
			@RequestBody InventoryRequisitionModel inventoryItemRequisitionModel, HttpSession session) {
		logger.info("Method : deleteItemRequisition function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			inventoryItemRequisitionModel.setCreatedBy(userId);
		} catch (Exception e) {

		}
		try {

			res = restTemplate.postForObject(env.getInventoryUrl() + "rest-delete-requisition",
					inventoryItemRequisitionModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : deleteItemRequisition function Ends");
		return res;
	}

	/*
	 * post Mapping for approve ItemRequisition
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "view-requisition-approve-th-ajax")
	public @ResponseBody JsonResponse<Object> approveItemRequisition(
			@RequestBody InventoryRequisitionModel inventoryItemRequisitionModel, HttpSession session) {
		logger.info("Method : approveItemRequisition function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			inventoryItemRequisitionModel.setCreatedBy(userId);
		} catch (Exception e) {

		}
		try {

			res = restTemplate.postForObject(env.getInventoryUrl() + "rest-approve-requisition",
					inventoryItemRequisitionModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : approveItemRequisition function Ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-requisition-get-product-by-req-sku" })
	public @ResponseBody JsonResponse<InventoryRequisitionModel> getProductByReqList(@RequestParam String id,
			@RequestParam String prodId) {
		logger.info("Method : getProductByReqList starts");

		JsonResponse<InventoryRequisitionModel> res = new JsonResponse<InventoryRequisitionModel>();

		try {
			res = restTemplate.getForObject(
					env.getInventoryUrl() + "getProductByReqList?id=" + id + "&prodId=" + prodId, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getProductByReqList ends");
		return res;
	}

}
