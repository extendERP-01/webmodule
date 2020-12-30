/*
 * Defines Leave Period  Master  related method calls 
 */
package nirmalya.aathithya.webmodule.leave.controller;

import java.util.ArrayList;
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
import nirmalya.aathithya.webmodule.leave.model.LeaveRuleModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "leave")
public class LeaveRuleController {

	Logger logger = LoggerFactory.getLogger(LeaveRuleController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 *
	 * Add leave Period Master' page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/add-leave-rule-mstr")
	public String addLeaveRule(Model model, HttpSession session) {
		logger.info("Method : addLeaveRule starts");
		LeaveRuleModel leaverule = new LeaveRuleModel();
		try {
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");
			model.addAttribute("leaverule", leaverule);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Start leave type name dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leaveTypeData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getLeaveTypeName?Action=" + "getLeaveTypeName",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper1 = new ObjectMapper();
		leaveTypeData = mapper1.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("leaveTypeData", leaveTypeData);
		/* End leave type name dropdown */


		/* Start Department dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr4 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> deptData = new ArrayList<DropDownModel>();
		try {
			respTblMstr4 = restClient.getForObject(env.getLeaveUrl() + "getDeptName?Action=" + "getDeptName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr4 = respTblMstr4.getMessage();
		if (messageForTblMstr4 != null || messageForTblMstr4 != "") {
			model.addAttribute("message", messageForTblMstr4);
		}

		ObjectMapper mapper5 = new ObjectMapper();
		deptData = mapper5.convertValue(respTblMstr4.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("deptData", deptData);
		/* End Department dropdown */

		/* Start Leave Period dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr5 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leavePeriodData = new ArrayList<DropDownModel>();
		try {
			respTblMstr5 = restClient.getForObject(env.getLeaveUrl() + "getLeavePeriod?Action=" + "getLeavePeriod",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr5 = respTblMstr5.getMessage();

		if (messageForTblMstr5 != null || messageForTblMstr5 != "") {
			model.addAttribute("message", messageForTblMstr5);
		}
		ObjectMapper mapper6 = new ObjectMapper();
		leavePeriodData = mapper6.convertValue(respTblMstr5.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("leavePeriodData", leavePeriodData);
		/* End Leave Period dropdown */
		/* Start Avail Period dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr6 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> availPeriod = new ArrayList<DropDownModel>();
		try {
			respTblMstr6 = restClient.getForObject(
					env.getLeaveUrl() + "getRuleAvailPeriod?Action=" + "getRuleAvailPeriod", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr6 = respTblMstr6.getMessage();
		if (messageForTblMstr6 != null || messageForTblMstr6 != "") {
			model.addAttribute("message", messageForTblMstr6);
		}
		ObjectMapper mapper7 = new ObjectMapper();
		availPeriod = mapper7.convertValue(respTblMstr6.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("availPeriod", availPeriod);
		/* End Avail Period dropdown */

		logger.info("Method : addLeaveRule ends");
		return "leave/AddLeaveRule";
	}

	/*
	 *
	 * View leave Period Master' page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-leave-rule-mstr")
	public String viewLeaveRuleMstr(Model model, HttpSession session) {
		logger.info("Method : viewLeaveRuleMstr starts");
		/* Start leave type name dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leaveTypeData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getLeaveTypeName?Action=" + "getLeaveTypeName",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		ObjectMapper mapper1 = new ObjectMapper();
		leaveTypeData = mapper1.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("leaveTypeData", leaveTypeData);
		/* End leave type name dropdown */
		logger.info("Method : viewLeaveRuleMstr ends");
		return "leave/ViewLeaveRule";
	}

	/*
	 * 
	 * Post Mapping add-Leave-Period
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-leave-rule-mstr")
	public String addLeaveRule(@ModelAttribute LeaveRuleModel leaverule, Model model, HttpSession session) {
		logger.info("Method : addLeaveRule starts");
		System.out.println("posted Leave Rule data" + leaverule);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			leaverule.setCreatedBy("user001");
			resp = restClient.postForObject(env.getLeaveUrl() + "/restAddLeaveRule" + "", leaverule, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != "" && resp.getMessage() != null) {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("leaverule", leaverule);
			return "redirect:/leave/add-leave-rule-mstr";
		}
		logger.info("Method : addLeaveRule ends");
		return "redirect:/leave/view-leave-rule-mstr";
	}

	/*
	 *
	 * View all Leave Rule data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-leave-rule-mstr-through-ajax")
	public @ResponseBody DataTableResponse viewLRuleThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewLRuleThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<LeaveRuleModel>> jsonResponse = new JsonResponse<List<LeaveRuleModel>>();
			jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getLRuleData", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<LeaveRuleModel> leaverule = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveRuleModel>>() {
					});
			String s = "";

			for (LeaveRuleModel m : leaverule) {
				byte[] pId = Base64.getEncoder().encode(m.getRuleId().getBytes());

				s = s + "<a href='edit-leave-rule-mstr?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" + m.getRuleId()
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:24px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; "
						+ "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ m.getRuleId() + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
				// System.out.println("data in status "+m.getDistrictStatus());
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(leaverule);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewLRuleThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * 'Delete-leave-rule Page by Id
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("delete-leave-rule-mstr")
	public @ResponseBody JsonResponse<Object> deleteLRule(@RequestBody String id, HttpSession session) {
		logger.info("Method : deleteLRule starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(env.getLeaveUrl() + "deleteLRuleById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}
		logger.info("Method : deleteLRule ends");
		return resp;
	}

	/*
	 * 
	 * Modal View of Leave Rule
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-leave-rule-mstr-model" })
	public @ResponseBody JsonResponse<Object> modelViewLeaveRule(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelViewLeaveRule starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {

			res = restClient.getForObject(
					env.getLeaveUrl() + "getLeaveRuleByIdModel?id=" + index + "&Action=" + "ModelView",
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
		System.out.println(res);
		logger.info("Method : modelViewLeaveRule ends");
		return res;
	}

	/*
	 * 'Edit Leave Rule Master' By Id
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-leave-rule-mstr")
	public String editLRule(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {
		logger.info("Method : editLRule starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));
		LeaveRuleModel leaverule = new LeaveRuleModel();
		JsonResponse<LeaveRuleModel> jsonResponse = new JsonResponse<LeaveRuleModel>();
		try {
			jsonResponse = restClient.getForObject(
					env.getLeaveUrl() + "getLeaveRuleById?id=" + id + "&Action=viewEditLeaveRule", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		leaverule = mapper.convertValue(jsonResponse.getBody(), LeaveRuleModel.class);
		session.setAttribute("message", "");

		model.addAttribute("leaverule", leaverule);
		if (leaverule != null) {
			/* Start leave type name dropdown */
			JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> leaveTypeData = new ArrayList<DropDownModel>();
			try {
				respTblMstr = restClient.getForObject(
						env.getLeaveUrl() + "getLeaveTypeName?Action=" + "getLeaveTypeName", JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr = respTblMstr.getMessage();

			if (messageForTblMstr != null || messageForTblMstr != "") {
				model.addAttribute("message", messageForTblMstr);
			}

			ObjectMapper mapper1 = new ObjectMapper();

			leaveTypeData = mapper1.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("leaveTypeData", leaveTypeData);
			/* End leave type name dropdown */

			/* Start jobTitle dropdown */
			JsonResponse<List<DropDownModel>> respTblMstr1 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> jobTitleData = new ArrayList<DropDownModel>();
			try {
				respTblMstr1 = restClient.getForObject(
						env.getLeaveUrl() + "getJobTitle?Action=" + "getJobTitle&id=" + leaverule.getDepartment(),
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr1 = respTblMstr1.getMessage();

			if (messageForTblMstr1 != null || messageForTblMstr1 != "") {
				model.addAttribute("message", messageForTblMstr1);
			}

			ObjectMapper mapper2 = new ObjectMapper();

			jobTitleData = mapper2.convertValue(respTblMstr1.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("jobTitleData", jobTitleData);
			/* End jobTitle dropdown */

			/* Start Employment Status dropdown */
			JsonResponse<List<DropDownModel>> respTblMstr2 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> emplStatusData = new ArrayList<DropDownModel>();
			try {
				respTblMstr2 = restClient.getForObject(
						env.getLeaveUrl() + "getEmplmtStatus?Action=" + "getEmplmtStatus&id=" + leaverule.getEmpl(),
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr2 = respTblMstr2.getMessage();

			if (messageForTblMstr2 != null || messageForTblMstr2 != "") {
				model.addAttribute("message", messageForTblMstr2);
			}

			ObjectMapper mapper3 = new ObjectMapper();

			emplStatusData = mapper3.convertValue(respTblMstr2.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("emplStatusData", emplStatusData);
			/* End Employment Status dropdown */

			/* Start Employee dropdown */
			JsonResponse<List<DropDownModel>> respTblMstr3 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> emplData = new ArrayList<DropDownModel>();
			try {
				respTblMstr3 = restClient.getForObject(
						env.getLeaveUrl() + "getEmpName?Action=" + "getEmpName &id=" + leaverule.getJobTitle(),
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr3 = respTblMstr3.getMessage();

			if (messageForTblMstr3 != null || messageForTblMstr3 != "") {
				model.addAttribute("message", messageForTblMstr3);
			}

			ObjectMapper mapper4 = new ObjectMapper();

			emplData = mapper4.convertValue(respTblMstr3.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("emplData", emplData);
			/* End Employee dropdown */

			/* Start Department dropdown */
			JsonResponse<List<DropDownModel>> respTblMstr4 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> deptData = new ArrayList<DropDownModel>();
			try {
				respTblMstr4 = restClient.getForObject(env.getLeaveUrl() + "getDeptName?Action=" + "getDeptName",
						JsonResponse.class);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr4 = respTblMstr4.getMessage();

			if (messageForTblMstr4 != null || messageForTblMstr4 != "") {
				model.addAttribute("message", messageForTblMstr4);
			}

			ObjectMapper mapper5 = new ObjectMapper();

			deptData = mapper5.convertValue(respTblMstr4.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("deptData", deptData);
			/* End Department dropdown */

			/* Start Leave Period dropdown */
			JsonResponse<List<DropDownModel>> respTblMstr5 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> leavePeriodData = new ArrayList<DropDownModel>();
			try {
				respTblMstr5 = restClient.getForObject(env.getLeaveUrl() + "getLeavePeriod?Action=" + "getLeavePeriod",
						JsonResponse.class);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr5 = respTblMstr5.getMessage();

			if (messageForTblMstr5 != null || messageForTblMstr5 != "") {
				model.addAttribute("message", messageForTblMstr5);
			}
			ObjectMapper mapper6 = new ObjectMapper();
			leavePeriodData = mapper6.convertValue(respTblMstr5.getBody(), new TypeReference<List<DropDownModel>>() {
			});
			model.addAttribute("leavePeriodData", leavePeriodData);
			/* End Leave Period dropdown */

			/* Start Avail Period dropdown */
			JsonResponse<List<DropDownModel>> respTblMstr6 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> availPeriod = new ArrayList<DropDownModel>();
			try {
				respTblMstr6 = restClient.getForObject(
						env.getLeaveUrl() + "getRuleAvailPeriod?Action=" + "getRuleAvailPeriod", JsonResponse.class);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr6 = respTblMstr6.getMessage();

			if (messageForTblMstr6 != null || messageForTblMstr6 != "") {
				model.addAttribute("message", messageForTblMstr6);
			}
			ObjectMapper mapper7 = new ObjectMapper();
			availPeriod = mapper7.convertValue(respTblMstr6.getBody(), new TypeReference<List<DropDownModel>>() {
			});
			model.addAttribute("availPeriod", availPeriod);
			/* End Avail Period dropdown */
		}
		logger.info("Method : editLRule ends");
		return "leave/AddLeaveRule";
	}

	/*
	 * drop down for supervisor by job title
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-leave-rule-mstr-jobtitle" })
	public @ResponseBody JsonResponse<DropDownModel> getJobTitleLIst(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getJobTitleLIstt starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getLeaveUrl() + "getJobTypeOnChange?deptId=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getJobTitleLIst    ends");
		return res;
	}

	/*
	 * drop down for employee by job title
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-leave-rule-mstr-employeeList" })
	public @ResponseBody JsonResponse<DropDownModel> getEmployeeList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getEmployeeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getLeaveUrl() + "getEmployeeList?jobTitleId=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getEmployeeList  ends");
		return res;
	}

	/*
	 * drop down for employment list
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-leave-rule-mstr-employmentStatus" })
	public @ResponseBody JsonResponse<DropDownModel> getEmploymentList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getEmploymentList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getLeaveUrl() + "getEmploymentList?empId=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getEmploymentList  ends");
		return res;
	}

}
