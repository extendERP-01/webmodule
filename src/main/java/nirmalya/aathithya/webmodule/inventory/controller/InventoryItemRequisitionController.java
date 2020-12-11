/**
 * Defines Inventory related method call for ItemRequisition
 */
package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList;
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemRequisitionModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory/" })
public class InventoryItemRequisitionController {
	Logger logger = LoggerFactory.getLogger(InventoryItemRequisitionController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * 
	 * Get Mapping for add Item Requisition
	 * 
	 */
	@SuppressWarnings({ "unused" })
	@GetMapping(value = { "add-item-requisitions" })
	public String addItemRequisition(Model model, HttpSession session) {
		logger.info("Method : addItemRequisition function starts");

		String userId = (String) session.getAttribute("USER_ID");
		InventoryItemRequisitionModel itemRequisitionModel = null;
		try {
			itemRequisitionModel = (InventoryItemRequisitionModel) session.getAttribute("itemRequisition");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Requisition Type
		 *
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-requisitionType", DropDownModel[].class);
			List<DropDownModel> requisitionTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("RequisitionTypeList", requisitionTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for costCenter
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-cost-center", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(dropDownModel);
			model.addAttribute("costCenterList", costCenterList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for item Type add-items-getSubGroup-throughAjax
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-item-type", DropDownModel[].class);
			List<DropDownModel> itemList = Arrays.asList(dropDownModel);
			model.addAttribute("itemList", itemList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for itemCategory
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-item-category", DropDownModel[].class);
			List<DropDownModel> itemCatList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCatList", itemCatList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Serve Type
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-serveType", DropDownModel[].class);
			List<DropDownModel> serveTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serveTypeList", serveTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Service Type
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-serviceType", DropDownModel[].class);
			List<DropDownModel> serviceTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serviceTypeList", serviceTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Sac Code
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-sacCode", DropDownModel[].class);
			List<DropDownModel> sacList = Arrays.asList(dropDownModel);
			model.addAttribute("sacList", sacList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Store
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-store?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dropDownModel);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		model.addAttribute("itemRequisitionModel", itemRequisitionModel);
		logger.info("Method : addItemRequisition function ends");
		return "inventory/addItemRequisition";
	}

	/*
	 * post Mapping for add ItemRequisition
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-item-requisitions", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveItemRequisition(
			@RequestBody List<InventoryItemRequisitionModel> inventoryItemRequisitionModel, Model model,
			HttpSession session) {
		logger.info("Method : saveItemRequisition function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		for (InventoryItemRequisitionModel m : inventoryItemRequisitionModel) {
			m.setCreatedBy(userId);
		}
		try {

			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-addNew-item-requisition",
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

	/*
	 * 
	 * GetMapping For Listing ItemRequisition pending for apporve by approver
	 * 
	 * 
	 */
	@GetMapping(value = { "view-requisition-approval" })
	public String viewRequisitionApproval(Model model) {
		logger.info("Method : viewRequisitionApproval starts");

		/*
		 * dropDown value for RequisitionType used for search param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-requisitionType", DropDownModel[].class);
			List<DropDownModel> RequisitionTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("RequisitionTypeList", RequisitionTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for costCenter used for search param3
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-cost-center", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(dropDownModel);
			model.addAttribute("costCenterList", costCenterList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		JsonResponse<Object> requisitions = new JsonResponse<Object>();
		model.addAttribute("requisitions", requisitions);
		logger.info("Method : viewRequisitionApproval ends");
		return "inventory/approveItemRequisition";
	}

	/*
	 * view Item throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-requisition-approval-list-throughAjax" })
	public @ResponseBody DataTableResponse viewRequisitionApprovalListThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2,
			@RequestParam String param3, @RequestParam String param4, @RequestParam String param5,
			HttpSession session) {
		logger.info("Method : viewRequisitionApprovalListThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String UserId = (String) session.getAttribute("USER_ID");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			tableRequest.setUserId(UserId);
			JsonResponse<List<InventoryItemRequisitionModel>> jsonResponse = new JsonResponse<List<InventoryItemRequisitionModel>>();
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-all-Item-requsitions-approve", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();

			List<InventoryItemRequisitionModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryItemRequisitionModel>>() {
					});

			String s = "";

			for (InventoryItemRequisitionModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getItemRequisition().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";

				s = s + "&nbsp;&nbsp<a href='javascript:void' onclick='pdfCreate(\"" + new String(pId)
						+ "\")'><i class='fa fa-download'></i></a>";

				if ((m.getCurrentStageNo() == m.getApproverStageNo()) && (m.getApproveStatus() != 1)) {

					if (m.getApproveStatus() != 3) {

						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardRequisition(\""
								+ new String(pId) + "\")'><i class='fa fa-forward'></i></a> &nbsp;&nbsp; ";
					} else {

						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectRequisition(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectRequisition(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a> &nbsp;&nbsp; ";

					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectRequisition(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a> &nbsp;&nbsp; ";
				}

				m.setAction(s);
				s = "";

				if (m.getiRStatus()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}

				if (m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewRequisitionApprovalListThroughAjax ends");
		return response;
	}

	/*
	 * Forward Requisition to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "save-requisition-approval-action" })
	public @ResponseBody JsonResponse<Object> saveRequisitionApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : saveRequisitionApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
					+ "save-requisition-approval-action?id=" + id + "&createdBy=" + userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : saveRequisitionApprovalAction ends");
		return resp;
	}

	/*
	 * Reject Requisition
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "save-requisition-reject-action" })
	public @ResponseBody JsonResponse<Object> saveRequisitionRejectAction(Model model,
			@RequestBody InventoryItemRequisitionModel reqobject, BindingResult result, HttpSession session) {
		logger.info("Method : saveRequisitionRejectAction starts");

		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getItemRequisition());
		String reqstnId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;

		reqobject.setCostCenter(userId);
		reqobject.setItemRequisition(reqstnId);

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "save-requisition-reject-action",
					reqobject, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : saveRequisitionRejectAction ends");
		return res;
	}

	/**
	 * View selected ItemRequisition in Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-requisition-approval-modal" })
	public @ResponseBody JsonResponse<InventoryItemRequisitionModel> modalItemRequisitionApprove(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method : modalItemRequisitionApprove starts");

		JsonResponse<InventoryItemRequisitionModel> res = new JsonResponse<InventoryItemRequisitionModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "getRequisitionById?id=" + index,
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

		logger.info("Method : modalItemRequisitionApprove ends");
		return res;
	}

	/*
	 * 
	 * GetMapping For Listing ItemRequisition
	 * 
	 * 
	 */
	@GetMapping(value = { "view-item-requisitions" })
	public String viewItemRequisitions(Model model, HttpSession session) {
		logger.info("Method : viewItemRequisitions starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		/*
		 * dropDown value for RequisitionType used for search param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-requisitionType", DropDownModel[].class);
			List<DropDownModel> RequisitionTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("RequisitionTypeList", RequisitionTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for costCenter used for search param3
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-cost-center", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(dropDownModel);
			model.addAttribute("costCenterList", costCenterList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value of Serve Type for search Param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-store?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeName = Arrays.asList(dropDownModel);
			model.addAttribute("storeName", storeName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		JsonResponse<Object> requisitions = new JsonResponse<Object>();
		model.addAttribute("requisitions", requisitions);
		logger.info("Method : viewItemRequisitions ends");
		return "inventory/viewItemRequisition";
	}

	/*
	 * view Item throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-item-requisitions-list-throughAjax" })
	public @ResponseBody DataTableResponse viewItemRequisitionListThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, @RequestParam String param5, @RequestParam String param6,
			HttpSession session) {
		logger.info("Method : viewItemRequisitionListThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			tableRequest.setParam6(param6);
			tableRequest.setUserId(userId);

			JsonResponse<List<InventoryItemRequisitionModel>> jsonResponse = new JsonResponse<List<InventoryItemRequisitionModel>>();
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-all-Item-requsitions", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryItemRequisitionModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryItemRequisitionModel>>() {
					});
			String s = "";

			for (InventoryItemRequisitionModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getItemRequisition().getBytes());

				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search'></i></a>";
				s = s + "&nbsp; &nbsp;<a href='edit-item-requisitions?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a>  ";
				s = s + "&nbsp; &nbsp; <a href='javascript:void(0)' onclick='deleteItemRequisition(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a>  ";
				s = s + "&nbsp; &nbsp;<a href='javascript:void' onclick='pdfCreate(\"" + new String(pId)
						+ "\")'><i class='fa fa-download'></i></a>";

				m.setAction(s);

				if (Boolean.TRUE.equals(m.getiRStatus())) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}

				if (m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewItemRequisitionListThroughAjax ends");
		return response;
	}

	/**
	 * View selected ItemRequisition in Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-item-requisitions-model" })
	public @ResponseBody JsonResponse<InventoryItemRequisitionModel> modalItemRequisition(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method : modalItemRequisition starts");

		JsonResponse<InventoryItemRequisitionModel> res = new JsonResponse<InventoryItemRequisitionModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "getRequisitionById?id=" + index,
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

		logger.info("Method : modalItemRequisition ends");
		return res;
	}

	/*
	 * Delete ItemRequisition
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-requisitions-byId" })
	public @ResponseBody JsonResponse<Object> deleteItem(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {
		logger.info("Method : deleteRequisition starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "delete-requisition?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteRequisition ends");
		return resp;
	}
	/*
	 * 
	 * 
	 * GetMapping for Edit ItemRequisition
	 * 
	 * 
	 * 
	 */

	@GetMapping(value = { "edit-item-requisitions" })
	public String editItemRequisition(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editItemRequisition starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));

		/*
		 * dropDown value for RequisitionType
		 */

		try {
			DropDownModel[] requisition = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-requisitionType", DropDownModel[].class);
			List<DropDownModel> RequisitionTypeList = Arrays.asList(requisition);
			model.addAttribute("RequisitionTypeList", RequisitionTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for itemCategory
		 */
		try {
			DropDownModel[] itemCategory = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(itemCategory);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for costCenter
		 */
		try {
			DropDownModel[] costCenter = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-cost-center", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);
			model.addAttribute("costCenterList", costCenterList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}

		/*
		 * dropDown value for Store
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-store?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dropDownModel);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {

			InventoryItemRequisitionModel[] itemRequisitionModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "edit-itemRequisition-byId?id=" + id,
					InventoryItemRequisitionModel[].class);
			List<InventoryItemRequisitionModel> reqList = Arrays.asList(itemRequisitionModel);

			try {
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();
				for (InventoryItemRequisitionModel m : reqList) {
					try {
						DropDownModel[] dropDownModel = restTemplate.getForObject(
								environmentVaribles.getInventoryUrl() + "getSubCategory?id=" + m.getDlItemCategory(),
								DropDownModel[].class);
						List<DropDownModel> subcat = Arrays.asList(dropDownModel);
						subCatLists.add(subcat);
					} catch (RestClientException e) {
						e.printStackTrace();
					}
					try {
						DropDownModel[] dropDownModel1 = restTemplate.getForObject(
								environmentVaribles.getInventoryUrl() + "getItemName?id=" + m.getDlItemSubCategory(),
								DropDownModel[].class);
						List<DropDownModel> item = Arrays.asList(dropDownModel1);
						itemLists.add(item);
					} catch (RestClientException e) {
						e.printStackTrace();
					} // get itemLists
					model.addAttribute("subCatlists", subCatLists);
					model.addAttribute("itemLists", itemLists);

				}
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			model.addAttribute("itemReq", reqList.get(0).getItemRequisition());
			model.addAttribute("itemRequisitionModel", reqList);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editItemRequisition ends");
		return "inventory/addItemRequisition";
	}

	/*
	 * Excel Download
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("download-excel-item-requisitions") public ModelAndView
	 * downloadExcelItemRequisition(HttpServletResponse servResponse,HttpSession
	 * session,@RequestParam("param1") String encodedParam1,@RequestParam("param2")
	 * String encodedParam2,@RequestParam("param3") String
	 * encodedParam3,@RequestParam("param4") String
	 * encodedParam4,@RequestParam("param5") String encodedParam5) {
	 * logger.info("Method : downloadExcelItemRequisition start"); byte[]
	 * encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes()); byte[]
	 * encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes()); byte[]
	 * encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes()); byte[]
	 * encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes()); byte[]
	 * encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes()); String
	 * param1 = (new String(encodeByte1)); String param2 = (new
	 * String(encodeByte2)); String param3 = (new String(encodeByte3)); String
	 * param4 = (new String(encodeByte4)); String param5 = (new
	 * String(encodeByte5));
	 * 
	 * Map<String,Object> map = new HashMap<String,Object>(); JsonResponse<Object>
	 * jsonResponse = new JsonResponse<Object>(); DataTableRequest tableRequest =
	 * new DataTableRequest(); tableRequest.setParam1(param1);
	 * tableRequest.setParam2(param2); tableRequest.setParam3(param3);
	 * tableRequest.setParam4(param4); tableRequest.setParam5(param5); try {
	 * jsonResponse =
	 * restTemplate.postForObject(environmentVaribles.getInventoryUrl()+
	 * "get-all-item-requisition-excel",tableRequest, JsonResponse.class);
	 * ObjectMapper mapper = new ObjectMapper(); List<InventoryItemRequisitionModel>
	 * inventoryItemRequisitionModel = mapper.convertValue(jsonResponse.getBody(),
	 * new TypeReference<List<InventoryItemRequisitionModel>>() { });
	 * map.put("inventoryItemRequisitionModel", inventoryItemRequisitionModel);
	 * servResponse.setContentType("application/ms-excel");
	 * servResponse.setHeader("Content-disposition", "attachment; filename="+new
	 * Date().getTime()+".xls");
	 * 
	 * }catch(Exception e) { e.printStackTrace(); logger.
	 * error("InventoryItemRequisitionModel -> downloadExcelItemRequisition GET"
	 * ,e); } logger.info("Method : downloadExcelItemRequisition ends"); return new
	 * ModelAndView(new InventoryItemRequisitionExcelModel(), map); }
	 */
	/*
	 * Method For PDF
	 * 
	 */
	@GetMapping("/view-item-requisitions-generate-report")
	public String generateItemRequisitionReport(Model model, HttpSession session) {

		logger.info("Method : generateItemRequisitionReport starts");
		/*
		 * dropDown value for RequisitionType used for search param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-requisitionType", DropDownModel[].class);
			List<DropDownModel> RequisitionTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("RequisitionTypeList", RequisitionTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for costCenter used for search param3
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-cost-center", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(dropDownModel);
			model.addAttribute("costCenterList", costCenterList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateItemRequisitionReport ends");
		return "inventory/generateItemRequisitionReport";
	}

	/*
	 * 
	 * Method for autoComplete of requisition Number in view search Param
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-item-requisitions-getReqNo" })
	public @ResponseBody JsonResponse<DropDownModel> getRequisitionNumberList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getRequisitionNumberList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "view-item-req-getRequisitionNo?id=" + searchValue,
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

		logger.info("Method : getRequisitionNumberList ends");
		return res;
	}

	/*
	 * 
	 * Method for autoComplete of requisition Number in generate report
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-item-requisitions-generateReqNo" })
	public @ResponseBody JsonResponse<DropDownModel> generateRequisitionNumberList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : generateRequisitionNumberList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "view-item-req-generateRequisitionNo?id=" + searchValue,
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

		logger.info("Method : generateRequisitionNumberList ends");
		return res;
	}

	/*
	 * 
	 * Method for autoComplete of requisition Number in generate pdf report search
	 * Param
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-item-requisitions-generateRequisitionNumber" })
	public @ResponseBody JsonResponse<DropDownModel> generateRequisitionNumberListPdf(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : generateRequisitionNumberListPdf starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "generateRequisitionNoForPdf?id=" + searchValue,
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

		logger.info("Method : generateRequisitionNumberListPdf ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-item-requisitions-get-item" })
	public @ResponseBody JsonResponse<InventoryItemRequisitionModel> getItemAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<InventoryItemRequisitionModel> res = new JsonResponse<InventoryItemRequisitionModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getItemListByAutoSearch?id=" + searchValue,
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

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-item-requisitions-getSubCategory-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> restGetItemCategory(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : restGetItemCategory starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-item-subCategory?id=" + itemCategory,
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
		logger.info("Method : restGetItemCategory ends");
		return res;
	}

	/*
	 * get Purchase Subgroup by the onChange of AccountSubGroup selected in addItem
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-item-requisitions-getPurchaseSubGroup-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> restGetPurchaseSubGroup(Model model,
			@RequestBody String tAccountGroupType, BindingResult result) {
		logger.info("Method : restGetPurchaseSubGroup starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-purchaseSubGroup?id=" + tAccountGroupType,
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
		logger.info("Method : restGetPurchaseSubGroup ends");
		return res;
	}

	/*
	 * 
	 * get Sales Subgroup by the onChange of AccountSubGroup selected in addItem
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-item-requisitions-getSalesSubGroup-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> restGetSalesSubGroup(Model model,
			@RequestBody String tAccountGroupType, BindingResult result) {
		logger.info("Method : restGetSalesSubGroup starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-salesSubGroup?id=" + tAccountGroupType,
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
		logger.info("Method : restGetSalesSubGroup ends");
		return res;
	}

	/*
	 * post Mapping for add ItemRequisition
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-item-requisitions-addItem", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveItem(@RequestBody InventoryItemModel itemModel, Model model,
			HttpSession session) {
		logger.info("Method : saveItemRequisition function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			itemModel.setCreatedBy(userId);
			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "add-newItem", itemModel,
					JsonResponse.class);
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
