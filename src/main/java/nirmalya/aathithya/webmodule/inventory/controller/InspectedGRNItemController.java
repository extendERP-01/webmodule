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
import org.springframework.web.bind.annotation.GetMapping;
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReceiveModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class InspectedGRNItemController {

	Logger logger = LoggerFactory.getLogger(InspectedGRNItemController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;
	
	@GetMapping(value = { "view-inspected-grn" })
	public String viewGoodsReceivenote1(Model model) {
		logger.info("Method : viewGoodsReceivenote starts");

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-invoices-numbers",
					DropDownModel[].class);
			List<DropDownModel> invoiceNumber = Arrays.asList(dropDownModel);
			model.addAttribute("invoice", invoiceNumber);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		JsonResponse<Object> goodreceive = new JsonResponse<Object>();
		model.addAttribute("goodreceive", goodreceive);
		logger.info("Method : viewGoodsReceivenote ends");
		return "inventory/viewInspectedGRNItem";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-inspected-grn-throughAjax" })
	public @ResponseBody DataTableResponse viewGoodsReceiveThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, HttpSession session) {
		logger.info("Method : viewGoodsReceiveThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
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
			tableRequest.setUserId(userId);
			JsonResponse<List<InventoryGoodsReceiveModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReceiveModel>>();
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-alls-goodsreceives", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryGoodsReceiveModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryGoodsReceiveModel>>() {
					});
			String s = "";

			for (InventoryGoodsReceiveModel m : form) {

				byte[] pId = Base64.getEncoder().encode(m.getgRNInvoiceId().getBytes());

				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ m.getgRNInvoiceId() + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='view-inspected-grn-allocate-rack?id=" + new String(pId) 
						+ "' ><i class=\"fa fa-tasks\" aria-hidden=\"true\"></i></a> ";
				 
				m.setgRNInvoiceId("<a href='javascript:void' onclick='pdfCreate(\"" + m.getgRNInvoiceId() + "\")'>"
						+ m.getgRNInvoiceId() + "</a>");
				if (m.getgRNPurchaseOrderId() != null && m.getgRNPurchaseOrderId() != "") {
					m.setgRNPurchaseOrderId("<a href='javascript:void' onclick='pdfCreatePurchase(\""
							+ m.getgRNPurchaseOrderId() + "\")'>" + m.getgRNPurchaseOrderId() + "</a>");
				} else {
					m.setgRNPurchaseOrderId("N/A");
				}

				if (m.getgRnInvoiceActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("InActive");
				}
					
				m.setAction(s);

				s = "";	
				
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
		logger.info("Method : viewGoodsReceiveThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unused")
	@GetMapping(value = { "view-inspected-grn-allocate-rack" })
	public String allocateGRNItemToRack(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : allocateGRNItemToRack starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		JsonResponse<InventoryGoodsReceiveModel> jsonResponse = new JsonResponse<InventoryGoodsReceiveModel>();
		List<DropDownModel> vendor = new ArrayList<DropDownModel>();
		try {
			InventoryGoodsReceiveModel[] invGoodsReceiveModel = restClient.getForObject(
					env.getInventoryUrl() + "edit-goods-receives-byId?id=" + id, InventoryGoodsReceiveModel[].class);

			List<InventoryGoodsReceiveModel> invGRN = Arrays.asList(invGoodsReceiveModel);
			if (invGRN.get(0).getgRNPurchaseOrderId() != null || invGRN.get(0).getgRNPurchaseOrderId() != "") {
				try {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "editVendor?id=" + invGRN.get(0).getgRNPurchaseOrderId(),
							DropDownModel[].class);
					List<DropDownModel> vendorList = Arrays.asList(dropDownModel);

					vendor.addAll(vendorList);
					model.addAttribute("vendorList", vendor);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*
				 * dropDown value for purchase order number
				 */
				try {
					DropDownModel[] dropDownModel = restClient
							.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
					List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
					model.addAttribute("purchase", purchaseOrder);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				/*
				 * dropDown for item Type add-items-getSubGroup-throughAjax
				 */
				try {
					String userId = (String) session.getAttribute("USER_ID");
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "rest-get-godown?userId=" + userId, DropDownModel[].class);
					List<DropDownModel> godownList = Arrays.asList(dropDownModel);
					model.addAttribute("godownList", godownList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			try {

				int i = 1;
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();
				List<Object> qtyLists = new ArrayList<Object>();
				List<Object> pendingLists = new ArrayList<Object>();
				List<Object> priceLists = new ArrayList<Object>();

				for (InventoryGoodsReceiveModel m : invGRN) {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "get-subs-categories?id=" + m.getItmCategory(),
							DropDownModel[].class);
					List<DropDownModel> subcat = Arrays.asList(dropDownModel);

					subCatLists.add(subcat);

					DropDownModel[] dropDownModel1 = restClient.getForObject(
							env.getInventoryUrl() + "get-item-Id?id=" + m.getItmSubCategory(), DropDownModel[].class);
					List<DropDownModel> itemS = Arrays.asList(dropDownModel1);

					itemLists.add(itemS);// get itemlist

					// dropdown for quantity in edit form

					DropDownModel[] dropDownModel2 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-quantity?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> item = Arrays.asList(dropDownModel2);
					qtyLists.add(item);

					// drpdown for pending quantity
					DropDownModel[] dropDownModel3 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-pending?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> pending = Arrays.asList(dropDownModel3);
					pendingLists.add(pending);

					// drpdown for price
					DropDownModel[] dropDownModel4 = restClient.getForObject(
							env.getInventoryUrl() + "editPrice?id=" + m.getgRnInvoiceItmName(), DropDownModel[].class);

					List<DropDownModel> prc = Arrays.asList(dropDownModel4);

					priceLists.add(prc);

				}

				model.addAttribute("subCatlists", subCatLists);
				model.addAttribute("itemLists", itemLists);
				model.addAttribute("qtyLists", qtyLists);
				model.addAttribute("pendingLists", pendingLists);
				model.addAttribute("priceLists", priceLists);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			try {

				InventoryGoodsReceiveModel[] purchasedetails = restClient
						.getForObject(
								env.getInventoryUrl() + "lists-purchases-orders?id=" + invGRN.get(0).getPorder()
										+ "&invId=" + invGRN.get(0).getgRNInvoiceId(),
								InventoryGoodsReceiveModel[].class);
				List<InventoryGoodsReceiveModel> purchaseOrder = Arrays.asList(purchasedetails);

				model.addAttribute("purchase1", purchaseOrder);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			model.addAttribute("invGoods", invGRN.get(0).getgRNInvoiceId());

			session.setAttribute("message", "");

			String ImgName = invGRN.get(0).getInvImg();
			if (ImgName != null && ImgName != "") {
				String s = "";
				if (ImgName.endsWith(".xls") || ImgName.endsWith(".docx")) {
					s = "/document/excel/" + ImgName;
					invGRN.get(0).setAction(s);
				} else {
					s = "/document/image/" + ImgName;
					invGRN.get(0).setAction(s);
				}
				session.setAttribute("quotationPFileEdit", invGRN.get(0).getInvImg());

				System.out.println(invGRN);
			}
			model.addAttribute("purchase", invGRN.get(0).getgRNPurchaseOrderId());
			model.addAttribute("invGoodsReceiveModel", invGRN);
			System.out.println(invGoodsReceiveModel);
			if (vendor.size() == 0) {
				model.addAttribute("vendorList", vendor);
			} else {
				model.addAttribute("vendorList", null);
			}
			System.out.println(vendor);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : allocateGRNItemToRack ends");
		return "inventory/allocateGRNItemToRack";
	}
}
