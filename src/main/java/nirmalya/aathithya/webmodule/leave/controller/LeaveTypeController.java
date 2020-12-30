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
import nirmalya.aathithya.webmodule.leave.model.LeaveTypeModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "leave")
public class LeaveTypeController {

	Logger logger = LoggerFactory.getLogger(LeaveTypeController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 *
	 * Add leave Type Master' page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/add-leave-type-mstr")
	public String addLeaveTypeMstr(Model model, HttpSession session) {
		logger.info("Method : addLeaveTypeMstr starts");
		// Start of Avail Period Data drop down
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> AvailPeriod = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getAvailPeriod?Action=" + "getAvailPeriod",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		ObjectMapper mapper = new ObjectMapper();
		AvailPeriod = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("AvailPeriod", AvailPeriod);
		// End Of Avail Period data dropdown

		LeaveTypeModel leavetype = new LeaveTypeModel();
		try {
			String message = (String) session.getAttribute("message");
			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");
			model.addAttribute("leavetype", leavetype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : addLeaveTypeMstr ends");
		return "leave/AddLeaveType";
	}

	/*
	 *
	 * View leave Type Master' page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-leave-type-mstr")
	public String viewLeaveType(Model model, HttpSession session) {
		logger.info("Method : viewLeaveType starts");
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> lTypeData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getTypeName?Action=" + "getTypeName",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		ObjectMapper mapper = new ObjectMapper();
		lTypeData = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("lTypeData", lTypeData);
		logger.info("Method : viewLeaveType ends");
		return "leave/ViewLeaveType";
	}

	/*
	 * 
	 * Post Mapping add-Leave-Period
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-leave-type-mstr")
	public String addLeaveType(@ModelAttribute LeaveTypeModel leavetype, Model model, HttpSession session) {
		logger.info("Method : addLeaveType starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			leavetype.setCreatedBy("user001");
			resp = restClient.postForObject(env.getLeaveUrl() + "/restAddLeaveType" + "", leavetype,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != "" && resp.getMessage() != null) {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("leavetype", leavetype);
			return "redirect:/leave/add-leave-type-mstr";
		}
		logger.info("Method : addLeaveType ends");
		return "redirect:/leave/view-leave-type-mstr";
	}

	/*
	 *
	 * View all Leave type data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-leave-type-mstr-through-ajax")
	public @ResponseBody DataTableResponse viewLtypeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewLtypeThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<LeaveTypeModel>> jsonResponse = new JsonResponse<List<LeaveTypeModel>>();

			jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getLtypeData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<LeaveTypeModel> leavetype = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveTypeModel>>() {
					});
			String s = "";

			for (LeaveTypeModel m : leavetype) {
				byte[] pId = Base64.getEncoder().encode(m.getlTypeId().getBytes());

				if (m.getlTypeStatus()) {
					m.setlTypeShowActive("Active");
				} else {
					m.setlTypeShowActive("Inactive");

				}
				if (m.getlAccrueEnb()) {
					m.setlTypeShowAccrue("Yes");
				} else {
					m.setlTypeShowAccrue("No");

				}
				if (m.getlCarriedFwd()) {
					m.setlTypeShowCarried("Yes");
				} else {
					m.setlTypeShowCarried("No");

				}
				s = s + "<a href='edit-leave-type-mstr?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" + m.getlTypeId()
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:24px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; "
						+ "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ m.getlTypeId() + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(leavetype);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewLtypeThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * Modal View of Leave Type
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-leave-type-mstr-model" })
	public @ResponseBody JsonResponse<Object> modelViewLeaveType(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelViewLeaveType starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getLeaveUrl() + "getLeaveTypeById?id=" + index + "&Action=" + "ModelView",
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
		logger.info("Method : modelViewLeaveType ends");
		return res;
	}

	/*
	 * 
	 * 'Delete-leave-type Page by Id
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("delete-leave-type-mstr")
	public @ResponseBody JsonResponse<Object> deleteLType(@RequestBody String id, HttpSession session) {
		logger.info("Method : deleteLType starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(env.getLeaveUrl() + "deleteLtypeById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}
		logger.info("Method : deleteLType ends");
		return resp;
	}

	/*
	 * 'Edit Leave type Master' By Id
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-leave-type-mstr")
	public String editLType(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {
		logger.info("Method : editLType starts");
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> AvailPeriod = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getAvailPeriod?Action=" + "getAvailPeriod",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		ObjectMapper mapper = new ObjectMapper();
		AvailPeriod = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("AvailPeriod", AvailPeriod);
		// End Of Avail Period data dropdown
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));
		LeaveTypeModel leavetype = new LeaveTypeModel();
		JsonResponse<LeaveTypeModel> jsonResponse = new JsonResponse<LeaveTypeModel>();
		try {
			jsonResponse = restClient.getForObject(
					env.getLeaveUrl() + "getLeaveTypeById?id=" + id + "&Action=viewEditLeaveType", JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper1 = new ObjectMapper();
		leavetype = mapper1.convertValue(jsonResponse.getBody(), LeaveTypeModel.class);
		session.setAttribute("message", "");
		model.addAttribute("leavetype", leavetype);
		logger.info("Method : editLType ends");
		return "leave/AddLeaveType";
	}
}
