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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.production.model.ProductionPipeScrapModel;

@Controller
@RequestMapping(value = "production")
public class ProductionScrapController {

	Logger logger = LoggerFactory.getLogger(ProductionScrapController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * get mapping for view payment voucher
	 */
	@GetMapping("/pipe-scrap-report")
	public String viewScrapReport(Model model, HttpSession session) {

		logger.info("Method : viewScrapReport starts");

		// Drop Down Grade
		try {
			DropDownModel[] MCoilGrade = restClient.getForObject(env.getProduction() + "getMotherCoilGrade",
					DropDownModel[].class);
			List<DropDownModel> GradeList = Arrays.asList(MCoilGrade);

			model.addAttribute("gradeList", GradeList);
		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		logger.info("Method : viewScrapReport ends");
		return "production/pipe-scrap-generate-report";
	}

	/*
	 * Generate Pdf For mother coil slit
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/pipe-scrap-report-download" })
	public void viewScrapReport(HttpServletResponse response, Model model, @RequestParam("grade") String encodedParam1,
			@RequestParam("thickness") String encthickness, @RequestParam("param3") String encodedParam3,
			@RequestParam("param4") String encodedParam4, @RequestParam("slitWidth") String encodedParam5,
			@RequestParam("pipeSize") String encodedParam6) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encthickness.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		byte[] encodeByte6 = Base64.getDecoder().decode(encodedParam6.getBytes());
		String grade = (new String(encodeByte1));
		String thickness = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String slitWidth = (new String(encodeByte5));
		String pipeSize = (new String(encodeByte6));
		JsonResponse<List<ProductionPipeScrapModel>> jsonResponse = new JsonResponse<List<ProductionPipeScrapModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getProduction() + "getAllScrapReports?grade=" + grade
					+ "&thickness=" + thickness + "&startDate=" + param3 + "&endDate=" + param4 + "&slitWidth="
					+ slitWidth + "&pipeSize=" + pipeSize, JsonResponse.class);

		} catch (RestClientException e) { 
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<ProductionPipeScrapModel> scrapList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<ProductionPipeScrapModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);

		data.put("scrapList", scrapList);
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

		data.put("logoImage", variable + "document/hotel/" + logo + "");
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=PipeScrabReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("production/pipe-scrap-generate-report-pdf", data);
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
	 * dropDown value for thickness
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "pipe-scrap-report-getthickness" })

	public @ResponseBody JsonResponse<DropDownModel> getThickness(Model model,

			@RequestBody String grade, BindingResult result) {
		logger.info("Method : getThickness starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getProduction() + "getThicknessByGrade?id=" + grade, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getThickness ends");
		return res;
	}

	/*
	 * dropDown value for thickness
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "pipe-scrap-report-getSlitWidth" })

	public @ResponseBody JsonResponse<DropDownModel> getSlitWidth(

			@RequestParam String grade, @RequestParam String thickness) {
		logger.info("Method : getSlitWidth starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(
					env.getProduction() + "getSlitWidthByThicknessProduction?id=" + grade + "&thickness=" + thickness,
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
		logger.info("Method : getSlitWidth ends");
		return res;
	}

	/*
	 * dropDown value for thickness
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "pipe-scrap-report-getPipeSize" })

	public @ResponseBody JsonResponse<DropDownModel> getPipeSize(

			@RequestParam String grade, @RequestParam String thickness, @RequestParam String slitWidth) {
		logger.info("Method : getPipeSize starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getProduction() + "getPipeSizeBySlitWidth?id=" + grade + "&thickness="
					+ thickness + "&slitWidth=" + slitWidth, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getPipeSize ends");
		return res;
	}

	/*
	 * Generate Pdf For polishing report preview
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/pipe-scrap-report-preview" })
	public String viewPipeProductionReportPriview(HttpServletResponse response, Model model,
			@RequestParam("grade") String encodedParam1, @RequestParam("thickness") String encthickness,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4,
			@RequestParam("slitWidth") String encodedParam5, @RequestParam("pipeSize") String encodedParam6) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encthickness.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		byte[] encodeByte6 = Base64.getDecoder().decode(encodedParam6.getBytes());
		String grade = (new String(encodeByte1));
		String thickness = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String slitWidth = (new String(encodeByte5));
		String pipeSize = (new String(encodeByte6));

		JsonResponse<List<ProductionPipeScrapModel>> jsonResponse = new JsonResponse<List<ProductionPipeScrapModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getProduction() + "getAllScrapReports?grade=" + grade
					+ "&thickness=" + thickness + "&startDate=" + param3 + "&endDate=" + param4 + "&slitWidth="
					+ slitWidth + "&pipeSize=" + pipeSize, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<ProductionPipeScrapModel> scrapList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<ProductionPipeScrapModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);

		model.addAttribute("pdfCurrentDate", pdfCurrentDate);
		model.addAttribute("polishingList", scrapList);
		model.addAttribute("encodedParam1", encodedParam1);
		model.addAttribute("encthickness", encthickness);
		model.addAttribute("encodedParam3", encodedParam3);
		model.addAttribute("encodedParam4", encodedParam4);
		model.addAttribute("encodedParam5", encodedParam5);
		model.addAttribute("encodedParam6", encodedParam6);

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

		return "production/pipe-scrap-generate-report-preview";
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("pipe-scrap-report-excel")
	public ModelAndView downloadExcelForProductionReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("grade") String encodedParam1, @RequestParam("thickness") String encthickness,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4,
			@RequestParam("slitWidth") String encodedParam5, @RequestParam("pipeSize") String encodedParam6) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encthickness.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		byte[] encodeByte6 = Base64.getDecoder().decode(encodedParam6.getBytes());
		String grade = (new String(encodeByte1));
		String thickness = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String slitWidth = (new String(encodeByte5));
		String pipeSize = (new String(encodeByte6));

		Map<String, Object> map = new HashMap<String, Object>();

		JsonResponse<List<ProductionPipeScrapModel>> jsonResponse = new JsonResponse<List<ProductionPipeScrapModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getProduction() + "getAllScrapReports?grade=" + grade
					+ "&thickness=" + thickness + "&startDate=" + param3 + "&endDate=" + param4 + "&slitWidth="
					+ slitWidth + "&pipeSize=" + pipeSize, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPipeScrapModel> scrapList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPipeScrapModel>>() {
					});
			map.put("scrapList", scrapList);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AccountLedgerReportController -> downloadExcelForProductionReport GET", e);
		}
		logger.info("Method : downloadExcelForProductionReport ends");
		return new ModelAndView(new ProductionExcelScrapReport(), map);
	}

}
