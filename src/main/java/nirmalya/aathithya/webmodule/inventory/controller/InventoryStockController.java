package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.filedownload.InventoryStockReportExcel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryStockDailyReportFinalModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryStockModel;

/*
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryStockController {
	Logger logger = LoggerFactory.getLogger(InventoryStockController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("view-stock")
	public String generateInventoryStockReport(Model model, HttpSession session) {

		logger.info("Method : generateInventoryStockReport starts");

		logger.info("Method : generateInventoryStockReport ends");
		return "inventory/view-stock";

	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-stock-trough-ajax" })
	public @ResponseBody List<InventoryStockModel> viewStockThroughAjax(Model model, HttpServletRequest request,
			/*
			 * @RequestParam String param1, @RequestParam String param2, @RequestParam
			 * String param3,
			 */ HttpSession session) {
		logger.info("Method : viewStockThroughAjax starts");
		JsonResponse<List<InventoryStockModel>> jsonResponse = new JsonResponse<List<InventoryStockModel>>();

		DataTableRequest tableRequest = new DataTableRequest();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {

			tableRequest.setUserId(userId);

			jsonResponse = restTemplate.postForObject(env.getInventoryUrl() + "get-stock-details", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryStockModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryStockModel>>() {
					});

			jsonResponse.setBody(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewStockThroughAjax ends");
		return jsonResponse.getBody();
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-stock-daily-report" })
	public @ResponseBody JsonResponse<InventoryStockDailyReportFinalModel> getSalesReportGraph(@RequestParam String id,
			HttpSession session) {
		logger.info("Method : getSalesReportGraph starts");
		JsonResponse<InventoryStockDailyReportFinalModel> res = new JsonResponse<InventoryStockDailyReportFinalModel>();

		try {
			String userId = (String) session.getAttribute("USER_ID");
			res = restTemplate.getForObject(
					env.getInventoryUrl() + "view-stock-daily-report?id=" + id + "&empId=" + userId,
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
		logger.info("Method : getSalesReportGraph ends");

		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("view-stock-daily-report-excel")
	public ModelAndView generateStockChartExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam String id) {
		logger.info("Method : generateStockChartExcel start");

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			String userId = (String) session.getAttribute("USER_ID");
			jsonResponse = restTemplate.getForObject(
					env.getInventoryUrl() + "view-stock-daily-report?id=" + id + "&empId=" + userId,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			InventoryStockDailyReportFinalModel excelData = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<InventoryStockDailyReportFinalModel>() {
					});

			map.put("excelData", excelData);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition",
					"attachment; filename= Stock_Report_" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EmployeeReportController -> generateStockChartExcel GET", e);
		}
		logger.info("Method : generateStockChartExcel ends");
		return new ModelAndView(new InventoryStockReportExcel(), map);
	}
}
