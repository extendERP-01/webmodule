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

import nirmalya.aathithya.webmodule.account.model.IncomeTaxPayableModel;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.filedownload.ExcelIncomeTaxPayableReport;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")

public class IncomeTaxPayableController {

	Logger logger = LoggerFactory.getLogger(IncomeTaxPayableController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/**
	 * Web Controller -Income Tax Payable REPORT
	 *
	 */
	@GetMapping("generate-income-tax-payable-report")
	public String generateIncomeTaxPayableReport(Model model, HttpSession session) {

		logger.info("Method : generateIncomeTaxPayableReport starts");

		logger.info("Method : generatePurchaseLedgerReport ends");
		return "account/incomeTaxPayableReport";
	}

	/**
	 * download Income Tax Payable REPORT
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/download-income-tax-payable-report")
	public void downloadIncomeTaxPayableReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {
		logger.info("Method : downloadIncomeTaxPayableReportPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<IncomeTaxPayableModel>> jsonResponse = new JsonResponse<List<IncomeTaxPayableModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "generateIncomeTaxPayableReportPdf",
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
					env.getAccountUrl() + "restLogoImage-incomeTaxPayable?logoType=" + "header-Logo",
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
					env.getAccountUrl() + "restLogoImage-incomeTaxPayable?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		List<IncomeTaxPayableModel> incomeTaxPayable = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<IncomeTaxPayableModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		double finalAmount = 0.0;

		for (IncomeTaxPayableModel a : incomeTaxPayable) {

			finalAmount = (a.gettPymntTaxableAmount() - a.gettPymntTDSAmount());
			a.settPymntTotalAmount(finalAmount);

		}

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();

		String currdate = "";
		if (incomeTaxPayable.size() != 0) {

			incomeTaxPayable.get(0).setCurDate(curDate);
			currdate = incomeTaxPayable.get(0).getCurDate();

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", currdate);

			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("incomeTaxPayable", incomeTaxPayable);

		} else {

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=IncomeTaxPayableReportPdf.pdf");
		File file;
		byte[] fileData = null;
		try {

			file = pdfGeneratorUtil.createPdf("account/incomeTaxPayableReportPdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : downloadIncomeTaxPayableReportPdf ends");
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("excel-download-income-tax-payable-report")
	public ModelAndView downloadExcelForIncomeTaxReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2) {
		logger.info("Method : downloadExcelForIncomeTaxReport start");
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
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "downloadExcelForIncomeTaxReport",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<IncomeTaxPayableModel> incomeTaxPayable = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<IncomeTaxPayableModel>>() {
					});
			map.put("incomeTaxPayable", incomeTaxPayable);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IncomeTaxPayableController -> downloadExcelForIncomeTaxReport GET", e);
		}
		logger.info("Method : downloadExcelForIncomeTaxReport ends");
		return new ModelAndView(new ExcelIncomeTaxPayableReport(), map);
	}

	/*
	 * View sales Preview page
	 *
	 */

	@GetMapping(value = { "preview-income-tax-payable-report" })
	public String previewIncomeTaxPayableReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2) {
		logger.info("Method : previewIncomeTaxPayableReport starts");

		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);

		logger.info("Method : previewIncomeTaxPayableReport ends");
		return "account/incomeTaxPayableReportPreview";
	}

	/*
	 * View Preview page through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/preview-income-tax-payable-report-throughAjax")
	public @ResponseBody DataTableResponse viewIncomeTaxPayableReportThroughAjax(Model model,
			HttpServletRequest request, @RequestParam("param1") String encodedPraram1,
			@RequestParam("param2") String encodedPraram2) {
		logger.info("Method : viewIncomeTaxPayableReportThroughAjax starts");
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

		JsonResponse<List<IncomeTaxPayableModel>> jsonResponse = new JsonResponse<List<IncomeTaxPayableModel>>();
		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "previewIncomeTaxPayableReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<IncomeTaxPayableModel> incomeTaxPayable = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<IncomeTaxPayableModel>>() {
					});

			double finalAmount = 0.0;

			for (IncomeTaxPayableModel a : incomeTaxPayable) {

				finalAmount = (a.gettPymntTaxableAmount() - a.gettPymntTDSAmount());
				a.settPymntTotalAmount(finalAmount);

			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(incomeTaxPayable);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewIncomeTaxPayableReportThroughAjax ends");
		return response;
	}

}
