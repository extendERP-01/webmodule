package nirmalya.aathithya.webmodule.account.controller;


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

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.AccountLedgerModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.filedownload.ExcelAccountLedgerReport;



/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")

public class AccountLedgerController {

	Logger logger = LoggerFactory.getLogger(AccountLedgerController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;


	
	/**
	 * Web Controller - Account Ledger REPORT
	 *
	 */
	@GetMapping("generate-ledger-report")
	public String generateLedgerReport(Model model, HttpSession session) {

		logger.info("Method : generateCVReport starts");

		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restClient.getForObject(env.getAccountUrl() + "restGetLedgerCostCenter",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Account Head
		 *
		 */

		try {
			DropDownModel[] accountHead = restClient.getForObject(env.getAccountUrl() + "restGetLedgerAccountHead",
					DropDownModel[].class);
			List<DropDownModel> accountHeadList = Arrays.asList(accountHead);

			model.addAttribute("accountHeadList", accountHeadList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateCVReport ends");
		return "account/report-Ledger-master";
	}

	/**
	 * download ledger report
	 *
	 */


	@SuppressWarnings("unchecked")
	@GetMapping("/download-ledger-report")
	public void generateLedgerReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4) {
		logger.info("Method : generateLedgerReportPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());


		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
	
		

		JsonResponse<List<AccountLedgerModel>> jsonResponse = new JsonResponse<List<AccountLedgerModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
	
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "generateLedgerReportPdf",
					tableRequest, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-accountLedger?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

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
					env.getAccountUrl() + "restLogoImage-accountLedger?logoType=" + "background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		List<AccountLedgerModel> accountLedger = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountLedgerModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		String printedBy = "Ajad";
		for (AccountLedgerModel m : accountLedger) {
			m.setPrintedBy(printedBy);

		}
	
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		
		String currdate = "";
		if (accountLedger.size() != 0) {

			accountLedger.get(0).setCurDate(curDate);
			currdate = accountLedger.get(0).getCurDate();
			printedBy = accountLedger.get(0).getPrintedBy();

			data.put("image", variable + "image/" + background + "");
				data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", currdate);
			data.put("printedBy", printedBy);
			data.put("accountLedger", accountLedger);
		} else {

			data.put("image", variable + "image/" + background + "");
				data.put("logoImage", variable + "image/" + logo + "");
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=AccountLedgerReport.pdf");
		File file;
		byte[] fileData = null;
		try {

			file = pdfGeneratorUtil.createPdf("account/ledgerReportPdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			//response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("excel-download-ledger-report")
	public ModelAndView downloadExcelForLedgerReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3,	@RequestParam("param4") String encodedPraram4) {
		logger.info("Method : downloadExcelForLedgerReport start");
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
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getLedgerReportListForDownload",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<AccountLedgerModel> accountLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountLedgerModel>>() {
					});
			map.put("accountLedger", accountLedger);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AccountLedgerReportController -> downloadExcelForLedgerReport GET", e);
		}
		logger.info("Method : downloadExcelForLedgerReport ends");
		return new ModelAndView(new ExcelAccountLedgerReport(), map);
	}
		
	
	/*
	 * View Preview page
	 *
	 */

	@GetMapping(value = { "preview-ledger-report" })
	public String previewReport(Model model, @RequestParam("param1") String param1, @RequestParam("param2") String param2,
			@RequestParam("param3") String param3,	@RequestParam("param4") String param4) {
		logger.info("Method : previewReport starts");
		
		
		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);
		model.addAttribute("param3", param3);
		model.addAttribute("param4", param4);
		
		logger.info("Method : previewReport ends");
		return "account/ledgerReportPreview";
	}
	
	
	/*
	 * View Preview page through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/preview-ledger-report-throughAjax")
	public @ResponseBody DataTableResponse viewLedgerReportThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3,	@RequestParam("param4") String encodedPraram4) {
		logger.info("Method : viewLedgerReportThroughAjax starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedPraram4.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			JsonResponse<List<AccountLedgerModel>> jsonResponse = new JsonResponse<List<AccountLedgerModel>>();
			try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "previewReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AccountLedgerModel> accountLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountLedgerModel>>() {
					});

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(accountLedger);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewLedgerReportThroughAjax ends");
		return response;
	}
	

	/*
	 * Preview Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-ledger-report-download-excel")
	public ModelAndView downloadExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String param1, @RequestParam("param2") String param2,
			@RequestParam("param3") String param3,	@RequestParam("param4") String param4) {
		logger.info("Method : downloadExcel start");
		byte[] encodeByte1 = Base64.getDecoder().decode(param1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(param2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(param3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(param4.getBytes());
		String param_1 = (new String(encodeByte1));
		String param_2 = (new String(encodeByte2));
		String param_3 = (new String(encodeByte3));
		String param_4 = (new String(encodeByte4));
	
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param_1);
		tableRequest.setParam2(param_2);
		tableRequest.setParam3(param_3);
		tableRequest.setParam4(param_4);
		try {
			jsonResponse = restClient.postForObject(
					env.getAccountUrl() + "getExcelForDownload", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<AccountLedgerModel> accountLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountLedgerModel>>() {
					});

			map.put("accountLedger", accountLedger);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AccountLedgerReportController -> downloadExcelForLedgerReport GET", e);
		}
		logger.info("Method : downloadExcel ends");
		return new ModelAndView(new ExcelAccountLedgerReport(), map);
	}
	
}
