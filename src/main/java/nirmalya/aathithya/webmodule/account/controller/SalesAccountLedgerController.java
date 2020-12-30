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

import nirmalya.aathithya.webmodule.account.model.SalesAccountLedgerModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.filedownload.ExcelSalesLedgerReport;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")

public class SalesAccountLedgerController {

	Logger logger = LoggerFactory.getLogger(AccountLedgerController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/**
	 * Web Controller -Sales Account Ledger REPORT
	 *
	 */
	@GetMapping("generate-sales-ledger-report")
	public String generateSalesLedgerReport(Model model, HttpSession session) {

		logger.info("Method : generateSalesLedgerReport starts");

		logger.info("Method : generateSalesLedgerReport ends");
		return "account/salesLedgerReport";
	}

	/**
	 * download ledger report
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/download-sales-ledger-report")
	public void downloadSalesLedgerReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {
		logger.info("Method : downloadSalesLedgerReportPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<SalesAccountLedgerModel>> jsonResponse = new JsonResponse<List<SalesAccountLedgerModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "generateSalesLedgerReportPdf", tableRequest,
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
					env.getAccountUrl() + "restLogoImage-salesLedger?logoType=" + "header-Logo", DropDownModel[].class);
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
					env.getAccountUrl() + "restLogoImage-salesLedger?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		List<SalesAccountLedgerModel> salesLedger = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<SalesAccountLedgerModel>>() {
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
		if (salesLedger.size() != 0) {

			salesLedger.get(0).setCurDate(curDate);
			currdate = salesLedger.get(0).getCurDate();

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", currdate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);

			data.put("salesLedger", salesLedger);
		} else {

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesAccountLedgerReport.pdf");
		File file;
		byte[] fileData = null;
		try {

			file = pdfGeneratorUtil.createPdf("account/salesLedgerReportPdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : downloadSalesLedgerReportPdf ends");
	}

	/*
	 * View sales Preview page
	 *
	 */

	@GetMapping(value = { "preview-sales-ledger-report" })
	public String previewSalesReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2) {
		logger.info("Method : previewSalesReport starts");

		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);

		logger.info("Method : previewSalesReport ends");
		return "account/salesLedgerReportPreview";
	}

	/*
	 * View Preview page through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/preview-sales-ledger-report-throughAjax")
	public @ResponseBody DataTableResponse viewSalesLedgerReportThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2) {
		logger.info("Method : viewSalesLedgerReportThroughAjax starts");
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

		JsonResponse<List<SalesAccountLedgerModel>> jsonResponse = new JsonResponse<List<SalesAccountLedgerModel>>();
		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "previewSalesReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SalesAccountLedgerModel> salesLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesAccountLedgerModel>>() {
					});

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(salesLedger);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewSalesLedgerReportThroughAjax ends");
		return response;
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("excel-download-sales-ledger-report")
	public ModelAndView downloadExcelForSalesLedgerReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2) {
		logger.info("Method : downloadExcelForSalesLedgerReport start");
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
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getSalesLedgerReportForDownload",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<SalesAccountLedgerModel> salesLedger = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesAccountLedgerModel>>() {
					});

			for (SalesAccountLedgerModel m : salesLedger) {
				Double gst = m.gettRVcIGST();
				if (gst == null) {
					m.settRVcIGST(0.0);
				}

			}

			map.put("salesLedger", salesLedger);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SalesAccountLedgerController -> downloadExcelForSalesLedgerReport GET", e);
		}
		logger.info("Method : downloadExcelForSalesLedgerReport ends");
		return new ModelAndView(new ExcelSalesLedgerReport(), map);
	}

}
