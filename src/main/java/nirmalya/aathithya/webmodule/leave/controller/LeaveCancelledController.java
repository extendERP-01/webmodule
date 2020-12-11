/*
 * Defines Leave Cancelled 
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
import nirmalya.aathithya.webmodule.leave.model.LeaveApplyModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "leave")
public class LeaveCancelledController {

	Logger logger = LoggerFactory.getLogger(LeaveCancelledController.class);
	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 *
	 * View Apply Leave Cancel page
	 * 
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("/view-cancelled-leave") public String viewCancelledLeave(Model
	 * model, HttpSession session) {
	 * 
	 * logger.info("Method : viewCancelledLeave starts");
	 * 
	 * JsonResponse<List<DropDownModel>> respTblMstr = new
	 * JsonResponse<List<DropDownModel>>(); List<DropDownModel> RLeaveType = new
	 * ArrayList<DropDownModel>(); try { respTblMstr =
	 * restClient.getForObject(env.getLeaveUrl() + "getCLeaveType?Action=" +
	 * "getCLeaveType", JsonResponse.class);
	 * 
	 * } catch (RestClientException e) { e.printStackTrace(); } String
	 * messageForTblMstr = respTblMstr.getMessage();
	 * 
	 * if (messageForTblMstr != null || messageForTblMstr != "") {
	 * model.addAttribute("message", messageForTblMstr); }
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * 
	 * RLeaveType = mapper.convertValue(respTblMstr.getBody(), new
	 * TypeReference<List<DropDownModel>>() { });
	 * 
	 * model.addAttribute("LeaveType", RLeaveType);
	 * logger.info("Method : viewCancelledLeave ends");
	 * 
	 * return "hrms/ViewApplyLeaveCancelled"; }
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-cancelled-leave")
	public String viewCancelledLeave(Model model, HttpSession session) {
		logger.info("Method : viewCancelledLeave starts");
		LeaveApplyModel leaveCancelled = new LeaveApplyModel();
		try {

			try {
				String empId = (String) session.getAttribute("USER_ID");
				leaveCancelled.setEmpName(empId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = (String) session.getAttribute("message");
			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");
			model.addAttribute("leaveCancelled", leaveCancelled);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start Leave Type drop down
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> CLeaveType = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getCLeaveType?Action=" + "getCLeaveType",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		CLeaveType = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("LeaveType", CLeaveType);
		// End Drop Down List

		logger.info("Method : viewCancelledLeave ends");
		return "leave/ViewApplyLeaveCancelled";
	}

	/*
	 *
	 * View all Cancelled Leave Applied through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-cancelled-leave-through-ajax")
	public @ResponseBody DataTableResponse viewApplyCLThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewApplyCLThroughAjax starts");

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

			JsonResponse<List<LeaveApplyModel>> jsonResponse = new JsonResponse<List<LeaveApplyModel>>();

			jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getCLApplyData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<LeaveApplyModel> applyleave = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveApplyModel>>() {
					});

			String s = "";
			for (LeaveApplyModel m : applyleave) {
				@SuppressWarnings("unused")
				byte[] pId = Base64.getEncoder().encode(m.getApplyId().getBytes());

				s = s + "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ m.getApplyId() + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(applyleave);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewApplyCLThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * Modal View of Leave Cancelled
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-cancelled-leave-model" })
	public @ResponseBody JsonResponse<Object> modelViewLeaveCancelled(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelViewLeaveCancelled starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(
					env.getLeaveUrl() + "getLeaveCancelById?id=" + index + "&Action=" + "ModelView",
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
		logger.info("Method : modelViewLeaveCancelled ends");
		return res;
	}
}
