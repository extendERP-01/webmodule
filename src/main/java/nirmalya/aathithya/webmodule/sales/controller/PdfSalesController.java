package nirmalya.aathithya.webmodule.sales.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.sales.model.FoodOrderPaymentDetails;
import nirmalya.aathithya.webmodule.sales.model.QuotationMasterModel;
import nirmalya.aathithya.webmodule.sales.model.SaleInvoiceReturnModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderAdvancePayDtlsParentModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderAdvancePaymentModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderPaymentPdfModel;
import nirmalya.aathithya.webmodule.sales.model.SalesInvoiceModel;
import nirmalya.aathithya.webmodule.sales.model.SalesPaymentModel;

/**
 * @author USER
 *
 */
@Controller
@RequestMapping(value = "download/")
public class PdfSalesController {

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(PdfSalesController.class);

	/**
	 * 
	 * PDF FOR QUOTATION MASTER
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-quotation-master-one-pdf")
	public void generateQuotationPdf(HttpServletResponse response, Model model, @RequestParam("id") String encodeId) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<List<QuotationMasterModel>> jsonResponse = new JsonResponse<List<QuotationMasterModel>>();
		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "QuotationPdf?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<QuotationMasterModel> quotation = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<QuotationMasterModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		for (QuotationMasterModel m : quotation) {
		}

		/**
		 * get Customer List
		 *
		 */

		try {
			QuotationMasterModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomerList?id=" + quotation.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> custList = Arrays.asList(customer);
			data.put("custList", custList);
			model.addAttribute("custList", custList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel List
		 *
		 */
		try {
			QuotationMasterModel[] hotel = restClient.getForObject(
					env.getSalesUrl() + "restGetHotelList?id=" + quotation.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
			model.addAttribute("hotelList", hotelList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			data.put("logoBgList", logoBgList);

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
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "header-Logo", DropDownModel[].class);
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
		String currdate = "";

		quotation.get(0).setCurDate(curDate);
		currdate = quotation.get(0).getCurDate();

		data.put("currdate", currdate);
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();

		data.put("image", env.getBaseUrlPath() + "document/hotel/" + background + "");
		data.put("logoImage", env.getBaseUrlPath() + "document/hotel/" + logo + "");
		data.put("quotation", quotation);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=QuotationMaster.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-quotation-master", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * PDF FOR SALES ORDER
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-sales-order-one-pdf")
	public void generateSalesOrderPdf(HttpServletResponse response, Model model, @RequestParam("id") String encodeId) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<SaleOrderAdvancePayDtlsParentModel> jsonResponse = new JsonResponse<SaleOrderAdvancePayDtlsParentModel>();
		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "SalesOrderPdf?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		SaleOrderAdvancePayDtlsParentModel saleOrderAdvancePayDtlsParentModel = mapper
				.convertValue(jsonResponse.getBody(), new TypeReference<SaleOrderAdvancePayDtlsParentModel>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		List<QuotationMasterModel> quotation = saleOrderAdvancePayDtlsParentModel.getQuotationMasterModelList();

		List<SaleOrderAdvancePaymentModel> saleOrderAdvancePaymentModelList = saleOrderAdvancePayDtlsParentModel
				.getSaleOrderAdvancePaymentModelList();
		/**
		 * get Customer List
		 *
		 */

		try {
			QuotationMasterModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomerList?id=" + quotation.get(0).getSalesOrder(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> custList = Arrays.asList(customer);
			data.put("custList", custList);
			model.addAttribute("custList", custList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel List
		 *
		 */
		try {
			QuotationMasterModel[] hotel = restClient.getForObject(
					env.getSalesUrl() + "restGetHotelList?id=" + quotation.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
			model.addAttribute("hotelList", hotelList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			data.put("logoBgList", logoBgList);

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
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "header-Logo", DropDownModel[].class);
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
		String currdate = "";

		quotation.get(0).setCurDate(curDate);
		currdate = quotation.get(0).getCurDate();
		data.put("currdate", currdate);
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		data.put("quotation", quotation); 
		data.put("saleOrderAdvancePaymentModelList", saleOrderAdvancePaymentModelList);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesOrder.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-sales-order", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * PDF FOR SALES INVOICE
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-sale-invoice-one-pdf")
	public void generateSalesInvoicePdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<List<SalesInvoiceModel>> jsonResponse = new JsonResponse<List<SalesInvoiceModel>>();
		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "SaleInvoicePdf?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<SalesInvoiceModel> salesInvoice = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<SalesInvoiceModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		int count = 0;
		Double totalDiscount = 0.0;
		for(SalesInvoiceModel m : salesInvoice) {
			if(m.getSaleDiscount()!=0.0 && m.getSaleDiscount()!=null) {
				count = count + 1;
				totalDiscount = totalDiscount + m.getSaleDiscount();
			}
		}
		data.put("count", count);
		data.put("totalDiscount", totalDiscount);

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String currdate = "";

		/**
		 * get Customer List
		 *
		 */

		try {
			QuotationMasterModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomer?id=" + salesInvoice.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> custList = Arrays.asList(customer);
			data.put("custList", custList);
			model.addAttribute("custList", custList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel List
		 *
		 */
		try {
			QuotationMasterModel[] hotel = restClient.getForObject(
					env.getSalesUrl() + "restGetHotelList?id=" + salesInvoice.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
			model.addAttribute("hotelList", hotelList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-SaleInvoice?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			data.put("logoBgList", logoBgList);

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

		salesInvoice.get(0).setCurDate(curDate);
		currdate = salesInvoice.get(0).getCurDate();
		data.put("currdate", currdate);
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		data.put("salesInvoice", salesInvoice);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesInvoice.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-sale-invoice", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * PDF FOR SALES INVOICE PAYMENT
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-sale-invoice-download-pdf")
	public void generateSalesInvoicePaymentPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId, @RequestParam("id2") String encodeId2) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		byte[] encodeByte2 = Base64.getDecoder().decode(encodeId2.getBytes());
		String id2 = (new String(encodeByte2));
		List<SalesInvoiceModel> SalesInvoiceModelList = new ArrayList<SalesInvoiceModel>();
		List<FoodOrderPaymentDetails> FoodOrderPaymentDetailsList = new ArrayList<FoodOrderPaymentDetails>();

		JsonResponse<SaleOrderPaymentPdfModel> jsonResponse = new JsonResponse<SaleOrderPaymentPdfModel>();
		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "saleInvoicePayment?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		SaleOrderPaymentPdfModel paymentList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<SaleOrderPaymentPdfModel>() {
				});
		if (paymentList != null) {
			SalesInvoiceModelList = paymentList.getSalesInvoiceModelList();
			FoodOrderPaymentDetailsList = paymentList.getFoodOrderPaymentDetailsList();
		}
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
		
		data.put("totalDiscount", totalDiscount);
		data.put("count", count);

		String s = "";

		/**
		 * get Customer List
		 *
		 */

		try {
			QuotationMasterModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomer?id=" + SalesInvoiceModelList.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> custList = Arrays.asList(customer);
			data.put("custList", custList);
			model.addAttribute("custList", custList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel List
		 *
		 */
		try {
			QuotationMasterModel[] hotel = restClient.getForObject(
					env.getSalesUrl() + "restGetHotelList?id=" + SalesInvoiceModelList.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
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
		data.put("logoImage", "/document/hotel/" + logo + "");
		SalesInvoiceModelList.get(0).setCurDate(curDate);
		data.put("salesInvoice", SalesInvoiceModelList);
		data.put("payDetails", FoodOrderPaymentDetailsList);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesInvoicePayment.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-sale-invoice-payment", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * PDF FOR SALE INVOICE RETURN
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-saleinvoice-return-note-one-pdf")
	public void generateSaleInvoiceReturnPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<List<SaleInvoiceReturnModel>> jsonResponse = new JsonResponse<List<SaleInvoiceReturnModel>>();
		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "SaleInvReturnPdf?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<SaleInvoiceReturnModel> saleInvReturn = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<SaleInvoiceReturnModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();

		/**
		 * get Customer List
		 *
		 */
		String salesOrder = saleInvReturn.get(0).getSalesOrderId();

		try {
			SaleInvoiceReturnModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomerSaleInvRtn?id=" + salesOrder, SaleInvoiceReturnModel[].class);
			List<SaleInvoiceReturnModel> custList = Arrays.asList(customer);
			data.put("custList", custList);
			model.addAttribute("custList", custList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel List
		 *
		 */

		try {
			SaleInvoiceReturnModel[] hotel = restClient.getForObject(env.getSalesUrl() + "restGetHotelRtrnDetails",
					SaleInvoiceReturnModel[].class);
			List<SaleInvoiceReturnModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
			model.addAttribute("hotelList", hotelList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-SaleInvoiceReturn?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			data.put("logoBgList", logoBgList);

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
					env.getSalesUrl() + "restLogoImage-SaleInvoiceReturn?logoType=" + "header-Logo",
					DropDownModel[].class);
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

		data.put("currdate", curDate);
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();

		data.put("image", env.getBaseUrlPath() + "document/hotel/" + background + "");
		data.put("logoImage", env.getBaseUrlPath() + "document/hotel/" + logo + "");
		data.put("saleInvReturn", saleInvReturn);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SaleInvoiceReturn.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-saleinvoice-return-note", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * PDF FOR SALES ORDER ADVANCE PAYMENT
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-sales-order-advance-payment-pdf")
	public void salesOrderAdvancePayment(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<List<SalesInvoiceModel>> jsonResponse = new JsonResponse<List<SalesInvoiceModel>>();
		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "AdvancePaymentPdf?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<QuotationMasterModel> quotation = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<QuotationMasterModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";

		/**
		 * get Customer List
		 *
		 */

		try {
			QuotationMasterModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomer?id=" + quotation.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> custList = Arrays.asList(customer);
			data.put("custList", custList);
			model.addAttribute("custList", custList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel List
		 *
		 */
		try {
			QuotationMasterModel[] hotel = restClient.getForObject(
					env.getSalesUrl() + "restGetHotelList?id=" + quotation.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
			model.addAttribute("hotelList", hotelList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			data.put("logoBgList", logoBgList);

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
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "header-Logo", DropDownModel[].class);
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
		String currdate = "";

		quotation.get(0).setCurDate(curDate);
		currdate = quotation.get(0).getCurDate();
		data.put("currdate", currdate);
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		data.put("quotation", quotation);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesOrderAdvancePayment.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-sales-order-advance-payment", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * PDF FOR SALES INVOICE PAYMENT
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-sale-invoice-total-payment-pdf")
	public void saleInvoiceTotalPaymentPDF(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId, @RequestParam("id2") String encodeId2) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		byte[] encodeByte2 = Base64.getDecoder().decode(encodeId2.getBytes());
		String id2 = (new String(encodeByte2));

		JsonResponse<List<SalesInvoiceModel>> jsonResponse = new JsonResponse<List<SalesInvoiceModel>>();
		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "SaleInvTotalPdf?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<SalesInvoiceModel> salesInvoice = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<SalesInvoiceModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		for (SalesInvoiceModel m : salesInvoice) {

			if (m.getsIGST() != 0.0) {
				m.setPayableAmt(m.getSubTotal() + m.getsIGST());
			} else {
				m.setPayableAmt(m.getSubTotal() + m.getsCGST() + m.getsSGST());
			}
		}

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String currdate = "";
		 SalesPaymentModel[] totalPayment = restClient.getForObject(
				env.getSalesUrl() + "totalPaymentDetails?id=" + salesInvoice.get(0).getSalesInvoice(),
				SalesPaymentModel[].class);
		List<SalesPaymentModel> totalPaymentDtls = Arrays.asList(totalPayment);
		data.put("totalPaymentDtls", totalPaymentDtls); 

		/**
		 * get Sale Invoice Return Details
		 *
		 */

		SaleInvoiceReturnModel[] saleInvReturn = restClient.getForObject(
				env.getSalesUrl() + "restGetSaleInvoiceReturn?id=" + salesInvoice.get(0).getSalesInvoice(),
				SaleInvoiceReturnModel[].class);
		List<SaleInvoiceReturnModel> salesInvoiceReturn = Arrays.asList(saleInvReturn);
		data.put("salesInvoiceReturn", salesInvoiceReturn);
		if (salesInvoiceReturn.size() == 0) {
			data.put("salesInvoiceReturn", "");
		}

		/**
		 * get Customer List
		 *
		 */

		try {
			QuotationMasterModel[] customer = restClient.getForObject(
					env.getSalesUrl() + "restGetCustomer?id=" + salesInvoice.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> custList = Arrays.asList(customer);
			data.put("custList", custList);
			model.addAttribute("custList", custList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel List
		 *
		 */
		try {
			QuotationMasterModel[] hotel = restClient.getForObject(
					env.getSalesUrl() + "restGetHotelList?id=" + salesInvoice.get(0).getQuotationId(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
			model.addAttribute("hotelList", hotelList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-SaleInvoice?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			data.put("logoBgList", logoBgList);

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

		salesInvoice.get(0).setCurDate(curDate);
		currdate = salesInvoice.get(0).getCurDate();
		data.put("currdate", currdate);
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		data.put("salesInvoice", salesInvoice);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesInvoiceTotalPayment.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-sale-invoice-total-payment", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
