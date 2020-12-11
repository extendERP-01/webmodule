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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping; 
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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

import nirmalya.aathithya.webmodule.recruitment.model.JobTitleModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "recruitment")
public class JobTittleContoller {
	Logger logger = LoggerFactory.getLogger(JobTittleContoller.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/add-job-tittle")
	public String addJobTittle(Model model, HttpSession session) {

		logger.info("Method : addJobTittle starts");

		JobTitleModel patientJob = new JobTitleModel();
		JobTitleModel sessionJob = (JobTitleModel) session.getAttribute("sessionJob");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionJob != null) {
			model.addAttribute("sessionJob", sessionJob);
			session.setAttribute("sessionJob", null);
		} else {
			model.addAttribute("sessionJob", patientJob);
		}

		/**
		 * JOB Department DROP DOWN
		 *
		 */
		try {
			DropDownModel[] jobDepartment = restClient.getForObject(env.getRecruitment() + "dropDownJobDepartment",
					DropDownModel[].class);
			List<DropDownModel> getjobDepartmentList = Arrays.asList(jobDepartment);
			model.addAttribute("jobDepartmentList", getjobDepartmentList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * JOB Type DROP DOWN
		 *
		 */

		try {
			DropDownModel[] jobType = restClient.getForObject(env.getRecruitment() + "dropDownJobType",
					DropDownModel[].class);
			List<DropDownModel> getjobTypeList = Arrays.asList(jobType);
			model.addAttribute("jobTypeList", getjobTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addJobTittle ends");

		return "recruitment/add-job-tittle";
	}
	/*
	 * post Mapping for add Add Patient
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-jobtitle-through-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addJobDetailsPost(@RequestBody JobTitleModel jobForm, Model model,
			HttpSession session) {
		logger.info("Method : addPatientDetailsPost function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			jobForm.setCreatedBy(userId);

			res = restClient.postForObject(env.getRecruitment() + "add-jobtitle-through-ajax", jobForm,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : addJobDetailsPost ends");
		return res;
	}

	@GetMapping("/view-job-tittle")
	public String viewJobTittle(Model model, HttpSession session) {

		logger.info("Method : viewJobTittle starts");
		/**
		 * JOB Tittle DROP DOWN
		 *
		 */

		try {
			DropDownModel[] jobType = restClient.getForObject(env.getRecruitment() + "dropDownJobTittle",
					DropDownModel[].class);
			List<DropDownModel> getjobTittleList = Arrays.asList(jobType);
			model.addAttribute("jobTittleList", getjobTittleList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * JOB Type DROP DOWN
		 *
		 */

		try {
			DropDownModel[] jobType = restClient.getForObject(env.getRecruitment() + "dropDownJobType",
					DropDownModel[].class);
			List<DropDownModel> getjobTypeList = Arrays.asList(jobType);
			model.addAttribute("jobTypeList", getjobTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewJobTittle ends");

		return "recruitment/view-job-tittle";
	}
	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-job-tittle-through-ajax")
	public @ResponseBody DataTableResponse viewPatientAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewPatientAjax  statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			JsonResponse<List<JobTitleModel>> jsonResponse = new JsonResponse<List<JobTitleModel>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "getJobDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<JobTitleModel> jobForm = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<JobTitleModel>>() {
					});

			String s = "";

			for (JobTitleModel m : jobForm) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getJobId().getBytes());
				s = s + "<a href='view-job-tittle-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" title='Edit' style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='DeleteItem(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" title='Delete' aria-hidden=\"true\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")' ><i class=\"fa fa-search search\"aria-hidden=\"true\" style=\"font-size:20px\"></i></a>";

				m.setAction(s);
				s = "Active";

				m.setActiveStatus(s);
				;
				;
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(jobForm);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewPatientAjax   ends");

		return response;
	}
	/**
	 * Delete job Detail in Modal
	 *
	 *//*
		 * 
		 * @SuppressWarnings("unchecked")
		 * 
		 * @GetMapping(value = { "delete-job-tittle" }) public @ResponseBody
		 * JsonResponse<Object> deleteJobDetail(Model model, @RequestParam("id") String
		 * encodeId, HttpSession session) {
		 * 
		 * logger.info("Method : deleteJobDetail starts"); byte[] encodeByte =
		 * Base64.getDecoder().decode(encodeId.getBytes()); String id = (new
		 * String(encodeByte)); String createdBy = "u0002"; JsonResponse<Object> resp =
		 * new JsonResponse<Object>();
		 * 
		 * try { resp = restClient.getForObject( env.getRecruitment() +
		 * "delete-job-tittle?id=" + id + "&createdBy=" + createdBy,
		 * JsonResponse.class);
		 * 
		 * } catch (RestClientException e) {
		 * 
		 * e.printStackTrace(); }
		 * 
		 * if (resp.getMessage() != null && resp.getMessage() != "") {
		 * 
		 * resp.setCode(resp.getMessage()); resp.setMessage("Unsuccess"); } else {
		 * resp.setMessage("success"); } logger.info("Method : deleteJobDetail ends");
		 * return resp; }
		 */

	/**
	 * Web Controller - Delete job
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-job-tittle-delete")
	public @ResponseBody JsonResponse<Object> deleteState(Model model, @RequestParam String id, HttpSession session) {

		logger.info("Method : job-tittle-delete starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			resp = restClient.getForObject(
					env.getRecruitment() + "job-tittle-delete?id=" + index + "&createdBy=" + userId,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : job-tittle-delete ends");

		return resp;
	}

	/**
	 * View selected Job in Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-job-tittle-modal" })
	public @ResponseBody JsonResponse<JobTitleModel> modalviewOfJOb(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalviewOfJOb starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<JobTitleModel> res = new JsonResponse<JobTitleModel>();

		try {
			res = restClient.postForObject(env.getRecruitment() + "view-job-modal-index", id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modalviewOfJOb ends");
		return res;
	}

	/*
	 * for Edit Job Details
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-job-tittle-edit")
	public String editJobDetails(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editJobDetails starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		JsonResponse<JobTitleModel> res = new JsonResponse<JobTitleModel>();
		JobTitleModel jobForm = new JobTitleModel();

		try {
			res = restClient.getForObject(env.getRecruitment() + "edit-job-details?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		jobForm = mapper.convertValue(res.getBody(), JobTitleModel.class);
		session.setAttribute("message", "");
		model.addAttribute("jobForm", jobForm);
		model.addAttribute("jobId", jobForm.getJobId());

		/**
		 * JOB Department DROP DOWN
		 *
		 */
		try {
			DropDownModel[] jobDepartment = restClient.getForObject(env.getRecruitment() + "dropDownJobDepartment",
					DropDownModel[].class);
			List<DropDownModel> getjobDepartmentList = Arrays.asList(jobDepartment);
			model.addAttribute("jobDepartmentList", getjobDepartmentList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * JOB Type DROP DOWN
		 *
		 */

		try {
			DropDownModel[] jobType = restClient.getForObject(env.getRecruitment() + "dropDownJobType",
					DropDownModel[].class);
			List<DropDownModel> getjobTypeList = Arrays.asList(jobType);
			model.addAttribute("jobTypeList", getjobTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : editjobDetails ends");
		return "recruitment/add-job-tittle";

	}
}
