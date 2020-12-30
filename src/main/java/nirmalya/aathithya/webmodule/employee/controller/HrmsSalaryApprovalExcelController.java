package nirmalya.aathithya.webmodule.employee.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.excel.AdvancePaymentExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.AttendanceExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.EpfExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.EsicExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.ExtraDayExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.FinalSalaryExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.FoodDetailsExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.IncomeTaxDetailsExcel;
import nirmalya.aathithya.webmodule.employee.excel.LeaveExcelReport;
import nirmalya.aathithya.webmodule.employee.excel.TripBonusExcelReport;
import nirmalya.aathithya.webmodule.employee.model.EmployeeFoodTrackingModel;
import nirmalya.aathithya.webmodule.employee.model.EmployeeIncomeTaxDetails;
import nirmalya.aathithya.webmodule.employee.model.HrmsAdvancePaymentModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsAttendanceApprovalModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeBonousModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsEpfExcelModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsEsicExcelModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsExtraSalaryModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsLeaveApprovalModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsSalaryModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "download/")
public class HrmsSalaryApprovalExcelController {
	Logger logger = LoggerFactory.getLogger(HrmsEmployeeSalaryController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	// Excel File Creation
	@SuppressWarnings("unchecked")
	@GetMapping("generate-advance-payment-excel-report")
	public ModelAndView downloadExcelReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : downloadExcelReport start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAdvancePaymentDetailsExcel",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsAdvancePaymentModel> consumption = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsAdvancePaymentModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename= " + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ConsumptionReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : downloadExcelReport ends");
		return new ModelAndView(new AdvancePaymentExcelReport(), map);
	}

	// Excel File Creation
	@SuppressWarnings("unchecked")
	@GetMapping("generate-excel-trip-report")
	public ModelAndView generateTripExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : generateTripExcel start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getTripExcelReports", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsEmployeeBonousModel> consumption = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeBonousModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition",
					"attachment; filename= TripControl" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("TripReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : downloadExcelReport ends");
		return new ModelAndView(new TripBonusExcelReport(), map);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("generate-attendence-report")
	public ModelAndView generateAttendanceExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : generateAttendanceExcel start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "generateAttendanceExcel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsAttendanceApprovalModel> consumption = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsAttendanceApprovalModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition",
					"attachment; filename= EmployeeControl" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EmployeeReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : generateAttendanceExcel ends");
		return new ModelAndView(new AttendanceExcelReport(), map);
	}
	@SuppressWarnings("unchecked")
	@GetMapping("generate-leave-report")
	public ModelAndView generateLeaveExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : generateLeaveExcel start");
		
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());
		
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		
		try {
			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "generateLeaveExcel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsLeaveApprovalModel> consumption = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsLeaveApprovalModel>>() {
			});
			
			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition",
					"attachment; filename= EmployeeControl" + new Date().getTime() + ".xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EmployeeReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : generateAttendanceExcel ends");
		return new ModelAndView(new LeaveExcelReport(), map);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("generate-food-excel-report")
	public ModelAndView generateFoodDetailsExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : downloadExcelReport start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResp = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResp = restClient.postForObject(env.getEmployeeUrl() + "getFoodDetailsExcel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<EmployeeFoodTrackingModel> consumption = mapper.convertValue(jsonResp.getBody(),
					new TypeReference<List<EmployeeFoodTrackingModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename= " + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ConsumptionReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : downloadExcelReport ends");
		return new ModelAndView(new FoodDetailsExcelReport(), map);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("generate-extra-sal-excel-report")
	public ModelAndView generateExtraSalaryDetailsExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : downloadExcelReport start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResp = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResp = restClient.postForObject(env.getEmployeeUrl() + "getExtraSalDetailsExcel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsExtraSalaryModel> consumption = mapper.convertValue(jsonResp.getBody(),
					new TypeReference<List<HrmsExtraSalaryModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename= " + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ConsumptionReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : downloadExcelReport ends");
		return new ModelAndView(new ExtraDayExcelReport(), map);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("generate-income-tax-excel-report")
	public ModelAndView generateIncomeTaxDetailsExcel(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : downloadExcelReport start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResp = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResp = restClient.postForObject(env.getEmployeeUrl() + "getIncomeTaxDetailsExcel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<EmployeeIncomeTaxDetails> consumption = mapper.convertValue(jsonResp.getBody(),
					new TypeReference<List<EmployeeIncomeTaxDetails>>() {
					});

			map.put("consumption", consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename= " + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ConsumptionReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : downloadExcelReport ends");
		return new ModelAndView(new IncomeTaxDetailsExcel(), map);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("generate-salary-final-report")
	public ModelAndView generateFinalSalary(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : generateFinalSalary start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResp = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResp = restClient.postForObject(env.getEmployeeUrl() + "getFinalSalaryExcel", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper(); 
			List<HrmsSalaryModel> consumption = mapper.convertValue(jsonResp.getBody(),
					new TypeReference<List<HrmsSalaryModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename= " + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ConsumptionReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : generateFinalSalary ends");
		return new ModelAndView(new FinalSalaryExcelReport(), map);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("generate-epf-excel-report")
	public ModelAndView generateEpfExcelReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : generateEpfExcelReport start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResp = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResp = restClient.postForObject(env.getEmployeeUrl() + "generateEpfExcelReport", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsEpfExcelModel> consumption = mapper.convertValue(jsonResp.getBody(),
					new TypeReference<List<HrmsEpfExcelModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename= " + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ConsumptionReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : generateEpfExcelReport ends");
		return new ModelAndView(new EpfExcelReport(), map);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("generate-esic-excel-report")
	public ModelAndView generateEsicExcelReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param2") String encodedPraram2, @RequestParam("param3") String encodedPraram3) {
		logger.info("Method : generateEsicExcelReport start");

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());

		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResp = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResp = restClient.postForObject(env.getEmployeeUrl() + "generateEsicExcelReport", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsEsicExcelModel> consumption = mapper.convertValue(jsonResp.getBody(),
					new TypeReference<List<HrmsEsicExcelModel>>() {
					});

			map.put("consumption", consumption);
			System.out.println(consumption);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename= " + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ConsumptionReportController -> downloadExcelReport GET", e);
		}
		logger.info("Method : generateEsicExcelReport ends");
		return new ModelAndView(new EsicExcelReport(), map);
	}
}
