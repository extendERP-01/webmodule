package nirmalya.aathithya.webmodule.recruitment.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.recruitment.model.AddRecruitentModel;
import nirmalya.aathithya.webmodule.recruitment.model.RequisitionActivityModel;

@Controller
@RequestMapping(value = "recruitment")
public class AddRecruitmentController {

	Logger logger = LoggerFactory.getLogger(AddRecruitmentController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping add recruitment
	 * 
	 * @GetMapping("/add-recruitment") public String addRecruitment(Model model,
	 * HttpSession session) {
	 * 
	 * logger.info("Method : addRecruitment starts");
	 * 
	 * logger.info("Method : addRecruitment ends");
	 * 
	 * return "recruitment/add-recruitment"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-recruitment") public String viewRecruitment(Model model,
	 * HttpSession session) {
	 * 
	 * logger.info("Method : viewRecruitment starts");
	 * 
	 * logger.info("Method : viewRecruitment ends");
	 * 
	 * return "recruitment/view-recruitment"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-resource") public String viewResource(Model model,
	 * HttpSession session) {
	 * 
	 * logger.info("Method : viewResource starts");
	 * 
	 * logger.info("Method : viewResource ends");
	 * 
	 * return "recruitment/view-resource"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-resource-add-vendor") public String
	 * viewResourceAddVendor(Model model, HttpSession session) {
	 * 
	 * logger.info("Method : viewResourceAddVendor starts");
	 * 
	 * logger.info("Method : viewResourceAddVendor ends");
	 * 
	 * return "recruitment/view-resource-add-vendor"; }
	 * 
	 * @GetMapping("/view-resource-upload-resume") public String uploadResume(Model
	 * model, HttpSession session) { logger.info("Method : uploadResume starts");
	 * 
	 * logger.info("Method : uploadResume ends"); return
	 * "recruitment/upload-resume"; }
	 * 
	 * @GetMapping("/upload-candidate-resume") public String
	 * viewJobListForUploadRsume(Model model, HttpSession session) {
	 * logger.info("Method : viewJobListForUploadRsume starts");
	 * 
	 * logger.info("Method : viewJobListForUploadRsume ends"); return
	 * "recruitment/upload-candidate-resume"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-resource-shortlist") public String
	 * viewResourceshortlist(Model model, HttpSession session) {
	 * 
	 * logger.info("Method : viewResourceshortlist starts");
	 * 
	 * logger.info("Method : viewResourceshortlist ends");
	 * 
	 * return "recruitment/view-resource-shortlist"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-resource-add-shortlist") public String
	 * viewResourceAddshortlist(Model model, HttpSession session) {
	 * 
	 * logger.info("Method : viewResourceshortlist starts");
	 * 
	 * logger.info("Method : viewResourceshortlist ends");
	 * 
	 * return "recruitment/view-resource-add-shortlist"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-resource-employee") public String
	 * viewResourceAddshortlistEmployee(Model model, HttpSession session) {
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee starts");
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee ends");
	 * 
	 * return "recruitment/view-resource-employee"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-resource-add-to-employee") public String
	 * viewResourceAddshortlistAddEmployee(Model model, HttpSession session) {
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee starts");
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee ends");
	 * 
	 * return "recruitment/view-resource-add-to-employee"; }
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-resource-final") public String
	 * viewResourceAddshortlistFinal(Model model, HttpSession session) {
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee starts");
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee ends");
	 * 
	 * return "recruitment/view-resource-final"; }
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-employee-feedback") public String
	 * viewEmployeeFeedback(Model model, HttpSession session) {
	 * logger.info("Method : viewResourceAddshortlistAddEmployee starts");
	 * 
	 * String id = (String) session.getAttribute("id");
	 * 
	 * model.addAttribute("id", id);
	 * 
	 * session.removeAttribute("id");
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee ends"); return
	 * "recruitment/view-employee-feedback"; }
	 * 
	 * 
	 * Get Mapping view recruitment
	 * 
	 * @GetMapping("/view-employee-feedback-add") public String
	 * viewEmployeeFeedbackAdd(Model model, HttpSession session) {
	 * logger.info("Method : viewResourceAddshortlistAddEmployee starts");
	 * 
	 * session.setAttribute("id", "1");
	 * 
	 * logger.info("Method : viewResourceAddshortlistAddEmployee ends"); return
	 * "recruitment/add-employee-feedback"; }
	 * 
	 * @GetMapping("/add-training-vendor-sourcing") public String
	 * addVendorsourcing(Model model, HttpSession session) {
	 * logger.info("Method : uploadResume starts");
	 * 
	 * logger.info("Method : uploadResume ends"); return
	 * "recruitment/add-training-vendor-sourcing"; }
	 * 
	 * 
	 * @GetMapping("/view-training-vendor-sourcing") public String
	 * viewVendorSourcing(Model model, HttpSession session) {
	 * logger.info("Method : viewJobListForUploadRsume starts");
	 * 
	 * logger.info("Method : viewJobListForUploadRsume ends"); return
	 * "recruitment/view-training-vendor-sourcing"; }
	 */

	// Summary
	@GetMapping("/view-new-requi-mstr")
	public String requisition(Model model, HttpSession session) {

		logger.info("Method : requisition starts");

		try {
			
			DropDownModel[] jobType = restTemplate.getForObject(env.getRecruitment() + "jobTypeList",
					DropDownModel[].class);
			List<DropDownModel> jobTypeList = Arrays.asList(jobType);
			model.addAttribute("jobTypeList", jobTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] location = restTemplate.getForObject(env.getRecruitment() + "jobLocationList",
					DropDownModel[].class);
			List<DropDownModel> jobLocationList = Arrays.asList(location);
			model.addAttribute("jobLocationList", jobLocationList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] department = restTemplate.getForObject(env.getRecruitment() + "DepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] manager = restTemplate.getForObject(env.getRecruitment() + "managerList",
					DropDownModel[].class);
			List<DropDownModel> managerList = Arrays.asList(manager);
			model.addAttribute("managerList", managerList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] band = restTemplate.getForObject(env.getRecruitment() + "bandList",
					DropDownModel[].class);
			List<DropDownModel> bandList = Arrays.asList(band);
			model.addAttribute("bandList", bandList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] education = restTemplate.getForObject(env.getRecruitment() + "educationList",
					DropDownModel[].class);
			List<DropDownModel> educationList = Arrays.asList(education);
			model.addAttribute("educationList", educationList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] workHour = restTemplate.getForObject(env.getRecruitment() + "workHourList",
					DropDownModel[].class);
			List<DropDownModel> workHourList = Arrays.asList(workHour);
			model.addAttribute("workHourList", workHourList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] benefits = restTemplate.getForObject(env.getRecruitment() + "benefitsList",
					DropDownModel[].class);
			List<DropDownModel> benefitsList = Arrays.asList(benefits);
			model.addAttribute("benefitsList", benefitsList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			
			DropDownModel[] aboutCompany = restTemplate.getForObject(env.getRecruitment() + "aboutCompany",
					DropDownModel[].class);
			List<DropDownModel> aboutCompanyData = Arrays.asList(aboutCompany);
			model.addAttribute("aboutComapany", aboutCompanyData);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		logger.info("Method : requisition ends");

		return "recruitment/view-new-requi-mstr";
	}
	
	@GetMapping("/view-candidate")
	public String viewCandidate(Model model, HttpSession session) {

		logger.info("Method : viewCandidate starts");

		try {
			DropDownModel[] Gender = restTemplate.getForObject(env.getEmployeeUrl() + "getgenderList1",
					DropDownModel[].class);
			List<DropDownModel> genderTypeList = Arrays.asList(Gender);

			model.addAttribute("genderTypeList", genderTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] Nationality = restTemplate.getForObject(env.getEmployeeUrl() + "getnationalityList1",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(Nationality);

			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] BloodGroup = restTemplate.getForObject(env.getEmployeeUrl() + "getbloodgroupList1",
					DropDownModel[].class);
			List<DropDownModel> bloodgroupList = Arrays.asList(BloodGroup);

			model.addAttribute("bloodgroupList", bloodgroupList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] MaritalStatus = restTemplate.getForObject(env.getEmployeeUrl() + "getmaritalstatusList1",
					DropDownModel[].class);
			List<DropDownModel> maritalstatusList = Arrays.asList(MaritalStatus);

			model.addAttribute("maritalstatusList", maritalstatusList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] Country = restTemplate.getForObject(env.getEmployeeUrl() + "getCountryList",
					DropDownModel[].class);
			List<DropDownModel> counntryList = Arrays.asList(Country);

			model.addAttribute("counntryList", counntryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] State = restTemplate.getForObject(env.getEmployeeUrl() + "getstateList1",
					DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(State);

			model.addAttribute("stateList", stateList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] City = restTemplate.getForObject(env.getEmployeeUrl() + "getcityList1",
					DropDownModel[].class);
			List<DropDownModel> cityList = Arrays.asList(City);

			model.addAttribute("cityList", cityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			
			DropDownModel[] education = restTemplate.getForObject(env.getRecruitment() + "educationList",
					DropDownModel[].class);
			List<DropDownModel> educationList = Arrays.asList(education);
			model.addAttribute("educationList", educationList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewCandidate ends");

		return "recruitment/view-candidate";
	}
	
	@GetMapping("/review")
	public String review(Model model, HttpSession session) {

		logger.info("Method : review starts");

		logger.info("Method : review ends");

		return "recruitment/review";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/view-new-requi-mstr-ajax")
	public @ResponseBody JsonResponse<Object> addRequisition(Model model, HttpSession session, @RequestBody AddRecruitentModel reqModel){
		
		logger.info("Method : addRequisition starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		reqModel.setCreatedBy(userId);
		try {
			resp = restTemplate.postForObject(env.getRecruitment() + "addRequisition", reqModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("Success");
		}

		
		logger.info("Method : addRequisition ends");
		
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("view-new-requi-mstr-view-data")
	public @ResponseBody List<AddRecruitentModel> viewRequisitionThroughAjax(Model model, HttpServletRequest request) {
		logger.info("Method : viewRequisitionThroughAjax starts");

		JsonResponse<List<AddRecruitentModel>> jsonResponse = new JsonResponse<List<AddRecruitentModel>>();

		int count = 0;
		try {

			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "viewRequistion",
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AddRecruitentModel> addreq = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AddRecruitentModel>>() {
					});
			for(AddRecruitentModel m : addreq) {
				count++;
				m.setCount(count);
				if(m.getActivityStatus().equals("1")) {
					
					m.setActivityStatus("Created");
				} else if(m.getActivityStatus().equals("2")) {
					m.setActivityStatus("Active");
				} else if(m.getActivityStatus().equals("3")) {
					m.setActivityStatus("Closed");
				}
			}
			
			jsonResponse.setBody(addreq);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method ; viewRequisitionThroughAjax ends");

		return jsonResponse.getBody();
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("view-new-requi-mstr-delete")
	public @ResponseBody JsonResponse<Object> deleterequistion(Model model, @RequestParam String id,
			HttpSession session) {
		logger.info("Method : delectRequistion starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String createdBy = "";

		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			resp = restTemplate.getForObject(
					env.getRecruitment() + "deleteRequistion?id="+ id,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}

		logger.info("Method :  delectRequistion ends");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-new-requi-mstr-edit")
	public @ResponseBody JsonResponse<List<AddRecruitentModel>> editRequisition(Model model, @RequestParam String id, HttpSession session) {
		
		logger.info("Method : editRequisition starts");
		
		JsonResponse<List<AddRecruitentModel>> jsonResponse = new JsonResponse<List<AddRecruitentModel>>();
		System.out.println(id);
		try {
			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "editRequisition?id=" + id,
					JsonResponse.class);
			
		} catch(RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		 
		if (jsonResponse.getMessage() != null && jsonResponse.getMessage() != "") {
			jsonResponse.setCode(jsonResponse.getMessage());
			jsonResponse.setMessage("Unsuccess");
			
		} else {
			jsonResponse.setMessage("Success");
		}
		
		System.out.println("##@@@@"+jsonResponse);
		logger.info("Method : editRequisition ends");
		return jsonResponse;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-new-requi-mstr-view-activity")
	public @ResponseBody List<RequisitionActivityModel> activityRequisition(Model model, @RequestParam String id, HttpSession session) {
		
		logger.info("Method : activityRequisition starts");
		
		JsonResponse<List<RequisitionActivityModel>> jsonResponse = new JsonResponse<List<RequisitionActivityModel>>();
		
		try {
			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "activityRequisition?id=" + id,
					JsonResponse.class);
			
		} catch(RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		 ObjectMapper mapper = new ObjectMapper();
		 List<RequisitionActivityModel> req = mapper.convertValue(jsonResponse.getBody(),new
		 TypeReference<List<RequisitionActivityModel>>() {});
		
		for(RequisitionActivityModel m : req) {
			if(m.getActivityHistoryStatus().equals("1")) {
				m.setActivityHistoryStatus("Created");
			}
			if(m.getActivityHistoryStatus().equals("2")) {
				m.setActivityHistoryStatus("Updated");
			}
		}
		 
		if (jsonResponse.getMessage() != null && jsonResponse.getMessage() != "") {
			jsonResponse.setCode(jsonResponse.getMessage());
			jsonResponse.setMessage("Unsuccess");
			
		} else {
			jsonResponse.setMessage("Success");
		}
		jsonResponse.setBody(req);
		
		logger.info("Method : activityRequisition ends");
		return jsonResponse.getBody();
	}
	
}

