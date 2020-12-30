package nirmalya.aathithya.webmodule.reimbursement.controller;

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
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsTravelingRequisituionModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "reimbursement")
public class HrmsTrvelingRequisitionController {
	Logger logger = LoggerFactory.getLogger(HrmsTrvelingRequisitionController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add traveling requisition view page
	 */
	@GetMapping("/add-traveling-requisition")
	public String addRtravelingrequisition(Model model, HttpSession session) {

		logger.info("Method : addRtravelingrequisition starts");

		HrmsTravelingRequisituionModel travelingrequisition = new HrmsTravelingRequisituionModel();
		HrmsTravelingRequisituionModel sessiontravelingrequisition = (HrmsTravelingRequisituionModel) session
				.getAttribute("sessiontravelingrequisition");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessiontravelingrequisition != null) {
			model.addAttribute("travelingrequisition", sessiontravelingrequisition);
			session.setAttribute("sessiontravelingrequisition", null);
		} else {
			model.addAttribute("travelingrequisition", travelingrequisition);
		}

		logger.info("Method : addRtravelingrequisition ends");

		return "reimbursement/add-traveling-requisition";
	}

	/*
	 * Post Mapping for adding new traveling requisition
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-traveling-requisition")
	public String addRtravelingrequisitionPost(@ModelAttribute HrmsTravelingRequisituionModel travelingrequisition, Model model,
			HttpSession session) {

		logger.info("Method : addRtravelingrequisition Post starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		travelingrequisition.setCreatedBy(userId);
		travelingrequisition.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getReimbursementUrl() + "restAddTravelingRequisitions", travelingrequisition,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessiontravelingrequisition", travelingrequisition);
			return "redirect:/reimbursement/add-traveling-requisition";
		}
		logger.info("Method : addRtravelingrequisition Post ends");

		return "redirect:/reimbursement/view-traveling-requisition";
	}

	/*
	 * Get Mapping view traveling requisition master
	 */
	@GetMapping("/view-traveling-requisition")
	public String viewtravelingrequisition(Model model, HttpSession session) {

		logger.info("Method : viewtravelingrequisition starts");

		logger.info("Method : viewtravelingrequisition ends");

		return "reimbursement/view-traveling-requisition";
	}

	/*
	 * For view traveling requisition for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-traveling-requisition-ThroughAjax")
	public @ResponseBody DataTableResponse viewdepartmentMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1 ,HttpSession session) {

		logger.info("Method : viewdepartmentMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
        String userId = "";
		try {
			
			 userId = (String) session.getAttribute("USER_ID");
			 
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
            
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
            tableRequest.setUserId(userId);
            
			JsonResponse<List<HrmsTravelingRequisituionModel>> jsonResponse = new JsonResponse<List<HrmsTravelingRequisituionModel>>();

			jsonResponse = restClient.postForObject(env.getReimbursementUrl() + "getTravelingRequisitionDetails",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsTravelingRequisituionModel> travelingrequisition = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsTravelingRequisituionModel>>() {
					});

			String s = "";

			for (HrmsTravelingRequisituionModel m : travelingrequisition) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getReqId().getBytes());
				if(m.getReqStatus() != 1 ) {
				s = s + "<a href='view-traveling-requisition-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletetravelingType(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
				
				s = s + " &nbsp; <a data-toggle='modal' title='View'  href='javascript:void' onclick='viewHistoryInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-history' aria-hidden='true' style=\"font-size:20px\"></i></a>";
				}else {
					s = s + "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
							+ new String(encodeId)
							+ "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
					
					s = s + " &nbsp; <a data-toggle='modal' title='View'  href='javascript:void' onclick='viewHistoryInModel(\""
							+ new String(encodeId)
							+ "\")'><i class='fa fa-history' aria-hidden='true' style=\"font-size:20px\"></i></a>";
				}
				m.setAction(s);
				s = "";
				if(m.getAdvanceNeed()) {
					m.setDelete("YES");
				}else {
					m.setDelete("NO");
				}
				if(m.getReqStatus() == 3)
					m.setApproveStatus("Returned");
				else if(m.getReqStatus() == 1)
					m.setApproveStatus("Approved");
				else if(m.getReqStatus() == 2)
					m.setApproveStatus("Rejected");
				else
					m.setApproveStatus("Open");
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(travelingrequisition);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewdepartmentMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit traveling requisition
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-traveling-requisition-edit")
	public String edittraveling(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :edittraveling starts");

		HrmsTravelingRequisituionModel travelingrequisition = new HrmsTravelingRequisituionModel();
		JsonResponse<HrmsTravelingRequisituionModel> jsonResponse = new JsonResponse<HrmsTravelingRequisituionModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getReimbursementUrl() + "getTravelingRequisitionById?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		travelingrequisition = mapper.convertValue(jsonResponse.getBody(), HrmsTravelingRequisituionModel.class);
		session.setAttribute("message", "");
		model.addAttribute("travelingrequisition", travelingrequisition);

		logger.info("Method : edit edittraveling ends");
		return "reimbursement/add-traveling-requisition";
	}

	/*
	 * For Delete traveling requisition
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-traveling-requisition-delete")
	public @ResponseBody JsonResponse<Object> deletetraveling(@RequestParam String id, HttpSession session) {

		logger.info("Method : deletetraveling ends");

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
					env.getReimbursementUrl() + "deleteTravelingRequisitionById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deletetraveling  ends");

		return resp;
	}

	/*
	 * For Modal traveling requisition View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-traveling-requisition-modalView" })
	public @ResponseBody JsonResponse<Object> modaltraveling(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modaltraveling starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getTravelingRequisitionById?id=" + id,
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
		logger.info("Method :modaltraveling ends");
		return res;
	}
	
	
	
	/*
	 * Get Mapping view traveling requisition master
	 */
	@GetMapping("/view-traveling-requisition-approve-stage")
	public String viewtravelingrequisitionFirstStage(Model model, HttpSession session) {

		logger.info("Method : viewtravelingrequisitionFirstStage starts");

		logger.info("Method : viewtravelingrequisitionFirstStage ends");

		return "reimbursement/view-traveling-requisition-first-stage";
	}

	/*
	 * For view traveling requisition for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-traveling-requisition-approve-stage-ThroughAjax")
	public @ResponseBody DataTableResponse viewtravelingrequisitionFirstStagejax(Model model, HttpServletRequest request,
			@RequestParam String param1 , HttpSession session) {

		logger.info("Method : viewtravelingrequisitionFirstStagejax statrs");

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

			JsonResponse<List<HrmsTravelingRequisituionModel>> jsonResponse = new JsonResponse<List<HrmsTravelingRequisituionModel>>();

			jsonResponse = restClient.postForObject(env.getReimbursementUrl() + "getTravelingRequisitionDetailsFirst",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsTravelingRequisituionModel> travelingrequisition = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsTravelingRequisituionModel>>() {
					});

			String s = "";

		/*	for (HrmsTravelingRequisituionModel m : travelingrequisition) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getReqId().getBytes());
				s = s + "<a href='view-traveling-requisition-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletetravelingType(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
				if(m.getAdvanceNeed()) {
					m.setDelete("YES");
				}else {
					m.setDelete("NO");
				}
				
			}*/
			
			for (HrmsTravelingRequisituionModel m : travelingrequisition) {
				byte[] pId = Base64.getEncoder().encode(m.getReqId().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'style=\"font-size:20px\"></i></a>";
								
				if ((m.getCurrentStageNo() == m.getApproversStageNo()) && (m.getReqStatus() != 1)) {
					if(m.getReqStatus() != 3){
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardRequisition(\""
						+ new String(pId) + "\")'><i class='fa fa-forward'style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
					}else{
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectRequisition(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send' style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectRequisition(\""
						+ new String(pId) + "\",1)'><i class='fa fa-close'style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectRequisition(\""
						+ new String(pId) + "\",2)'><i class='fa fa-undo'style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
				}
				m.setAction(s);
				s = "";
				if(m.getAdvanceNeed()) {
					m.setDelete("YES");
				}else {
					m.setDelete("NO");
				}
				if(m.getReqStatus() == 3)
					m.setApproveStatus("Returned");
				else if(m.getReqStatus() == 1)
					m.setApproveStatus("Approved");
				else if(m.getReqStatus() == 2)
					m.setApproveStatus("Rejected");
				else
					m.setApproveStatus("Open");

			}
			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(travelingrequisition);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewtravelingrequisitionFirstStagejax Theme ends");

		return response;
	}
	
	/*
	 * Forward Requisition to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "save-requisition-approval-action" })
	public @ResponseBody JsonResponse<Object> saveRequisitionApprovalAction(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {
		logger.info("Method : saveRequisitionApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			
		};
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(env.getReimbursementUrl() + "save-requisition-approval-action?id=" + id+"&createdBy="+userId,
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
		logger.info("Method : saveRequisitionApprovalAction ends");
		return resp;
	}
	/*
	 * Reject Requisition 
	 * 
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "save-requisition-reject-action" })
	public @ResponseBody JsonResponse<Object> saveRequisitionRejectAction(Model model, @RequestBody HrmsTravelingRequisituionModel reqobject,
			BindingResult result,HttpSession session) {
		logger.info("Method : saveRequisitionRejectAction starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getReqId());
		String reqstnId = (new String(encodeByte));
		
		String userId = "";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			
		};
		reqobject.setCreatedBy(userId);
		reqobject.setReqId(reqstnId);
		/*
		 * reqobject.setCostCenter(userId);
		 * 
		 * reqobject.setItemRequisition(reqstnId);
		 */
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getReimbursementUrl()+ "save-requisition-reject-action",reqobject,
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
		logger.info("Method : saveRequisitionRejectAction ends");
		return res;
	}
	
	
	/*
	 * For Modal traveling requisition View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-traveling-requisition-approve-stage-modalView" })
	public @ResponseBody JsonResponse<Object> modaltravelingAprove(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modaltravelingAprove starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getTravelingRequisitionById?id=" + id,
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
		logger.info("Method :modaltravelingAprove ends");
		return res;
	}
	
	
	
	/*
	 * For Modal traveling requisition View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-traveling-requisition-history" })
	public @ResponseBody JsonResponse<List<Object>> viewHistoryInModel(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modaltravelingAprove starts");

		JsonResponse<List<Object>> res = new JsonResponse<List<Object>>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getTravelingRequisitionHistoryById?id=" + id,
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
		logger.info("Method :modaltravelingAprove ends");
		return res;
	}
}
