package nirmalya.aathithya.webmodule.sales.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.sales.model.FoodOrderPaymentDetails;
import nirmalya.aathithya.webmodule.sales.model.SaleInvoiceReturnModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderPaymentPdfModel;
import nirmalya.aathithya.webmodule.sales.model.SalesInvoiceModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "sales")
public class SaleInvoiceController {

	Logger logger = LoggerFactory.getLogger(SaleInvoiceController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Add Sale Invoice' page
	 *
	 */
	@GetMapping("/add-sale-invoice")
	public String defaultSaleInvoice(Model model, HttpSession session) {

		logger.info("Method : defaultSaleInvoice starts");

		SalesInvoiceModel salesInvoice = new SalesInvoiceModel();
		SalesInvoiceModel salesInvoiceSession = (SalesInvoiceModel) session.getAttribute("ssalesInvoice");
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			/**
			 * Get DropDown Value Store List
			 *
			 */
			try {
				String userId = (String) session.getAttribute("USER_ID");
				DropDownModel[] payMode = restClient
						.getForObject(env.getSalesUrl() + "restGetStoreList?userId=" + userId, DropDownModel[].class);
				List<DropDownModel> storeList = Arrays.asList(payMode);

				model.addAttribute("storeList", storeList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			/**
			 * Get DropDown Value Store List
			 *
			 */
			/*
			 * try { String userId = (String) session.getAttribute("USER_ID");
			 * DropDownModel[] item = restClient.getForObject(env.getSalesUrl() +
			 * "getItemList?userId="+ userId, DropDownModel[].class); List<DropDownModel>
			 * itemList = Arrays.asList(item);
			 * 
			 * model.addAttribute("itemList", itemList);
			 * 
			 * } catch (RestClientException e) { e.printStackTrace(); }
			 */
			session.setAttribute("message", "");
			if (salesInvoiceSession != null) {
				model.addAttribute("salesInvoice", salesInvoiceSession);
				session.setAttribute("ssalesInvoice", null);
			} else {
				model.addAttribute("salesInvoice", salesInvoice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultSaleInvoice ends");
		return "sales/add-sale-invoice";
	}

	/**
	 * Web Controller - Get Sales Order List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-sale-invoice-salesOrderAutoCompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesOrderList(@RequestParam String searchValue,
			@RequestParam String storeId) {
		logger.info("Method : getSalesOrderList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getSalesUrl() + "getSalesOrderListByAuotSearch?id=" + searchValue + "&storeId=" + storeId,
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

		logger.info("Method : getSalesOrderList ends");
		return res;
	}

	/**
	 * Web Controller - Get Quotation List
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-sale-invoice-get-quotation-list" })
	public @ResponseBody JsonResponse<Object> getQuotationList(@RequestParam String saleOrder,
			@RequestParam String storeId) {

		logger.info("Method : getQuotationList starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(
					env.getSalesUrl() + "getQuotationList?id=" + saleOrder + "&storeId=" + storeId, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getQuotationList ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-sale-invoice-get-item-list" })
	public @ResponseBody JsonResponse<Object> getItemList(Model model, @RequestBody Map<String, String> quotation,
			BindingResult result) {
		logger.info("Method : getItemList starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		if (quotation.get("itemCode") != "" && quotation.get("itemCode") != null && quotation.get("salesOrder") != ""
				&& quotation.get("salesOrder") != null) {
			try {
				res = restClient.postForObject(env.getSalesUrl() + "getItemList", quotation, JsonResponse.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {

				res.setMessage("success");
			}
		}
		logger.info("Method : getItemList ends");
		return res;

	}

	/**
	 * Web Controller - Add Sale Invoice
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-sale-invoice-save-saleinvoice", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addSalesInvoice(@RequestBody List<SalesInvoiceModel> salesInvoice,
			Model model, HttpSession session) {

		logger.info("Method : addSalesInvoice function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			String userId = (String) session.getAttribute("USER_ID");
			for (int i = 0; i < salesInvoice.size(); i++) {
				salesInvoice.get(i).setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getSalesUrl() + "addSalesInvoice", salesInvoice, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addSalesInvoice function Ends");
		return res;
	}

	/**
	 * Web Controller - Sales Invoice
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-sale-invoice-print" })
	public String salesInvoice(HttpServletResponse response, @RequestParam("id") String encodedParam1, Model model) {

		logger.info("Method : salesInvoice starts");
		String encodeId = encodedParam1;

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<SalesInvoiceModel>> jsonresponse = new JsonResponse<List<SalesInvoiceModel>>();
		try {
			jsonresponse = restClient.getForObject(env.getSalesUrl() + "getSalesInvoiceById?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<SalesInvoiceModel> salesInvoice = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<SalesInvoiceModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
		int count = 0;
		Double totalDiscount = 0.0;
		for(SalesInvoiceModel m : salesInvoice) {
			if(m.getSaleDiscount()!=0.0 && m.getSaleDiscount()!=null) {
				count = count + 1;
				totalDiscount = totalDiscount + m.getSaleDiscount();
			}
		}
		model.addAttribute("count", count);
		model.addAttribute("totalDiscount", totalDiscount);
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-SaleInvoice?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String logo = logoList.get(0).getName();
		model.addAttribute("logoImage", "/document/hotel/" + logo + "");
		salesInvoice.get(0).setCurDate(curDate);

		model.addAttribute("salesInvoice", salesInvoice);

		logger.info("Method : salesInvoice ends");
		return "sales/view-sale-invoice-print";
	}

	/**
	 * Default 'View Sales Invoice' page
	 *
	 */
	@GetMapping("/view-sale-invoice")
	public String viewSalesInvoice(Model model, HttpSession session) {

		logger.info("Method : viewSalesInvoice starts");

		logger.info("Method : viewSalesInvoice ends");
		return "sales/view-sale-invoice";
	}

	/**
	 * Web Controller - Get Quotation
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-sale-invoice-through-ajax")
	public @ResponseBody DataTableResponse viewSalesInvoiceThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewSalesInvoiceThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<SalesInvoiceModel>> jsonResponse = new JsonResponse<List<SalesInvoiceModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getSalesInvoice", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SalesInvoiceModel> quote = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesInvoiceModel>>() {
					});

			String s = "";

			for (SalesInvoiceModel m : quote) {
				byte[] pId = Base64.getEncoder().encode(m.getSalesInvoice().getBytes());
				byte[] sId = Base64.getEncoder().encode(m.getQuotationId().getBytes());

				s = s + " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ new String(pId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;";
				m.setSalesInvoice("<a href='javascript:void' onclick='pdfCreation(\"" + new String(pId) + "\")'>"
						+ m.getSalesInvoice() + "</a>");
				m.setSalesOrderId("<a href='javascript:void' onclick='pdfCreateSalesOrder(\"" + new String(sId)
						+ "\")'>" + m.getSalesOrderId() + "</a>");
				m.setQuotationId("<a href='javascript:void' onclick='pdfCreate(\"" + new String(sId) + "\")'>"
						+ m.getQuotationId() + "</a>");

				if (m.getPayStatus()) {

					m.setDateFrom(" <a data-toggle='modal' title='View Payment Details'  "
							+ "href='javascript:void' onclick='viewPaymentDetails(\"" + new String(pId)
							+ "\")'><button type='button' class='btn btn-info'>View</button></a>");
					s = s + " <a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='pdfTotalVoucher(\"" + new String(pId)
							+ "\",\""+new String(sId)+"\")'><i class=\"fa fa-file-pdf-o\" aria-hidden=\"true\" style=\"font-size:24px\"></i></a>";

				} else {
					if (!m.getPaymentType()) {

						m.setDateFrom(" <a data-toggle='modal' title='View Payment Details'  "
								+ "href='javascript:void' onclick='viewPaymentDetails(\"" + new String(pId)
								+ "\")'><button type='button' class='btn btn-info'>View</button></a>");

						s = s + "&nbsp;&nbsp;<a href='sale-invoice-payment?id=" + new String(pId)
								+ "'><i class='fa fa-money' title='Payment' style=\"font-size:24px\"></i></a>";
					} else {
						if (m.getVoucherId() == null || m.getVoucherId() == "") {
							m.setVoucherId("- - -");
						}

						if (m.getPayRefNo() == null || m.getPayRefNo() == "") {
							m.setPayRefNo("- - -");
						}

						s = s + "&nbsp;&nbsp;<a href='sale-invoice-payment?id=" + new String(pId)
								+ "'><i class='fa fa-money' title='Payment' style=\"font-size:24px\"></i></a>";
					}
				}
				m.setAction(s);
				s = ""; 
				if (m.getSalesOrder() == null) {
					m.setSalesOrder(m.getPurchaseOrder());
				} else {
					s = "<a href='/document/purchaseOrder/" + m.getSalesOrder() + "' title='" + m.getSalesOrder()
							+ "' target='_blank'>" + m.getPurchaseOrder() + "</a>";
					m.setSalesOrder(s);
					s = "";

				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(quote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSalesInvoiceThroughAjax ends");
		return response;
	}

	/**
	 * Web Controller - Get Sale Invoice For Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-invoice-modal" })
	public @ResponseBody JsonResponse<Object> modalQuotation(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalQuotation starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSalesInvoiceById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalQuotation ends");
		return res;
	}

	/**
	 * Web Controller - Sales Invoice Payment
	 *
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping(value = { "/sale-invoice-payment" })
	public String salesInvoicePayment(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method : salesInvoicePayment starts");
		String encodeId = encodedParam1;

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<SalesInvoiceModel>> jsonresponse = new JsonResponse<List<SalesInvoiceModel>>();
		try {
			jsonresponse = restClient.getForObject(env.getSalesUrl() + "getSalesInvoiceById?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		try {
			DropDownModel[] payMode = restClient.getForObject(env.getSalesUrl() + "restGetPayMode",
					DropDownModel[].class);
			List<DropDownModel> payModeList = Arrays.asList(payMode);

			model.addAttribute("payModeList", payModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] pMode = restClient.getForObject(env.getSalesUrl() + "restGetPaymentMode",
					DropDownModel[].class);
			List<DropDownModel> pModeList = Arrays.asList(pMode);

			model.addAttribute("pModeList", pModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<SalesInvoiceModel> salesInvoice = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<SalesInvoiceModel>>() {
				});
		/* Map<String, Object> data = new HashMap<String, Object>(); */

		/**
		 * for (QuotationMasterModel m : quotation) {
		 * 
		 * }
		 */
		String saleInvoice = salesInvoice.get(0).getSalesInvoice();

		List<SaleInvoiceReturnModel> salesInvoiceReturn = new ArrayList<SaleInvoiceReturnModel>();
		
		/**
		 * get Sale Invoice Return Details
		 *
		 */
		try {
			SaleInvoiceReturnModel[] saleInvReturn = restClient.getForObject(
					env.getSalesUrl() + "restGetSaleInvoiceReturn?id=" + saleInvoice, SaleInvoiceReturnModel[].class);
			salesInvoiceReturn = Arrays.asList(saleInvReturn);
//			model.addAttribute("salesInvoiceReturn", salesInvoiceReturn);
//			if (salesInvoiceReturn.isEmpty()) {
//				model.addAttribute("salesInvoiceReturn", "");
//			} 
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		Double qty = 0.0;
		Double amt = 0.0;
		Double disc = 0.0;
		Double saleQty = 0.0;
		Double totalDiscount = 0.0;
		int count = 0;
		for(int i = 0; i < salesInvoice.size(); i++) {
			if(salesInvoice.get(i).getSaleItemCode().contentEquals(salesInvoiceReturn.get(i).getsRItemCode())) {
				qty = salesInvoice.get(i).getSaleQuantity() - salesInvoiceReturn.get(i).getsRItemQty();
				amt = salesInvoice.get(i).getSaleAmount() - salesInvoiceReturn.get(i).getsRAmount();
				disc = salesInvoice.get(i).getSaleDiscount() - salesInvoiceReturn.get(i).getsRDiscount();
				saleQty = salesInvoice.get(i).getSaleQuantity();
				salesInvoice.get(i).setSaleQuantity(qty);
				salesInvoice.get(i).setSaleAmount(amt);
				salesInvoice.get(i).setSaleDiscount(disc);
				totalDiscount = totalDiscount + disc;
				if(disc!=0.0 && disc!=null) {
					count = count + 1;
				}
			}
		}
		System.out.println("Count===="+count);
		model.addAttribute("totalDiscount", totalDiscount);
		model.addAttribute("count", count);
		
		Double saleSubTotal = 0.0;
		Double saleTotalCGST = 0.0;
		Double saleCGST = 0.0;
		Double saleTotalIGST = 0.0;
		Double saleIGST = 0.0;
		Double totalCess = 0.0;
		Double itemCess = 0.0;
		for(SalesInvoiceModel m : salesInvoice) {
			saleSubTotal = saleSubTotal + m.getSaleAmount();
			saleCGST = ((m.getGstRate() / 2) / 100) * m.getSaleAmount();
			saleIGST = (m.getGstRate() / 100) * m.getSaleAmount();
			itemCess = (saleIGST * m.getItemCess())/100;
			saleTotalCGST = saleTotalCGST + saleCGST;
			saleTotalIGST = saleTotalIGST + saleIGST;
			totalCess = totalCess + itemCess;
		}
		
//		Double saleSubTotal = 0.0;
//		Double saleTotalCGST = 0.0;
//		Double saleCGST = 0.0;
//		Double saleTotalIGST = 0.0;
//		Double saleIGST = 0.0;
//		for(SalesInvoiceModel m : salesInvoice) {
//			Double amount = 0.0;
//			amount = m.getSalePrice() * saleQty;
//			saleSubTotal = saleSubTotal + amount - m.getSaleDiscount();
//			saleCGST = ((m.getGstRate() / 2) / 100) * amount;
//			saleIGST = (m.getGstRate() / 100) * amount;
//			saleTotalCGST = saleTotalCGST + saleCGST;
//			saleTotalIGST = saleTotalIGST + saleIGST;
//			System.out.println("Sale Amt==="+amount);
//		}
//		System.out.println("S = "+saleSubTotal);
//		
//		Double saleRSubTotal = 0.0;
//		Double saleRTotalCGST = 0.0;
//		Double saleRCGST = 0.0;
//		Double saleRTotalIGST = 0.0;
//		Double saleRIGST = 0.0;
//		for(SaleInvoiceReturnModel m : salesInvoiceReturn) {
//			Double amount = 0.0;
//			amount = m.getsRItemPrice() * m.getsRItemQty();
//			saleRSubTotal = saleRSubTotal + amount - m.getsRDiscount();
//			saleRCGST = ((m.getGstRate() / 2) / 100) * amount;
//			saleRIGST = (m.getGstRate() / 100) * amount;
//			saleRTotalCGST = saleRTotalCGST + saleRCGST;
//			saleRTotalIGST = saleRTotalIGST + saleRIGST;
//			System.out.println("Sale Rtrn Amt==="+amount);
//		}
//		
//		System.out.println("S R = "+saleRSubTotal);
//		
//		saleSubTotal = saleSubTotal - saleRSubTotal;
//		System.out.println("S New = "+saleSubTotal);
//		saleTotalCGST = saleTotalCGST - saleRTotalCGST;
//		saleTotalIGST = saleTotalIGST - saleRTotalIGST;
		
		for(SalesInvoiceModel m : salesInvoice) {
			m.setSubTotal(saleSubTotal);
			m.setTotalCess(totalCess);
			Double grandTotal = 0.0;
			if(m.getGstType()) {
				m.setsIGST(saleTotalIGST);
				m.setsCGST(0.00);
				m.setsSGST(0.00);
				grandTotal = saleSubTotal + saleTotalIGST + totalCess;
			} else {
				m.setsIGST(0.00);
				m.setsCGST(saleTotalCGST);
				m.setsSGST(saleTotalCGST);
				grandTotal = saleSubTotal + saleTotalCGST + saleTotalCGST + totalCess;
			}
			m.setGrandTotal(grandTotal);
		}
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-SaleInvoice?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String logo = logoList.get(0).getName();
		model.addAttribute("logoImage", "/document/hotel/" + logo + "");
		salesInvoice.get(0).setCurDate(curDate);

		model.addAttribute("salesInvoice", salesInvoice);

		logger.info("Method : salesInvoicePayment ends");
		return "sales/sale-invoice-payment";
	}

	/**
	 * Web Controller - Make Payment
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-sale-invoice-make-payment")
	public @ResponseBody JsonResponse<Object> makePayment(Model model, @RequestBody SalesInvoiceModel index,
			HttpSession session) {

		logger.info("Method : makePayment starts");

		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		index.setCreatedBy(userId);
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.postForObject(env.getSalesUrl() + "makePayment", index, JsonResponse.class);

		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") { 
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : makePayment ends");
		return resp;
	}

	/**
	 * Web Controller - Sales Invoice Payment Print
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-sale-invoice-make-payment-print" })
	public String salesInvoicePaymentPrint(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method : salesInvoicePaymentPrint starts");
		String encodeId = encodedParam1;

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		List<SalesInvoiceModel> SalesInvoiceModelList = new ArrayList<SalesInvoiceModel>();
		List<FoodOrderPaymentDetails> FoodOrderPaymentDetailsList = new ArrayList<FoodOrderPaymentDetails>();

		JsonResponse<SaleOrderPaymentPdfModel> jsonresponse = new JsonResponse<SaleOrderPaymentPdfModel>();
		try {
			jsonresponse = restClient.getForObject(env.getSalesUrl() + "saleInvoicePayment?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		SaleOrderPaymentPdfModel paymentList = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<SaleOrderPaymentPdfModel>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		Double totalDiscount = 0.0;
		int count = 0;
		if (paymentList != null) {
			SalesInvoiceModelList = paymentList.getSalesInvoiceModelList();
			FoodOrderPaymentDetailsList = paymentList.getFoodOrderPaymentDetailsList();
			
			for(SalesInvoiceModel m : paymentList.getSalesInvoiceModelList()) {
				if(m.getSaleDiscount()!=null && m.getSaleDiscount()!=0.0) {
					totalDiscount = totalDiscount + m.getSaleDiscount();
					count = count + 1;
				}
			}
		}
		
		model.addAttribute("totalDiscount", totalDiscount);
		model.addAttribute("count", count);

		/**
		 * get Sale Invoice Return Details
		 *
		 */
		/*
		 * try { SaleInvoiceReturnModel[] saleInvReturn = restClient.getForObject(
		 * env.getSalesUrl() + "restGetSaleInvoiceReturn?id=" +
		 * SalesInvoiceModelList.get(0).getSalesInvoice(),
		 * SaleInvoiceReturnModel[].class); List<SaleInvoiceReturnModel>
		 * salesInvoiceReturn = Arrays.asList(saleInvReturn);
		 * model.addAttribute("salesInvoiceReturn", salesInvoiceReturn); if
		 * (salesInvoiceReturn.size() == 0) { model.addAttribute("salesInvoiceReturn",
		 * ""); } System.out.println(salesInvoiceReturn.get(0).getSaleInvReturn()); }
		 * catch (RestClientException e) { e.printStackTrace(); }
		 */

		/**
		 * get Customer List
		 *
		 */

		try {
			SalesInvoiceModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomerList?id=" + SalesInvoiceModelList.get(0).getSalesOrderId(),
					SalesInvoiceModel[].class);
			List<SalesInvoiceModel> custList = Arrays.asList(customer);

			model.addAttribute("custList", custList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel List
		 *
		 */
		try {
			SalesInvoiceModel[] hotel = restClient.getForObject(env.getSalesUrl() + "restGetHotel?id="+SalesInvoiceModelList.get(0).getQuotationId(),
					SalesInvoiceModel[].class);
			List<SalesInvoiceModel> hotelList = Arrays.asList(hotel);

			model.addAttribute("hotelList", hotelList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-SaleInvoice?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String logo = logoList.get(0).getName();
		model.addAttribute("logoImage", "/document/hotel/" + logo + "");
		SalesInvoiceModelList.get(0).setCurDate(curDate); 
		model.addAttribute("salesInvoice", SalesInvoiceModelList); 
		model.addAttribute("payDetails", FoodOrderPaymentDetailsList);

		logger.info("Method : salesInvoicePaymentPrint ends");
		return "sales/sale-invoice-payment-print";
	}

	/**
	 * Web Controller - Get Sales Invoice List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-invoice-saleInvoiceAutoCompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getSaleInvoiceList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSaleInvoiceList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleInvoiceListByAuotSearch?id=" + searchValue,
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

		logger.info("Method : getSaleInvoiceList ends");
		return res;
	}

	/**
	 * Web Controller - Get Receipt Voucher List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-invoice-voucherAutoComplete" })
	public @ResponseBody JsonResponse<DropDownModel> voucherAutoComplete(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : voucherAutoComplete starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "voucherAutoComplete?id=" + searchValue,
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

		logger.info("Method : voucherAutoComplete ends");
		return res;
	}

	/**
	 * Web Controller - Sales Invoice Report
	 *
	 */
	@GetMapping("view-sale-invoice-generate-report")
	public String salesInvoiceGenerateReport(Model model, HttpSession session) {

		logger.info("Method : salesInvoiceGenerateReport starts");

		logger.info("Method : salesInvoiceGenerateReport ends");
		return "sales/report-sale-invoice";
	}

	/**
	 * Web Controller - Get Sales Order List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-invoice-generate-report-salesOrderAutoCompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesOrderListForReport(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getSalesOrderListForReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getSalesUrl() + "getSalesOrderListByAuotSearchForReport?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getSalesOrderListForReport ends");
		return res;
	}

	/**
	 * Web Controller - Get POS NUMBER By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-invoice-posAutoComplete" })
	public @ResponseBody JsonResponse<DropDownModel> posAutoComplete(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSalesOrderList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "posNumberByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getSalesOrderList ends");
		return res;
	}

	/**
	 * Web Controller - Get Last Payment List
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-invoice-get-last-payment-list" })
	public @ResponseBody JsonResponse<Object> getLastPaymentList(Model model, @RequestBody String id,
			BindingResult result) {

		logger.info("Method : getLastPaymentList starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getLastPaymentList?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getLastPaymentList ends");
		return res;
	}

	/**
	 * Web Controller - Payment Details Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-invoice-payment-details" })
	public @ResponseBody JsonResponse<Object> paymentDetailsModal(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : paymentDetailsModal starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getInvoiceDtlsForPayment?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : paymentDetailsModal ends");
		return res;
	}
}
