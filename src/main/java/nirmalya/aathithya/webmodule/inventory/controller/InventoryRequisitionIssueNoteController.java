/*
*Defines Inventory related method call for RequisitionIssueNote
 * 
 */
package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.filedownload.InventoryIssueNoteExcelModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryRequisitionIssueNoteModel;
import nirmalya.aathithya.webmodule.inventory.model.RequisitionDetailsModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory" })
public class InventoryRequisitionIssueNoteController {
	Logger logger = LoggerFactory.getLogger(InventoryRequisitionIssueNoteController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Adding new Issue Note
	 *
	 */
	@SuppressWarnings({ "unused" })
	@GetMapping(value = { "add-issue-note" })
	public String addIssueNote(Model model, HttpSession session) {
		logger.info("Method : addIssueNote starts");
		InventoryRequisitionIssueNoteModel inventoryRequisitionIssueNoteModel = new InventoryRequisitionIssueNoteModel();
		ObjectMapper mapper = new ObjectMapper();

		try {
			inventoryRequisitionIssueNoteModel = (InventoryRequisitionIssueNoteModel) session
					.getAttribute("requisitionIssueNote");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("inventoryRequisitionIssueNoteModel", inventoryRequisitionIssueNoteModel);
		/*
		 * dropDown value for Godown
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-godown", DropDownModel[].class);
			List<DropDownModel> godownList = Arrays.asList(dropDownModel);
			model.addAttribute("godownList", godownList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addIssueNote ends");
		return "inventory/addIssueNote";
	}

	/*
	 * 
	 * 
	 * Method For Get Requisition Detail
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-issue-note-get-reqList-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> requisitionDetails(Model model,
			@RequestBody String itemRequisition, BindingResult result) {
		logger.info("Method : requisitionDetails starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-all-requisition-details?id=" + itemRequisition,
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
		logger.info("Method : requisitionDetails ends");
		return res;
	}

	/*
	 * 
	 * 
	 * Method For Get Requisition Detail
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-issue-note-getBarCode-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getBarCodeDetails(@RequestParam("item") String item) {
		logger.info("Method : getBarCodeDetails starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-barcode-details?id=" + item,
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
		logger.info("Method : getBarCodeDetails ends");
		return res;
	}

	/*
	 * 
	 * 
	 * Method For Get Available quantity
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-issue-note-getAvilableQuantity-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getAvailableQuantity(@RequestParam("item") String item) {
		logger.info("Method : getAvailableQuantity starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-issueAvailableQuantity?id=" + item,
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
		logger.info("Method : getAvailableQuantity ends");
		return res;
	}

	/*
	 * 
	 * 
	 * Method For Get Issued quantity
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-issue-note-getIssuedQuantity-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getIssuedQuantity(@RequestParam("item") String item,
			@RequestParam("requistionNo") String requistionNo) {
		logger.info("Method : getIssuedQuantity starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-issuedQuantity?id=" + item
					+ "&requistionNo=" + requistionNo, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getIssuedQuantity ends");
		return res;
	}
	/*
	 * 
	 * Method For Get item Category
	 * 
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-issue-note-getItemCategory-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getIssueItemCategory(Model model,
			@RequestBody String requisitionNmuber, BindingResult result) {
		logger.info("Method : getIssueItemCategory starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-issueitemCategory?id=" + requisitionNmuber,
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
		logger.info("Method : getIssueItemCategory ends");
		return res;
	}

	/**
	 * get ItemSubCateroy by the onChange of itemCategory selected in Add Issue Note
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-issue-note-getItemSubCategory-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getIssueItemSubCategory(
			@RequestParam("itemCategory") String itemCategory, @RequestParam("requistionNo") String requistionNo) {
		logger.info("Method : getIssueItemSubCategory starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-issueitemSubCategory?id="
					+ itemCategory + "&reqNo=" + requistionNo, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getIssueItemSubCategory ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-issue-note-requisition-details" })
	public @ResponseBody JsonResponse<RequisitionDetailsModel> getRequisitionDetails(Model model,
			@RequestBody String id, BindingResult result) {
		logger.info("Method : getRequisitionDetails starts");

		JsonResponse<RequisitionDetailsModel> res = new JsonResponse<RequisitionDetailsModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "getRequisitionDetails?id=" + id,
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

		logger.info("Method : getRequisitionDetails ends");
		return res;
	}

	/**
	 * get ItemName by the onChange of ItemSubCategory selected in Add Issue Note
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-issue-note-getItemName-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getIssueItemName(Model model,
			@RequestParam("itemSubCategory") String itemSubCategory,
			@RequestParam("requistionNo") String requistionNo) {
		logger.info("Method : getIssueItemName starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-issueitemName?id="
					+ itemSubCategory + "&reqNo=" + requistionNo, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getIssueItemName ends");
		return res;
	}
	/*
	 * post Mapping for add Issue Note
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-issue-note", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> saveIssueNote(
			@RequestBody List<InventoryRequisitionIssueNoteModel> inventoryRequisitionIssueNoteModel, Model model,
			HttpSession session) {
		logger.info("Method : saveIssueNote function starts");
		System.out.println("inventoryRequisitionIssueNoteModel" + inventoryRequisitionIssueNoteModel);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		String s = "";

		for (InventoryRequisitionIssueNoteModel m : inventoryRequisitionIssueNoteModel) {
			for (String batchCode : m.getBatchCodeList()) {
				s = s + batchCode + ",";
			}
			if (s != "") {
				s = s.substring(0, s.length() - 1);
				m.setBatchCode(s);
			}

			s = "";
		}

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		try {
			inventoryRequisitionIssueNoteModel.get(0).setCreatedBy(userId);
			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-addIssue-note",
					inventoryRequisitionIssueNoteModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveIssueNote function Ends");
		return res;
	}

	/*
	 * post Mapping for Get Issue Auto Complete List
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-issue-note-getIssueAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getIssueAutocompleteList(Model model,
			@RequestBody String searchValue, BindingResult result, HttpSession session) {
		logger.info("Method : getIssueAutocompleteList starts");

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "getIssueListByAutoSearch?id="
					+ searchValue + "&userId=" + userId, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getIssueAutocompleteList ends");
		return res;
	}

	/*
	 * 
	 * GetMapping For Listing Issue Note
	 * 
	 * 
	 */
	@GetMapping(value = { "view-issue-note" })
	public String viewIssueNote(Model model, HttpSession session) {
		logger.info("Method : viewIssueNote starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-store?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dropDownModel);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewIssueNote ends");
		return "inventory/viewIssueNote";
	}

	/*
	 * view issue note throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-issue-note-throughAjax" })
	public @ResponseBody DataTableResponse viewIssueNoteListThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, HttpSession session, @RequestParam String param5) {
		logger.info("Method : viewIssueNoteListThroughAjax starts");
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
			tableRequest.setUserId(userId);

			JsonResponse<List<InventoryRequisitionIssueNoteModel>> jsonResponse = new JsonResponse<List<InventoryRequisitionIssueNoteModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-issue-note",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryRequisitionIssueNoteModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryRequisitionIssueNoteModel>>() {
					});
			String s = "";

			List<String> roles = null;
			try {
				roles = (List<String>) session.getAttribute("USER_ROLES");
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (InventoryRequisitionIssueNoteModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getReqstnIssueNote().getBytes());

				s = "";

				if (roles.contains("rol022") || roles.contains("rol025")) {
					s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;&nbsp;";
					s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + new String(pId)
							+ "\")'><i class='fa fa-download'></i></a>&nbsp;&nbsp;";
					if (!m.getpINoteActive()) {
						s = s + "<a href='javascript:void' onclick='changeRecvStatus(\"" + new String(pId)
								+ "\")'><i class='fa fa-times-circle'></i></a>&nbsp;&nbsp;";
					}
				} else if (roles.contains("rol025")) {
					s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
					s = s + " &nbsp;&nbsp<a href='edit-issue-note?id=" + new String(pId)
							+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp;";
					s = s + "<a href='javascript:void(0)' onclick='deleteIssueNote(\"" + new String(pId)
							+ "\")'><i class='fa fa-trash'></i></a>&nbsp;&nbsp;  ";
					s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + new String(pId)
							+ "\")'><i class='fa fa-download'></i></a>&nbsp;&nbsp;";
				} else {
					s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
					s = s + " &nbsp;&nbsp<a href='edit-issue-note?id=" + new String(pId)
							+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp;";
					s = s + "<a href='javascript:void(0)' onclick='deleteIssueNote(\"" + new String(pId)
							+ "\")'><i class='fa fa-trash'></i></a>&nbsp;&nbsp;  ";
					s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + new String(pId)
							+ "\")'><i class='fa fa-download'></i></a>&nbsp;&nbsp;";
				}

				m.setAction(s);
				s = "";

				if (m.getpINoteActive()) {
					m.setStatus("Received");
				} else {
					m.setStatus("Not Received");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewIssueNoteListThroughAjax ends");
		return response;
	}

	/**
	 * View selected modelviewOfIssueNote
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-issue-note-inModel" })
	public @ResponseBody JsonResponse<InventoryRequisitionIssueNoteModel> modelviewOfIssueNote(Model model,
			@RequestBody() String index, BindingResult result) {
		logger.info("Method : modelviewOfIssueNote starts");

		JsonResponse<InventoryRequisitionIssueNoteModel> res = new JsonResponse<InventoryRequisitionIssueNoteModel>();
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-issueNote-byId?id=" + index,
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
		logger.info("Method : modelviewOfIssueNote ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-issue-note-change-status" })
	public @ResponseBody JsonResponse<Object> changeRequestedItemStatus(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : changeRequestedItemStatus starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "changeRequestedItemStatus?id=" + index,
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
		logger.info("Method : changeRequestedItemStatus ends");
		return res;
	}

	/*
	 * Delete Issue Note
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-issue-note-byId" })
	public @ResponseBody JsonResponse<Object> deleteIssueNote(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : deleteIssueNote starts");
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
					environmentVaribles.getInventoryUrl() + "delete-issue-note?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteIssueNote ends");
		return resp;
	}

	/*
	 * 
	 * 
	 * GetMapping for Edit Issue Note
	 * 
	 * 
	 * 
	 */
	@GetMapping(value = { "edit-issue-note" })
	public String editIssueNote(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editIssueNote starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		try {

			InventoryRequisitionIssueNoteModel[] inventoryRequisitionIssueNoteModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "edit-issue-note-byId?id=" + id,
					InventoryRequisitionIssueNoteModel[].class);
			List<InventoryRequisitionIssueNoteModel> issueList = Arrays.asList(inventoryRequisitionIssueNoteModel);

			try {

				List<Object> avlQuantityLists = new ArrayList<Object>();
				List<Object> selectedBatchLists = new ArrayList<Object>();
				List<Object> availableBatchLists = new ArrayList<Object>();

				List<Object> issueQuantityLists = new ArrayList<Object>();

				for (InventoryRequisitionIssueNoteModel m : issueList) {

					try {
						DropDownModel[] dropDownModel5 = restTemplate
								.getForObject(
										environmentVaribles.getInventoryUrl() + "get-edit-issued-quantity?id="
												+ m.getItem() + "&requistionNo=" + m.getItemRequisition(),
										DropDownModel[].class);
						List<DropDownModel> item = Arrays.asList(dropDownModel5);
						issueQuantityLists.add(item);
					} catch (RestClientException e) {
						e.printStackTrace();
					}
					// Get available quantity
					try {
						DropDownModel[] dropDownModel4 = restTemplate
								.getForObject(
										environmentVaribles.getInventoryUrl() + "get-edit-issue-availableQuantity?id="
												+ m.getItem() + "&issueNo=" + m.getReqstnIssueNote(),
										DropDownModel[].class);
						List<DropDownModel> item = Arrays.asList(dropDownModel4);
						avlQuantityLists.add(item);
					} catch (RestClientException e) {
						e.printStackTrace();
					}

					// Get available batch code
					try {
						DropDownModel[] dropDownModel4 = restTemplate.getForObject(
								environmentVaribles.getInventoryUrl() + "get-edit-issue-getSelected-batchCode?id="
										+ m.getItem() + "&issueNo=" + m.getReqstnIssueNote(),
								DropDownModel[].class);
						List<DropDownModel> item = Arrays.asList(dropDownModel4);
						String selecedValues = "";
						for (DropDownModel items : item) {
							selecedValues = selecedValues + items.getKey() + ",";
						}

						if (selecedValues != "") {
							selecedValues = selecedValues.substring(0, selecedValues.length() - 1);
							m.setBatchCode(selecedValues);
						}

						selectedBatchLists.add(item);
					} catch (RestClientException e) {
						e.printStackTrace();
					}

					// Get selected batch code
					try {
						DropDownModel[] dropDownModel4 = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
								+ "get-edit-issue-getAvail-batchCode?id=" + m.getItem(), DropDownModel[].class);
						List<DropDownModel> item = Arrays.asList(dropDownModel4);
						availableBatchLists.add(item);

					} catch (RestClientException e) {
						e.printStackTrace();
					}

					/*
					 * dropDown value for Godown
					 */
					try {
						DropDownModel[] dropDownModel = restTemplate.getForObject(
								environmentVaribles.getInventoryUrl() + "get-godown", DropDownModel[].class);
						List<DropDownModel> godownList = Arrays.asList(dropDownModel);
						model.addAttribute("godownList", godownList);
					} catch (RestClientException e) {
						e.printStackTrace();
					}

					model.addAttribute("avlQuantityLists", avlQuantityLists);
					model.addAttribute("issueQuantityLists", issueQuantityLists);
					model.addAttribute("selectedBatchLists", selectedBatchLists);
					model.addAttribute("availableBatchLists", availableBatchLists);

				}
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// get Requisition Detail
			try {

				InventoryRequisitionIssueNoteModel[] Requisitiondetails = restTemplate
						.getForObject(
								environmentVaribles.getInventoryUrl() + "requisition-details?id="
										+ issueList.get(0).getItemRequisition(),
								InventoryRequisitionIssueNoteModel[].class);
				List<InventoryRequisitionIssueNoteModel> requisition = Arrays.asList(Requisitiondetails);
				model.addAttribute("requisition", requisition);

			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			model.addAttribute("issue", issueList.get(0).getReqstnIssueNote());
			model.addAttribute("issueList", issueList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editIssueNote ends");
		return "inventory/addIssueNote";
	}
	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-issue-note-download-excel")
	public ModelAndView downloadExcelIssueNote(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3, @RequestParam("param4") String encodedPraram4) {
		logger.info("Method : downloadExcelIssueNote start");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedPraram4.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		try {
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-all-issue-note-excel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryRequisitionIssueNoteModel> inventoryRequisitionIssueNoteModel = mapper.convertValue(
					jsonResponse.getBody(), new TypeReference<List<InventoryRequisitionIssueNoteModel>>() {
					});
			map.put("inventoryRequisitionIssueNoteModel", inventoryRequisitionIssueNoteModel);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("InventoryRequisitionIssueNoteModel -> downloadExcelIssueNote GET", e);
		}
		logger.info("Method : downloadExcelIssueNote ends");
		return new ModelAndView(new InventoryIssueNoteExcelModel(), map);
	}

	/*
	 * Method For PDF
	 * 
	 */
	@GetMapping("/view-issue-note-generate-report")
	public String generateIssueNoteReport(Model model, HttpSession session) {
		logger.info("Method : generateIssueNoteReport starts");
		logger.info("Method : generateIssueNoteReport ends");
		return "inventory/generateIssueNoteReport";
	}

	/*
	 * 
	 * Method for autoComplete of requisition no
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-issue-note-getReqNo" })
	public @ResponseBody JsonResponse<DropDownModel> getReqNoAutoSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getReqNoAutoSearch starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getReqNoAutoSearch?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getReqNoAutoSearch ends");
		return res;
	}

	/*
	 * 
	 * Method for autoComplete of requisition no
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-issue-note-generateReqNo" })
	public @ResponseBody JsonResponse<DropDownModel> geenerateReqNoAutoSearch(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : geenerateReqNoAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "generateReqNoAutoSearchPdf?id=" + searchValue,
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

		logger.info("Method : geenerateReqNoAutoSearch ends");
		return res;
	}

	/*
	 * 
	 * Method for autoComplete of requisition no
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-issue-note-generateIssueNo" })
	public @ResponseBody JsonResponse<DropDownModel> getIssueNo(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getIssueNo starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "generateIssueNoAutoSearchPdf?id=" + searchValue,
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

		logger.info("Method : getIssueNo ends");
		return res;
	}

	/*
	 * 
	 * Method for autoComplete of requisition no for generate pdf report
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-issue-note-getIssueNo" })
	public @ResponseBody JsonResponse<DropDownModel> generateIssueNoForPdf(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : generateIssueNoForPdf starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "generateIssueNoForPdf?id=" + searchValue,
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

		logger.info("Method : generateIssueNoForPdf ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-issue-note-get-item" })
	public @ResponseBody JsonResponse<InventoryRequisitionIssueNoteModel> getItemAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<InventoryRequisitionIssueNoteModel> res = new JsonResponse<InventoryRequisitionIssueNoteModel>();

		String a[] = searchValue.split(",");
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
					+ "getItemListByAutoSearchWithItemReq?id=" + a[0] + "&reqId=" + a[1], JsonResponse.class);
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
	 * post Mapping for Get Store Auto Complete List
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-issue-note-store-autocompleteList" })

	public @ResponseBody JsonResponse<List<DropDownModel>> getStoreAutoCompleteList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getStoreAutoCompleteList starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getStoreAutoCompleteList?id=" + searchValue,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			System.out.println("if block getmsg() not false : " + res.getMessage());
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getStoreAutoCompleteList ends");
		return res;
	}

}
