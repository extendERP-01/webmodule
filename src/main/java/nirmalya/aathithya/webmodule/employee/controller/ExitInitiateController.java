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
import nirmalya.aathithya.webmodule.employee.model.ExitInitiateModel;

@Controller
@RequestMapping(value = "employee")
public class ExitInitiateController {
	Logger logger = LoggerFactory.getLogger(ExitInitiateController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;  

	@GetMapping("/add-exit-initiate")
	public String addExitInitiate(Model model, HttpSession session, @RequestParam("id") String id,
			@RequestParam("deptId") String deptId, @RequestParam("empName") String empName) {
		logger.info("Method : addExitInitiate start"); 
		model.addAttribute("empId", id);
		model.addAttribute("deptId", deptId);
		model.addAttribute("empName", empName);

		ExitInitiateModel exitInitiateModel = new ExitInitiateModel();

		ExitInitiateModel form = (ExitInitiateModel) session.getAttribute("sExitInitiateModel");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");
		if (form != null) {
			model.addAttribute("exitInitiateModel", form);
			session.setAttribute("sExitInitiateModel", null);

		} else {
			model.addAttribute("exitInitiateModel", exitInitiateModel);
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
		logger.info("Method : addExitInitiate end");
		return "employee/add-exit-intiate";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-exit-initiate", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addExitInitiateAjax(
			@RequestBody List<ExitInitiateModel> exitInitiateModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addExitInitiateAjax function starts"); 
		try {
			for (ExitInitiateModel r : exitInitiateModel) {
				String userId = null;
				try {
					userId = (String) session.getAttribute("USER_ID");
				} catch (Exception e) {
					e.printStackTrace();
				}
				r.setCreatedBy(userId);
			}

			res = restClient.postForObject(env.getEmployeeUrl() + "rest-add-exit-initiate", exitInitiateModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addExitInitiateAjax function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-exit-initiate-empAutoComplete" })
	public @ResponseBody JsonResponse<ExitInitiateModel> getEmpAutoComplete(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getEmpAutoComplete starts");
		System.out.println(searchValue);
		JsonResponse<ExitInitiateModel> res = new JsonResponse<ExitInitiateModel>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmpAutoComplete?id=" + searchValue,
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
		logger.info("Method : getEmpAutoComplete ends");
		return res;
	}

	@GetMapping("/view-exit-initiate")
	public String viewExitInitiate(Model model, HttpSession session) {
		logger.info("Method : viewExitInitiate   starts");
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewExitInitiate   ends");
		return "employee/view-exit-intiate";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-exit-initiate-throughajax")
	public @ResponseBody DataTableResponse viewExitInitiateThroughAjax(Model model, HttpSession session,
			HttpServletRequest request, @RequestParam String param1) {
		logger.info("Method : viewExitInitiateThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			tableRequest.setUserId(userId);
			JsonResponse<List<ExitInitiateModel>> jsonResponse = new JsonResponse<List<ExitInitiateModel>>();
			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAllExitIntiate", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();

			List<ExitInitiateModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ExitInitiateModel>>() {
					});

			String s = "";
			for (ExitInitiateModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getEmpId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ m.getEmpId() + "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
				s = s + " &nbsp;<a href='view-exit-initiate-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a> ";
				/*
				 * s = s + " &nbsp;<a href='javascript:void(0)' onclick='deleteBlkDens(\"" + new
				 * String(pId) + "\")'><i class='fa fa-trash'></i></a> ";
				 */

				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewExitInitiateThroughAjax end");
		return response;
	}

	@GetMapping("/view-exit-initiate-edit")
	public String editExitInitiate(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method in web: editExitInitiate starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
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
		try {
			ExitInitiateModel[] exitInitiateModelList = restClient
					.getForObject(env.getEmployeeUrl() + "editExitIntiateById?id=" + id, ExitInitiateModel[].class);
			List<ExitInitiateModel> exitInitiateModel = Arrays.asList(exitInitiateModelList);

			model.addAttribute("id", exitInitiateModel.get(0).getIntiateId());
			model.addAttribute("exitInitiateModel", exitInitiateModel);
			System.out.println(exitInitiateModel);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editExitInitiate starts");

		return "employee/add-exit-intiate";
	}
}
