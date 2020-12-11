package nirmalya.aathithya.webmodule.employee.controller;

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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeLeaveUpdateModel;

@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeLeaveController {

	Logger logger = LoggerFactory.getLogger(HrmsEmployeeLeaveController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee Leave
	 */
	@GetMapping("/add-employee-leave")
	public String addEmploreeLeave(Model model, HttpSession session) {

		logger.info("Method : addEmploreeLeave starts");

		logger.info("Method : addEmploreeLeave ends");
		return "employee/add-employee-leave";
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-employee-leave-get-emp" })
	public @ResponseBody JsonResponse<List<DropDownModel>> getEmpIdAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getEmpIdAutoSearchList starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmpIdAutoSearchList?id=" + searchValue,
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

		logger.info("Method : getEmpIdAutoSearchList ends");
		return res;
	}

	/*
	 * For Modal Camp View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-employee-dtls-through-ajax" })
	public @ResponseBody JsonResponse<Object> empDtls(Model model, @RequestBody String id, BindingResult result) {

		logger.info("Method : empDtls starts");
		// System.out.println("ID-========" + id);
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String empid = (new String(decodeId));
		// System.out.println("empid" + empid);

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeDtls?id=" + empid, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :empDtls ends");
		// System.out.println("@@@@@@@@@@@@@@@" + res);
		return res;
	}

	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-employee-leave-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> updateEmployeeLeave(
			@RequestBody List<HrmsEmployeeLeaveUpdateModel> hrmsEmployeeLeaveModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : updateEmployeeLeave function starts");

		System.out.println("@@@@" + hrmsEmployeeLeaveModel);

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (HrmsEmployeeLeaveUpdateModel r : hrmsEmployeeLeaveModel) {

				r.setCreatedBy(userId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "restUpdateEmployeeLeave", hrmsEmployeeLeaveModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : updateEmployeeLeave function Ends");
		return res;
	}

	/*
	 * Get Mapping for add employee Leave
	 */
	@GetMapping("/view-employee-leave")
	public String viewEmploreeLeave(Model model, HttpSession session) {

		logger.info("Method : viewEmploreeLeave starts");

		logger.info("Method : viewEmploreeLeave ends");
		return "employee/view-employee-leave";
	}

	/*
	 * For view employee Language for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-leave-ThroughAjax")
	public @ResponseBody DataTableResponse viewEmployeLeave(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewEmployeLeave statrs");

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

			JsonResponse<List<HrmsEmployeeLeaveUpdateModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeLeaveUpdateModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAssignleaveDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeLeaveUpdateModel> assignLeave = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeLeaveUpdateModel>>() {
					});
			String s = "";

			for (HrmsEmployeeLeaveUpdateModel m : assignLeave) {
				s = "";

				byte[] pId = Base64.getEncoder().encode(m.getEmpId().getBytes());
				byte[] lId = Base64.getEncoder().encode(m.getDate().getBytes());

				s = s + "<a data-toggle='modal' title='Edit'  href='javascript:void' onclick='editLeave(\""
						+ new String(pId) + ',' + new String(lId)
						+ "\")'><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a data-toggle='modal' title='Delete'  href='javascript:void' onclick='deleteLeave(\""
						+ new String(pId) + ',' + new String(lId) + "\")'><i class='fa fa-trash'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignLeave);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewEmployeLeave ends");
		System.out.println("@@@@@" + response);

		return response;
	}

	/*
	 * For Delete Leave
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-leave-delete" })
	public @ResponseBody JsonResponse<Object> deleteLeave(Model model, @RequestBody HrmsEmployeeLeaveUpdateModel leaveModel,
			BindingResult result, HttpSession session) {

		logger.info("Method : deleteLeave ends");

		byte[] encodeByte = Base64.getDecoder().decode(leaveModel.getEmpId().getBytes());
		String index = (new String(encodeByte));
		System.out.println("@@@" + index);
		byte[] encodeByte1 = Base64.getDecoder().decode(leaveModel.getDate().getBytes());
		String lid = (new String(encodeByte1));
		System.out.println("@3333@@" + lid);

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp = restClient.getForObject(
					env.getEmployeeUrl() + "deleteLeaveById?id=" + index + "&lid=" + lid + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteLeave  ends");

		return resp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-employee-leave-date", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> updateDate(@RequestBody HrmsEmployeeLeaveUpdateModel hrmsEmployeeLeaveModel,
			Model model, HttpSession session) {
		logger.info("Method : updateDate function starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(hrmsEmployeeLeaveModel.getEmpId().getBytes());
		String id = (new String(encodeByte));
		hrmsEmployeeLeaveModel.setEmpId(id);

		byte[] encodeByte1 = Base64.getDecoder().decode(hrmsEmployeeLeaveModel.getPrevDate().getBytes());
		String index = (new String(encodeByte1));
		hrmsEmployeeLeaveModel.setPrevDate(index);
		System.out.println("$$$$$"+hrmsEmployeeLeaveModel);

		JsonResponse<Object> res = new JsonResponse<Object>();
		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			hrmsEmployeeLeaveModel.setCreatedBy(createdBy);
			res = restClient.postForObject(env.getEmployeeUrl() + "rest-update-date", hrmsEmployeeLeaveModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		System.out.println(message);

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : updateDate function Ends");
		System.out.println(res);
		return res;
	}

}
