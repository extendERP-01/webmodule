package nirmalya.aathithya.webmodule.account.controller;

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
import nirmalya.aathithya.webmodule.sales.model.SaleOrderPaymentPdfModel;
import nirmalya.aathithya.webmodule.sales.model.SalesCustomerModel;
import nirmalya.aathithya.webmodule.sales.model.SalesDeliveryChallanModel;
import nirmalya.aathithya.webmodule.sales.model.SalesInvoiceModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account/")
public class ReceivableController {

	Logger logger = LoggerFactory.getLogger(ReceivableController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@GetMapping("manage-invoice")
	public String manageInvoice(Model model, HttpSession session) {
		logger.info("Method : manageInvoice  starts"); 
		
		logger.info("Method : manageInvoice  end");
		return "account/manage-invoice";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/manage-invoice-through-ajax")
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

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getReceivableInvoiceDetails", tableRequest,
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
						+ "\")'><i class='fa fa-search search'></i></a>&nbsp;";
				m.setSalesInvoice("<a href='javascript:void' onclick='pdfCreation(\"" + new String(pId) + "\")'>"
						+ m.getSalesInvoice() + "</a>");
//				m.setSalesOrderId("<a href='javascript:void' onclick='pdfCreateSalesOrder(\"" + new String(sId)
//						+ "\")'>" + m.getSalesOrderId() + "</a>");
//				m.setQuotationId("<a href='javascript:void' onclick='pdfCreate(\"" + new String(sId) + "\")'>"
//						+ m.getQuotationId() + "</a>");

				if (m.getPayStatus()) {

					m.setDateFrom(" <a data-toggle='modal' title='View Payment Details'  "
							+ "href='javascript:void' onclick='viewPaymentDetails(\"" + new String(pId)
							+ "\")'><button type='button' class='btn btn-info'>View</button></a>");
					s = s + " <a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='pdfTotalVoucher(\"" + new String(pId)
							+ "\",\""+new String(sId)+"\")'><i class=\"fa fa-file-pdf-o\" aria-hidden=\"true\"></i></a>";

				} else {
					if (!m.getPaymentType()) {

						m.setDateFrom(" <a data-toggle='modal' title='View Payment Details'  "
								+ "href='javascript:void' onclick='viewPaymentDetails(\"" + new String(pId)
								+ "\")'><button type='button' class='btn btn-info'>View</button></a>");

//						s = s + "&nbsp;&nbsp;<a href='sale-invoice-payment?id=" + new String(pId)
//								+ "'><i class='fa fa-money' title='Payment' style=\"font-size:24px\"></i></a>";
					} else {
						if (m.getVoucherId() == null || m.getVoucherId() == "") {
							m.setVoucherId("- - -");
						}

						if (m.getPayRefNo() == null || m.getPayRefNo() == "") {
							m.setPayRefNo("- - -");
						}

//						s = s + "&nbsp;&nbsp;<a href='sale-invoice-payment?id=" + new String(pId)
//								+ "'><i class='fa fa-money' title='Payment' style=\"font-size:24px\"></i></a>";
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
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/manage-invoice-saleInvoiceAutoCompleteList" })
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
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/manage-invoice-payment-details" })
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
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/manage-invoice-modal" })
	public @ResponseBody JsonResponse<Object> modalQuotation(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalQuotation starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAccountUrl() + "getReceivableInvoiceById?id=" + id, JsonResponse.class);
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
	
	@GetMapping("manage-challan")
	public String manageChallan(Model model, HttpSession session) {
		logger.info("Method : manageChallan  starts"); 
		
		logger.info("Method : manageChallan  end");
		return "account/manage-challan";
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("/manage-challan-through-ajax")
	public @ResponseBody DataTableResponse viewReqQuotThroAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param3, @RequestParam String param4,
			@RequestParam String param5) {
		logger.info("Method : viewReqQuotThroAjax starts");

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
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);

			JsonResponse<List<SalesDeliveryChallanModel>> jsonResponse = new JsonResponse<List<SalesDeliveryChallanModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getDelChallanDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SalesDeliveryChallanModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesDeliveryChallanModel>>() {
					});

			String s = "";
			String p = "";
			String k = "";

			for (SalesDeliveryChallanModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getDelChallanId().getBytes());

				Byte delStatus = 0;
				Byte delGeneratedInv = 2;
				Byte delStatusDB = m.getApproveStatus();
				if (delStatus.equals(delStatusDB)) {

					m.setApproveShowStatus("Pending");

//					s = s + "<a data-toggle='modal' title='Approve'  "
//							+ "href='javascript:void' onclick='viewInModelApprove(\"" + m.getDelChallanId()
//							+ "\")'><i class=\"fa fa-times-circle\" title=\"Pending for Approve\" "
//							+ "style=\"font-size:24px;color:#e30f0f\"></i></a>";

				} else {
					m.setApproveShowStatus("Delivered");
					if (delGeneratedInv.equals(delStatusDB)) {
//						s = s + "<a href='javascript:void(0)'" + "' onclick='changeFoodOrderStatusData(\""
//								+ new String(pId) + ',' + m.getApproveStatus() + ',' + new String(pId)
//								+ "\")' ><i class=\"fa fa-check-circle\" title=\"Complete\" style=\"font-size:24px;color:#090\"></i></a>&nbsp;&nbsp;";

						s = s + "<a data-toggle='modal' title='View'  "
								+ "href='javascript:void' onclick='viewInModel(\"" + m.getDelChallanId()
								+ "\")'><i class='fa fa-search search'></i></a>&nbsp;";

//						s = s + "<a  title='DownLoad Pdf'  " + "href='javascript:void' onclick='downloadPdf(\""
//								+ new String(pId) + "\")'><i class='fa fa-download' style=\"font-size:24px\"></i></a>";
					} else {
//						s = s + "<a href='javascript:void(0)'" + "' onclick='changeFoodOrderStatusData(\""
//								+ new String(pId) + ',' + m.getApproveStatus() + ',' + new String(pId)
//								+ "\")' ><i class=\"fa fa-check-circle\" title=\"Complete\" style=\"font-size:24px;color:#090\"></i></a>&nbsp;&nbsp;";

						s = s + "<a data-toggle='modal' title='View'  "
								+ "href='javascript:void' onclick='viewInModel(\"" + m.getDelChallanId()
								+ "\")'><i class='fa fa-search search'></i></a>&nbsp;";

						/*
						 * s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='invoiceCreate100(\""
						 * + new String(pId) +
						 * "\")'><i class='fa fa-files-o' title='Invoice' style=\"font-size:24px\"></i></a>&nbsp;&nbsp"
						 * ;
						 */
					}
				}
				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reqQuotation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewReqQuotThroAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/manage-challan-AutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getChallanList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getChallanList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getChallanListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getChallanList ends");
		return res;
	}

	/**
	 * Web Controller - Get getAut Customer No By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/manage-challan-AutocompleteCus" })
	public @ResponseBody JsonResponse<DropDownModel> getAutCusList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getAutCusList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getAutCusListBySearch?id=" + searchValue,
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

		logger.info("Method : getAutCusList ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-challan-model" })
	public @ResponseBody JsonResponse<SalesDeliveryChallanModel> modelDeliveryChallan(Model model,
			@RequestBody String index, BindingResult result) {
		logger.info("Method : modelDeliveryChallan starts");
		JsonResponse<SalesDeliveryChallanModel> res = new JsonResponse<SalesDeliveryChallanModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getChallanDtlsById?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modelDeliveryChallan ends");
		return res;
	}
	
	@GetMapping("customer-discount")
	public String customerDiscount(Model model, HttpSession session) {
		logger.info("Method : customerDiscount  starts"); 
		
		logger.info("Method : customerDiscount  end");
		return "account/customer-discount";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "customer-discount-throughAjax" })
	public @ResponseBody DataTableResponse viewCustomer(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, @RequestParam String param5) {
		logger.info("Method : viewCustomer(through ajax) starts");
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
			tableRequest.setParam3(param4);
			tableRequest.setParam3(param5);
			JsonResponse<List<SalesCustomerModel>> jsonResponse = new JsonResponse<List<SalesCustomerModel>>();
			jsonResponse = restClient.postForObject(env.getSalesUrl() + "get-all-customer",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<SalesCustomerModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesCustomerModel>>() {
					});
			String s = "";
			for (SalesCustomerModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getCustomerId().getBytes());

				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "&nbsp;<a data-toggle='modal' title='Discount' data-target='#myModal1' href='javascript:void(0)' onclick='discount(\""
						+ new String(pId) + "\")'><i class='fa fa-plus'></i></a>";
//				s = s + " &nbsp;&nbsp <a href='edit-customer?id=" + new String(pId)
//						+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp; ";
//				s = s + "<a href='javascript:void(0)' onclick='deleteCustomer(\"" + new String(pId)
//						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getCustomerActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewCustomer(through ajax) ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "customer-discount-model" })
	public @ResponseBody JsonResponse<SalesCustomerModel> modelView(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelView starts");

		JsonResponse<SalesCustomerModel> resp = new JsonResponse<SalesCustomerModel>();

		try {
			resp = restClient.getForObject(env.getSalesUrl() + "view-modal?id=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null) {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}

		logger.info("Method : modelView ends");
		System.out.println("Response :" + resp);
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "customer-discount-edit-discount" })
	public @ResponseBody JsonResponse<DropDownModel> customerDiscountView(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : customerDiscountView starts");
		
		JsonResponse<DropDownModel> resp = new JsonResponse<DropDownModel>();
		
		try {
			resp = restClient.getForObject(env.getSalesUrl() + "customerDiscountView?id=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null) {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : customerDiscountView ends");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "customer-discount-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveCustomerDiscount(@RequestBody DropDownModel disc,
			Model model, HttpSession session) {

		logger.info("Method : saveCustomerDiscount function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			
			res = restClient.postForObject(env.getSalesUrl() + "saveCustomerDiscount", disc, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveCustomerDiscount function Ends");
		return res;
	}
	
	@GetMapping("send-receipt-voucher-digitally")
	public String sendReceiptVoucherDigitally(Model model, HttpSession session) {
		logger.info("Method : sendReceiptVoucherDigitally  starts"); 
		
		logger.info("Method : sendReceiptVoucherDigitally  end");
		return "account/send-receipt-voucher-digitally";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/send-receipt-voucher-digitally-through-ajax")
	public @ResponseBody DataTableResponse sendReceiptVoucherDigitallyThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : sendReceiptVoucherDigitallyThroughAjax starts");

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

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAccountReceiptVoucher", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SalesInvoiceModel> quote = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesInvoiceModel>>() {
					});

			String s = "";

			for (SalesInvoiceModel m : quote) {
				byte[] pId = Base64.getEncoder().encode(m.getSalesInvoice().getBytes());
				byte[] sId = Base64.getEncoder().encode(m.getQuotationId().getBytes());

				s = s + "&nbsp;&nbsp;<a href='send-receipt-voucher-digitally-mail?id=" + new String(pId)
						+ "'><i class='fa fa-envelope' title='Send Mail' ></i></a>";
				
				m.setAction(s);
				s = ""; 
				
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(quote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : sendReceiptVoucherDigitallyThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/send-receipt-voucher-digitally-mail" })
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
		return "account/digitally-receipt-voucher";
	}
}
