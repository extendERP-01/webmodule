package nirmalya.aathithya.webmodule.reimbursement.controller;

import java.util.Arrays;
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
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsPolicyMasterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "reimbursement")
public class HrmsPolicyMasterController {

	Logger logger = LoggerFactory.getLogger(HrmsPolicyMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add policy type view page
	 */
	@GetMapping("/add-policy-type-master")
	public String addRpolicyType(Model model, HttpSession session) {

		logger.info("Method : addRpolicyType starts");

		HrmsPolicyMasterModel policyType = new HrmsPolicyMasterModel();
		HrmsPolicyMasterModel sessionpolicyType = (HrmsPolicyMasterModel) session.getAttribute("sessionpolicyType");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionpolicyType != null) {
			model.addAttribute("policyType", sessionpolicyType);
			session.setAttribute("sessionpolicyType", null);
		} else {
			model.addAttribute("policyType", policyType);
		}

		/*
		 * for viewing drop down list of reimbursement type
		 */
		try {
			DropDownModel[] reimType = restClient.getForObject(env.getReimbursementUrl() + "getReimbTypeListForPolicy",
					DropDownModel[].class);
			List<DropDownModel> reimbTypeList = Arrays.asList(reimType);
			model.addAttribute("reimbTypeList", reimbTypeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list of reimbursement type
		 */
		try {
			DropDownModel[] reimType = restClient.getForObject(env.getReimbursementUrl() + "getTimePeriod",
					DropDownModel[].class);
			List<DropDownModel> timePeriodList = Arrays.asList(reimType);
			model.addAttribute("timePeriodList", timePeriodList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list of user Role
		 */
		try {
			DropDownModel[] userRole = restClient.getForObject(env.getReimbursementUrl() + "getUserRole",
					DropDownModel[].class);
			List<DropDownModel> userRoleList = Arrays.asList(userRole);
			model.addAttribute("userRoleList", userRoleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addRpolicyType ends");

		return "reimbursement/add-policy-master";
	}

	/*
	 * Post Mapping for adding new policy type
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-policy-type-master")
	public String addRpolicyTypePost(@ModelAttribute HrmsPolicyMasterModel policyType, Model model,
			HttpSession session) {

		logger.info("Method : addRpolicyType Post starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		policyType.setCreatedBy(userId);
		policyType.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getReimbursementUrl() + "restAddpolicyTypes", policyType,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionpolicyType", policyType);
			return "redirect:/reimbursement/add-policy-type-master";
		}
		logger.info("Method : addRpolicyType Post ends");

		return "redirect:/reimbursement/view-policy-type-master";
	}

	/*
	 * Get Mapping view policy type master
	 */
	@GetMapping("/view-policy-type-master")
	public String viewpolicyType(Model model, HttpSession session) {

		logger.info("Method : viewpolicyType starts");

		logger.info("Method : viewpolicyType ends");

		return "reimbursement/view-policy-master";
	}

	/*
	 * For view policy type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-policy-type-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewdepartmentMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewdepartmentMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsPolicyMasterModel>> jsonResponse = new JsonResponse<List<HrmsPolicyMasterModel>>();

			jsonResponse = restClient.postForObject(env.getReimbursementUrl() + "getpolicyTypeDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsPolicyMasterModel> policyType = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsPolicyMasterModel>>() {
					});

			String s = "";

			for (HrmsPolicyMasterModel m : policyType) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getPolicyId().getBytes());
				s = s + "<a href='view-policy-type-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletepolicyType(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\" ></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:20px\" ></i></a>";
				m.setAction(s);
				s = "";
				if (m.getStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(policyType);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewdepartmentMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit policy type
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-policy-type-master-edit")
	public String editpolicy(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editpolicy starts");

		HrmsPolicyMasterModel policyType = new HrmsPolicyMasterModel();
		JsonResponse<HrmsPolicyMasterModel> jsonResponse = new JsonResponse<HrmsPolicyMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getReimbursementUrl() + "getpolicyTypeById?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		/*
		 * for viewing drop down list of reimbursement type
		 */
		try {
			DropDownModel[] reimType = restClient.getForObject(env.getReimbursementUrl() + "getReimbTypeListForPolicy",
					DropDownModel[].class);
			List<DropDownModel> reimbTypeList = Arrays.asList(reimType);
			model.addAttribute("reimbTypeList", reimbTypeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list of reimbursement type
		 */
		try {
			DropDownModel[] reimType = restClient.getForObject(env.getReimbursementUrl() + "getTimePeriod",
					DropDownModel[].class);
			List<DropDownModel> timePeriodList = Arrays.asList(reimType);
			model.addAttribute("timePeriodList", timePeriodList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list of user Role
		 */
		try {
			DropDownModel[] userRole = restClient.getForObject(env.getReimbursementUrl() + "getUserRole",
					DropDownModel[].class);
			List<DropDownModel> userRoleList = Arrays.asList(userRole);
			model.addAttribute("userRoleList", userRoleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		policyType = mapper.convertValue(jsonResponse.getBody(), HrmsPolicyMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("policyType", policyType);

		logger.info("Method : edit editpolicy ends");

		return "reimbursement/add-policy-master";
	}

	/*
	 * For Delete policy type
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-policy-type-master-delete")
	public @ResponseBody JsonResponse<Object> deletepolicy(@RequestParam String id, HttpSession session) {

		logger.info("Method : deletepolicy ends");

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
					env.getReimbursementUrl() + "deletepolicyTypeById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deletepolicy  ends");

		return resp;
	}

	/*
	 * For Modal policy type View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-policy-type-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalpolicy(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalpolicy starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getpolicyTypeById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalpolicy ends");
		return res;
	}
}
