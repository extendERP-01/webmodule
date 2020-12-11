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
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsReimbursementTypeModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "reimbursement")
public class HrmsReimbursementTypeController {

	Logger logger = LoggerFactory.getLogger(HrmsReimbursementTypeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add reimbursement type view page
	 */
	@GetMapping("/add-reimbursement-type-master")
	public String addRreimbursementType(Model model, HttpSession session) {

		logger.info("Method : addRreimbursementType starts");

		HrmsReimbursementTypeModel reimbursementType = new HrmsReimbursementTypeModel();
		HrmsReimbursementTypeModel sessionreimbursementType = (HrmsReimbursementTypeModel) session
				.getAttribute("sessionreimbursementType");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionreimbursementType != null) {
			model.addAttribute("reimbursementType", sessionreimbursementType);
			session.setAttribute("sessionreimbursementType", null);
		} else {
			model.addAttribute("reimbursementType", reimbursementType);
		}

		logger.info("Method : addRreimbursementType ends");

		return "reimbursement/add-reimbursement-type";
	}

	/*
	 * Post Mapping for adding new reimbursement type
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-reimbursement-type-master")
	public String addRreimbursementTypePost(@ModelAttribute HrmsReimbursementTypeModel reimbursementType, Model model,
			HttpSession session) {

		logger.info("Method : addRreimbursementType Post starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		reimbursementType.setCreatedBy(userId);
		reimbursementType.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getReimbursementUrl() + "restAddreimbursementTypes", reimbursementType,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionreimbursementType", reimbursementType);
			return "redirect:/reimbursement/add-reimbursement-type-master";
		}
		logger.info("Method : addRreimbursementType Post ends");

		return "redirect:/reimbursement/view-reimbursement-type-master";
	}

	/*
	 * Get Mapping view reimbursement type master
	 */
	@GetMapping("/view-reimbursement-type-master")
	public String viewReimbursementType(Model model, HttpSession session) {

		logger.info("Method : viewReimbursementType starts");

		logger.info("Method : viewReimbursementType ends");

		return "reimbursement/view-reimbursement-type";
	}

	/*
	 * For view reimbursement type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-reimbursement-type-master-ThroughAjax")
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

			JsonResponse<List<HrmsReimbursementTypeModel>> jsonResponse = new JsonResponse<List<HrmsReimbursementTypeModel>>();

			jsonResponse = restClient.postForObject(env.getReimbursementUrl() + "getreimbursementTypeDetails",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsReimbursementTypeModel> reimbursementType = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsReimbursementTypeModel>>() {
					});

			String s = "";

			for (HrmsReimbursementTypeModel m : reimbursementType) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getReimbursementTypeId().getBytes());
				s = s + "<a href='view-reimbursement-type-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletereimbursementType(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
				m.setAction(s);
				s = "";
				if (m.getReimbursementTypeStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reimbursementType);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewdepartmentMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit reimbursement type
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-reimbursement-type-master-edit")
	public String editReimbursement(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editReimbursement starts");

		HrmsReimbursementTypeModel reimbursementType = new HrmsReimbursementTypeModel();
		JsonResponse<HrmsReimbursementTypeModel> jsonResponse = new JsonResponse<HrmsReimbursementTypeModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getReimbursementUrl() + "getreimbursementTypeById?id=" + id,
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

		reimbursementType = mapper.convertValue(jsonResponse.getBody(), HrmsReimbursementTypeModel.class);
		session.setAttribute("message", "");

		model.addAttribute("reimbursementType", reimbursementType);

		logger.info("Method : edit editReimbursement ends");

		return "reimbursement/add-reimbursement-type";
	}

	/*
	 * For Delete reimbursement type
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-reimbursement-type-master-delete")
	public @ResponseBody JsonResponse<Object> deleteReimbursement(@RequestParam String id, HttpSession session) {

		logger.info("Method : deleteReimbursement ends");

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
					env.getReimbursementUrl() + "deletereimbursementTypeById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteReimbursement  ends");

		return resp;
	}

	/*
	 * For Modal reimbursement type View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-reimbursement-type-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalReimbursement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalReimbursement starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getreimbursementTypeById?id=" + id,
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
		logger.info("Method :modalReimbursement ends");
		return res;
	}
}
