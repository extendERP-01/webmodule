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
import nirmalya.aathithya.webmodule.attendance.model.HolidayMasterModel;

/*
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "attendance")
public class HolidayMasterController {

	Logger logger = LoggerFactory.getLogger(HolidayMasterController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * View Default 'Add Work Week' page
	 *
	 */
	@GetMapping(value = { "add-holiday-master" })
	public String addHoliday(Model model, HttpSession session) {
		logger.info("Method : addHoliday starts");
		HolidayMasterModel holidayListMaster = new HolidayMasterModel();

		HolidayMasterModel holidayListMasterSession = (HolidayMasterModel) session.getAttribute("holidayListMaster");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (holidayListMasterSession != null) {
			model.addAttribute("holidayListMaster", holidayListMasterSession);
			session.setAttribute("holidayListMaster", null);

		} else {
			model.addAttribute("holidayListMaster", holidayListMaster);
		}

		/*
		 * dropDown value for working status
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getAttendanceUrl() + "get-holiday-status",
					DropDownModel[].class);
			List<DropDownModel> holidayStatus = Arrays.asList(dropDownModel);
			model.addAttribute("holidayStatus", holidayStatus);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addHoliday ends");
		return "attendance/addHolidayMaster";
	}

	/*
	 * Add Work Week Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-holiday-master" })
	public String addNewHoliday(@ModelAttribute HolidayMasterModel holidayListMaster, Model model,
			HttpSession session) {
		logger.info("Method : addNewHoliday starts");
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
			holidayListMaster.settHolidayCreatedBy(userId);
			holidayListMaster.settCompanyId(companyId);
			jsonResponse = restTemplate.postForObject(env.getAttendanceUrl() + "addHoliday", holidayListMaster,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("holidayListMaster", holidayListMaster);
			return "redirect:add-holiday-master";
		}
		logger.info("Method : addNewHoliday ends");
		return "redirect:view-holiday-master";
	}

	/*
	 * View Default 'Holiday List' page
	 *
	 */
	@GetMapping(value = { "view-holiday-master" })
	public String viewHoliday(Model model) {
		logger.info("Method : viewHoliday starts");

		logger.info("Method : viewHoliday ends");
		return "attendance/viewHolidayMaster";
	}

	/*
	 * View Holiday List through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-holiday-master-throughAjax")
	public @ResponseBody DataTableResponse viewHolidayThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewHolidayThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HolidayMasterModel>> jsonResponse = new JsonResponse<List<HolidayMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getAttendanceUrl() + "getAllHoliday", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HolidayMasterModel> holidayListMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HolidayMasterModel>>() {
					});

			String s = "";
			for (HolidayMasterModel m : holidayListMaster) {
				byte[] pId = Base64.getEncoder().encode(m.gettHoliday().getBytes());

				m.setStatus(s);
				;

				s = "";

				s = s + "<a  title='Edit' href='edit-holiday-master?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit' style=\"font-size:20px\"></i></a>";
				s = s + "&nbsp; &nbsp; <a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash' style=\"font-size:20px\" ></i></a> ";
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(holidayListMaster);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewHolidayThroughAjax ends");
		return response;
	}

	/*
	 * Edit defined work week
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-holiday-master")
	public String editHoliday(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editHoliday starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<HolidayMasterModel> res = new JsonResponse<HolidayMasterModel>();
		HolidayMasterModel holidayListMaster = new HolidayMasterModel();

		try {
			res = restTemplate.getForObject(env.getAttendanceUrl() + "editHoliday?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		holidayListMaster = mapper.convertValue(res.getBody(), HolidayMasterModel.class);
		session.setAttribute("message", "");
		model.addAttribute("holidayListMaster", holidayListMaster);
		/*
		 * dropDown value for working status
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getAttendanceUrl() + "get-holiday-status",
					DropDownModel[].class);
			List<DropDownModel> holidayStatus = Arrays.asList(dropDownModel);
			model.addAttribute("holidayStatus", holidayStatus);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : editHoliday ends");
		return "attendance/addHolidayMaster";
	}

	/*
	 * Delete selected Holiday
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-holiday-list" })
	public @ResponseBody JsonResponse<Object> deleteHoliday(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {

		logger.info("Method : deleteHoliday starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		String createdBy = "u0002";

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(env.getAttendanceUrl() + "deleteHoliday?id=" + id + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteHoliday ends");
		return resp;
	}

}
