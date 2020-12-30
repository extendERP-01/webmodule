/*

 * Defines Leave Period  Master  related method calls 
 */
package nirmalya.aathithya.webmodule.leave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.leave.model.LeaveEmpEntitleModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "leave")
public class LeaveEmpEntitlementController {

	Logger logger = LoggerFactory.getLogger(LeaveEmpEntitlementController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 *
	 * Add leave Period Master' page
	 * 
	 */
	@GetMapping("/view-leave-entitlement")
	public String viewLeaveEntitlement(Model model, HttpSession session) {

		logger.info("Method : viewLeaveEntitlement starts");

		LeaveEmpEntitleModel leavelist = new LeaveEmpEntitleModel();
		try {
			try {
				String empId = (String) session.getAttribute("USER_ID");
				leavelist.setEmpl(empId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");

			model.addAttribute("leavelist", leavelist);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewLeaveEntitlement ends");

		return "leave/ViewEmpEntitlement";
	}

	/*
	 *
	 * View Apply Leave Entitle page
	 * 
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("/view-leave-entitlement") public String
	 * viewLeaveEntitlement(Model model, HttpSession session) {
	 * 
	 * logger.info("Method : viewLeaveEntitlement starts");
	 * 
	 * 
	 * logger.info("Method : viewLeaveEntitlement ends");
	 * 
	 * return "hrms/ViewEmpEntitlement"; }
	 */

	/*
	 *
	 * View all Leave Entitlement through AJAX
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-leave-entitlement-through-ajax")
	public @ResponseBody DataTableResponse viewLeaveEntitleThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewLeaveEntitleThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<LeaveEmpEntitleModel>> jsonResponse = new JsonResponse<List<LeaveEmpEntitleModel>>();

			jsonResponse = restClient.postForObject(env.getLeaveUrl() + "getLEntitleData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<LeaveEmpEntitleModel> leavelist = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LeaveEmpEntitleModel>>() {
					});

			String s = "";
			for (LeaveEmpEntitleModel m : leavelist) {

				s = s + "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ m.getEmpl() + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(leavelist);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewLeaveEntitleThroughAjax ends");
		return response;
	}

	/*
	 *
	 * View all Leave Period data through AJAX
	 *
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("/view-leave-entitlement-through-ajax") public @ResponseBody
	 * DataTableResponse viewLEntitleThroughAjax(Model model, HttpServletRequest
	 * request, @RequestParam String param1) {
	 * logger.info("Method : viewLEntitleThroughAjax starts");
	 * 
	 * DataTableResponse response = new DataTableResponse(); DataTableRequest
	 * tableRequest = new DataTableRequest();
	 * 
	 * 
	 * try { String start = request.getParameter("start"); String length =
	 * request.getParameter("length"); String draw = request.getParameter("draw");
	 * 
	 * tableRequest.setStart(Integer.parseInt(start));
	 * tableRequest.setLength(Integer.parseInt(length));
	 * tableRequest.setParam1(param1);
	 * 
	 * 
	 * // System.out.println("param 1 is---------"+param1);
	 * 
	 * 
	 * JsonResponse<List<LeaveEmpEntitleModel>> jsonResponse = new
	 * JsonResponse<List<LeaveEmpEntitleModel>>();
	 * 
	 * jsonResponse = restClient.postForObject(env.getLeaveUrl() +
	 * "getLEntitleData", tableRequest, JsonResponse.class);
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * List<LeaveEmpEntitleModel> leavelist =
	 * mapper.convertValue(jsonResponse.getBody(), new
	 * TypeReference<List<LeavePeriodModel>>() { });
	 * 
	 * response.setRecordsTotal(jsonResponse.getTotal());
	 * response.setRecordsFiltered(jsonResponse.getTotal());
	 * response.setDraw(Integer.parseInt(draw)); response.setData(leavelist);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * logger.info("Method : viewLEntitleThroughAjax ends"); return response; }
	 */

	/*
	 * 
	 * Modal View of Leave Period
	 *
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "/view-leave-period-model" }) public @ResponseBody
	 * JsonResponse<Object> modelViewLeavePeriod(Model model, @RequestBody String
	 * index, BindingResult result) {
	 * 
	 * logger.info("Method : modelLeavePeriod starts");
	 * 
	 * JsonResponse<Object> res = new JsonResponse<Object>();
	 * 
	 * try {
	 * 
	 * res = restClient.getForObject(env.getLeaveUrl() + "getLeavePeriodById?id=" +
	 * index+"&Action="+"ModelView", JsonResponse.class); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * if (res.getMessage() != null) { res.setCode(res.getMessage());
	 * res.setMessage("Unsuccess"); } else { res.setMessage("success"); }
	 * logger.info("Method : modelLeavePeriod ends"); return res; }
	 */

	/*
	 * 
	 * 'Delete-leave-period Page by Id
	 *
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping("delete-leave-period") public @ResponseBody JsonResponse<Object>
	 * deleteLPeriod(@RequestBody String id,HttpSession session) {
	 * 
	 * logger.info("Method : deleteLPeriod starts"); //
	 * System.out.println("response delete "+id); JsonResponse<Object> resp = new
	 * JsonResponse<Object>();
	 * 
	 * try { resp =
	 * restClient.getForObject(env.getLeaveUrl()+"deleteLperiodById?id="+id,
	 * JsonResponse.class); } catch (RestClientException e) { e.printStackTrace(); }
	 * // System.out.println("response delete in web controller "+resp);
	 * if(resp.getMessage()== null || resp.getMessage()=="") {
	 * resp.setMessage("Success"); }else { resp.setMessage("Unsuccess"); }
	 * 
	 * logger.info("Method : deleteLPeriod ends"); return resp; }
	 */

	/*
	 * 'Edit Leave Period Master' By Id
	 * 
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("/edit-leave-period") public String editLPeriod(Model
	 * model, @RequestParam("id") String encodedIndex, HttpSession session) {
	 * 
	 * logger.info("Method : editLPeriod starts");
	 * 
	 * 
	 * 
	 * 
	 * byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
	 * String id = (new String(encodeByte));
	 * 
	 * LeavePeriodModel leaveperiod = new LeavePeriodModel();
	 * JsonResponse<LeavePeriodModel> jsonResponse = new
	 * JsonResponse<LeavePeriodModel>();
	 * 
	 * try { ///getDistrictById jsonResponse = restClient.getForObject(
	 * env.getLeaveUrl() + "getLeavePeriodById?id=" + id +
	 * "&Action=viewEditLeavePeriod", JsonResponse.class);
	 * 
	 * } catch (RestClientException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * String message = (String) session.getAttribute("message");
	 * 
	 * if (message != null && message != "") { model.addAttribute("message",
	 * message); }
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * leaveperiod = mapper.convertValue(jsonResponse.getBody(),
	 * LeavePeriodModel.class); session.setAttribute("message", "");
	 * 
	 * model.addAttribute("leaveperiod", leaveperiod);
	 * 
	 * logger.info("Method : editLPeriod ends"); return "hrms/AddLeavePeriod"; }
	 */
}
