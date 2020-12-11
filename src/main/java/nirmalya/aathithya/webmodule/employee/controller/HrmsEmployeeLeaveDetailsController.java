package nirmalya.aathithya.webmodule.employee.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeLeaveModel;

/*
 * @author Nirmalya labs
 */
@Controller
public class HrmsEmployeeLeaveDetailsController {
	Logger logger = LoggerFactory.getLogger(HrmsEmployeeLeaveDetailsController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add leave details view page
	 */
	@GetMapping("/add-emp-leave-details")
	public String addemergencyMaster(Model model, HttpSession session) {

		logger.info("Method : addemergencyMaster starts");

		HrmsEmployeeLeaveModel leaveDetails = new HrmsEmployeeLeaveModel();
		HrmsEmployeeLeaveModel sessionemergency = (HrmsEmployeeLeaveModel) session.getAttribute("sessionemergency");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}
		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception ex) {

		}
		leaveDetails.setLeaveGrantBy(userName);
		session.setAttribute("message", "");

		if (sessionemergency != null) {
			model.addAttribute("leaveDetails", sessionemergency);
			session.setAttribute("sessionemergency", null);
		} else {
			model.addAttribute("leaveDetails", leaveDetails);
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addemergencyMaster ends");

		return "employee/add-employee-leave-details";
	}

	/*
	 * Post Mapping for adding new emergency
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-emp-leave-details")
	public String addemergencyMasterPost(@ModelAttribute HrmsEmployeeLeaveModel leaveDetails, Model model,
			HttpSession session) {

		logger.info("Method : addemergencyMasterPost starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID"); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			System.out.println(leaveDetails);
			leaveDetails.setLeaveGrantBy(userId);
			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddEmpAvance", leaveDetails,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionemergency", leaveDetails);
			return "redirect:/employee/add-emp-leave-details";
		}
		logger.info("Method : addemergencyMasterPost ends");

		return "redirect:/employee/view-employee-leave-details";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-employee-leave-details")
	public String viewemergencyMaster(Model model, HttpSession session) {

		logger.info("Method : viewemergencyMaster starts");

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewemergencyMaster ends");

		return "employee/view-emp-leave-details";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-leave-details-ThroughAjax")
	public @ResponseBody DataTableResponse viewemergencyMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewemergencyMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsEmployeeLeaveModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeLeaveModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getleaveDetailsDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeLeaveModel> emergency = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeLeaveModel>>() {
					});

			String s = "";

			for (HrmsEmployeeLeaveModel m : emergency) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getLeaveId().getBytes());

				s = s + "<a href='view-emp-leave-details-edit?empId=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp; &nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(emergency);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewemergencyMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit emergency
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-emp-leave-details-edit")
	public String editemergency(Model model, @RequestParam("empId") String encodeId, HttpSession session) {

		logger.info("Method :editemergency starts");

		HrmsEmployeeLeaveModel leaveDetails = new HrmsEmployeeLeaveModel();
		JsonResponse<HrmsEmployeeLeaveModel> jsonResponse = new JsonResponse<HrmsEmployeeLeaveModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(
					env.getEmployeeUrl() + "getEmpAdvById?empId=" + id + "&action=" + "getleaveDetailsEdit",
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		leaveDetails = mapper.convertValue(jsonResponse.getBody(), HrmsEmployeeLeaveModel.class);
		session.setAttribute("message", "");

		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception ex) {

		}
		leaveDetails.setLeaveGrantBy(userName);
		model.addAttribute("leaveDetails", leaveDetails);
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] payMode = restClient.getForObject(env.getInventoryUrl() + "getPayMode",
					DropDownModel[].class);
			List<DropDownModel> PayModeList = Arrays.asList(payMode);

			model.addAttribute("PayModeList", PayModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : editemergency ends");

		return "employee/add-emp-leave-details";
	}

	/*
	 * For Modal emergency View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-leave-details-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalemergency starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(
					env.getEmployeeUrl() + "getEmpAdvById?empId=" + id + "&action=" + "getleaveDetailsModal",
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalemergency ends");
		return res;
	}
}
