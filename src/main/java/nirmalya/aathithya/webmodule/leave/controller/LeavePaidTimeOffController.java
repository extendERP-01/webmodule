/*

 * Defines Leave Period  Master  related method calls 
 */
package nirmalya.aathithya.webmodule.leave.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.leave.model.LeavePaidTimeOffModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "hrms")
public class LeavePaidTimeOffController {

	Logger logger = LoggerFactory.getLogger(LeavePaidTimeOffController.class);
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
	@GetMapping("/add-paid-time-off")
	public String addPaidTimeOff(Model model, HttpSession session) {
		logger.info("Method : addLeavePeriod starts");
		LeavePaidTimeOffModel leavepaidtime = new LeavePaidTimeOffModel();
		try {
			String message = (String) session.getAttribute("message");
			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");
			model.addAttribute("leavepaidtime", leavepaidtime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Start leave type name dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leaveTypeData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getHrmsUrl() + "getLeaveTypeName?Action=" + "getLeaveTypeName",
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

		/* Start Employee dropdown *//*
										 * JsonResponse<List<DropDownModel>> respTblMstr3 = new
										 * JsonResponse<List<DropDownModel>>(); List<DropDownModel> emplData = new
										 * ArrayList<DropDownModel>(); try { respTblMstr3 =
										 * restClient.getForObject(env.getHrmsUrl() + "getEmpName?Action=" +
										 * "getEmpName", JsonResponse.class);
										 * 
										 * } catch (RestClientException e) { e.printStackTrace(); } String
										 * messageForTblMstr3 = respTblMstr3.getMessage();
										 * 
										 * if (messageForTblMstr3 != null || messageForTblMstr3 != "") {
										 * model.addAttribute("message", messageForTblMstr3); }
										 * 
										 * ObjectMapper mapper4 = new ObjectMapper();
										 * 
										 * emplData = mapper4.convertValue(respTblMstr3.getBody(), new
										 * TypeReference<List<DropDownModel>>() { });
										 * 
										 * model.addAttribute("emplData", emplData);
										 */
		/* End Employee dropdown */

		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("emplData", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Start Leave Period dropdown */
		JsonResponse<List<DropDownModel>> respTblMstr5 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> leavePeriodData = new ArrayList<DropDownModel>();
		try {
			respTblMstr5 = restClient.getForObject(env.getHrmsUrl() + "getLeavePeriod?Action=" + "getLeavePeriod",
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
		logger.info("Method : addPaidTimeOff ends");
		return "leave/AddPaidTimeOff";
	}

	/*
	 *
	 * View leave Period Master' page
	 * 
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("/view-leave-period") public String viewLeavePeriod(Model model,
	 * HttpSession session) {
	 * 
	 * logger.info("Method : viewLeavePeriod starts");
	 * 
	 * JsonResponse<List<DropDownModel>> respTblMstr = new
	 * JsonResponse<List<DropDownModel>>(); List<DropDownModel> lPeriodData = new
	 * ArrayList<DropDownModel>(); //UserDistrictModel tableSession =
	 * (UserDistrictModel) session.getAttribute("sdistrict"); try { respTblMstr =
	 * restClient.getForObject(env.getHrmsUrl() + "getPeriodName?Action=" +
	 * "getPeriodName", JsonResponse.class);
	 * 
	 * } catch (RestClientException e) { e.printStackTrace(); } String
	 * messageForTblMstr = respTblMstr.getMessage();
	 * 
	 * if (messageForTblMstr != null || messageForTblMstr != "") {
	 * model.addAttribute("message", messageForTblMstr); }
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * lPeriodData = mapper.convertValue(respTblMstr.getBody(), new
	 * TypeReference<List<DropDownModel>>() { });
	 * 
	 * model.addAttribute("lPeriodData", lPeriodData);
	 * logger.info("Method : viewLeavePeriod ends");
	 * 
	 * return "hrms/ViewLeavePeriod"; }
	 */

	/*
	 * 
	 * Post Mapping add-Leave-Period
	 * 
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping("/add-leave-period") public String
	 * addLeavePeriod(@ModelAttribute LeavePeriodModel leaveperiod, Model model,
	 * HttpSession session) {
	 * 
	 * logger.info("Method : addLeavePeriod starts");
	 * 
	 * // System.out.println("posted District data" + leaveperiod);
	 * 
	 * JsonResponse<Object> resp = new JsonResponse<Object>(); try {
	 * leaveperiod.setCreatedBy("user001"); resp =
	 * restClient.postForObject(env.getHrmsUrl() + "/restAddLeavePeriod" + "",
	 * leaveperiod, JsonResponse.class); } catch (RestClientException e) {
	 * e.printStackTrace(); }
	 * 
	 * if (resp.getMessage() != "" && resp.getMessage() != null) {
	 * 
	 * session.setAttribute("message", resp.getMessage());
	 * session.setAttribute("leaveperiod", leaveperiod); return
	 * "redirect:/hrms/add-leave-period"; }
	 * 
	 * logger.info("Method : addLeavePeriod ends"); return
	 * "redirect:/hrms/view-leave-period"; }
	 */

	/*
	 *
	 * View all Leave Period data through AJAX
	 *
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("/view-leave-period-through-ajax") public @ResponseBody
	 * DataTableResponse viewLperiodThroughAjax(Model model, HttpServletRequest
	 * request, @RequestParam String param1) {
	 * 
	 * logger.info("Method : viewLperiodThroughAjax starts");
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
	 * JsonResponse<List<LeavePeriodModel>> jsonResponse = new
	 * JsonResponse<List<LeavePeriodModel>>();
	 * 
	 * jsonResponse = restClient.postForObject(env.getHrmsUrl() + "getLperiodData",
	 * tableRequest, JsonResponse.class);
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * List<LeavePeriodModel> leaveperiod =
	 * mapper.convertValue(jsonResponse.getBody(), new
	 * TypeReference<List<LeavePeriodModel>>() { });
	 * 
	 * String s = "";
	 * 
	 * 
	 * for (LeavePeriodModel m : leaveperiod) { byte[] pId =
	 * Base64.getEncoder().encode(m.getlPeriodId().getBytes());
	 * 
	 * 
	 * if(m.getlPeriodStatus()) { m.setlPeriodShowActive("Active"); }else{
	 * m.setlPeriodShowActive("Inactive");
	 * 
	 * }
	 * 
	 * 
	 * s = s + "<a href='edit-leave-period?id=" + new String(pId) +
	 * "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
	 * +"<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" +
	 * m.getlPeriodId() +
	 * "\")' ><i class=\"fa fa-trash\" style=\"font-size:24px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; "
	 * + "<a data-toggle='modal' title='View'  " +
	 * "href='javascript:void' onclick='viewInModel(\"" + m.getlPeriodId() +
	 * "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
	 * m.setAction(s); s = ""; //
	 * System.out.println("data in status   "+m.getDistrictStatus()); }
	 * 
	 * 
	 * response.setRecordsTotal(jsonResponse.getTotal());
	 * response.setRecordsFiltered(jsonResponse.getTotal());
	 * response.setDraw(Integer.parseInt(draw)); response.setData(leaveperiod);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * logger.info("Method : viewLperiodThroughAjax ends"); return response; }
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
	 * res = restClient.getForObject(env.getHrmsUrl() + "getLeavePeriodById?id=" +
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
	 * restClient.getForObject(env.getHrmsUrl()+"deleteLperiodById?id="+id,
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
	 * 
	 * LeavePeriodModel leaveperiod = new LeavePeriodModel();
	 * JsonResponse<LeavePeriodModel> jsonResponse = new
	 * JsonResponse<LeavePeriodModel>();
	 * 
	 * try { ///getDistrictById jsonResponse = restClient.getForObject(
	 * env.getHrmsUrl() + "getLeavePeriodById?id=" + id +
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
