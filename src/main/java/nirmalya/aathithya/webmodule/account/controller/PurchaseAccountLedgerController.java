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

import nirmalya.aathithya.webmodule.account.model.PurchaseAccountLedgerModel;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.filedownload.ExcelPurchaseLedgerReport;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")

public class PurchaseAccountLedgerController {

	Logger logger = LoggerFactory.getLogger(AccountLedgerController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/**
	 * Web Controller - Purchase Account Ledger REPORT
	 *
	 */
	@GetMapping("generate-purchase-ledger-report")
	public String generatePurchaseLedgerReport(Model model, HttpSession session) {

		logger.info("Method : generatePurchaseLedgerReport starts");

		logger.info("Method : generatePurchaseLedgerReport ends");
		return "account/purchaseLedgerReport";
	}

	/**
	 * download ledger report
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/download-purchase-ledger-report")
	public void downloadPurchaseLedgerReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {
		logger.info("Method : downloadPurchaseLedgerReportPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<PurchaseAccountLedgerModel>> jsonResponse = new JsonResponse<List<PurchaseAccountLedgerModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "generatePurchaseLedgerReportPdf",
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
					env.getAccountUrl() + "restLogoImage-purchaseLedger?logoType=" + "header-Logo",
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
					env.getAccountUrl() + "restLogoImage-purchaseLedger?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		List<PurchaseAccountLedgerModel> purchaseLedger = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<PurchaseAccountLedgerModel>>() {
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
		if (purchaseLedger.size() != 0) {

			purchaseLedger.get(0).setCurDate(curDate);
			currdate = purchaseLedger.get(0).getCurDate();
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", currdate);

			data.put("purchaseLedger", purchaseLedger);
		} else {

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("currdate", curDate);
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=PurchaseAccountLedgerReport.pdf");
		File file;
		byte[] fileData = null;
		try {

			file = pdfGeneratorUtil.createPdf("account/purchaseLedgerReportPdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : downloadPurchaseLedgerReportPdf ends");
	}

	/*
	 * View sales Preview page
	 *
	 */

	@GetMapping(value = { "preview-purchase-ledger-report" })
	public String previewPurchaseReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2) {
		logger.info("Method : previewPurchaseReport starts");

		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);

		logger.info("Method : previewPurchaseReport ends");
		return "account/purchaseLedgerReportPreview";
	}

	/*
	 * View Preview page through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/preview-purchase-ledger-reportt-throughAjax")
	public @ResponseBody DataTableResponse viewPurchaseLedgerReportThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2) {
		logger.info("Method : viewPurchaseLedgerReportThroughAjax starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);

		JsonResponse<List<PurchaseAccountLedgerModel>> jsonResponse = new JsonResponse<List<PurchaseAccountLedgerModel>>();
		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "previewPurchaseReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PurchaseAccountLedgerModel> purchaseLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PurchaseAccountLedgerModel>>() {
					});

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(purchaseLedger);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewPurchaseLedgerReportThroughAjax ends");
		return response;
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("excel-download-purchase-ledger-report")
	public ModelAndView downloadExcelForPurchaseLedgerReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2) {
		logger.info("Method : downloadExcelForPurchaseLedgerReport start");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);

		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getPurchaseLedgerReportForDownload",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<PurchaseAccountLedgerModel> purchaseLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PurchaseAccountLedgerModel>>() {
					});
			map.put("purchaseLedger", purchaseLedger);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PurchaseAccountLedgerController -> downloadExcelForPurchaseLedgerReport GET", e);
		}
		logger.info("Method : downloadExcelForPurchaseLedgerReport ends");
		return new ModelAndView(new ExcelPurchaseLedgerReport(), map);
	}

}
