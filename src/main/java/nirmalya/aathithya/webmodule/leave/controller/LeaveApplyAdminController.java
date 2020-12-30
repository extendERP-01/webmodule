/*
 * Defines Leave Apply List 
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
import nirmalya.aathithya.webmodule.leave.model.LeaveApplyAdminModel; 
/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "leave")
public class LeaveApplyAdminController {
	
	Logger logger = LoggerFactory.getLogger(LeaveApplyController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	 
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-all-leave-request")
	public String viewApplyAdminLeave(Model model, HttpSession session) {
	
		logger.info("Method : viewApplyAdminLeave starts");
		
		LeaveApplyAdminModel changestatus = new LeaveApplyAdminModel();
		try {
			String message = (String) session.getAttribute("message");
			if (message != null && message != "") {
				model.addAttribute("message", message);	
			}
			session.setAttribute("message", "");		 
			model.addAttribute("changestatus", changestatus);
		}catch(Exception e) {
			e.printStackTrace();
		}
			
		//Start Leave Type Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> LeaveType = new ArrayList<DropDownModel>(); 
		try {
			respTblMstr = restClient.getForObject(env.getLeaveUrl() + "getAdminLeaveType?Action=" + "getAdminLeaveType",
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
		//End Leave Type Drop Down List
		
		
		   //Start Leave Type Drop Down List
			JsonResponse<List<DropDownModel>> respTblMstr1 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> statusType = new ArrayList<DropDownModel>(); 
			try {
				respTblMstr1 = restClient.getForObject(env.getLeaveUrl() + "getStatusType?Action=" + "getStatusType",
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr1 = respTblMstr1.getMessage();

			if (messageForTblMstr1 != null || messageForTblMstr1 != "") {
				model.addAttribute("message", messageForTblMstr1);
			}

			ObjectMapper mapper1 = new ObjectMapper();
 	 
			statusType = mapper1.convertValue(respTblMstr1.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("statusType", statusType);
			//End Leave Type Drop Down List
		
		logger.info("Method : viewApplyAdminLeave ends");		
		return "leave/ViewApplyLeaveAdmin";
	}
	 
	
	/* 
	 * 
	 * Post Mapping add-Leave-Apply
	 *  
	 */  
	@SuppressWarnings("unchecked")
	@PostMapping("/view-all-leave-request-change-status")
	public String LeaveChangeStatus(@ModelAttribute LeaveApplyAdminModel changestatus, Model model, HttpSession session) {
		logger.info("Method : LeaveChangeStatus starts");		 
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.postForObject(env.getLeaveUrl() + "/restChangeStatus"
					+ "", changestatus, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != "" && resp.getMessage() != null) {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("changestatus", changestatus);
			return "redirect:/hrms/view-all-leave-request";
		}
		logger.info("Method : LeaveChangeStatus ends");
		return "redirect:/hrms/view-all-leave-request";
	}
	
	/*
	 *
	 * View all Applied Leave  through AJAX
	 *
	 */
 	@SuppressWarnings("unchecked")
	@GetMapping("/view-all-leave-request-through-ajax") 
	public @ResponseBody DataTableResponse viewApplyALLThroughAjax(Model model, HttpServletRequest request, @RequestParam String param1) {
		logger.info("Method : viewApplyALLThroughAjax starts");	
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			
			JsonResponse<List<LeaveApplyAdminModel>> jsonResponse = new JsonResponse<List<LeaveApplyAdminModel>>();

			jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getAllLApplyData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<LeaveApplyAdminModel> applyleave = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveApplyAdminModel>>() {
					});

			String s = "";
			for (LeaveApplyAdminModel m : applyleave) {
				@SuppressWarnings("unused")
				byte[] pId = Base64.getEncoder().encode(m.getApplyId().getBytes()); 
				String StatusType1="";
				StatusType1="Pending";  
				String StatusType2="";
				StatusType2="Approved"; 
				String StatusType3="";
				StatusType3="Rejected"; 
				if(m.getlApplyStatus().equalsIgnoreCase(StatusType1)) { 
					
					s = s 	+ "<a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + m.getApplyId()
							+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>"
							+"<a href='javascript:void(0)'" 
							+ "' onclick='ChangeStatus(\"" + m.getApplyId() + "\")' >"
									+ "<i class=\"fa fas fa-cog\" title=\"Change Status\" style=\"font-size:24px;color:gray\"></i></a>&nbsp;&nbsp; "
							;
					m.setAction(s); 
					s = "";
				}else if(m.getlApplyStatus().equalsIgnoreCase(StatusType2)) { 
					
					s = s 	+ "<a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + m.getApplyId()
							+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>"
							+"<a href='javascript:void(0)'" 
							+ "' onclick='ChangeStatus(\"" + m.getApplyId() + "\")' >"
									+ "<i class=\"fa fas fa-cog\" title=\"Change Status\" style=\"font-size:24px;color:gray\"></i></a>&nbsp;&nbsp; "
							;
					m.setAction(s); 
					s = "";
				}else if(m.getlApplyStatus().equalsIgnoreCase(StatusType3)) { 
					
					s = s 	+ "<a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + m.getApplyId()
							+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>"
							+"<a href='javascript:void(0)'" 
							+ "' onclick='ChangeStatus(\"" + m.getApplyId() + "\")' >"
									+ "<i class=\"fa fas fa-cog\" title=\"Change Status\" style=\"font-size:24px;color:gray\"></i></a>&nbsp;&nbsp; "
							;
					m.setAction(s); 
					s = "";
				}else { 
					s = s   + "<a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + m.getApplyId()
							+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
					m.setAction(s);
					s = "";
				}	 
			}	
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(applyleave);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		logger.info("Method : viewApplyALLThroughAjax ends");	
		return response;
	}
	
	/*
	 * 
	 * Modal View of Leave Apply 
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-all-leave-request-model" })
	public @ResponseBody JsonResponse<Object> modelViewLeaveApply(Model model, @RequestBody String index, BindingResult result) {	
		logger.info("Method : modelViewLeaveApply starts");	
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {			 
			res = restClient.getForObject(env.getLeaveUrl() + "getLeaveAdmById?id=" + index+"&Action="+"ModelView", JsonResponse.class);
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
	 *  'cancel-leave Page by Id
	 *
	 */
	/* @SuppressWarnings("unchecked")
	 @PostMapping("cancel-leave") 
	 public @ResponseBody JsonResponse<Object> cancelLeave(@RequestBody String id,HttpSession session){
	 logger.info("Method : cancelLeave starts");
	// System.out.println("response delete "+id);
	 JsonResponse<Object> resp = new JsonResponse<Object>();
	  
	 try { 
		 resp = restClient.getForObject(env.getLeaveUrl()+"cancelLeaveById?id="+id,JsonResponse.class);
	 } catch (RestClientException e) { 
		 e.printStackTrace(); 
	 }  
	 if(resp.getMessage()== null || resp.getMessage()=="") 
	 {
		 resp.setMessage("Success");
	 }else
	 {
		 resp.setMessage("Unsuccess");
	 }		
	 logger.info("Method : cancelLeave ends");	
	 return resp;
	 }*/
	 
}
