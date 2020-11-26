package nirmalya.aathithya.webmodule.production.controller;

import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse; 
import nirmalya.aathithya.webmodule.production.model.ProductionGradeDetailsModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "production")
public class ProductionIndividualGradeController {

	Logger logger = LoggerFactory.getLogger(ProductionPlanningController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Default 'add production planning ' page
	 *
	 */

	@GetMapping("/view-production-grade-details")
	public String viewProductionGradeDetails(Model model, HttpSession session) {

		logger.info("Method : viewProductionGradeDetails starts");

		/*
		 * for viewing drop down sales order list
		 */
		try {
			DropDownModel[] grade = restClient.getForObject(env.getProduction() + "getGrade", DropDownModel[].class);
			List<DropDownModel> gradeList = Arrays.asList(grade);

			model.addAttribute("gradeList", gradeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewProductionGradeDetails ends");

		return "production/view-production-grade-details";
	}

	/*
	 * get production details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-production-grade-details-through-ajax" })
	public @ResponseBody JsonResponse<ProductionGradeDetailsModel> getProductionGradeDetails(
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String thickness) {
		logger.info("Method : getProductionGradeDetails starts");

		JsonResponse<ProductionGradeDetailsModel> res = new JsonResponse<ProductionGradeDetailsModel>();

		try {
			res = restClient.getForObject(env.getProduction() + "getProductionGradeDetails?fromDate=" + fromDate
					+ "&toDate=" + toDate + "&thickness=" + thickness, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getProductionGradeDetails  ends");
		return res;
	}

	/*
	 * dropDown value for thickness
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "view-production-grade-details-getthickness" })

	public @ResponseBody JsonResponse<DropDownModel> getThickness(Model model,

			@RequestBody String grade, BindingResult result) {
		logger.info("Method : getThickness starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getProduction() + "getThicknessByGrade?id=" + grade, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getThickness ends");
		return res;
	}

}
