package nirmalya.aathithya.webmodule.employee.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.EmployeeFoodTrackingModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "employee")
public class FoodTrackingController {

	Logger logger = LoggerFactory.getLogger(FoodTrackingController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/add-food-tracking-data")
	public String defaultFoodTracing(Model model, HttpSession session) {
		logger.info("Method : defaultFoodTracing start");

		logger.info("Method : defaultFoodTracing end");
		return "employee/add-food-tracking-data";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-food-tracking-data-get-employee" })
	public @ResponseBody JsonResponse<List<EmployeeFoodTrackingModel>> getEmployeeDetails(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getEmployeeDetails starts");

		JsonResponse<List<EmployeeFoodTrackingModel>> res = new JsonResponse<List<EmployeeFoodTrackingModel>>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeDetailsForFoodTracking?id=" + searchValue,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<EmployeeFoodTrackingModel> foodList = mapper.convertValue(res.getBody(),
					new TypeReference<List<EmployeeFoodTrackingModel>>() {
					});
			if (foodList != null) {
				for (EmployeeFoodTrackingModel m : foodList) {
					if (m.getDayMeal() == null) {
						m.setDayMeal(0);
					}
					if (m.getNightMeal() == null) {
						m.setNightMeal(0);
					}
				}
			}

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

		logger.info("Method : getEmployeeDetails ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-food-tracking-data-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveFoodTrackIngData(
			@RequestBody List<EmployeeFoodTrackingModel> testData, Model model, HttpSession session) {
		logger.info("Method : saveFoodTrackIngData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			for (EmployeeFoodTrackingModel m : testData) {
				m.setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getEmployeeUrl() + "saveFoodTrackIngData", testData, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveFoodTrackIngData function Ends");
		return res;
	}
}
