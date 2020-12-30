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
import javax.servlet.http.HttpSession;

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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.sales.model.SaleInvoiceReturnModel;
import nirmalya.aathithya.webmodule.sales.model.SalesInvoiceModel;
import nirmalya.aathithya.webmodule.sales.model.SalesOrdeReportModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "sales")
public class ReportSalesController {

	Logger logger = LoggerFactory.getLogger(ReportSalesController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/**
	 * 
	 * PDF FOR SALES ORDER FOR REPORT
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-sales-order-download-report")
	public void generateReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,HttpSession session, @RequestParam("param3") String encodedParam3, 
			@RequestParam("param4") String encodedParam4, @RequestParam("param5") String encodedParam5, @RequestParam("param6") String encodedParam6,
			@RequestParam("param7") String encodedParam7, @RequestParam("param8") String encodedParam8) {
		
		
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		byte[] encodeByte6 = Base64.getDecoder().decode(encodedParam6.getBytes());
		byte[] encodeByte7 = Base64.getDecoder().decode(encodedParam7.getBytes());
		byte[] encodeByte8 = Base64.getDecoder().decode(encodedParam8.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		String param6 = (new String(encodeByte6));
		String param7 = (new String(encodeByte7));
		String param8 = (new String(encodeByte8));

		JsonResponse<List<SalesOrdeReportModel>> jsonResponse = new JsonResponse<List<SalesOrdeReportModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			tableRequest.setParam6(param6);
			tableRequest.setParam7(param7);
			tableRequest.setParam8(param8);

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getSalesOrderReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<SalesOrdeReportModel> quotation = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<SalesOrdeReportModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		// String s = "";

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

		String printedBy = "";
		printedBy = (String) session.getAttribute("USER_NAME");
		if (quotation.size() != 0) {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("quotation", "");
		}

		for (SalesOrdeReportModel m : quotation) {
			
			if(m.getRcvDate()=="" || m.getRcvDate()==null) {
				m.setRcvDate("--");
			} 
			
			if(m.getRcvTime()=="" || m.getRcvTime()==null) {
				m.setRcvTime("--");
			}
			
			if (m.getSalesOrderActive()) {
				m.setStatus("Complete");
			} else {
				m.setStatus("Pending");
			}
		}

		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		data.put("quotation", quotation);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesOrderList.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-sales-order-list", data);
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
	 * PDF FOR SALES INVOICE FOR REPORT
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-sale-invoice-download-report")
	public void generateSALESINVOICEReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<List<SalesInvoiceModel>> jsonResponse = new JsonResponse<List<SalesInvoiceModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getSalesInvoiceReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<SalesInvoiceModel> saleInvoice = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<SalesInvoiceModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		// String s = "";

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

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String printedBy = "SAGAR";
		if (saleInvoice.size() != 0) {

			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("saleInvoice", "");
		}

		for (SalesInvoiceModel m : saleInvoice) {
			if (m.getPayStatus()) {
				m.setAction("Paid");
			} else {
				m.setAction("Not Paid");
			}
		}

		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		data.put("saleInvoice", saleInvoice);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesInvoiceList.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-sale-invoice-list", data);
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
	 * PDF FOR SALES INVOICE RETURN FOR REPORT
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-saleinvoice-return-note-download-report")
	public void generateSaleInvReturnReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));

		JsonResponse<List<SaleInvoiceReturnModel>> jsonResponse = new JsonResponse<List<SaleInvoiceReturnModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getSalesInvReturnReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<SaleInvoiceReturnModel> saleInvReturn = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<SaleInvoiceReturnModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		// String s = "";

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
		String printedBy = "Sagar Anchal";
		if (saleInvReturn.size() != 0) {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("saleInvReturn", "");
		}

		/**
		 * for(SaleInvoiceReturnModel m : saleInvReturn) {
		 * 
		 * }
		 */

		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		data.put("saleInvReturn", saleInvReturn);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesInvoiceReturnList.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/pdf-saleinvoice-return-note-list", data);
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
