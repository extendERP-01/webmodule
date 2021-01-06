package nirmalya.aathithya.webmodule.master.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.GlobalMasterModel;

@Controller
@RequestMapping(value = "master")
public class GlobalMasterController {

	Logger logger = LoggerFactory.getLogger(GlobalMasterController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	// Summary
	@GetMapping("/geography-master")

	public String manageLocation(Model model, HttpSession session) {
		logger.info("Method : manageGlobal starts");

		try {

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : manageGlobal ends");
		return "master/geography-master";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/geography-master-country-type")
	public @ResponseBody JsonResponse<Object> addGlobalMaster(@RequestBody GlobalMasterModel global,
			HttpSession session) {
		logger.info("Method : addGlobalMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		global.setCreatedBy(userId);

		try {
			resp = restTemplate.postForObject(env.getMasterUrl() + "addGlobalMaster", global, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {

			resp.setMessage("Success");
		}

		logger.info("Method : addGlobalMaster starts");
		System.out.println(resp);

		return resp;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/geography-master-country-type-view")
	public @ResponseBody List<GlobalMasterModel> viewGlobalMaster(HttpSession session) {
		logger.info("Method : viewGlobalMaster starts");

		JsonResponse<List<GlobalMasterModel>> resp = new JsonResponse<List<GlobalMasterModel>>();

		try {
			resp = restTemplate.getForObject(env.getMasterUrl() + "viewGlobalMaster", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {

			resp.setMessage("Success");
		}

		logger.info("Method : viewGlobalMaster ends");
		System.out.println(resp.getBody());
		return resp.getBody();
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/geography-master-state-type")
	public @ResponseBody JsonResponse<Object> addStateMaster(@RequestBody GlobalMasterModel state,
			HttpSession session) {
		logger.info("Method : addStateMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		state.setCreatedBy(userId);

		try {
			resp = restTemplate.postForObject(env.getMasterUrl() + "addStateMaster", state, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {

			resp.setMessage("Success");
		}

		logger.info("Method : addStateMaster starts");
		System.out.println(resp);

		return resp;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/geography-master-state-type-view")
	public @ResponseBody List<GlobalMasterModel> viewStateMaster(HttpSession session) {
		logger.info("Method : viewStateMaster starts");

		JsonResponse<List<GlobalMasterModel>> resp = new JsonResponse<List<GlobalMasterModel>>();

		try {
			resp = restTemplate.getForObject(env.getMasterUrl() + "viewStateMaster", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {

			resp.setMessage("Success");
		}

		logger.info("Method : viewStateMaster ends");
		System.out.println(resp.getBody());
		return resp.getBody();
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/geography-master-city-type")
	public @ResponseBody JsonResponse<Object> addCityMaster(@RequestBody GlobalMasterModel city, HttpSession session) {
		logger.info("Method : addCityMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		city.setCreatedBy(userId);

		try {
			resp = restTemplate.postForObject(env.getMasterUrl() + "addCityMaster", city, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {

			resp.setMessage("Success");
		}

		logger.info("Method : addCityMaster starts");
		System.out.println(resp);

		return resp;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/geography-master-city-type-view")
	public @ResponseBody List<GlobalMasterModel> viewCityMaster(HttpSession session) {
		logger.info("Method : viewCityMaster starts");

		JsonResponse<List<GlobalMasterModel>> resp = new JsonResponse<List<GlobalMasterModel>>();

		try {
			resp = restTemplate.getForObject(env.getMasterUrl() + "viewCityMaster", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {

			resp.setMessage("Success");
		}

		logger.info("Method : viewCityMaster ends");
		System.out.println(resp.getBody());
		return resp.getBody();
	}

}
