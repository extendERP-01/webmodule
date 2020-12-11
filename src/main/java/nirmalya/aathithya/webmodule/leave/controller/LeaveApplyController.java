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
import nirmalya.aathithya.webmodule.leave.model.LeaveApplyModel;
import nirmalya.aathithya.webmodule.leave.model.LeaveEmpEntitleModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "leave")
public class LeaveApplyController {

	Logger logger = LoggerFactory.getLogger(LeaveApplyController.class);

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
	@GetMapping("/apply-leave")
	public String applyLeave(Model model, HttpSession session) {

		logger.info("Method : applyLeave starts");

		LeaveApplyModel applyleave = new LeaveApplyModel();
		String empId = "";
		try {

			try {
				empId = (String) session.getAttribute("USER_ID");
				applyleave.setEmpName(empId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");

			model.addAttribute("applyleave", applyleave);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Start leave type name dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leaveTypeData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getLeaveType?Action=" + "getLeaveType",
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

		/* Start leave type name dropdown */
		JsonResponse<List<DropDownModel>> avilLeaves = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leaveAvailData = new ArrayList<DropDownModel>();
		try {
			avilLeaves = restClient.getForObject(env.getLeaveUrl() + "getAvailLeave?empId=" + empId,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageavilLeaves = respTblMstr.getMessage();
		if (messageavilLeaves != null || messageavilLeaves != "") {
			model.addAttribute("message", messageavilLeaves);
		}

		leaveAvailData = mapper1.convertValue(avilLeaves.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("leaveAvailData", leaveAvailData);

		/* End leave type name dropdown */
		logger.info("Method : applyLeave ends");
		return "leave/ApplyLeave";
	}

	/**
	 * get total Leave Details for Employee On Change of Leave Type
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "apply-leave-getLeaveDetails-throughAjax" })
	public @ResponseBody JsonResponse<LeaveEmpEntitleModel> getLeaveList(Model model,
			@RequestBody List<LeaveEmpEntitleModel> params) {
		logger.info("Method :getLeaveList starts");
		JsonResponse<LeaveEmpEntitleModel> res = new JsonResponse<LeaveEmpEntitleModel>();
		try {
			res = restClient.postForObject(env.getLeaveUrl() + "restGetLeaveList", params, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :getLeaveList  ends");
		return res;
	}

	/*
	 * post mapping for leave through ajax
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/apply-leave-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addLeaveAjax(@RequestBody List<LeaveApplyModel> leaveApplyModel,
			Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addLeaveAjax function starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (LeaveApplyModel r : leaveApplyModel) {

				r.setCreatedBy(userId);
				r.setEmpName(userId);
			}

			res = restClient.postForObject(env.getLeaveUrl() + "restAddLeave", leaveApplyModel, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addLeaveAjax function Ends");
		return res;
	}

	/*
	 *
	 * Add leave Apply Master' page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-apply-leave")
	public String viewApplyLeave(Model model, HttpSession session) {
		logger.info("Method : viewApplyLeave starts");
		LeaveApplyModel leaveApply = new LeaveApplyModel();
		try {
			try {
				String empId = (String) session.getAttribute("USER_ID");
				leaveApply.setEmpName(empId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");
			model.addAttribute("leaveApply", leaveApply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start Leave Type drop down
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> LeaveType = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getLeaveType?Action=" + "getLeaveType",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();
		LeaveType = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("LeaveType", LeaveType);
		// End Drop Down List

		logger.info("Method : viewApplyLeave ends");
		return "leave/ViewApplyLeave";
	}

	/*
	 *
	 * View all Applied Leave through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-apply-leave-through-ajax")
	public @ResponseBody DataTableResponse viewApplyLThroughAjax(Model model, HttpServletRequest request,
			HttpSession session, @RequestParam String param1) {

		logger.info("Method : viewApplyLThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			String UserId = (String) session.getAttribute("USER_ID");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setUserId(UserId);

			JsonResponse<List<LeaveApplyModel>> jsonResponse = new JsonResponse<List<LeaveApplyModel>>();

			jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getLApplyData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<LeaveApplyModel> applyleave = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveApplyModel>>() {
					});
			String s = "";
			System.out.println("date are " + applyleave);
			for (LeaveApplyModel m : applyleave) {
				byte[] pId = Base64.getEncoder().encode(m.getApplyId().getBytes());
				s = s + "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				s = s + " &nbsp; <a data-toggle='modal' title='View'  href='javascript:void' onclick='viewHistoryInModel(\""
						+ new String(pId)
						+ "\")'><i class='fa fa-history' aria-hidden='true' style=\"font-size:24px\"></i></a> &nbsp;";
				System.out.println(m.getEmplId()+" * "+userId+" * "+m.getApproveStageNo()+" * "+m.getCurrentStageNo());
				if (m.getEmplId().equals(userId) && m.getApproveStageNo() == m.getCurrentStageNo()) {

					s = s + "<a href='view-apply-leave-edit?leavId=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a> &nbsp; ";
					s = s + "<a href='javascript:void(0)'" + "' onclick='CancelApplication(\"" + m.getApplyId()
							+ "\")' >"
							+ "<i class=\"fa fa-times-circle\" title=\"Cancel\" style=\"font-size:24px;color:#e30f0f\"></i></a>&nbsp;&nbsp; ";

				}
				if (m.getApproveStatus() == 3) {
					s = s + "<a href='view-apply-leave-edit?leavId=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a> &nbsp; ";
				}

				

				m.setAction(s);
				s = "";

				if (m.getApproveStatus() == 3)
					m.setStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setStatusName("Rejected");
				else
					m.setStatusName("Open");

			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(applyleave);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewApplyLThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * Modal View of Leave Apply
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-apply-leave-model" })
	public @ResponseBody JsonResponse<List<Object>> modelViewLeaveApply(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelViewLeaveApply starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<Object>> res = new JsonResponse<List<Object>>();
		try {
			res = restClient.getForObject(env.getLeaveUrl() + "getLeaveApplyById?id=" + id + "&Action=" + "ModelView",
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
		logger.info("Method : modelViewLeaveApply ends");
		return res;
	}

	/*
	 * 
	 * 'cancel-leave Page by Id
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("view-apply-leave-cancel-leave")
	public @ResponseBody JsonResponse<Object> cancelLeave(@RequestBody String id, HttpSession session) {
		logger.info("Method : cancelLeave starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(env.getLeaveUrl() + "cancelLeaveById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}
		logger.info("Method : cancelLeave ends");
		return resp;
	}

	/*
	 * for Edit assign certification
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-apply-leave-edit")
	public String editLeave(Model model, @RequestParam("leavId") String encodeId, HttpSession session) {

		logger.info("Method :editLeave starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<Object>> res = new JsonResponse<List<Object>>();
		try {

			res = restClient.getForObject(
					env.getLeaveUrl() + "getLeaveApplyById?id=" + id + "&Action=" + "editLeaveById",
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<LeaveApplyModel> applyleave = mapper.convertValue(res.getBody(),
					new TypeReference<List<LeaveApplyModel>>() {
					});
			if (applyleave != null) {
				applyleave.get(0).setEditId("edit");
			}
			model.addAttribute("applyleave", applyleave);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Start leave type name dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leaveTypeData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getLeaveType?Action=" + "getLeaveType",
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
		String empId = "";
		try {
			empId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Start leave type name dropdown */
		JsonResponse<List<DropDownModel>> avilLeaves = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leaveAvailData = new ArrayList<DropDownModel>();
		try {
			avilLeaves = restClient.getForObject(env.getLeaveUrl() + "getAvailLeave?empId=" + empId,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageavilLeaves = respTblMstr.getMessage();
		if (messageavilLeaves != null || messageavilLeaves != "") {
			model.addAttribute("message", messageavilLeaves);
		}

		leaveAvailData = mapper1.convertValue(avilLeaves.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("leaveAvailData", leaveAvailData);

		logger.info("Method : editLeave ends");

		return "leave/ApplyLeave";
	}

	/*
	 * Get Mapping view traveling leave approval process
	 */
	@GetMapping("/view-apply-leave-approve")
	public String viewLeaveApprove(Model model, HttpSession session) {

		logger.info("Method : viewLeaveApprove starts");

		logger.info("Method : viewLeaveApprove ends");

		return "leave/view-apply-leave-approve";
	}

	/*
	 * For view reimbursement approval process dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-apply-leave-approve-through-ajax")
	public @ResponseBody DataTableResponse viewReimbursementApproveajax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {
		logger.info("Method : viewReimbursementApproveajax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {

			String UserId = (String) session.getAttribute("USER_ID");

			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setUserId(UserId);

			JsonResponse<List<LeaveApplyModel>> jsonResponse = new JsonResponse<List<LeaveApplyModel>>();

			jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getLApplyDataApprove", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<LeaveApplyModel> applyleave = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveApplyModel>>() {
					});

			String s = "";

			for (LeaveApplyModel m : applyleave) {
				byte[] pId = Base64.getEncoder().encode(m.getApplyId().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				if ((m.getCurrentStageNo() == m.getApproveStageNo()) && (m.getApproveStatus() != 1)) {
					if (m.getApproveStatus() != 3) {
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardLeave(\""
								+ new String(pId) + "\")'><i class='fa fa-forward'></i></a> &nbsp;&nbsp; ";
					} else {
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectLeave(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectLeave(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectLeave(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a> &nbsp;&nbsp; ";
				}
				m.setAction(s);
				s = "";

				if (m.getApproveStatus() == 3)
					m.setStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setStatusName("Rejected");
				else
					m.setStatusName("Open");

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(applyleave);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewReimbursementApproveajax Theme ends");

		return response;
	}

	/*
	 * Forward reimbursement to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "save-leave-approval-action" })
	public @ResponseBody JsonResponse<Object> savereimbursementApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : savereimbursementApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(
					env.getLeaveUrl() + "save-leave-approval-action?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : savereimbursementApprovalAction ends");
		return resp;
	}
	/*
	 * Reject reimbursement
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "save-leave-reject-action" })
	public @ResponseBody JsonResponse<Object> saveLeaveRejectAction(Model model, @RequestBody LeaveApplyModel reqobject,
			BindingResult result, HttpSession session) {
		logger.info("Method : saveLeaveRejectAction starts");

		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getApplyId());
		String applyId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		reqobject.setCreatedBy(userId);
		reqobject.setApplyId(applyId);

		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(
					env.getLeaveUrl() + "save-leave-reject-action?id=" + applyId + "&createdBy=" + userId + "&desc="
							+ reqobject.getlReason() + "&rejectType=" + reqobject.getRejectionType(),
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveLeaveRejectAction ends");
		return res;
	}

	/*
	 * For Modal traveling reimbursement View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-apply-leave-history" })
	public @ResponseBody JsonResponse<List<Object>> viewHistoryInModel(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modaltravelingAprove starts");

		JsonResponse<List<Object>> res = new JsonResponse<List<Object>>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getLeaveUrl() + "getLeaveHistoryById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modaltravelingAprove ends");
		return res;
	}

	/*
	 * 
	 * Modal View of Leave Apply
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-apply-leave-approve-model" })
	public @ResponseBody JsonResponse<List<Object>> modelViewLeaveApprove(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelViewLeaveApprove starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<Object>> res = new JsonResponse<List<Object>>();
		try {
			res = restClient.getForObject(env.getLeaveUrl() + "getLeaveApplyById?id=" + id + "&Action=" + "ModelView",
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
		logger.info("Method : modelViewLeaveApprove ends");
		return res;
	}
}
