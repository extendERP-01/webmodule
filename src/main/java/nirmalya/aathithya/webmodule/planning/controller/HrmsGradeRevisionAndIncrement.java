
package nirmalya.aathithya.webmodule.planning.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.planning.model.EmployeeSalaryIncementModel;
import nirmalya.aathithya.webmodule.planning.model.HrmsGraderevisionModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "planning")
public class HrmsGradeRevisionAndIncrement {

	Logger logger = LoggerFactory.getLogger(HrmsGradeRevisionAndIncrement.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping view employee-appraisal-details
	 */
	@GetMapping("/view-grade-revision-promotion")
	public String viewGradeRevisionAndPromotionn(Model model, HttpSession session) {

		logger.info("Method : viewGradeRevisionAndPromotionn  starts");
		/*
		 * for viewing drop down list of Department
		 */
		try {
			DropDownModel[] department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Pay Grade
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-payGradeList",
					DropDownModel[].class);
			List<DropDownModel> payGradeList = Arrays.asList(dropDownModel);
			model.addAttribute("payGradeList", payGradeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] jobTitle = restClient.getForObject(env.getEmployeeUrl() + "getJobTitleList",
					DropDownModel[].class);
			List<DropDownModel> jobTitleList = Arrays.asList(jobTitle);
			model.addAttribute("jobTitleList", jobTitleList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewGradeRevisionAndPromotionn  ends");

		return "planning/add-grade-revision-promotion";
	}

	/*
	 * drop down for get employee list by department
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-grade-revision-promotion-get-employee" })
	public @ResponseBody JsonResponse<DropDownModel> getEmployeeList(@RequestParam String departmentId,
			@RequestParam String fromDate, @RequestParam String toDate) {
		logger.info("Method : getEmployeeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPlanningUrl() + "get-employee-list?deptId=" + departmentId
					+ "&fromDate=" + fromDate + "&toDate=" + toDate, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getEmployeeList  ends");
		return res;
	}

	/*
	 * drop down for get employee list by department
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-grade-revision-promotion-get-employee-details" })
	public @ResponseBody JsonResponse<HrmsGraderevisionModel> getEmployeeDetails(@RequestParam String departmentId,
			@RequestParam String employeeId, @RequestParam String fromDate, @RequestParam String toDate) {
		logger.info("Method : getEmployeeDetails starts");

		JsonResponse<HrmsGraderevisionModel> res = new JsonResponse<HrmsGraderevisionModel>();

		try {
			res = restClient.getForObject(env.getPlanningUrl() + "get-employee-details?deptId=" + departmentId
					+ "&empId=" + employeeId + "&fromDate=" + fromDate + "&toDate=" + toDate, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getEmployeeDetails  ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-grade-revision-promotion-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveGradeRevisionData(@RequestBody List<HrmsGraderevisionModel> testData,
			Model model, HttpSession session) {
		logger.info("Method : saveGradeRevisionData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			testData.forEach(s -> s.setCreatedBy((String) session.getAttribute("USER_ID")));

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			testData = testData.stream().filter(s -> s.getStatus()).collect(Collectors.toList());

			res = restClient.postForObject(env.getPlanningUrl() + "saveGradeRevisionData", testData,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<EmployeeSalaryIncementModel> dataList = mapper.convertValue(res.getBody(),
				new TypeReference<List<EmployeeSalaryIncementModel>>() {

				});
		dataList.forEach(s -> s.setCalcTypeName(s.getCalculationType().contentEquals("1") ? "% of Ctc"
				: s.getCalculationType().contentEquals("2") ? "% Of Basic " : "Flat Amount"));
		System.out.println("dataList" + dataList);
		res.setBody(dataList);
		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveGradeRevisionData function Ends");
		return res;
	}
}
