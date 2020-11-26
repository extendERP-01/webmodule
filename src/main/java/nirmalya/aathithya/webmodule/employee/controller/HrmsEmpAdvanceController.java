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
import nirmalya.aathithya.webmodule.employee.model.HrmsAdvancePaymentModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmpAdvanceController {

	Logger logger = LoggerFactory.getLogger(HrmsEmpAdvanceController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add emergency view page
	 */
	@GetMapping("/add-emp-advance")
	public String addemergencyMaster(Model model, HttpSession session) {

		logger.info("Method : addemergencyMaster starts");

		HrmsAdvancePaymentModel advance = new HrmsAdvancePaymentModel();
		HrmsAdvancePaymentModel sessionemergency = (HrmsAdvancePaymentModel) session.getAttribute("sessionemergency");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}
		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		}catch(Exception ex) {
			
		}
		advance.setPaymentMadeBy(userName);
		session.setAttribute("message", "");

		if (sessionemergency != null) {
			model.addAttribute("advance", sessionemergency);
			session.setAttribute("sessionemergency", null);
		} else {
			model.addAttribute("advance", advance);
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
		try {
			DropDownModel[] payMode = restClient.getForObject(env.getInventoryUrl() + "getPayMode",
					DropDownModel[].class);
			List<DropDownModel> PayModeList = Arrays.asList(payMode);

			model.addAttribute("PayModeList", PayModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addemergencyMaster ends");

		return "employee/add-employee-advance";
	}

	/*
	 * Post Mapping for adding new emergency
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-emp-advance")
	public String addemergencyMasterPost(@ModelAttribute HrmsAdvancePaymentModel advancePayment, Model model,
			HttpSession session) {

		logger.info("Method : addemergencyMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			System.out.println(advancePayment);
			advancePayment.setPaymentMadeBy(userId);
			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddEmpAvance", advancePayment, JsonResponse.class);

		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionemergency", advancePayment);
			return "redirect:/employee/add-emp-advance";
		}
		logger.info("Method : addemergencyMasterPost ends");

		return "redirect:/employee/view-employee-advance";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-employee-advance")
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

		return "employee/view-employee-advance";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-advance-ThroughAjax")
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

			JsonResponse<List<HrmsAdvancePaymentModel>> jsonResponse = new JsonResponse<List<HrmsAdvancePaymentModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAdvanceDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsAdvancePaymentModel> emergency = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsAdvancePaymentModel>>() {
					});

			String s = "";

			for (HrmsAdvancePaymentModel m : emergency) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getAdvPayId().getBytes());

				s = s + "<a href='view-emp-advance-edit?empId=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a> &nbsp; &nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + "\")'><i class='fa fa-search search'style=\"font-size:20px\"></i></a>";
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
	@GetMapping("/view-emp-advance-edit")
	public String editemergency(Model model, @RequestParam("empId") String encodeId, HttpSession session) {

		logger.info("Method :editemergency starts");

		HrmsAdvancePaymentModel advance = new HrmsAdvancePaymentModel();
		JsonResponse<HrmsAdvancePaymentModel> jsonResponse = new JsonResponse<HrmsAdvancePaymentModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getEmpAdvById?empId=" + id +"&action=" +"getAdvanceEdit",
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

		advance = mapper.convertValue(jsonResponse.getBody(), HrmsAdvancePaymentModel.class);
		session.setAttribute("message", "");
		
		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		}catch(Exception ex) {
			
		}
		advance.setPaymentMadeBy(userName);
		

		advance.setEditId("for Edit");
		
		model.addAttribute("advance", advance);
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

		return "employee/add-employee-advance";
	}

	/*
	 * For Modal emergency View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-advance-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalemergency starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmpAdvById?empId=" + id +"&action=" +"getAdvanceModal", JsonResponse.class);
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
