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
import nirmalya.aathithya.webmodule.employee.model.ExitFinancialSettelmentModel;
import nirmalya.aathithya.webmodule.employee.model.ExitManagementModel;

@Controller
@RequestMapping(value = "employee")
public class ExistManagementController {
	Logger logger = LoggerFactory.getLogger(ExistManagementController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@GetMapping("/add-exist-management")
	public String addExistManagement(Model model, HttpSession session) {

		logger.info("Method : addExistManagement starts");

		ExitManagementModel exitManagement = new ExitManagementModel();
		ExitManagementModel sessionExitManagment = (ExitManagementModel) session.getAttribute("sessionExitManagment");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionExitManagment != null) {
			model.addAttribute("exitManagement", sessionExitManagment);
			session.setAttribute("sessionExitManagment", null);
		} else {
			model.addAttribute("exitManagement", exitManagement);
		}
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addExistManagement ends");

		return "employee/add-exist-management";
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/add-exist-management")
	public String addExistManagementData(@ModelAttribute ExitManagementModel exitManagement, Model model, HttpSession session) {

		logger.info("Method : addExistManagementData starts");

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		exitManagement.setCreatedBy(userId);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddExitManagement", exitManagement, JsonResponse.class);
			System.out.println(exitManagement);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionExitManagment", exitManagement);
			return "redirect:/employee/add-exist-management";
		}
		logger.info("Method : addExistManagementData ends");

		return "redirect:/employee/view-exist-management";
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-exist-management-emp-degination-ajax" })
	public @ResponseBody JsonResponse<Object> getExitManagementEmpDeginationAjax(Model model, @RequestBody String employeName,
			BindingResult result) {
		logger.info("Method : getExitManagementEmpDeginationAjax starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-empdegination?id=" + employeName,
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
		logger.info("Method : getExitManagementEmpDeginationAjax ends");
		return res;

	}
	
	@GetMapping("/view-exist-management")
	public String viewExistManagement(Model model, HttpSession session) {

		logger.info("Method : viewExistManagement starts");
		
		logger.info("Method : viewExistManagement ends");

		return "employee/view-exist-management";
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-exist-management-ThroughAjax")
	public @ResponseBody DataTableResponse viewExistManagementDetailsAjax(Model model,HttpSession session, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2) {

		logger.info("Method : viewExistManagementDetailsAjax statrs");

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

			JsonResponse<List<ExitManagementModel>> jsonResponse = new JsonResponse<List<ExitManagementModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "viewExitManagementDtls", tableRequest,
					JsonResponse.class);
			List<String> roles = null;
			try {
				roles = (List<String>) session.getAttribute("USER_ROLES");
			} catch (Exception e) {
				e.printStackTrace();
			}
			ObjectMapper mapper = new ObjectMapper();

			List<ExitManagementModel> exitdtls = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ExitManagementModel>>() {
					});

			String s = "";
			System.out.println(exitdtls);
			for (ExitManagementModel m : exitdtls) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getExitManagementId().getBytes());
				if (roles.contains("rol026")) {
					System.out.println("rol001");
					s = s + "<a Title='Initiate' href='/employee/add-exit-initiate?id="
							+ m.getEmpName()+ "&deptId=" + m.getEmpDepartment() + "&empName="
									+ m.getEmpId() + "'><i class='fa fa-tasks' aria-hidden='true' style=\"font-size:20px\"></i></a>&nbsp;&nbsp;";
				}
				if (roles.contains("rol026") && m.getExitStatus()==1) {
					s = s + "<a Title='Initiate' href='/employee/add-exist-finance-settlement?id="
							+ m.getEmpName()+ "&deptId=" + m.getEmpDepartment() + "&empName="
									+ m.getEmpId() + "&depName="
											+ m.getEmpDepartmentName() + "'><i class='fa fa-money' aria-hidden='true'  style=\"font-size:20px\"></i></a>&nbsp;&nbsp;";
				}
				s = s + "<a href='view-exist-management-edit?id=" + new String(encodeId) 
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteEmployeeExit(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";

		
			/*	s = s + "<a href='view-exist-management-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a><a href='javascript:void(0)'\"+ \"' onclick='deleteEmployeeExit(\\\"\" + new String(encodeId)+ \"\\\")'><i class=\\\"fa fa-trash\\\" aria-hidden=\\\"true\\\" style=\\\"font-size:24px\\\"></i></a>";*/
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(exitdtls);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewExistManagementDetailsAjax Theme ends");

		return response;
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-exist-management-edit")
	public String editExitManagement(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editExitManagement starts");

		ExitManagementModel exitManagementModel = new ExitManagementModel();
		JsonResponse<ExitManagementModel> jsonResponse = new JsonResponse<ExitManagementModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {
			
			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getExitById?id=" + id,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		exitManagementModel = mapper.convertValue(jsonResponse.getBody(), ExitManagementModel.class);
		try {
			
			DropDownModel[] designation = restClient.getForObject(
					env.getEmployeeUrl() + "getEmpDesignationEdit?id=" + exitManagementModel.getEmpId(),
					DropDownModel[].class);
			List<DropDownModel> designationList = Arrays.asList(designation);
			model.addAttribute("designationList", designationList);
		
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		session.setAttribute("message", "");

		model.addAttribute("exitManagement", exitManagementModel);

		logger.info("Method : editExitManagement ends");

		return "employee/add-exist-management";
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-exist-management-delete")
	public @ResponseBody JsonResponse<Object> getExitManagementDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getExitManagementDelete ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp = restClient.getForObject(
					env.getEmployeeUrl() + "deleteExitManagementById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : getExitManagementDelete  ends");

		return resp;
	}

	/*
	 * For Modal Goal Master View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-exist-management-modalView" })
	public @ResponseBody JsonResponse<Object> modalExitMaster(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalGoalMaster starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getExitById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		System.out.println(res);
		logger.info("Method :modalExitMaster ends");
		return res;
	}
	
}
