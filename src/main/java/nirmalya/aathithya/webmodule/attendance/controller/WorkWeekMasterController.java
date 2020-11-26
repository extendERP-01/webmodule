package nirmalya.aathithya.webmodule.attendance.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
import nirmalya.aathithya.webmodule.attendance.model.WorkWeekMasterModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "attendance")
public class WorkWeekMasterController {

	Logger logger = LoggerFactory.getLogger(WorkWeekMasterController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * View Default 'Add Work Week' page
	 *
	 */
	@GetMapping(value = { "add-workweek-master" })
	public String addWorkWeek(Model model, HttpSession session) {
		logger.info("Method : addWorkWeek starts");
		WorkWeekMasterModel workWeekMaster = new WorkWeekMasterModel();

		WorkWeekMasterModel workWeekMasterSession = (WorkWeekMasterModel) session.getAttribute("workWeekMaster");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (workWeekMasterSession != null) {
			model.addAttribute("workWeekMaster", workWeekMasterSession);
			session.setAttribute("workWeekMaster", null);

		} else {
			model.addAttribute("workWeekMaster", workWeekMaster);
		}

		/*
		 * dropDown value for week Day
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getAttendanceUrl() + "rest-get-dayName",
					DropDownModel[].class);
			List<DropDownModel> dayName = Arrays.asList(dropDownModel);
			model.addAttribute("dayName", dayName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for working status
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getAttendanceUrl() + "rest-get-workingStatus",
					DropDownModel[].class);
			List<DropDownModel> workingStatus = Arrays.asList(dropDownModel);
			model.addAttribute("workingStatus", workingStatus);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addWorkWeek ends");
		return "attendance/addWorkWeekMaster";
	}

	/*
	 * Add Work Week Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-workweek-master" })
	public String addNewWorkWeek(@ModelAttribute WorkWeekMasterModel workWeekMaster, Model model, HttpSession session) {
		logger.info("Method : addNewWorkWeek starts");
		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			workWeekMaster.settWorkDayCreatedBy(userId);
			workWeekMaster.settCompanyId(companyId);
			jsonResponse = restTemplate.postForObject(env.getAttendanceUrl() + "addWorkWeek", workWeekMaster,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("workWeekMaster", workWeekMaster);
			return "redirect:add-workweek-master";
		}
		logger.info("Method : addNewWorkWeek ends");
		return "redirect:view-workweek-master";
	}

	/*
	 * View Default 'View Work Week' page
	 *
	 */
	@GetMapping(value = { "view-workweek-master" })
	public String viewWorkWeek(Model model) {
		logger.info("Method : viewWorkWeek starts");

		logger.info("Method : viewWorkWeek ends");
		return "attendance/viewWorkWeekMaster";
	}

	/*
	 * View Work Week through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-workweek-master-throughAjax")
	public @ResponseBody DataTableResponse viewworkWeekThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewworkWeekThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<WorkWeekMasterModel>> jsonResponse = new JsonResponse<List<WorkWeekMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getAttendanceUrl() + "getAllWorWeek", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<WorkWeekMasterModel> workWeekMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<WorkWeekMasterModel>>() {
					});

			String s = "";
			for (WorkWeekMasterModel m : workWeekMaster) {
				byte[] pId = Base64.getEncoder().encode(m.gettWorkDay().getBytes());

				m.setStatus(s);
				;

				s = "";

				s = s + "<a  title='Edit' href='edit-workweek-master?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit' style=\"font-size:20px\" ></i></a>";

				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(workWeekMaster);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewworkWeekThroughAjax ends");
		return response;
	}

	/*
	 * Edit defined work week
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-workweek-master")
	public String editWorkWeek(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editWorkWeek starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<WorkWeekMasterModel> res = new JsonResponse<WorkWeekMasterModel>();
		WorkWeekMasterModel workWeekMaster = new WorkWeekMasterModel();

		try {
			res = restTemplate.getForObject(env.getAttendanceUrl() + "editWorkWeek?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		workWeekMaster = mapper.convertValue(res.getBody(), WorkWeekMasterModel.class);
		session.setAttribute("message", "");
		model.addAttribute("workWeekMaster", workWeekMaster);
		/*
		 * dropDown value for Week Day
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getAttendanceUrl() + "rest-get-dayName",
					DropDownModel[].class);
			List<DropDownModel> dayName = Arrays.asList(dropDownModel);
			model.addAttribute("dayName", dayName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for working status
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getAttendanceUrl() + "rest-get-workingStatus",
					DropDownModel[].class);
			List<DropDownModel> workingStatus = Arrays.asList(dropDownModel);
			model.addAttribute("workingStatus", workingStatus);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : editWorkWeek ends");
		return "attendance/addWorkWeekMaster";
	}

}
