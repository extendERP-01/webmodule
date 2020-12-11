package nirmalya.aathithya.webmodule.attendance.controller;

import java.time.LocalDate;
import java.util.Arrays;

import java.util.Date;
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
import nirmalya.aathithya.webmodule.common.utils.DateFormatter;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.attendance.model.AttendenceDetailsModel;

/*
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "attendance")
public class AttendenceDetailsController {

	Logger logger = LoggerFactory.getLogger(AttendenceDetailsController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env; 

	/*
	 * View Default 'Add Attendence' page
	 *
	 */
	@GetMapping(value = { "add-attendence-details" })
	public String addAttendence(Model model, HttpSession session) {
		logger.info("Method : addAttendence starts");
		AttendenceDetailsModel attendenceDetails = new AttendenceDetailsModel();

		AttendenceDetailsModel attendenceDetailsSession = (AttendenceDetailsModel) session
				.getAttribute("attendenceDetails");
		Date d = new Date();
		Object newdate = DateFormatter.returnStringDate(d);
		String x = (String) newdate;
		attendenceDetails.settAttndncDate(x);
		String date = attendenceDetails.gettAttndncDate();
		
		String empId = "";
		attendenceDetails.gettEmployee();
		try {
			empId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (attendenceDetailsSession != null) {
			model.addAttribute("attendenceDetails", attendenceDetailsSession);
			session.setAttribute("attendenceDetails", null);

		} else {
			model.addAttribute("attendenceDetails", attendenceDetails);
		}
		
		System.out.println("@@@"+attendenceDetails);
		date =  LocalDate.now().toString();
		System.out.println("@@@@@@@"+date);
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					env.getAttendanceUrl() + "get-details?empId=" + empId + "&date=" + date, DropDownModel[].class);
			List<DropDownModel> details = Arrays.asList(dropDownModel);

			model.addAttribute("details", details);
			String isOut  = null ;
			if(!details.isEmpty()) {
				isOut = details.get(0).getName();	
			}
			model.addAttribute("isOut" , isOut);
			boolean arr = details.isEmpty();

			if (!arr) {
				return "attendance/addPunchOutDetails";
			}

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addAttendence ends");
		return "attendance/addPunchInDetails";
	}

	/*
	 * Add Punch In Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-attendence-details" })
	public String addNewAttendence(@ModelAttribute AttendenceDetailsModel attendenceDetails, Model model,
			HttpSession session) {
		logger.info("Method : addNewAttendence starts");
		Date d = new Date();
		Object newdate = DateFormatter.returnStringDate(d);
		String x = (String) newdate;

		Date dt = new Date();
		Object datetime = DateFormatter.returnStringDateTime(dt);

		String x1 = (String) datetime;

		String userId = "";
		String empId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			empId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {

			attendenceDetails.settAttndncCreatedBy(userId);
			attendenceDetails.settEmployee(empId);
			attendenceDetails.settAttndncDate(x);
			attendenceDetails.settAttndncPunchIn(x1);

			jsonResponse = restTemplate.postForObject(env.getAttendanceUrl() + "addAttendencePunchIn", attendenceDetails,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("attendenceDetails", attendenceDetails);
			return "redirect:add-attendence-details";
		}
		logger.info("Method : addNewAttendence ends");
		return "redirect:view-attendence-details";
	}

	/*
	 * Add Punch Out Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-attendence-details-punchout" })
	public String addNewAttendencePunchOut(@ModelAttribute AttendenceDetailsModel attendenceDetails, Model model,
			HttpSession session) {
		logger.info("Method : addNewAttendencePunchOut starts");
		Date d = new Date();
		Object newdate = DateFormatter.returnStringDate(d);
		String x = (String) newdate;

		Date dt2 = new Date();
		Object datetime2 = DateFormatter.returnStringDateTime(dt2);

		String x2 = (String) datetime2;

		String userId = "";
		String empId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			empId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {

			attendenceDetails.settAttndncCreatedBy(userId);
			attendenceDetails.settEmployee(empId);
			attendenceDetails.settAttndncDate(x);

			attendenceDetails.settAttndncPunchOut(x2);

			jsonResponse = restTemplate.postForObject(env.getAttendanceUrl() + "addAttendencePunchOut", attendenceDetails,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("attendenceDetails", attendenceDetails);
			return "redirect:add-attendence-details";
		}
		logger.info("Method : addNewAttendencePunchOut ends");
		return "redirect:view-attendence-details";
	}

	/*
	 * View Default 'Attendence Details' page
	 *
	 */
	@GetMapping(value = { "view-attendence-details" })
	public String viewAttendence(Model model) {
		logger.info("Method : viewAttendence starts");
 
		logger.info("Method : viewAttendence ends");
		return "attendance/viewAttendenceDetails";
	}

	/**
	 * View Attendance Details through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-attendence-details-throughAjax")
	public @ResponseBody DataTableResponse viewAttendenceDetailsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAttendenceDetailsThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<AttendenceDetailsModel>> jsonResponse = new JsonResponse<List<AttendenceDetailsModel>>();

			jsonResponse = restTemplate.postForObject(env.getAttendanceUrl() + "getAttendenceDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AttendenceDetailsModel> attendenceDetails = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AttendenceDetailsModel>>() {
					});

			for (AttendenceDetailsModel m : attendenceDetails) {

				if (m.gettAttndncPunchInLoc() == 1) {
					m.setStatus("office");
				} else {
					m.setStatus("other");
				}

				if (m.gettAttndncPunchOut_Loc() == 1) {
					m.setAction("office");
				} else {
					m.setAction("other");
				}

			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(attendenceDetails);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewAttendenceDetailsThroughAjax ends");
		return response;
	}

}
