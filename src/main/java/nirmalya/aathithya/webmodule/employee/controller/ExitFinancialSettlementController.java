package nirmalya.aathithya.webmodule.employee.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
public class ExitFinancialSettlementController {
	Logger logger = LoggerFactory.getLogger(ExistManagementController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@GetMapping("/add-exist-finance-settlement")
	public String addExistFinance(Model model, HttpSession session,@RequestParam("id") String id,@RequestParam("deptId") String deptId,@RequestParam("empName") String empName,@RequestParam("depName") String depName) throws UnsupportedEncodingException {

		logger.info("Method : addExistManagement starts");
		
		ExitFinancialSettelmentModel exitFinancialSettelmentModel = new ExitFinancialSettelmentModel();
		ExitFinancialSettelmentModel sessionExitFinancialSettelment = (ExitFinancialSettelmentModel) session.getAttribute("sessionExitFinancialSettelment");
		
		
		exitFinancialSettelmentModel.setEmployeeId(id);
		exitFinancialSettelmentModel.setEmpName(empName);
		exitFinancialSettelmentModel.setEmpDepartment(deptId);
		
		exitFinancialSettelmentModel.setEmpDepartmentName(URLDecoder.decode(depName, "UTF-8"));
		//depName
		model.addAttribute("deptId", deptId);
		
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionExitFinancialSettelment != null) {
			model.addAttribute("exitFinancialSettelmentModel", sessionExitFinancialSettelment);
			session.setAttribute("sessionExitFinancialSettelment", null);
		} else {
			model.addAttribute("exitFinancialSettelmentModel", exitFinancialSettelmentModel);
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
		try {
			DropDownModel[] department = restClient.getForObject(env.getEmployeeUrl() + "getEmpDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : addExistFinance ends");

		return "employee/add-exist-finance";
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/add-exist-finance-settlement")
	public String addExistFinance(@ModelAttribute ExitFinancialSettelmentModel exitFinancialSettelmentModel, Model model, HttpSession session) {

		logger.info("Method : addExistFinance starts");
		
		System.out.println(exitFinancialSettelmentModel);

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		exitFinancialSettelmentModel.setCreatedBy(userId);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddExitFinance", exitFinancialSettelmentModel, JsonResponse.class);
			
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionExitFinancialSettelment", exitFinancialSettelmentModel);
			return "redirect:/employee/add-exist-finance-settlement";
		}
		logger.info("Method : addExistFinance ends");

		return "redirect:/employee/view-exist-finance-settlement";
	}
	@GetMapping("/view-exist-finance-settlement")
	public String viewExistFinanceSettelement(Model model, HttpSession session) {

		logger.info("Method : viewExistFinanceSettelement starts");
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewExistFinanceSettelement ends");

		return "employee/view-exist-finance";
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-exist-finance-settlement-ThroughAjax")
	public @ResponseBody DataTableResponse viewExistFinanceSettelementAjax(Model model,HttpSession session, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewExistFinanceSettelementAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			

			JsonResponse<List<ExitFinancialSettelmentModel>> jsonResponse = new JsonResponse<List<ExitFinancialSettelmentModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "viewExitFinanceDtls", tableRequest,
					JsonResponse.class);
			List<String> roles = null;
			try {
				roles = (List<String>) session.getAttribute("USER_ROLES");
			} catch (Exception e) {
				e.printStackTrace();
			}
			ObjectMapper mapper = new ObjectMapper();

			List<ExitFinancialSettelmentModel> exitFinancialSettelmentModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ExitFinancialSettelmentModel>>() {
					});

			String s = "";
			System.out.println(exitFinancialSettelmentModel);
			for (ExitFinancialSettelmentModel m : exitFinancialSettelmentModel) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getFinanceId().getBytes());
			
				s = s + "<a href='view-exist-finance-settlement-edit?id=" + new String(encodeId) 
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";

		
			/*	s = s + "<a href='view-exist-management-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a><a href='javascript:void(0)'\"+ \"' onclick='deleteEmployeeExit(\\\"\" + new String(encodeId)+ \"\\\")'><i class=\\\"fa fa-trash\\\" aria-hidden=\\\"true\\\" style=\\\"font-size:24px\\\"></i></a>";*/
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(exitFinancialSettelmentModel);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewExistFinanceSettelementAjax Theme ends");

		return response;
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-exist-finance-settlement-edit")
	public String editExitFinance(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editExitFinance starts");

		ExitFinancialSettelmentModel exitFinancialSettelmentModel = new ExitFinancialSettelmentModel();
		JsonResponse<ExitFinancialSettelmentModel> jsonResponse = new JsonResponse<ExitFinancialSettelmentModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {
			
			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getExitFinanceById?id=" + id,
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

		exitFinancialSettelmentModel = mapper.convertValue(jsonResponse.getBody(), ExitFinancialSettelmentModel.class);
		
		session.setAttribute("message", "");

		model.addAttribute("exitFinancialSettelmentModel", exitFinancialSettelmentModel);

		logger.info("Method : exitFinancialSettelmentModel ends");

		return "employee/add-exist-finance";
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-exist-finance-settlement-modalView" })
	public @ResponseBody JsonResponse<Object> modalExitSettlementMaster(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalExitSettlementMaster starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getExitFinanceById?id=" + id, JsonResponse.class);
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
		logger.info("Method :modalExitSettlementMaster ends");
		return res;
	}
}
