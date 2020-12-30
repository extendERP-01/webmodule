/*
 * Defines Leave Rejected 
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
public class LeaveRejectedController {
	
	Logger logger = LoggerFactory.getLogger(LeaveRejectedController.class); 
	@Autowired
	RestTemplate restClient; 
	@Autowired
	EnvironmentVaribles env;
	
	 
	/*
	 *
	 *  View Apply Leave Rejected page
	 * 
	 */
	/*@SuppressWarnings("unchecked")
	@GetMapping("/view-rejected-leave")
	public String viewRejectedLeave(Model model, HttpSession session) {
	
		logger.info("Method : viewRejectedLeave starts");
		
	  JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> RLeaveType = new ArrayList<DropDownModel>(); 
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getRLeaveType?Action=" + "getRLeaveType",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		 
		RLeaveType = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("LeaveType", RLeaveType);  
		logger.info("Method : viewRejectedLeave ends");	
		
		return "hrms/ViewApplyLeaveRejected";
	}
	*/
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-rejected-leave")
	public String viewRejectedLeave(Model model, HttpSession session) { 
		logger.info("Method : viewRejectedLeave starts"); 
		LeaveApplyModel leaveRejected = new LeaveApplyModel();
		try {
			try {
				String empId = (String) session.getAttribute("USER_ID");
				leaveRejected.setEmpName(empId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = (String) session.getAttribute("message"); 
			if (message != null && message != "") {
				model.addAttribute("message", message);	
			}
			session.setAttribute("message", ""); 
			model.addAttribute("leaveRejected", leaveRejected);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//Start Leave Type drop down 
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> RLeaveType = new ArrayList<DropDownModel>(); 
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getRLeaveType?Action=" + "getRLeaveType",
					JsonResponse.class); 
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		} 
		ObjectMapper mapper = new ObjectMapper(); 
		RLeaveType = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("LeaveType", RLeaveType);   
		//End Drop Down List
		
		logger.info("Method : viewRejectedLeave ends");	 
		return "leave/ViewApplyLeaveRejected";
	}
	 
	/*
	 *
	 * View all Rejected Leave Applied through AJAX
	 *
	 */
  	@SuppressWarnings("unchecked")
	@GetMapping("/view-rejected-leave-through-ajax") 
	public @ResponseBody DataTableResponse viewApplyRLThroughAjax(Model model, HttpServletRequest request, @RequestParam String param1, @RequestParam String param2) {
		
		logger.info("Method : viewApplyRLThroughAjax starts"); 
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
            jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getRLApplyData", tableRequest,
					JsonResponse.class); 
			ObjectMapper mapper = new ObjectMapper(); 
			List<LeaveApplyModel> applyleave = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveApplyModel>>() {
					});

			String s = "";
			for (LeaveApplyModel m : applyleave) {
				@SuppressWarnings("unused")
				byte[] pId = Base64.getEncoder().encode(m.getApplyId().getBytes());
							
				s = s  + "<a data-toggle='modal' title='View'  "
						+ "href='javascript:void' onclick='viewInModel(\"" + m.getApplyId()
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
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
		logger.info("Method : viewApplyRLThroughAjax ends");	
		return response;
	}
 
	
	/*
	 * 
	 * Modal View of Leave Rejected
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-rejected-leave-model" })
	public @ResponseBody JsonResponse<Object> modelViewLeaveRejected(Model model, @RequestBody String index, BindingResult result) {
		
		logger.info("Method : modelViewLeaveRejected starts"); 
		JsonResponse<Object> res = new JsonResponse<Object>(); 
		try { 
			res = restClient.getForObject(env.getLeaveUrl() + "getLeaveRejectedById?id=" + index+"&Action="+"ModelView", JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (res.getMessage() != null) {
		 	res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}		
		logger.info("Method : modelViewLeaveRejected ends");	
		return res;
	}
	 
}
