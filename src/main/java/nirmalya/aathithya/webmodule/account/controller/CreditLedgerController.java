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

import nirmalya.aathithya.webmodule.account.model.DebitCreditLedgerModel;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.filedownload.ExcelCreditLedgerReport;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")

public class CreditLedgerController {
	Logger logger = LoggerFactory.getLogger(AccountLedgerController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/**
	 * Web Controller - Credit Ledger Report
	 *
	 */
	@GetMapping("generate-credit-ledger-report")
	public String generateCreditLedgerReport(Model model, HttpSession session) {

		logger.info("Method : generateCreditLedgerReport starts");

		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restClient.getForObject(env.getAccountUrl() + "creditLedgerCostCenter",
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
			DropDownModel[] accountHead = restClient.getForObject(env.getAccountUrl() + "creditLedgerAccountHead",
					DropDownModel[].class);
			List<DropDownModel> accountHeadList = Arrays.asList(accountHead);

			model.addAttribute("accountHeadList", accountHeadList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateCreditLedgerReport ends");
		return "account/creditLedgerReport";
	}

	/**
	 * download ledger report
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/download-credit-ledger-report")
	public void generateCreditLedgerReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {
		logger.info("Method : generateCreditLedgerReportPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
	

		JsonResponse<List<DebitCreditLedgerModel>> jsonResponse = new JsonResponse<List<DebitCreditLedgerModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "generateCreditLedgerReportPdf", tableRequest,
					JsonResponse.class);

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
					env.getAccountUrl() + "restLogoImage-creditLedger?logoType=" + "header-Logo",
					DropDownModel[].class);
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
					env.getAccountUrl() + "restLogoImage-creditLedger?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		List<DebitCreditLedgerModel> creditLedger = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<DebitCreditLedgerModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		String printedBy = "Ajad";
		for (DebitCreditLedgerModel m : creditLedger) {
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
		if (creditLedger.size() != 0) {

			creditLedger.get(0).setCurDate(curDate);
			currdate = creditLedger.get(0).getCurDate();
			printedBy = creditLedger.get(0).getPrintedBy();

			for (DebitCreditLedgerModel m : creditLedger) {
				Double total = m.getCreditAmount() - m.getDebitAmount();
				m.settTotalAmount(total);
		
			
			}

			

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", currdate);
			data.put("printedBy", printedBy);
		
			data.put("creditLedger", creditLedger);
		} else {

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=CreditLedgerReport.pdf");
		File file;
		byte[] fileData = null;
		try {

			file = pdfGeneratorUtil.createPdf("account/creditLedgerReportPdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			// response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : generateCreditLedgerReportPdf ends");
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("excel-download-credit-ledger-report")
	public ModelAndView downloadExcelCreditLedgerReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3, @RequestParam("param4") String encodedPraram4) {
		logger.info("Method : downloadExcelCreditLedgerReport start");
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
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getCreditLedgerReportList", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<DebitCreditLedgerModel> creditLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DebitCreditLedgerModel>>() {
					});
		
			for(DebitCreditLedgerModel m: creditLedger) {
				if(m.gettLedgerType()==1) {
					m.setCreditAmount(m.gettTotalAmount());
					m.setDebitAmount(0.0);
				}else {
					m.setDebitAmount(m.gettTotalAmount());
					m.setCreditAmount(0.0);
				}
			}
			
			
			System.out.println("creditLedger : "+creditLedger);
			map.put("creditLedger", creditLedger);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=Credit_Ledger" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CreditLedgerController -> downloadExcelCreditLedgerReport GET", e);
		}
		
		logger.info("Method : downloadExcelCreditLedgerReport ends");
		return new ModelAndView(new ExcelCreditLedgerReport(), map);
	}

	/*
	 * View Preview page
	 *
	 */

	@GetMapping(value = { "preview-credit-ledger-report" })
	public String previewCreditLedgerReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3,
			@RequestParam("param4") String param4) {
		logger.info("Method : previewCreditLedgerReport starts");

		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);
		model.addAttribute("param3", param3);
		model.addAttribute("param4", param4);

		logger.info("Method : previewCreditLedgerReport ends");
		return "account/creditLedgerReportPreview";
	}

	/*
	 * View Preview page through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/preview-credit-ledger-report-throughAjax")
	public @ResponseBody DataTableResponse viewCreditLedgerReportThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3, @RequestParam("param4") String encodedPraram4) {
		logger.info("Method : viewCreditLedgerReportThroughAjax starts");
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
		JsonResponse<List<DebitCreditLedgerModel>> jsonResponse = new JsonResponse<List<DebitCreditLedgerModel>>();
		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "previewCreditLedgerReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<DebitCreditLedgerModel> creditLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DebitCreditLedgerModel>>() {
					});
			for (DebitCreditLedgerModel m : creditLedger) {
			Double total = m.getCreditAmount() - m.getDebitAmount();
			m.settTotalAmount(total);
				
	
			}


			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(creditLedger);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewCreditLedgerReportThroughAjax ends");
		return response;
	}

}
