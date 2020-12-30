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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.production.model.MotherCoilSlitReportModel;

@Controller
@RequestMapping(value = "production")
public class MotherCoilSlitReportController {
	Logger logger = LoggerFactory.getLogger(MotherCoilSlitReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("/moter-coil-slit-batch-report")
	public String viewMotorCoilSlitBatchReport(Model model, HttpSession session) {

		logger.info("Method : viewMotorCoilSlitBatchReport starts");

		logger.info("Method : viewMotorCoilSlitBatchReport ends");
		return "production/moter-coil-slit-batch-report";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/moter-coil-slit-batch-report-throughajax")
	public @ResponseBody DataTableResponse motherCoilSlitGenerateReportThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : motherCoilSlitGenerateReportThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			JsonResponse<List<MotherCoilSlitReportModel>> jsonResponse = new JsonResponse<List<MotherCoilSlitReportModel>>();
			jsonResponse = restClient.postForObject(env.getProduction() + "getMotherCoilBatchReport", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();

			List<MotherCoilSlitReportModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<MotherCoilSlitReportModel>>() {
					});
 

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		logger.info("Method : motherCoilSlitGenerateReportThroughAjax end");

		return response;
	}

	/*
	 * Generate Pdf For mother coil slit
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/moter-coil-slit-batch-report-download" })
	public void viewMotherCoilSlitReport(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<MotherCoilSlitReportModel>> jsonResponse = new JsonResponse<List<MotherCoilSlitReportModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		try {

			jsonResponse = restClient.postForObject(env.getProduction() + "getMotherCoilBatchReportPDF", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) { 
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<MotherCoilSlitReportModel> motherCoilRpt = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<MotherCoilSlitReportModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);
		data.put("motherCoilRpt", motherCoilRpt);

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
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=MotherCoilSlitReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("production/motherCoilSlitBatchReportPdf", data);
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

	@SuppressWarnings("unchecked")
	@GetMapping("/moter-coil-slit-batch-report-excel")
	public ModelAndView downloadMotherCoilSlitBatchExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2) {
		logger.info("Method : downloadExcel start");
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
			jsonResponse = restClient.postForObject(env.getProduction() + "getMotherCoilBatchReportPDF", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<MotherCoilSlitReportModel> motherCoilSlitReportModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<MotherCoilSlitReportModel>>() {
					});

			map.put("motherCoilSlitReportModel", motherCoilSlitReportModel);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PropertyBookingController -> downloadExcel GET", e);
		}
		logger.info("Method : downloadExcel ends");
		return new ModelAndView(new ExcelMotherCoilSlitBatchReport(), map);
	}
}
