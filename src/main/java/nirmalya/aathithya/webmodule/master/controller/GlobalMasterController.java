package nirmalya.aathithya.webmodule.master.controller;

import java.io.IOException;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.GlobalMasterModel;
import nirmalya.aathithya.webmodule.master.model.LocationMasterModel;

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

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = "/geography-master-country-type") public String
	 * addCountryType(@RequestBody GlobalMasterModel globalmastermodel, Model model,
	 * HttpSession session) { logger.info("Method : addCountryType starts");
	 * 
	 * JsonResponse<Object> jsonResponse = new JsonResponse<Object>(); String userId
	 * = null;
	 * 
	 * try { userId = (String) session.getAttribute("USER_ID"); } catch (Exception
	 * e) { e.printStackTrace(); } try { globalmastermodel.setCreatedBy(userId);
	 * jsonResponse = restTemplate.postForObject(env.getMasterUrl() +
	 * "rest-add-country-type", globalmastermodel, JsonResponse.class); } catch
	 * (RestClientException e) { e.printStackTrace(); }
	 * 
	 * if (jsonResponse.getMessage() != "") { session.setAttribute("message",
	 * jsonResponse.getMessage()); session.setAttribute("Model", globalmastermodel);
	 * 
	 * return "redirect:geography-master-country-type"; }
	 * 
	 * logger.info("Method : addCountryType ends");
	 * 
	 * return "Redirect:master/geography-master"; }
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("geography-master-country-type") public @ResponseBody
	 * List<GlobalMasterModel> viewRequisitionThroughAjax(Model model,
	 * HttpServletRequest request) {
	 * logger.info("Method : viewRequisitionThroughAjax starts");
	 * 
	 * JsonResponse<List<GlobalMasterModel>> jsonResponse = new
	 * JsonResponse<List<GlobalMasterModel>>();
	 * 
	 * int count = 0; try {
	 * 
	 * jsonResponse = restTemplate.getForObject(env.getRecruitment() +
	 * "viewCountry", JsonResponse.class);
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * List<GlobalMasterModel> addreq = mapper.convertValue(jsonResponse.getBody(),
	 * new TypeReference<List<GlobalMasterModel>>() { });
	 * 
	 * for(GlobalMasterModel m : addreq) { count++; m.setCount(count);
	 * if(m.getActivityStatus().equals("1")) {
	 * 
	 * m.setActivityStatus("Created"); } else if(m.getActivityStatus().equals("2"))
	 * { m.setActivityStatus("Active"); } else if(m.getActivityStatus().equals("3"))
	 * { m.setActivityStatus("Closed"); } }
	 * 
	 * 
	 * jsonResponse.setBody(addreq);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * logger.info("Method ; viewRequisitionThroughAjax ends");
	 * System.out.println(jsonResponse); return jsonResponse.getBody(); }
	 */

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
	public @ResponseBody List<GlobalMasterModel> viewGlobalMaster(
			HttpSession session) {
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

	
}
