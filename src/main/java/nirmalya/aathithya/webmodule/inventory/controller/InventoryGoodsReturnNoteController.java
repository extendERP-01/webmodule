package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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
import nirmalya.aathithya.webmodule.inventory.model.GRNReturnListModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReturnsNoteModel;

@Controller
@RequestMapping(value = "inventory")
public class InventoryGoodsReturnNoteController {
	Logger logger = LoggerFactory.getLogger(InventoryGoodsReturnNoteController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Add Inventory Goods return note' page
	 *
	 */
	@GetMapping("add-inventory-goodsreturns")
	public String addInventoryGoodsReturns(Model model, HttpSession session) {
		logger.info("Method : addInventoryGoodsReturn method starts");
		InventoryGoodsReturnsNoteModel inventoryGoodsReturnsNoteModel = new InventoryGoodsReturnsNoteModel();
		InventoryGoodsReturnsNoteModel returnInventory = null;
		try {
			returnInventory = (InventoryGoodsReturnsNoteModel) session.getAttribute("returnInventories");
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
		/*
		 * dropDown value for purchase order number
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-purchase-order",
					DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addInventoryGoodsReturns method ends");
		return "inventory/AddGoodsReturnsNote";
	}

	/**
	 * get ItemSubcategory by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-inventory-goodsreturns-get-itemSubCategory-through-ajax" })
	public @ResponseBody JsonResponse<DropDownModel> getSubList(@RequestParam("itemCategory") String itemCategory,
			@RequestParam("gRNInvoiceId") String gRNInvoiceId) {
		logger.info("Method : getSubList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "get-GRN-itemSubCategory?id=" + itemCategory + "&gRNInvoiceId=" + gRNInvoiceId,
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
		logger.info("Method : getSubList ends");
		return res;
	}
	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-inventory-goodsreturns-get-itemName-through-ajax" })
	public @ResponseBody JsonResponse<DropDownModel> getItemsLists(
			@RequestParam("itemSubCategory") String itemSubCategory,
			@RequestParam("gRNInvoiceId") String gRNInvoiceId) {
		logger.info("Method : getItemsLists starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "get-GRN-itemName?id=" + itemSubCategory + "&gRNInvoiceId=" + gRNInvoiceId,
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
		logger.info("Method : getItemsLists ends");
		return res;
	}

	/*
	 * post Mapping for add Inventory Goods return
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-inventory-goodsreturns", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> postAddGoodsReturn(
			@RequestBody List<InventoryGoodsReturnsNoteModel> inventoryGoodsReturnsNoteModel, Model model,
			HttpSession session) {
		logger.info("Method : postAddGoodsReturn function starts");
		
		System.out.println("GRN Return List=========="+inventoryGoodsReturnsNoteModel);
		List<GRNReturnListModel> grnList = new ArrayList<GRNReturnListModel>();
		try {
			GRNReturnListModel[] grn = restClient.getForObject(env.getInventoryUrl() + "restGetGRNDetailsForReturn?id="+inventoryGoodsReturnsNoteModel.get(0).getPurchaseOrder()+"&grn="+inventoryGoodsReturnsNoteModel.get(0).getgRNInvoiceId(),
					GRNReturnListModel[].class);
			grnList = Arrays.asList(grn);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println("GRN List========="+grnList);
		
		Double price = 0.0;
		Double gstRate = 0.0;
		Double qty = 0.0;
		Double lineTotal = 0.0;
		Double cgst = 0.0;
		Double sgst = 0.0;
		Double igst = 0.0;
		Double totalCGST = 0.0;
		Double totalSGST = 0.0;
		Double totalIGST = 0.0;
		Double subTotal = 0.0;
		Double grandTotal = 0.0;
		Double cessAmount = 0.0;
		Double totalCess = 0.0;
		Boolean gstType = null;
		
		if(grnList.size()>0) {
			for(int i = 0; i < inventoryGoodsReturnsNoteModel.size(); i++) {
				if(inventoryGoodsReturnsNoteModel.get(i).getItemName().contentEquals(grnList.get(i).getItemId())) {
					price = grnList.get(i).getPrice();
					gstRate = grnList.get(i).getGst();
					qty = inventoryGoodsReturnsNoteModel.get(i).getgRtNQty();
					gstType = grnList.get(i).getGstType();
					cessAmount = grnList.get(i).getCessAmount();
					lineTotal = price * qty;
					
					subTotal = subTotal + lineTotal;
					
					Double cess = (((lineTotal * gstRate) / 100)*cessAmount)/100;
					totalCess = totalCess + cess;
					
					if(gstType) {
						igst = (lineTotal * gstRate) / 100;
						
						totalIGST = totalIGST + igst;
						
					} else {
						cgst = (lineTotal * gstRate) / 200;
						sgst = (lineTotal * gstRate) / 200;
						
						totalCGST = totalCGST + cgst;
						totalSGST = totalSGST + sgst;
					}
					
					inventoryGoodsReturnsNoteModel.get(i).setPrice(price);
					inventoryGoodsReturnsNoteModel.get(i).setGstRate(gstRate);
					inventoryGoodsReturnsNoteModel.get(i).setGstType(gstType);
					inventoryGoodsReturnsNoteModel.get(i).setLineTotal(lineTotal);
					inventoryGoodsReturnsNoteModel.get(i).setCessAmount(cessAmount);
				}
			}
		}
		String userId=null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(grnList.size()>0) {
			for(int i = 0; i < inventoryGoodsReturnsNoteModel.size(); i++) {
				if(gstType) {
					grandTotal = totalIGST + subTotal + totalCess;
				} else {
					grandTotal = totalCGST + totalCGST + subTotal + totalCess;
				}
				inventoryGoodsReturnsNoteModel.get(i).setGsubTotal(subTotal);
				inventoryGoodsReturnsNoteModel.get(i).setCgst(totalCGST);
				inventoryGoodsReturnsNoteModel.get(i).setSgst(totalSGST);
				inventoryGoodsReturnsNoteModel.get(i).setIgst(totalIGST);
				inventoryGoodsReturnsNoteModel.get(i).setGrandTotal(grandTotal);
				inventoryGoodsReturnsNoteModel.get(i).setgRDtlsCreatedBy(userId);
				inventoryGoodsReturnsNoteModel.get(i).setTotalCess(totalCess);
			}
		}
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		try {
			System.out.println("Latest Data====="+inventoryGoodsReturnsNoteModel);
			res = restClient.postForObject(env.getInventoryUrl() + "rest-addNew-goods-returnnote",
					inventoryGoodsReturnsNoteModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : postAddGoodsReturn function Ends");
		return res;
	}
	/*
	 * Listing in add Page
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-inventory-goodsreturns-get-list-through-ajax" })
	public @ResponseBody JsonResponse<Object> getList(Model model, @RequestBody String purchaseOrder,
			BindingResult result) {
		logger.info("Method : getList starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-allList?id=" + purchaseOrder,
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
		logger.info("Method : getList ends");
		return res;

	}
	@GetMapping(value = { "view-inventory-goodsreturns" })
	public String viewGoodsreturnNote(Model model) {
		logger.info("Method : viewGoodsreturnNote starts");
		/*
		 * dropDown value for purchase order number
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-purchase-order",
					DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonResponse<Object> goodreturns = new JsonResponse<Object>();
		model.addAttribute("goodreturns", goodreturns);
		logger.info("Method : viewGoodsreturnNote ends");
		return "inventory/ViewGoodsReturnNote";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-inventory-goodsreturns-throughAjax" })
	public @ResponseBody DataTableResponse viewGoodsReturnThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {
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
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			JsonResponse<List<InventoryGoodsReturnsNoteModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReturnsNoteModel>>();
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-all-goodsreturn", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryGoodsReturnsNoteModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryGoodsReturnsNoteModel>>() {
					});
			String s = "";

			for (InventoryGoodsReturnsNoteModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getGoodsReturnNote().getBytes());
				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ m.getGoodsReturnNote() + "\")'><i class='fa fa-search search'></i></a>";
//				s = s + " &nbsp;&nbsp <a href='edit-goodsreturn-note?id=" + new String(pId)
//						+ "' ><i class='fa fa-edit'></i></a>  ";
				s = s + "&nbsp;&nbsp;<a href='javascript:void(0)'   onclick='deleteReturnnote(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a>&nbsp;&nbsp; ";
				s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + m.getGoodsReturnNote()
						+ "\")'><i class='fa fa-download'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getgRtNActive()) {

					m.setActivity("Active");
				} else {
					m.setActivity("Inactive");
				}
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
		String userId=null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.getForObject(
					env.getInventoryUrl() + "delete-goodsreturn?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : deleteGoodsreturn ends");
		return resp;
	}

	/*
	 * 
	 * 
	 * GetMapping for Edit Goods return note
	 * 
	 * 
	 * 
	 */

	@GetMapping(value = { "edit-goodsreturn-note" })
	public String editGoodsReturnNote(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editGoodsReturnNote starts");

		/*
		 * dropDown value for purchase order number
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-purchase-order",
					DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));

		try {
			InventoryGoodsReturnsNoteModel[] inventoryGoodsReturnsNoteModel = restClient.getForObject(
					env.getInventoryUrl() + "edit-goodsreturn-byId?id=" + id, InventoryGoodsReturnsNoteModel[].class);
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
				DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "editGetStoreListByInv?id=" + invGRN.get(0).getgRNInvoiceId(),
						DropDownModel[].class);
				List<DropDownModel> storeList = Arrays.asList(store);
				model.addAttribute("storeList", storeList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();

				for (InventoryGoodsReturnsNoteModel m : invGRN) {
					
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "getinvSubCategory?id=" + m.getItemCategory(), DropDownModel[].class);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {

				InventoryGoodsReturnsNoteModel[] invoicesdetails = restClient.getForObject(
						env.getInventoryUrl() + "list-purchase-order?id=" + invGRN.get(0).getgRNInvoiceId(),
						InventoryGoodsReturnsNoteModel[].class);
				
				List<InventoryGoodsReturnsNoteModel> invoices = Arrays.asList(invoicesdetails);
				model.addAttribute("invoices", invoices);

			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * dropDown value for item category
			 */

			try {
				DropDownModel[] dropDownModel = restClient.getForObject(
						env.getInventoryUrl() + "rest-get-inv-itemCategory?id=" + invGRN.get(0).getgRNInvoiceId(),
						DropDownModel[].class);
				List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
				model.addAttribute("itemCategoryList", itemCategoryList);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
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
	@PostMapping(value = { "view-inventory-goodsreturns-modal-view-goodsreturn" })
	public @ResponseBody JsonResponse<Object> modelviewGoodsreturn(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewGoodsreturn starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-goodsreturn-modal?id=" + index,
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

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-inventory-goodsreturns-get-itemCategry-through-ajax" })
	public @ResponseBody JsonResponse<Object> getItemCategry(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : getItemCategry starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-itemCategry?id=" + itemCategory,
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
	
		logger.info("Method : getItemCategry ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("add-inventory-goodsreturns-get-calculated-price-items")
	public @ResponseBody JsonResponse<Object> getCalculatedPrice(Model model,
			@RequestBody Map<String, String> invService, HttpSession session) {
		logger.info("Method : getCalculatedPrice function starts");
		JsonResponse<Object> response = new JsonResponse<Object>();
		if (invService.get("gRNInvoiceId") != "" && invService.get("itemId") != "") {
			try {

				response = restClient.postForObject(env.getInventoryUrl() + "calculateItemPrice", invService,
						JsonResponse.class);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String message = response.getMessage();

			if (message != null && message != "") {
				response.setCode("Unsuccess");
			} else {
				response.setCode("Success");
			}
		}

		logger.info("Method : getCalculatedPrice function ends");
		return response;
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : generateGoodsreturnnoteReport ends");
		return "inventory/pdfReportGoodsreturn";
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-inventory-goodsreturns-get-all-quantity-through-ajax" })
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
	@PostMapping(value = { "/add-inventory-goodsreturns-getPurchaseOrderAutocompleteList" })
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
	@PostMapping(value = { "/add-inventory-goodsreturns-getPurchseNo" })
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
	@PostMapping(value = { "/add-inventory-goodsreturns-getpurchaseNos" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseOrdrPdfList(Model model, @RequestBody String searchValue,
			BindingResult result) {
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
	public @ResponseBody JsonResponse<DropDownModel> searchPurchaseOrdrList(Model model, @RequestBody String searchValue,
			BindingResult result) {
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
	@PostMapping(value = { "/add-inventory-goodsreturns-getSuggestinvoiceNos" })
	public @ResponseBody JsonResponse<DropDownModel> getInvoiceNumberSuggestList(Model model, @RequestBody String searchValue,
			BindingResult result) {
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
	public @ResponseBody JsonResponse<DropDownModel> searchInvoiceNumberSuggestList(Model model, @RequestBody String searchValue,
			BindingResult result) {
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
	
	
	//for getting data in autocomplte invoice Id
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-inventory-goodsreturns-get-grninvoiceid-through-ajax" })
	public @ResponseBody JsonResponse<Object> getInvoiceId(@RequestParam("purchaseOrder") String purchaseOrder,
			@RequestParam("gRNInvoiceId") String gRNInvoiceId) {
		logger.info("Method : getInvoiceId starts");
	
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
		
			res = restClient.getForObject(
					env.getInventoryUrl() + "get-invoiceId?id=" + purchaseOrder + "&gRNInvoiceId=" + gRNInvoiceId,
					JsonResponse.class);
		}
		catch (Exception e) {
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
	@PostMapping(value = { "/add-inventory-goodsreturns-getSuggestioninvoiceNos" })
	public @ResponseBody JsonResponse<DropDownModel> getInvoiceNumberSuggestionInvList(Model model, @RequestBody String searchValue,
			BindingResult result) {
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
	@PostMapping(value = { "add-inventory-goodsreturns-get-discount-through-ajax" })
	public @ResponseBody JsonResponse<Object> getDiscounts(@RequestParam("itemId") String itemId,
			@RequestParam("gRNInvoiceId") String gRNInvoiceId) {
		logger.info("Method : getDiscounts starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			
			res = restClient.getForObject(
					env.getInventoryUrl() + "get-discounts?id=" + itemId + "&gRNInvoiceId=" + gRNInvoiceId,
					JsonResponse.class);
		}
		catch (Exception e) {
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
	@PostMapping(value = { "add-inventory-goodsreturns-get-gsts-rate-through-ajax" })
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
	@PostMapping(value = { "/add-inventory-goodsreturns-get-item" })
	public @ResponseBody JsonResponse<InventoryGoodsReturnsNoteModel> getItemAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<InventoryGoodsReturnsNoteModel> res = new JsonResponse<InventoryGoodsReturnsNoteModel>();

		String a[] = searchValue.split(",");
		try {
			res = restClient.getForObject(env.getInventoryUrl()
					+ "getItemListByAutoSearchWithGRNINV?id=" + a[0] + "&poId=" + a[1], JsonResponse.class);
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
