package nirmalya.aathithya.webmodule.planning.controller;

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
import nirmalya.aathithya.webmodule.planning.model.HrmsWorksheetPlanningModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "planning")
public class HrmsWorkForcePlanning {

	Logger logger = LoggerFactory.getLogger(HrmsWorkForcePlanning.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping add work sheet planning
	 */
	@GetMapping("/add-planning-worksheet")
	public String addWorkForcePlanning(Model model, HttpSession session) {

		logger.info("Method : viewEmployeeAppraisalGoals  starts");

		/*
		 * for viewing drop down list of Department
		 */
		try {
			DropDownModel[] department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewEmployeeAppraisalGoals  ends");

		return "planning/add-planning-worksheet";
	}

	/*
	 * post Mapping for Get Employee Auto Complete List
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-planning-worksheet-employee-auto-complete" })

	public @ResponseBody JsonResponse<List<DropDownModel>> getEmployeeListAutoComplete(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getEmployeeListAutoComplete starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getEmployeeListAutoComplete ends");
		return res;
	}

	/*
	 * drop down for get employee list by department
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-planning-worksheet-get-employee" })
	public @ResponseBody JsonResponse<HrmsWorksheetPlanningModel> getEmployeeDetails(
			@RequestParam String departmentId) {
		logger.info("Method : getEmployeeDetails starts");

		JsonResponse<HrmsWorksheetPlanningModel> res = new JsonResponse<HrmsWorksheetPlanningModel>();

		try {
			res = restClient.getForObject(env.getPlanningUrl() + "get-apprisal-employee-details?deptId=" + departmentId,
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

		logger.info("Method : getEmployeeDetails  ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-planning-worksheet-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveGradeRevisionData(
			@RequestBody List<HrmsWorksheetPlanningModel> testData, Model model, HttpSession session) {
		logger.info("Method : saveGradeRevisionData function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			for (HrmsWorksheetPlanningModel m : testData) {
				m.setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getPlanningUrl() + "saveWorkSheetData", testData, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveGradeRevisionData function Ends");
		return res;
	}

	/*
	 * Get Mapping view work sheet planning
	 */
	@GetMapping("/view-planning-worksheet")
	public String viewWorkForcePlanning(Model model, HttpSession session) {

		logger.info("Method : viewEmployeeAppraisalGoals  starts");

		logger.info("Method : viewEmployeeAppraisalGoals  ends");

		return "planning/view-planning-worksheet";
	}

	/*
	 * For view employee Grade Salary Master details for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-planning-worksheet-through-ajax")
	public @ResponseBody DataTableResponse viewGradeSalaryMasterThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewGradeSalaryMasterThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsWorksheetPlanningModel>> jsonResponse = new JsonResponse<List<HrmsWorksheetPlanningModel>>();

			jsonResponse = restClient.postForObject(env.getPlanningUrl() + "get-work-sheet-planning-details",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsWorksheetPlanningModel> gradeSalaryMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsWorksheetPlanningModel>>() {
					});

			String s = "";

			for (HrmsWorksheetPlanningModel m : gradeSalaryMaster) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getWorkForcePlanId().getBytes());

				s = s + "<a href='view-planning-worksheet-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;";
				s = s + "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
				m.setAction(s);
				s = "";

			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(gradeSalaryMaster);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewGradeSalaryMasterThroughAjax ends");

		return response;
	}
	
	/*
	 * edit Grade Salary Master details
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-planning-worksheet-edit")
	public String editGradeSalaryMaster(Model model, @RequestParam("id") String grade ,HttpSession session) {

		logger.info("Method : editGradeSalaryMaster starts");
		byte[] encodeByte = Base64.getDecoder().decode(grade.getBytes()); 
		String id = (new String(encodeByte)); 
		JsonResponse<Object> res = new JsonResponse<Object>(); 
		/*
		 * for viewing drop down list of Department
		 */
		try {
			DropDownModel[] department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			res = restClient.getForObject(env.getPlanningUrl()+ "get-work-plan-details-by-id?id=" + id,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<HrmsWorksheetPlanningModel> planningList = mapper.convertValue(res.getBody(),
				new TypeReference<List<HrmsWorksheetPlanningModel>>() {
				});
		model.addAttribute("id", planningList.get(0).getWorkForcePlanId()); 
		model.addAttribute("planningList", planningList);
	 

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editGradeSalaryMaster ends");
		return "planning/add-planning-worksheet";

	}
	
	/*
	 * For Modal Grade Salary Master details
	 */
	 
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-planning-worksheet-modalView" })
	public @ResponseBody JsonResponse<Object> modalGradeSalaryMaster(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalGradeSalaryMaster starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getPlanningUrl()+ "get-work-plan-details-by-id?id=" + id,
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
		logger.info("Method :modalGradeSalaryMaster ends");
		return res;
	}
}
