package nirmalya.aathithya.webmodule.employee.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.EmployeeIncomeTaxDetails;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeIncomeTaxController {
	Logger logger = LoggerFactory.getLogger(FoodTrackingController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/add-employee-income-tax")
	public String addincomeTaxDetails(Model model, HttpSession session) {
		logger.info("Method : addincomeTaxDetails start");

		logger.info("Method : addincomeTaxDetails end");
		return "employee/add-employee-income-tax";
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-employee-income-tax-get-employee" })
	public @ResponseBody JsonResponse<List<EmployeeIncomeTaxDetails>> getEmployeeDetailsForIncomeTax(
			@RequestParam String fromDate, @RequestParam String toDate) {
		logger.info("Method : getEmployeeDetailsForIncomeTax starts");

		JsonResponse<List<EmployeeIncomeTaxDetails>> res = new JsonResponse<List<EmployeeIncomeTaxDetails>>();

		try {
			res = restClient.getForObject(
					env.getEmployeeUrl() + "getEmployeeDetailsForIncomeTax?fromDate=" + fromDate + "&toDate=" + toDate,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<EmployeeIncomeTaxDetails> foodList = mapper.convertValue(res.getBody(),
					new TypeReference<List<EmployeeIncomeTaxDetails>>() {
					});

			res.setBody(foodList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getEmployeeDetailsForIncomeTax ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "add-employee-income-tax-data-save")
	public @ResponseBody JsonResponse<Object> saveIncomeTaxData(@RequestBody List<EmployeeIncomeTaxDetails> testData,
			Model model, HttpSession session) {
		logger.info("Method : saveIncomeTaxData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			for (EmployeeIncomeTaxDetails m : testData) {
				m.setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getEmployeeUrl() + "saveIncomeTaxData", testData, JsonResponse.class);
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

}
