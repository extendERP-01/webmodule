package nirmalya.aathithya.webmodule.employee.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeCertificationModel;
import nirmalya.aathithya.webmodule.employee.model.PereferencePreviewModel;

@Controller
@RequestMapping(value = "employee")
public class PreferencePreviewController {
	Logger logger = LoggerFactory.getLogger(PreferencePreviewController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/add-performance-review")
	public String addPreference(Model model, HttpSession session) {

		logger.info("Method : addPreference starts");

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList2",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addPreference ends");

		return "employee/add-performance-review";
	}

	/*
	 * Get Mapping for employee certification
	 */
	@GetMapping("/add-organization-goal")
	public String addOrganizationGoal(Model model, HttpSession session) {

		logger.info("Method : addOrganizationGoal starts");

		PereferencePreviewModel orggoal = new PereferencePreviewModel();
		PereferencePreviewModel sessionOrgGoal = (PereferencePreviewModel) session.getAttribute("sessionOrgGoal");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionOrgGoal != null) {
			model.addAttribute("sessionOrgGoal", sessionOrgGoal);
			session.setAttribute("sessionOrgGoal", null);
		} else {
			model.addAttribute("sessionOrgGoal", orggoal);
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] level = restClient.getForObject(env.getEmployeeUrl() + "getPerformancelevel",
					DropDownModel[].class);
			List<DropDownModel> LevelList = Arrays.asList(level);
			model.addAttribute("LevelList", LevelList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addOrganizationGoal ends");
		return "employee/add-organization-goal";
	}

	@GetMapping("/add-initiation")
	public String initiation(Model model, HttpSession session) {

		logger.info("Method : initiation starts");

		logger.info("Method : initiation ends");

		return "employee/add-initiation";
	}

	@GetMapping("/add-development-goal")
	public String dgoal(Model model, HttpSession session) {

		logger.info("Method : dgoal starts");

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList2",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : dgoal ends");

		return "employee/add-development-goal";
	}

}
