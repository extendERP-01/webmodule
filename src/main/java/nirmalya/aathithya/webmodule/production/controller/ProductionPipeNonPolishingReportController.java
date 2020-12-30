package nirmalya.aathithya.webmodule.production.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.production.model.ProductionPipeNonpolishModel;

@Controller
@RequestMapping(value = "production")
public class ProductionPipeNonPolishingReportController {
	Logger logger = LoggerFactory.getLogger(ProductionPipeNonPolishingReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("/production-pipe-nonpolish-report")
	public String viewProductionPipeNonPolishReport(Model model, HttpSession session) {

		logger.info("Method : viewProductionPipeNonPolishReport starts");

		logger.info("Method : viewProductionPipeNonPolishReport ends");
		return "production/production-pipe-nonpolish-report";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/production-pipe-nonpolish-report-throughajax")
	public @ResponseBody DataTableResponse productionNonPolishingGenerateReportThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : productionNonPolishingGenerateReportThroughAjax starts");

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

			JsonResponse<List<ProductionPipeNonpolishModel>> jsonResponse = new JsonResponse<List<ProductionPipeNonpolishModel>>();
			jsonResponse = restClient.postForObject(env.getProduction() + "getNonPolishReport", tableRequest,
					JsonResponse.class);
			System.out.println(jsonResponse);
			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPipeNonpolishModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPipeNonpolishModel>>() {
					});

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : productionNonPolishingGenerateReportThroughAjax end");

		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/production-pipe-nonpolish-report-pdf")
	public void getProductionPipeNonPolishReportPDF(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<ProductionPipeNonpolishModel>> jsonResponse = new JsonResponse<List<ProductionPipeNonpolishModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			jsonResponse = restClient.postForObject(env.getProduction() + "getNonPolishReportPDF", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<ProductionPipeNonpolishModel> productionPipeNonpolishModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<ProductionPipeNonpolishModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		// String s = "";

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		String printedBy = "SRADHA";
		if (productionPipeNonpolishModel.size() != 0) {

			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("productionPipeNonpolishModel", "");
		}

		data.put("productionPipeNonpolishModel", productionPipeNonpolishModel);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=productionNonpolish.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("production/productionNonpolishPdf", data);
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
	@GetMapping("production-pipe-nonpolish-report-excel")
	public ModelAndView downloadExcelForNonPolishingReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2) {
		logger.info("Method : downloadExcelForNonPolishingReport start");
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
			jsonResponse = restClient.postForObject(env.getProduction() + "getNonPolishReportPDF", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<ProductionPipeNonpolishModel> productionPipeNonpolishModel = mapper
					.convertValue(jsonResponse.getBody(), new TypeReference<List<ProductionPipeNonpolishModel>>() {
					});

			map.put("productionPipeNonpolishModel", productionPipeNonpolishModel);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PropertyBookingController -> downloadExcel GET", e);
		}
		logger.info("Method : downloadExcelForNonPolishingReport ends");
		return new ModelAndView(new ExcelNonPolishingReport(), map);
	}
}
