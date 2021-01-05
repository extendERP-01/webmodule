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
import nirmalya.aathithya.webmodule.master.model.HrMasterModel;

@Controller
@RequestMapping(value = "master")
public class HrMasterController {

	Logger logger = LoggerFactory.getLogger(HrMasterController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;
		
		@GetMapping("/referenceHr")
		public String displayHrPage(Model model, HttpSession session) {
			logger.info("Method : hr starts");
			
			logger.info("Method : hr ends");
			return "master/referenceHr";
		}
		
		@SuppressWarnings("unchecked")
		@PostMapping(value = "referenceHr-add-job-type")
		public String addJobType(@RequestBody HrMasterModel hrMasterModel , Model model, HttpSession session) {
			logger.info("Method : addJobType starts");
			
			JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
			String userId = "";
			
			try {
				userId = (String)session.getAttribute("USER_ID");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			hrMasterModel.setCreatedBy(userId);
			
			try {
				hrMasterModel.setCreatedBy(userId);
				jsonResponse = restTemplate.postForObject(
						env.getMasterUrl()+ "rest-addnew-job-type", hrMasterModel ,JsonResponse.class);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			String message = jsonResponse.getMessage();
			
			if (message != null && message != "") {

			} else {

				jsonResponse.setMessage("Success");
			}
			
			logger.info("Method : addJobType ends");
			System.out.println(jsonResponse);
			return "master/referenceHr";
		}
		
		
//        View page for Hr job types
		
	
	@SuppressWarnings("unchecked")
	@GetMapping("referenceHr-view-job-type")
	public @ResponseBody List<HrMasterModel> viewJobType(HttpSession session) {
		logger.info("Method : viewJobTypes starts");

		JsonResponse<List<HrMasterModel>> resp = new JsonResponse<List<HrMasterModel>>();

		try {
			resp = restTemplate.getForObject(env.getMasterUrl() + "rest-view-Job-Type", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {

			resp.setMessage("Success");
		}

		logger.info("Method : viewJobTypes ends");

		System.out.println(resp);

		return resp.getBody();
	}
	 
}
