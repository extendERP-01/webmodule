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

import nirmalya.aathithya.webmodule.filedownload.ExcelDebitLedgerReport;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")

public class DebitLedgerController {

	Logger logger = LoggerFactory.getLogger(DebitLedgerController.class);

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
	@GetMapping("generate-debit-ledger-report")
	public String generateDebitLedgerReport(Model model, HttpSession session) {

		logger.info("Method : generateDebitLedgerReport starts");

		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restClient.getForObject(env.getAccountUrl() + "debitLedgerCostCenter",
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
			DropDownModel[] accountHead = restClient.getForObject(env.getAccountUrl() + "debitLedgerAccountHead",
					DropDownModel[].class);
			List<DropDownModel> accountHeadList = Arrays.asList(accountHead);

			model.addAttribute("accountHeadList", accountHeadList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateDebitLedgerReport ends");
		return "account/debitLedgerReport";
	}

	/**
	 * download ledger report
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/download-debit-ledger-report")
	public void generateDebitLedgerReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {
		logger.info("Method : generateDebitLedgerReportPdf starts");
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

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "generateDebitLedgerReportPdf", tableRequest,
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
					env.getAccountUrl() + "restLogoImage-debitLedger?logoType=" + "header-Logo", DropDownModel[].class);
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
					env.getAccountUrl() + "restLogoImage-debitLedger?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		List<DebitCreditLedgerModel> debitLedger = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<DebitCreditLedgerModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();

		String currdate = "";
		if (debitLedger.size() != 0) {

			debitLedger.get(0).setCurDate(curDate);
			currdate = debitLedger.get(0).getCurDate();
		
			for (DebitCreditLedgerModel m : debitLedger) {
				Double total = m.getCreditAmount() - m.getDebitAmount();
				m.settTotalAmount(total);
		
			}

			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", currdate);
			
		
			data.put("debitLedger", debitLedger);
		} else {

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("currdate", curDate);
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=DebitLedgerReport.pdf");
		File file;
		byte[] fileData = null;
		try {

			file = pdfGeneratorUtil.createPdf("account/debitLedgerReportPdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : generateDebitLedgerReportPdf ends");
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("excel-download-debit-ledger-report")
	public ModelAndView downloadExcelDebitLedgerReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3, @RequestParam("param4") String encodedPraram4) {
		logger.info("Method : downloadExcelDebitLedgerReport start");
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
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getDebitLedgerReportList", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<DebitCreditLedgerModel> debitLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DebitCreditLedgerModel>>() {
					});
			for(DebitCreditLedgerModel m: debitLedger) {
				if(m.gettLedgerType()==1) {
					m.setCreditAmount(m.gettTotalAmount());
					m.setDebitAmount(0.0);
				}else {
					m.setDebitAmount(m.gettTotalAmount());
					m.setCreditAmount(0.0);
				}
			}

			
			System.out.println("debitLedger : "+debitLedger);
			map.put("debitLedger", debitLedger);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=Debit_Ledger" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DebitLedgerController -> downloadExcelDebitLedgerReport GET", e);
		}
		logger.info("Method : downloadExcelDebitLedgerReport ends");
		return new ModelAndView(new ExcelDebitLedgerReport(), map);
	}

	/*
	 * View Preview page
	 *
	 */

	@GetMapping(value = { "preview-debit-ledger-report" })
	public String previewDebitLedgerReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3,
			@RequestParam("param4") String param4) {
		logger.info("Method : previewDebitLedgerReport starts");

		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);
		model.addAttribute("param3", param3);
		model.addAttribute("param4", param4);

		logger.info("Method : previewDebitLedgerReport ends");
		return "account/debitLedgerReportPreview";
	}

	/*
	 * View Preview page through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/preview-debit-ledger-report-throughAjax")
	public @ResponseBody DataTableResponse viewDebitLedgerReportThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3, @RequestParam("param4") String encodedPraram4) {
		logger.info("Method : viewDebitLedgerReportThroughAjax starts");
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
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "previewDebitLedgerReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();
		
			List<DebitCreditLedgerModel> debitLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DebitCreditLedgerModel>>() {
					});
			for (DebitCreditLedgerModel m : debitLedger) {
				Double total = m.getCreditAmount() - m.getDebitAmount();
				m.settTotalAmount(total);
		
				}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(debitLedger);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewDebitLedgerReportThroughAjax ends");
		return response;
	}

}
