/**
 * Defines Inventory related method call for Purchase Order
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
import nirmalya.aathithya.webmodule.common.utils.MailService;
import nirmalya.aathithya.webmodule.inventory.filedownload.InventoryPurrchaseOrderExcelModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryCustomerMailModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryDataForDblValModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryPurchaseOrderModel;
import nirmalya.aathithya.webmodule.inventory.model.InventorySearchPurchaseOrderModel;

/**
 * @author NirmalyaLabs
 * 
 *
 */
@Controller
@RequestMapping(value = { "inventory/" })
public class InventoryPurchaseOrderController {
	Logger logger = LoggerFactory.getLogger(InventoryPurchaseOrderController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;
	@Autowired
	MailService mailService;
	/*
	 * GetMApping for Adding new PurchaseOrder
	 *
	 */

	private static final String user_Id = "USER_ID";

	@GetMapping(value = { "add-purchase-order" })
	public String addPurchaseOreder(Model model, HttpSession session) {
		logger.info("Method : addPurchaseOreder starts");
		InventoryPurchaseOrderModel inventoryPurchaseOrderModel = new InventoryPurchaseOrderModel();

		try {
			inventoryPurchaseOrderModel = (InventoryPurchaseOrderModel) session.getAttribute("purchaseOrder");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String userId = "";

		try {
			userId = (String) session.getAttribute(user_Id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/*
		 * dropDown value for item Category in PurchaseOrder
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-pOrder-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] store = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getPOrderStoreList?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		model.addAttribute("inventoryPurchaseOrderModel", inventoryPurchaseOrderModel);
		logger.info("Method : addPurchaseOreder ends");
		return "inventory/addPurchaseOrder";
	}

	/*
	 *
	 * purchase order page for generate pdf according to requisition Number
	 * 
	 */
	@GetMapping("/view-purchase-order-generate-report")
	public String generatePdfReq(Model model, HttpSession session) {

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-pOrder-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generate pdf according to requisition Number starts");

		logger.info("Method : addTableMaster ends");

		return "inventory/viewPurchaseOrderGenPdf";
	}

	/**
	 * get ItemSubcateroy by the onChange of itemCategory selected in addPurchase
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-purchase-order-getItemCategory-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getItemCategory(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : getItemCategory starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-pitemSubCategory?id=" + itemCategory,
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
		logger.info("Method : getItemCategory ends");
		return res;
	}

	/**
	 * get ItemName by the onChange of ItemSubCategory selected in Purchase Order
	 * form add time
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-purchase-order-getItemName-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getpItemName(Model model, @RequestBody String itemSubCategory,
			BindingResult result) {
		logger.info("Method : getItemCategory starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-pItemName?id=" + itemSubCategory, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getItemCategory ends");
		return res;
	}

	/**
	 * get ItemName by the onChange of ItemSubCategory selected in Purchase Order
	 * form edit time
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "edit-purchase-order-getItemName-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getpItemNameEdit(Model model, @RequestBody String itemSubCategory,
			BindingResult result) {
		logger.info("Method : getpItemNameEdit starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-pItemName?id=" + itemSubCategory, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getpItemNameEdit ends");
		return res;
	}

	/**
	 * get VendorName by the onChange of itemCategory selected in Purchase Order
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-purchase-order-getVendorName-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getpVendorName(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : getpVendorName starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-pVendorName?id=" + itemCategory, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getpVendorName ends");
		return res;
	}

	/*
	 * post Mapping for add Purchase Order
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = "add-purchase-order")
	public @ResponseBody JsonResponse<Object> savePurchaseOrder1(
			@RequestBody List<InventoryPurchaseOrderModel> inventoryPurchaseOrderModel, Model model,
			HttpSession session) {
		logger.info("Method : savePurchaseOrder1 function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute(user_Id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (InventoryPurchaseOrderModel i : inventoryPurchaseOrderModel) {

				i.setCreatedBy(createdBy);
			}
			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-addpurchase-order",
					inventoryPurchaseOrderModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();

		}
		ObjectMapper mapper = new ObjectMapper();
		List<InventoryPurchaseOrderModel> purchase = mapper.convertValue(res.getBody(),
				new TypeReference<List<InventoryPurchaseOrderModel>>() {
				});
		try {
			String myEmail = purchase.get(0).getItemCategory();
			String orderNo = purchase.get(0).getPurchaseOrder();
			String vendorName = purchase.get(0).getpODescription();

			/*
			 * mailService.sendEmail(myEmail, "From Nirmalya Labs", "Dear " + vendorName +
			 * " Your Purchase Order has sussessfully received  Your Order Id is  " +
			 * orderNo);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}

		logger.info("Method : savePurchaseOrder1 function Ends");
		return res;
	}

	/*
	 * post Mapping for edit Purchase Order
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = "edit-purchase-order")
	public @ResponseBody JsonResponse<Object> updatePurchaseOrder(
			@RequestBody List<InventoryPurchaseOrderModel> inventoryPurchaseOrderModel, Model model,
			HttpSession session) {
		logger.info("Method : updatePurchaseOrder function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-update-purchase-order",
					inventoryPurchaseOrderModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();

		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : updatePurchaseOrder function Ends");
		return res;
	}

	/*
	 * 
	 * GetMapping For Listing PurchaseOrder
	 * 
	 * 
	 */
	@GetMapping(value = { "view-purchase-order" })
	public String viewPurchase(Model model, HttpSession session) {
		logger.info("Method : viewPurchase starts");

		String userId = "";

		try {
			userId = (String) session.getAttribute(user_Id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			DropDownModel[] store = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getPOrderStoreList?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for PurchaseOrder
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-purchaseOrder", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchaseOrder", purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for vendor
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-vendorList", DropDownModel[].class);
			List<DropDownModel> vendorList = Arrays.asList(dropDownModel);
			model.addAttribute("vendorList", vendorList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		JsonResponse<Object> purchase = new JsonResponse<Object>();
		model.addAttribute("purchase", purchase);
		logger.info("Method : viewPurchase ends");
		return "inventory/viewPurchaseOrder";
	}
	
	@GetMapping(value = { "po-generated-from-rfq" })
	public String poGeneratedFromRFQ(Model model, HttpSession session) {
		logger.info("Method : poGeneratedFromRFQ starts");
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute(user_Id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			DropDownModel[] store = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getPOrderStoreList?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/*
		 * dropDown value for PurchaseOrder
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-purchaseOrder", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchaseOrder", purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for vendor
		 */
		
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-vendorList", DropDownModel[].class);
			List<DropDownModel> vendorList = Arrays.asList(dropDownModel);
			model.addAttribute("vendorList", vendorList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		JsonResponse<Object> purchase = new JsonResponse<Object>();
		model.addAttribute("purchase", purchase);
		logger.info("Method : poGeneratedFromRFQ ends");
		return "inventory/poGeneratedFromRFQ";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-purchase-order-throughAjax" })
	public @ResponseBody DataTableResponse viewPutrchaseOrderListThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, HttpSession session, @RequestParam String param5) {
		logger.info("Method : viewPutrchaseOrderListThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		String userid = "";

		try {
			userid = (String) session.getAttribute(user_Id);
		} catch (Exception e1) {
			e1.printStackTrace();
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
			tableRequest.setUserId(userid);
			JsonResponse<List<InventoryPurchaseOrderModel>> jsonResponse = new JsonResponse<List<InventoryPurchaseOrderModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-purchase-order",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryPurchaseOrderModel> purchase = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryPurchaseOrderModel>>() {
					});
			String s = "";
			for (InventoryPurchaseOrderModel m : purchase) {
				byte[] pId = Base64.getEncoder().encode(m.getPurchaseOrder().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-purchase-order?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp; ";
				s = s + "<a href='javascript:void(0)' onclick='deletePurchaseOrder(\"" + m.getPurchaseOrder()
						+ "\")'><i class='fa fa-trash'></i></a> &nbsp;&nbsp; ";
				s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + new String(pId)
						+ "\")'><i class='fa fa-download'></i></a>";
				m.setAction(s);
				s = "";

				if (m.getpOStatus()) {
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
			response.setData(purchase);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewPutrchaseOrderListThroughAjax ends");
		return response;
	}

	/**
	 * View selected PurchaseOrder in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-purchase-order-model" })
	public @ResponseBody JsonResponse<InventoryPurchaseOrderModel> modelviewForPurchaseOrder(Model model,
			@RequestBody String index, BindingResult result) {
		logger.info("Method : modelviewForPurchaseOrder starts");
		JsonResponse<InventoryPurchaseOrderModel> res = new JsonResponse<InventoryPurchaseOrderModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-purchaseOrder-forModel?id=" + index,
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
		logger.info("Method : modelviewForPurchaseOrder ends");
		return res;
	}

	/*
	 * For delete a purchase order
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("delete-purchase-order-byId")
	public @ResponseBody JsonResponse<Object> deletePurchaseOrder(@RequestBody String id, HttpSession session) {

		logger.info("Method : deletePurchaseOrder starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "delete-purchase-order?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}

		logger.info("Method : deletePurchaseOrder ends");
		return resp;
	}

	/*
	 * EDIT PAGE FOR PURCHASE ORDER
	 */

	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping(value = { "edit-purchase-order" })
	public String editPurchaseOrder(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editPurchaseOrder starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		ObjectMapper mapper = new ObjectMapper();
		String id = (new String(encodeByte));

		String userId = "";

		try {
			userId = (String) session.getAttribute(user_Id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			DropDownModel[] store = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getPOrderStoreList?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/* Start Drop down For Category Name */
		JsonResponse<List<DropDownModel>> respTblMstr3 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> CategoryList = new ArrayList<DropDownModel>();
		try {
			respTblMstr3 = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getCategoryEdit?Action=" + "getCategoryEdit",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr3 = respTblMstr3.getMessage();

		if (messageForTblMstr3 != null || messageForTblMstr3 != "") {
			model.addAttribute("message", messageForTblMstr3);
		}

		CategoryList = mapper.convertValue(respTblMstr3.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("CategoryList", CategoryList);
		/* End Drop down For Category Name */

		/* Start Drop down For item Name */
		JsonResponse<List<DropDownModel>> respTblMstr7 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> itemNameList = new ArrayList<DropDownModel>();
		try {
			respTblMstr7 = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getItemList?category=" + "getItemList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr7 = respTblMstr7.getMessage();

		if (messageForTblMstr7 != null || messageForTblMstr7 != "") {
			model.addAttribute("message", messageForTblMstr7);
		}

		itemNameList = mapper.convertValue(respTblMstr7.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("itemNameList", itemNameList);
		/* End Drop down For sub item Name */

		try {

			InventoryPurchaseOrderModel[] inventoryPurchaseOrderModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "edit-purchase-order-byId?id=" + id,
					InventoryPurchaseOrderModel[].class);
			List<InventoryPurchaseOrderModel> purchaseList = Arrays.asList(inventoryPurchaseOrderModel);

			/* Requisition table for edit purchase order */
			JsonResponse<List<InventorySearchPurchaseOrderModel>> requistionwithid = new JsonResponse<List<InventorySearchPurchaseOrderModel>>();
			List<InventorySearchPurchaseOrderModel> requistionDtls = new ArrayList<InventorySearchPurchaseOrderModel>();

			String messageForReqwithid = requistionwithid.getMessage();

			if (messageForReqwithid != null || messageForReqwithid != "") {
				model.addAttribute("message", messageForReqwithid);
			}

			ObjectMapper mapper7 = new ObjectMapper();

			requistionDtls = mapper7.convertValue(requistionwithid.getBody(),
					new TypeReference<List<InventorySearchPurchaseOrderModel>>() {
					});

			model.addAttribute("requistionDtls", requistionDtls);

			// dropDown value for itemCategory

			try {
				DropDownModel[] dropDownModel = restTemplate.getForObject(
						environmentVaribles.getInventoryUrl() + "get-pOrder-itemCategory", DropDownModel[].class);
				List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
				model.addAttribute("itemCategoryList", itemCategoryList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				int i = 1;
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> vendorLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();
				for (InventoryPurchaseOrderModel m : purchaseList) {
					try {
						DropDownModel[] dropDownModel = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
								+ "get-edit-porder-SubCategory?id=" + m.getItemCategory(), DropDownModel[].class);
						List<DropDownModel> subcat = Arrays.asList(dropDownModel);
						subCatLists.add(subcat);
					} catch (RestClientException e) {
						e.printStackTrace();
					}

					// dropDown value for item list

					try {

						DropDownModel[] dropDownModel3 = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
								+ "get-edit-porder-itemName?id=" + m.getItemCategory(), DropDownModel[].class);
						List<DropDownModel> item = Arrays.asList(dropDownModel3);
						itemLists.add(item);// get itemlist
					} catch (RestClientException e) {
						e.printStackTrace();
					}
					model.addAttribute("subCatlists", subCatLists);
					model.addAttribute("itemLists", itemLists);

				}
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (InventoryPurchaseOrderModel m : purchaseList) {

				Double minStock = m.getMinStock();
				if (minStock == null) {
					m.setMinStock(0.0);
					;
				}

				Double totalReq = m.getTotalReq();
				if (totalReq == null) {
					m.setTotalReq(0.0);
				}

				Double totalAvailQnt = m.getAvailQnt();
				if (totalAvailQnt == null) {
					m.setAvailQnt(0.0);
				}
			}
			model.addAttribute("purchase", purchaseList.get(0).getPurchaseOrder());
			model.addAttribute("purchaseList", purchaseList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editPurchaseOrder ends");
		return "inventory/editPurchaseOrder";

	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("download-excel-purchase-order")
	public ModelAndView downloadExcelPurchaseOrder(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3, @RequestParam("param4") String encodedPraram4) {
		logger.info("Method : downloadExcelPurchaseOrder start");
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
					environmentVaribles.getInventoryUrl() + "get-all-purchase-order-excel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryPurchaseOrderModel> inventoryPurchaseOrderModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryPurchaseOrderModel>>() {
					});
			map.put("inventoryPurchaseOrderModel", inventoryPurchaseOrderModel);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("InventoryPurchaseOrderModel -> downloadExcelPurchaseOrder GET", e);
		}
		logger.info("Method : downloadExcelPurchaseOrder ends");
		return new ModelAndView(new InventoryPurrchaseOrderExcelModel(), map);
	}

	/**
	 * get Min Stock by the onChange of itemCategory selected in Purchase Order form
	 * add time
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-purchase-order-getMinStock-throughAjax" })
	public @ResponseBody JsonResponse<InventoryDataForDblValModel> getMinStock(@RequestParam String itemId,
			@RequestParam String store) {
		logger.info("Method : getMinStock starts");
		JsonResponse<InventoryDataForDblValModel> res = new JsonResponse<InventoryDataForDblValModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getMinStock?id=" + itemId + "&store=" + store,
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

		logger.info("Method : getMinStock ends");
		return res;
	}

	/**
	 * get Min Stock by the onChange of itemCategory selected in Purchase Order form
	 * edit time
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "edit-purchase-order-getMinStock-throughAjax" })
	public @ResponseBody JsonResponse<InventoryDataForDblValModel> getMinStockEdit(Model model,
			@RequestBody String itemId, BindingResult result) {
		logger.info("Method : getMinStockEdit starts");
		JsonResponse<InventoryDataForDblValModel> res = new JsonResponse<InventoryDataForDblValModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "getMinStock?id=" + itemId,
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

		logger.info("Method : getMinStockEdit ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-purchase-order-get-item" })
	public @ResponseBody JsonResponse<InventorySearchPurchaseOrderModel> getItemAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<InventorySearchPurchaseOrderModel> res = new JsonResponse<InventorySearchPurchaseOrderModel>();

		String[] a = searchValue.split(",");
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
					+ "getItemListByAutoSearchWithCategory?id=" + a[0] + "&vendorId=" + a[1], JsonResponse.class);
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
	 * get Min Stock by the onChange of itemCategory selected in Purchase Order form
	 * add time
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-purchase-order-getTotalPrice-throughAjax" })
	public @ResponseBody JsonResponse<InventoryDataForDblValModel> getTotalPrice(Model model,
			@RequestParam("itemId") String itemId, @RequestParam("vendor") String vendor, HttpSession session) {
		logger.info("Method : getTotalPrice starts");
		JsonResponse<InventoryDataForDblValModel> res = new JsonResponse<InventoryDataForDblValModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getTotalPrice?itemId=" + itemId + "&vendor=" + vendor,
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

		logger.info("Method : getTotalPrice ends");
		return res;
	}

	/*
	 *
	 * GetMapping For Listing PurchaseOrder
	 *
	 *
	 */
	@GetMapping(value = { "approve-purchase" })
	public String approvePurchase(Model model, HttpSession session) {
		logger.info("Method : approvePurchase starts");

		String userId = "";

		try {
			userId = (String) session.getAttribute(user_Id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/*
		 * dropDown value for PurchaseOrder
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-purchaseOrder", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchaseOrder", purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for vendor
		 */

		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "get-vendorList", DropDownModel[].class);
			List<DropDownModel> vendorList = Arrays.asList(dropDownModel);
			model.addAttribute("vendorList", vendorList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] dd = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getPOrderStoreList?id=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		JsonResponse<Object> purchase = new JsonResponse<Object>();
		model.addAttribute("purchase", purchase);
		logger.info("Method : viewPurchase ends");
		return "inventory/approvePurchaseOrder";
	}

	/*
	 * view throughAjax
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "approve-purchase-throughAjax" })
	public @ResponseBody DataTableResponse approvePutrchaseOrderListThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, HttpSession session, @RequestParam String param5) {
		logger.info("Method : approvePutrchaseOrderListThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String userId = (String) session.getAttribute(user_Id);
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
			JsonResponse<List<InventoryPurchaseOrderModel>> jsonResponse = new JsonResponse<List<InventoryPurchaseOrderModel>>();
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-approve-purchase-order", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryPurchaseOrderModel> purchase = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryPurchaseOrderModel>>() {
					});
			String s = "";

			for (InventoryPurchaseOrderModel m : purchase) {
				byte[] pId = Base64.getEncoder().encode(m.getPurchaseOrder().getBytes());
				byte[] vId = Base64.getEncoder().encode(m.getVendor().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='javascript:void' onclick='pdfCreate(\"" + new String(pId)
						+ "\")'><i class='fa fa-download'></i></a>";
				if ((m.getCurrentStageNo() == m.getApproverStageNo()) && (m.getApproveStatus() != 1)) {
					if (m.getApproveStatus() != 3) {
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardPurchase(\""
								+ new String(pId) + "\")'><i class='fa fa-forward'></i></a>";
					} else {
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectPurchase(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a>";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectPurchase(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a>";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectPurchase(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a>";
				}
				if (m.getApproveStatus() == 1) {
					s = s + " &nbsp;&nbsp <a title='Mail' href='javascript:void(0)' onclick='mailSentToVendor(\""
							+ new String(pId) +","+ new String(vId) + "\")'><i class='fa fa-envelope'></i></a>";
				}

				m.setAction(s);
				s = "";

				if (m.getpOStatus()) {
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
			response.setData(purchase);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : approvePutrchaseOrderListThroughAjax ends");
		return response;
	}

	/**
	 * View selected PurchaseOrder in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "approve-purchase-order-model" })
	public @ResponseBody JsonResponse<InventoryPurchaseOrderModel> approveviewForPurchaseOrder(Model model,
			@RequestBody String index, BindingResult result) {
		logger.info("Method : approveviewForPurchaseOrder starts");
		JsonResponse<InventoryPurchaseOrderModel> res = new JsonResponse<InventoryPurchaseOrderModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-purchaseOrder-forModel?id=" + index,
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
		logger.info("Method : approveviewForPurchaseOrder ends");
		return res;
	}

	/*
	 * Forward Requisition to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "approve-purchase-save" })
	public @ResponseBody JsonResponse<Object> savePurchaseApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : savePurchaseApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute(user_Id);
		} catch (Exception e) {

		}
		;
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "forwardPurchase?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : savePurchaseApprovalAction ends");
		return resp;
	}
	/*
	 * Reject Requisition
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "approve-purchase-reject-action" })
	public @ResponseBody JsonResponse<Object> savePurchaseRejectAction(Model model,
			@RequestBody InventoryPurchaseOrderModel reqobject, BindingResult result, HttpSession session) {
		logger.info("Method : savePurchaseRejectAction starts");

		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getPurchaseOrder());
		String reqstnId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute(user_Id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		reqobject.setCreatedBy(userId);
		reqobject.setPurchaseOrder(reqstnId);

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rejectPurchase", reqobject,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : savePurchaseRejectAction ends");
		return res;
	}

	/*
	 * for purchase order auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-purchase-order-get-vendor-auto-complete" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVendorList starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getVendorByAutosearch?id=" + searchValue,
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

		logger.info("Method : getVendorList ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "approve-purchase-sent-mail-to-vendor" })
	public @ResponseBody JsonResponse<Object> sendMailToVendor(Model model,
			@RequestBody String index, BindingResult result) {
		logger.info("Method : sendMailToVendor starts");
		
		List<DropDownModel> form = new ArrayList<DropDownModel>();
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getVendorEMailIdByPO?id=" + index,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			form = mapper.convertValue(res.getBody(), new TypeReference<List<DropDownModel>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (form.get(0).getName() != null && form.get(0).getName() != "") {
			System.out.println("MailId = "+form.get(0).getName());
			try {
				String myEmail = form.get(0).getName();
				String orderNo = index;
				String vendorName = form.get(0).getKey();
				StringBuilder sb = new StringBuilder();
				sb.append("Dear " + vendorName + ",");
				sb.append("\n");
				sb.append("We have sent you Purchase Order - " + orderNo + " ...!!");
				sb.append("\n");
				sb.append("\n");
				sb.append(" Thank You,");
				sb.append("\n");
				mailService.sendEmail(myEmail, "From XYZ", sb.toString());

			} catch (Exception ex) {
				logger.error(ex.getMessage());
				res.setMessage(ex.getMessage());
			}
		} else {
			res.setMessage("Something Went Wrong.");
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : sendMailToVendor ends");
		return res;
	}

}
