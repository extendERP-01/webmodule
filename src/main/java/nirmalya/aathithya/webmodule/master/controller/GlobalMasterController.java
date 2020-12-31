package nirmalya.aathithya.webmodule.master.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.GlobalMasterModel;

@Controller
@RequestMapping(value = "settings")
public class GlobalMasterController {

	Logger logger = LoggerFactory.getLogger(GlobalMasterController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	// Summary
	@GetMapping("/geography")

	public String manageLocation(Model model, HttpSession session) {
		logger.info("Method : manageGlobal starts");

		try {
			GlobalMasterModel[] global = restTemplate.getForObject(env.getMasterUrl() + "getGlobalList",
					GlobalMasterModel[].class);
			List<GlobalMasterModel> globalList = Arrays.asList(global);

			model.addAttribute("globalList", globalList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : manageGlobal ends");
		return "master/geography-master";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/geography-master-country-type")
	public String addCountryType(@RequestBody GlobalMasterModel globalmastermodel, Model model, HttpSession session) {
		logger.info("Method : addCountryType starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = null;

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			globalmastermodel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "rest-add-country-type", globalmastermodel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("Model", globalmastermodel);

			return "redirect:geography-master-country-type";
		}

		logger.info("Method : addCountryType ends");

		return "Redirect:master/geography-master";
	}

}
