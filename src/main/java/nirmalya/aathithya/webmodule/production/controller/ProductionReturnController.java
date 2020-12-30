package nirmalya.aathithya.webmodule.production.controller;

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
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReturnsNoteModel;
import nirmalya.aathithya.webmodule.production.model.ProductionGoCoolModel;
import nirmalya.aathithya.webmodule.production.model.ProductionReturnModel;

@Controller
@RequestMapping(value = "production")
public class ProductionReturnController {

	Logger logger = LoggerFactory.getLogger(ProductionReturnController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Add Inventory Goods return note' page
	 *
	 */
	@GetMapping("add-production-goodsreturns")
	public String addInventoryGoodsReturns(Model model, HttpSession session) {
		logger.info("Method : addInventoryGoodsReturn method starts");
		ProductionReturnModel inventoryGoodsReturnsNoteModel = new ProductionReturnModel();
		ProductionReturnModel returnInventory = null;
		try {
			returnInventory = (ProductionReturnModel) session.getAttribute("returnInventories");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (returnInventory != null) {
			model.addAttribute("inventoryGoodsReturnsNoteModel", returnInventory);
		} else {
			model.addAttribute("inventoryGoodsReturnsNoteModel", inventoryGoodsReturnsNoteModel);
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
		logger.info("Method : addInventoryGoodsReturns method ends");
		return "production/production-return-note";
	}

	/*
	 * post Mapping for add Inventory Goods return
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "add-production-goodsreturns")
	public @ResponseBody JsonResponse<Object> postAddGoodsReturn(
			@RequestBody List<ProductionReturnModel> productionReturnModel, Model model, HttpSession session) {
		logger.info("Method : postAddGoodsReturn function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			productionReturnModel.forEach(a -> a.setCreatedBy((String) session.getAttribute(StringConstands.USERID)));
			res = restClient.postForObject(env.getProduction() + "add-return-production", productionReturnModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : postAddGoodsReturn function Ends");
		return res;
	}

	/**
	 * DROP DOWN DATA FOR PRODUCTION ITEM ON PLAN ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-production-goodsreturns-get-plannings" })
	public @ResponseBody JsonResponse<DropDownModel> getPlannings(@RequestParam String storeId) {
		logger.info("Method : getPlannings starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getProduction() + "get-requisition?storeId=" + storeId,
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

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-production-goodsreturns-get-raw-details" })
	public @ResponseBody JsonResponse<ProductionGoCoolModel> getBatchDetails(@RequestParam String planId,
			@RequestParam String storeId) {
		logger.info("Method : getBatchDetails starts");

		JsonResponse<ProductionGoCoolModel> res = new JsonResponse<ProductionGoCoolModel>();

		try {
			res = restClient.getForObject(
					env.getProduction() + "rest-get-raw-details?planId=" + planId + "&storeId=" + storeId,
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

	@GetMapping(value = { "view-production-goodsreturns" })
	public String viewGoodsreturnNote(Model model, HttpSession session) {
		logger.info("Method : viewGoodsreturnNote starts");
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
		/*
		 * dropDown value for invoice number
		 * 
		 * try { DropDownModel[] dropDownModel =
		 * restClient.getForObject(env.getInventoryUrl() + "rest-get-invoice-number",
		 * DropDownModel[].class); List<DropDownModel> invoicenumbr =
		 * Arrays.asList(dropDownModel);
		 * 
		 * model.addAttribute("invoice", invoicenumbr); } catch (RestClientException e)
		 * { e.printStackTrace(); }
		 */
		JsonResponse<Object> goodreturns = new JsonResponse<Object>();
		model.addAttribute("goodreturns", goodreturns);
		logger.info("Method : viewGoodsreturnNote ends");
		return "production/view-production-return-note";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-production-goodsreturns-throughAjax" })
	public @ResponseBody DataTableResponse viewGoodsReturnThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : viewGoodsReturnThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2); 
			JsonResponse<List<ProductionReturnModel>> jsonResponse = null;
			jsonResponse = restClient.postForObject(env.getProduction() + "get-goods-return", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<ProductionReturnModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionReturnModel>>() {
					}); 

			for (ProductionReturnModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getReurnId().getBytes());
				String s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ m.getReurnId() + "\")'><i class='fa fa-search search'></i></a>";
				/*
				 * s = s + " &nbsp;&nbsp <a href='view-production-goodsreturns-edit?id=" + new
				 * String(pId) + "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp; "; s = s +
				 * "<a href='javascript:void(0)'   onclick='deleteReturnnote(\"" + new
				 * String(pId) + "\")'><i class='fa fa-trash'></i></a>&nbsp;&nbsp; ";
				 */
				/*
				 * s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + m.getReurnId() +
				 * "\")'><i class='fa fa-download'></i></a>";
				 */
				m.setAction(s);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewGoodsReturnThroughAjax ends");
		return response;
	}

	/*
	 * Delete ItemRequisition
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-goodsreturn-note" })
	public @ResponseBody JsonResponse<Object> deleteGoodsreturn(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {
		logger.info("Method : deleteGoodsreturn starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		String userId = null;
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.getForObject(
					env.getInventoryUrl() + "delete-goodsreturn?id=" + id + "&createdBy=" + userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteGoodsreturn ends");
		return resp;
	}

	/*
	 * GetMapping for Edit Goods return note	
	 * 
	 */

	@GetMapping(value = { "view-production-goodsreturns-edit" })
	public String editGoodsReturnNote(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editGoodsReturnNote starts");

		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));

		try {
			InventoryGoodsReturnsNoteModel[] inventoryGoodsReturnsNoteModel = restClient.getForObject(
					env.getInventoryUrl() + "edit-goods-return-byId?id=" + id, InventoryGoodsReturnsNoteModel[].class);
			List<InventoryGoodsReturnsNoteModel> invGRN = Arrays.asList(inventoryGoodsReturnsNoteModel);
			String purchase = invGRN.get(0).getPurchaseOrder();
			// for the value of invoice number on change of purchase order in edit

			try {
				DropDownModel[] inv = restClient.getForObject(env.getInventoryUrl() + "restGetINV?id=" + purchase,
						DropDownModel[].class);
				List<DropDownModel> invoiceIdList = Arrays.asList(inv);

				model.addAttribute("invoiceIdList", invoiceIdList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();

				for (InventoryGoodsReturnsNoteModel m : invGRN) {

					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "getinvSubCategory?id=" + m.getItemCategory(),
							DropDownModel[].class);
					List<DropDownModel> subcat = Arrays.asList(dropDownModel);

					subCatLists.add(subcat);

					DropDownModel[] dropDownModel1 = restClient.getForObject(
							env.getInventoryUrl() + "getinvItemId?id=" + m.getItemSubCategory(), DropDownModel[].class);
					List<DropDownModel> itemS = Arrays.asList(dropDownModel1);

					itemLists.add(itemS);// get itemlist

				}

				model.addAttribute("subCatlists", subCatLists);

				model.addAttribute("itemLists", itemLists);
			} catch (RestClientException e) { 
				e.printStackTrace();
			} 
			 
			model.addAttribute("inventoryGoodsReturnNote", invGRN.get(0).getGoodsReturnNote());
			model.addAttribute("inventoryGoodsReturnNoteModel", invGRN);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editGoodsReturnNote ends");
		return "inventory/AddGoodsReturnsNote";
	}

	/**
	 * View selected Goods return in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-production-goodsreturns-modal-view" })
	public @ResponseBody JsonResponse<Object> modelviewGoodsreturn(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewGoodsreturn starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getProduction() + "get-goods-return-modal?id=" + index,
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
		logger.info("Method : modelviewGoodsreturn ends");

		return res;
	}  

	// for report
	@GetMapping("/view-inventory-goodsreturns-generate-report")
	public String generateGoodsreturnnoteReport(Model model, HttpSession session) {

		logger.info("Method : generateGoodsreturnnoteReport starts");
		/*
		 * drop down data for purchase order Number
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-purchase-order",
					DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) { 
			e.printStackTrace();
		}
		/*
		 * dropDown value for invoice number
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-invoice-number",
					DropDownModel[].class);
			List<DropDownModel> invoicenumbr = Arrays.asList(dropDownModel);
			model.addAttribute("invoice", invoicenumbr);
		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		logger.info("Method : generateGoodsreturnnoteReport ends");
		return "inventory/pdfReportGoodsreturn";
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-production-goodsreturns-get-all-quantity-through-ajax" })
	public @ResponseBody JsonResponse<Object> getQuantities(@RequestParam("itemName") String itemName,
			@RequestParam("gRNInvoiceId") String gRNInvoiceId) {
		logger.info("Method : getQuantities starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "get-all-quantities?id=" + itemName + "&gRNInvoiceId=" + gRNInvoiceId,
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
		logger.info("Method : getQuantities ends");
		return res;

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-production-goodsreturns-getPurchaseOrderAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseOrList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getPurchaseOrList starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPurchaseOrderByAutosearch?id=" + searchValue,
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

		logger.info("Method : getPurchaseOrList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-production-goodsreturns-getPurchseNo" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseOrdrList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getPurchaseOrdrList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPurchaseOrderByAutosuggest?id=" + searchValue,
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

		logger.info("Method : getPurchaseOrdrList ends");
		return res;
	}

	/*
	 * 
	 * get method for auto complete of purchase order
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-production-goodsreturns-getpurchaseNos" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseOrdrPdfList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getPurchaseOrdrPdfList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPurchaseOrderByPdfLists?id=" + searchValue,
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

		logger.info("Method : getPurchaseOrdrPdfList ends");
		return res;
	}

	/*
	 * 
	 * get method for auto complete of purchase order for search
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-inventory-goodsreturns-getPurchseNo" })
	public @ResponseBody JsonResponse<DropDownModel> searchPurchaseOrdrList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : searchPurchaseOrdrList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPurchaseOrderSearch?id=" + searchValue,
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

		logger.info("Method : searchPurchaseOrdrList ends");
		return res;
	}

	/*
	 * 
	 * get method for auto complete of invoice
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-production-goodsreturns-getSuggestinvoiceNos" })
	public @ResponseBody JsonResponse<DropDownModel> getInvoiceNumberSuggestList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getInvoiceNumberSuggestList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getInvoiceNumberSuggestList?id=" + searchValue,
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

		logger.info("Method : getInvoiceNumberSuggestList ends");
		return res;
	}

	/*
	 * 
	 * get method for auto complete of invoice for search
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-inventory-goodsreturns-getSuggestioninvoiceNos" })
	public @ResponseBody JsonResponse<DropDownModel> searchInvoiceNumberSuggestList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : searchInvoiceNumberSuggestList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getInvoiceNumberSearch?id=" + searchValue,
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

		logger.info("Method : searchInvoiceNumberSuggestList ends");
		return res;
	}

	// for getting data in autocomplte invoice Id
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-production-goodsreturns-get-grninvoiceid-through-ajax" })
	public @ResponseBody JsonResponse<Object> getInvoiceId(@RequestParam("purchaseOrder") String purchaseOrder,
			@RequestParam("gRNInvoiceId") String gRNInvoiceId) {
		logger.info("Method : getInvoiceId starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restClient.getForObject(
					env.getInventoryUrl() + "get-invoiceId?id=" + purchaseOrder + "&gRNInvoiceId=" + gRNInvoiceId,
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
		logger.info("Method : getInvoiceId ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-production-goodsreturns-getSuggestioninvoiceNos" })
	public @ResponseBody JsonResponse<DropDownModel> getInvoiceNumberSuggestionInvList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getInvoiceNumberSuggestionInvList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getInvoiceNumberSuggestionList?id=" + searchValue,
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

		logger.info("Method : getInvoiceNumberSuggestionInvList ends");
		return res;
	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-production-goodsreturns-get-discount-through-ajax" })
	public @ResponseBody JsonResponse<Object> getDiscounts(@RequestParam("itemId") String itemId,
			@RequestParam("gRNInvoiceId") String gRNInvoiceId) {
		logger.info("Method : getDiscounts starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restClient.getForObject(
					env.getInventoryUrl() + "get-discounts?id=" + itemId + "&gRNInvoiceId=" + gRNInvoiceId,
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

		logger.info("Method : getDiscounts ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-production-goodsreturns-get-gsts-rate-through-ajax" })
	public @ResponseBody JsonResponse<Object> getUnitGst(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : getUnitGst starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-unitGst-goods-return?id=" + itemCategory,
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

		logger.info("Method : getUnitGst ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-production-goodsreturns-get-item" })
	public @ResponseBody JsonResponse<InventoryGoodsReturnsNoteModel> getItemAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<InventoryGoodsReturnsNoteModel> res = new JsonResponse<InventoryGoodsReturnsNoteModel>();

		String a[] = searchValue.split(",");
		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "getItemListByAutoSearchWithGRNINV?id=" + a[0] + "&poId=" + a[1],
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
}
