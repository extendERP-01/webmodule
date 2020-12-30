package nirmalya.aathithya.webmodule.production.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import nirmalya.aathithya.webmodule.common.utils.StringConstands;
import nirmalya.aathithya.webmodule.production.excel.PlanningExcelReport;
import nirmalya.aathithya.webmodule.production.model.ProductionSummaryDetailsModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "production")
public class ProductionPlanningRawMaterialReportController {
	Logger logger = LoggerFactory.getLogger(ProductionPlanningRawMaterialReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * Get Mapping for add employee dependent
	 */

	@GetMapping("planning-report")
	public String itemReqReport(Model model, HttpSession session) {

		logger.info("Method : itemReqReport starts");
		/**
		 * Get DropDown Value Store List
		 *
		 */
		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : itemReqReport ends");
		return "production/gocool-production-planning-report";
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("planning-report-excel")
	public ModelAndView downloadExcelForPlanning(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {
		logger.info("Method : downloadExcelForPlanning starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<List<ProductionSummaryDetailsModel>> jsonResponse = new JsonResponse<List<ProductionSummaryDetailsModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		try {

			jsonResponse = restClient.postForObject(env.getProduction() + "get-plannings-raw-material-reports",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionSummaryDetailsModel> itemReqiList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionSummaryDetailsModel>>() {
					});

			map.put("itemReqiList", itemReqiList);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition",
					"attachment; filename=" + "Reqi_Item" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("downloadExcelForItemRequisition -> ItemWiseReqReport GET", e);
		}
		logger.info("Method : downloadExcelForPlanning ends");
		return new ModelAndView(new PlanningExcelReport(), map);
	}
}
