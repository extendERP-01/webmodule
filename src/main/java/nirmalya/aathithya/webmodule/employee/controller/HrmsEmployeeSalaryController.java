package nirmalya.aathithya.webmodule.employee.controller;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.EmployeeFoodTrackingModel;
import nirmalya.aathithya.webmodule.employee.model.EmployeeIncomeTaxDetails;
import nirmalya.aathithya.webmodule.employee.model.HrmsAdvancePaymentModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsAttendanceApprovalModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeBonousModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsExtraSalaryModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsLeaveApprovalModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsSalaryApproveCountModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsSalaryModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeSalaryController {
	Logger logger = LoggerFactory.getLogger(HrmsEmployeeSalaryController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Default 'add production planning ' page
	 *
	 */

	@GetMapping("/add-salary")
	public String addSalary(Model model, HttpSession session) {

		logger.info("Method : addSalary starts");

		HrmsSalaryModel productiionPlanning = new HrmsSalaryModel();
		HrmsSalaryModel sessionProductiionPlanning = (HrmsSalaryModel) session.getAttribute("sesssionSalaryList");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (sessionProductiionPlanning != null) {
			model.addAttribute("productionPlanning", sessionProductiionPlanning);
			session.setAttribute("sessionDeliveryChalanModel", null);
		} else {
			model.addAttribute("productionPlanning", productiionPlanning);
		}

		logger.info("Method : addSalary ends");

		return "employee/add-salary";
	}

	/*
	 * get production details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-salary-search-by-emp" })
	public @ResponseBody JsonResponse<HrmsSalaryModel> getSalaryDetails(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String employeeName) {
		logger.info("Method : getSalaryDetails starts");

		JsonResponse<HrmsSalaryModel> res = new JsonResponse<HrmsSalaryModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "search-by-emp?fromDate=" + fromDate + "&toDate="
					+ toDate + "&employeeName=" + employeeName, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getSalaryDetails  ends");
		return res;
	}

	/*
	 * get salary details
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-salary-get-salary-details" })
	public @ResponseBody JsonResponse<HrmsSalaryModel> getDetailsSalary(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String status) {
		logger.info("Method : getDetailsSalary starts");

		JsonResponse<HrmsSalaryModel> res = new JsonResponse<HrmsSalaryModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getSalaryDetails?fromDate=" + fromDate + "&toDate="
					+ toDate + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getSalaryDetails  ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-salary-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveSalaryData(@RequestBody List<HrmsSalaryModel> hrmsSalaryModelList,
			Model model, HttpSession session) {
		logger.info("Method : saveSalaryData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveSalaryData", hrmsSalaryModelList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveSalaryData function Ends");
		return res;
	}

	/**
	 * Default 'add production planning ' page
	 *
	 */

	@GetMapping("/salary-approval")
	public String gatSalaryApproval(Model model, HttpSession session) {

		logger.info("Method : gatSalaryApproval starts");

		logger.info("Method : gatSalaryApproval ends");

		return "employee/salary-approval-details";
	}

	/*
	 * get trip details details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/salary-approval-get-trip-dtls" })
	public @ResponseBody JsonResponse<HrmsEmployeeBonousModel> getTripData(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String status) {
		logger.info("Method : getTripData starts");

		JsonResponse<HrmsEmployeeBonousModel> res = new JsonResponse<HrmsEmployeeBonousModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getTripData?fromDate=" + fromDate + "&toDate="
					+ toDate + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getTripData  ends");
		return res;
	}

	/*
	 * get food details details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/salary-approval-get-food-dtls" })
	public @ResponseBody JsonResponse<EmployeeFoodTrackingModel> getFoodData(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String empId, @RequestParam String status) {
		logger.info("Method : getFoodData starts");

		JsonResponse<EmployeeFoodTrackingModel> res = new JsonResponse<EmployeeFoodTrackingModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getFoodData?fromDate=" + fromDate + "&toDate="
					+ toDate + "&empId=" + empId + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getFoodData  ends");
		return res;
	}

	/*
	 * get extra salary details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/salary-approval-get-extra-salary-dtls" })
	public @ResponseBody JsonResponse<HrmsExtraSalaryModel> getExtraSalary(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String empId, @RequestParam String status) {
		logger.info("Method : getExtraSalary starts");

		JsonResponse<HrmsExtraSalaryModel> res = new JsonResponse<HrmsExtraSalaryModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getExtraSalary?fromDate=" + fromDate + "&toDate="
					+ toDate + "&empId=" + empId + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getExtraSalary  ends");
		return res;
	}

	/*
	 * get attendance salary details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/salary-approval-get-attendance-dtls" })
	public @ResponseBody JsonResponse<HrmsAttendanceApprovalModel> getAttendanceDetails(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String empId, @RequestParam String status) {
		logger.info("Method : getAttendanceDetails starts");

		JsonResponse<HrmsAttendanceApprovalModel> res = new JsonResponse<HrmsAttendanceApprovalModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getAttendanceDetails?fromDate=" + fromDate
					+ "&toDate=" + toDate + "&empId=" + empId + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAttendanceDetails  ends");
		return res;
	}

	/*
	 * get advance salary details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/salary-approval-get-advance-dtls" })
	public @ResponseBody JsonResponse<HrmsAdvancePaymentModel> getAdvanceDetails(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String empId, @RequestParam String status) {
		logger.info("Method : getAdvanceDetails starts");

		JsonResponse<HrmsAdvancePaymentModel> res = new JsonResponse<HrmsAdvancePaymentModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getAdvanceDetails?fromDate=" + fromDate + "&toDate="
					+ toDate + "&empId=" + empId + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAdvanceDetails  ends");
		return res;
	}

	/*
	 * get income tax details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/salary-approval-get-income-tax-dtls" })
	public @ResponseBody JsonResponse<EmployeeIncomeTaxDetails> getIncomeTaxDetails(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String empId, @RequestParam String status) {
		logger.info("Method : getAdvanceDetails starts");

		JsonResponse<EmployeeIncomeTaxDetails> res = new JsonResponse<EmployeeIncomeTaxDetails>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getIncomeTaxDetails?fromDate=" + fromDate + "&toDate="
					+ toDate + "&empId=" + empId + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getIncomeTaxDetails  ends");
		return res;
	}

	/*
	 * save advance details
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "salary-approval-save-advance-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveAdvanceData(
			@RequestBody List<HrmsAdvancePaymentModel> hrmsAdvanceList, Model model, HttpSession session) {
		logger.info("Method : saveAdvanceData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveAdvanceData", hrmsAdvanceList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveAdvanceData function Ends");
		return res;
	}

	/*
	 * save advance details
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "salary-approval-save-income-tax-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveIncomeTaxData(
			@RequestBody List<EmployeeIncomeTaxDetails> hrmsAdvanceList, Model model, HttpSession session) {
		logger.info("Method : saveIncomeTaxData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveIncomeTaxApproveData", hrmsAdvanceList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveIncomeTaxData function Ends");
		return res;
	}

	/*
	 * save advance details
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "salary-approval-save-trip-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveTripData(
			@RequestBody List<HrmsEmployeeBonousModel> HrmsEmployeeBonousModelList, Model model, HttpSession session) {
		logger.info("Method : saveAdvanceData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveTripData", HrmsEmployeeBonousModelList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveAdvanceData function Ends");
		return res;
	}

	/*
	 * save advance details
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "salary-approval-save-food-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveFoodData(
			@RequestBody List<EmployeeFoodTrackingModel> employeeFoodTrackingModelList, Model model,
			HttpSession session) {
		logger.info("Method : saveFoodData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveFoodData", employeeFoodTrackingModelList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveFoodData function Ends");
		return res;
	}

	/*
	 * save extra Salary details
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "salary-approval-save-extra-salary-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveExtraSalaryData(
			@RequestBody List<HrmsExtraSalaryModel> hrmsExtraSalaryModellList, Model model, HttpSession session) {
		logger.info("Method : saveExtraSalaryData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveExtraSalaryData", hrmsExtraSalaryModellList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveExtraSalaryData function Ends");
		return res;
	}

	/*
	 * save extra Salary details
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "salary-approval-save-attendance-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveAttendanceData(
			@RequestBody List<HrmsAttendanceApprovalModel> hrmsAttendanceApprovalModelList, Model model,
			HttpSession session) {
		logger.info("Method : saveAttendanceData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveAttendanceData", hrmsAttendanceApprovalModelList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveAttendanceData function Ends");
		return res;
	}
	/*
	 * save extra Leave details
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "salary-approval-save-leave-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveLeaveData(
			@RequestBody List<HrmsLeaveApprovalModel> hrmsLeaveApprovalModel, Model model,
			HttpSession session) {
		logger.info("Method : saveAttendanceData function starts");
		
		System.out.println("@@@@@@"+hrmsLeaveApprovalModel);
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "saveLeaveData", hrmsLeaveApprovalModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = res.getMessage();
		if (message != null && message != "") {
			
		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveAttendanceData function Ends");
		return res;
	}

	/**
	 * Default 'add production planning ' page
	 *
	 */

	@GetMapping("salary-approval-final-salary")
	public String finalSalary(Model model, HttpSession session, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String status) {

		logger.info("Method : addSalary starts");

		byte[] fromDate2 = Base64.getDecoder().decode(fromDate.getBytes());
		byte[] toDate2 = Base64.getDecoder().decode(toDate.getBytes());
		byte[] status3 = Base64.getDecoder().decode(status.getBytes());

		String param2 = (new String(fromDate2));
		String param3 = (new String(toDate2));
		String param4 = (new String(status3));

		HrmsSalaryModel productiionPlanning = new HrmsSalaryModel();
		HrmsSalaryModel sessionProductiionPlanning = (HrmsSalaryModel) session.getAttribute("sesssionSalaryList");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (sessionProductiionPlanning != null) {
			model.addAttribute("productionPlanning", sessionProductiionPlanning);
			session.setAttribute("sessionDeliveryChalanModel", null);
		} else {
			model.addAttribute("productionPlanning", productiionPlanning);
		}
		model.addAttribute("fromDate", param2);
		model.addAttribute("toDate", param3);
		model.addAttribute("status", param4);
		logger.info("Method : addSalary ends");

		return "employee/add-final-salary";
	}

	/*
	 * get trip details details
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/salary-approval-get-count-dtls" })
	public @ResponseBody JsonResponse<HrmsSalaryApproveCountModel> getCountDetails(@RequestParam String fromDate,
			@RequestParam String toDate) {
		logger.info("Method : getCountDetails starts");

		JsonResponse<HrmsSalaryApproveCountModel> res = new JsonResponse<HrmsSalaryApproveCountModel>();

		try {
			res = restClient.getForObject(
					env.getEmployeeUrl() + "getCountDetails?fromDate=" + fromDate + "&toDate=" + toDate,
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

		logger.info("Method : getCountDetails  ends");
		return res;
	}
	
	/*
	 * get Leave details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/salary-approval-get-leave-dtls" })
	public @ResponseBody JsonResponse<HrmsLeaveApprovalModel> getLeaveDetails(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String empId, @RequestParam String status) {
		logger.info("Method : getAttendanceDetails starts");

		JsonResponse<HrmsLeaveApprovalModel> res = new JsonResponse<HrmsLeaveApprovalModel>(); 

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getLeaveDetails?fromDate=" + fromDate
					+ "&toDate=" + toDate + "&empId=" + empId + "&status=" + status, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAttendanceDetails  ends");
		return res;
	}
	
	
	/**
	 * Default 'add production planning ' page
	 *
	 */

	@GetMapping("/epf-esi-report")
	public String epfEsiReport(Model model, HttpSession session) {

		logger.info("Method : gatSalaryApproval starts");

		logger.info("Method : gatSalaryApproval ends");

		return "employee/epf-esic-excel-report-page";
	}
}
