package nirmalya.aathithya.webmodule.production.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
 
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil; 
import nirmalya.aathithya.webmodule.production.model.MotherCoilSlitModel;

@Controller
@RequestMapping(value = "production")
public class ProductionMotherSlitReportController {
	Logger logger = LoggerFactory.getLogger(ProductionMotherSlitReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * get mapping for view payment voucher
	 */
	@GetMapping("/mother-coil-slit-report")
	public String viewMotherCoilSlitReport(Model model, HttpSession session) {

		logger.info("Method : viewMotherCoilSlitReport starts");

		// Drop Down Grade
		try {
			DropDownModel[] MCoilGrade = restClient.getForObject(env.getProduction() + "getMotherCoilGrade",
					DropDownModel[].class);
			List<DropDownModel> GradeList = Arrays.asList(MCoilGrade);

			model.addAttribute("gradeList", GradeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewMotherCoilSlitReport ends");
		return "production/mother-coil-report";
	}

	/*
	 * Generate Pdf For mother coil slit
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/mother-coil-slit-report-download" })
	public void viewMotherCoilSlitReport(HttpServletResponse response, Model model,
			@RequestParam("grade") String encodedParam1, @RequestParam("thickness") String encthickness,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));

		JsonResponse<List<MotherCoilSlitModel>> jsonResponse = new JsonResponse<List<MotherCoilSlitModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		try {

			jsonResponse = restClient.postForObject(env.getProduction() + "getAllMotherCoilSlit", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<MotherCoilSlitModel> motherCoil = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<MotherCoilSlitModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);

		data.put("motherCoil", motherCoil);

		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-PaymentVoucher?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);

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
					env.getProduction() + "restLogoImage?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);

			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName(); 
		data.put("image", variable + "document/hotel/" + background);
		data.put("logoImage", variable + "document/hotel/" + logo);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=MotherCoilSlitReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("production/mother-coil-report-pdf", data);
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

	/*
	 * Generate Pdf For mother coil slit preview
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/mother-coil-slit-report-preview" })
	public String viewMotherCoilSlitReportPriview(HttpServletResponse response, Model model,
			@RequestParam("grade") String encodedParam1, @RequestParam("thickness") String encthickness,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encthickness.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));

		JsonResponse<List<MotherCoilSlitModel>> jsonResponse = new JsonResponse<List<MotherCoilSlitModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		try {

			jsonResponse = restClient.postForObject(env.getProduction() + "getAllMotherCoilSlit", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<MotherCoilSlitModel> motherCoil = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<MotherCoilSlitModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);

		model.addAttribute("pdfCurrentDate", pdfCurrentDate);
		model.addAttribute("motherCoil", motherCoil);
		model.addAttribute("encodedParam1", encodedParam1);
		model.addAttribute("encthickness", encthickness);
		model.addAttribute("encodedParam3", encodedParam3);
		model.addAttribute("encodedParam4", encodedParam4);

		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getProduction() + "restLogoImage?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);

			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();

		String logo = logoList.get(0).getName();

		model.addAttribute("logoImage", variable + "document/hotel/" + logo);

		return "production/mother-coil-report-preview";
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("mother-coil-slit-report-excel")
	public ModelAndView downloadExcelForMotherCoilReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("grade") String encodedParam1, @RequestParam("thickness") String encthickness,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encthickness.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));

		JsonResponse<List<MotherCoilSlitModel>> jsonResponse = new JsonResponse<List<MotherCoilSlitModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		try {

			jsonResponse = restClient.postForObject(env.getProduction() + "getAllMotherCoilSlit", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<MotherCoilSlitModel> motherCoil = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<MotherCoilSlitModel>>() {
					});
			map.put("motherCoil", motherCoil);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AccountLedgerReportController -> downloadExcelForMotherCoilReport GET", e);
		}
		logger.info("Method : downloadExcelForMotherCoilReport ends");
		return new ModelAndView(new ProductionExcelMotherCoilReport(), map);
	}

}
