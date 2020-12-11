package nirmalya.aathithya.webmodule.property.controller;

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
import nirmalya.aathithya.webmodule.property.model.PropertyAssignPropertyToStaff;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "property")
public class PropertyAssignPropertyToStaffController {

	Logger logger = LoggerFactory.getLogger(PropertyAssignAssetToStaffController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Default 'Assign Asset To Staff' page
	 *
	 */
	@GetMapping("/assign-property-to-staff")
	public String defaultAssignPropertyToStaff(Model model, HttpSession session) {

		logger.info("Method : defaultAssignPropertyToStaff starts");

		PropertyAssignPropertyToStaff propertyAssignPropertyToStaff = new PropertyAssignPropertyToStaff();
		PropertyAssignPropertyToStaff propertyAssignPropertyToStaffSession = (PropertyAssignPropertyToStaff) session
				.getAttribute("assignTabSession");
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
				System.out.println(message);

			}

			session.setAttribute("message", "");
			if (propertyAssignPropertyToStaffSession != null) {
				model.addAttribute("propertyAssignPropertyToStaff", propertyAssignPropertyToStaffSession);
				session.setAttribute("propertyAssignPropertyToStaff", null);
			} else {
				// model.addAttribute("surveyIsEdit",null);
				model.addAttribute("propertyAssignPropertyToStaff", propertyAssignPropertyToStaff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for cost center name
		 *
		 */

		try {
			DropDownModel[] cost = restClient.getForObject(env.getPropertyUrl() + "restGetCostCenter",
					DropDownModel[].class);
			List<DropDownModel> costCenter = Arrays.asList(cost);

			model.addAttribute("costCenterList", costCenter);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : defaultAssignPropertyToStaff ends");
		return "property/AddAssignPropertyToStaff";
	}
	/*
	 * post mapping for adding new Amenity Item
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/assign-property-to-staff")
	public String assignPropertyToStaff(@ModelAttribute PropertyAssignPropertyToStaff propertyAssignPropertyToStaff,
			Model model, HttpSession session) {

		logger.info("Method : assignPropertyToStaff starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String s = "";

		for (String m : propertyAssignPropertyToStaff.getPropertyList()) {
			s = s + m + ",";
		}

		if (s != "") {
			s = s.substring(0, s.length() - 1);
			propertyAssignPropertyToStaff.setPropertyListNames(s);
			;
		}
		propertyAssignPropertyToStaff.setCreatedBy("DJ");
		try {

			resp = restClient.postForObject(env.getPropertyUrl() + "restAssignPropertyToStaff",
					propertyAssignPropertyToStaff, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("amenityItem", propertyAssignPropertyToStaff);
			return "redirect:/property/assign-property-to-staff";
		}
		logger.info("Method : assignPropertyToStaff ends");
		return "redirect:/property/view-assigned-property-to-staff";
	}

	/*
	 * drop down for user role by cost center
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-property-to-staff-getUserRole" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyType(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : get User Role List starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getUserRole?costCenter=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : get User Role List starts  ends");
		return res;
	}

	/*
	 * drop down for user list by user role
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-property-to-staff-getUserList" })
	public @ResponseBody JsonResponse<DropDownModel> getUserList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : get User Role List starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getUserList?userRole=" + index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : get User Role List starts  ends");
		return res;
	}

	/*
	 * drop down for property list by cost center
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-property-to-staff-getPropertyList" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : get property List starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		System.out.println(index);
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getPropertyList?costCenter=" + index,
					JsonResponse.class);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : get Property List starts  ends");
		return res;
	}

	/*
	 * get mapping for the view page
	 */
	@GetMapping("/view-assigned-property-to-staff")
	public String listAssignedProperty(Model model, HttpSession session) {
		logger.info("Method : listAssignedProperty starts");

		/**
		 * get DropDown value for cost center name
		 *
		 */

		try {
			DropDownModel[] cost = restClient.getForObject(env.getPropertyUrl() + "restGetCostCenter",
					DropDownModel[].class);
			List<DropDownModel> costCenter = Arrays.asList(cost);

			model.addAttribute("costCenterList", costCenter);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] cost = restClient.getForObject(env.getPropertyUrl() + "getPropertyIdNames",
					DropDownModel[].class);
			List<DropDownModel> costCenter = Arrays.asList(cost);

			model.addAttribute("PropertyList", costCenter);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : listAssignedProperty end");
		return "property/ListAssignedPropertyToStaff";
	}

	/*
	 * get mapping through ajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-assigned-property-to-staff-ThAjax")
	public @ResponseBody DataTableResponse viewAssignedPropertyThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {
		logger.info("Method : viewAssignedPropertyThroughAjax starts");

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
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			JsonResponse<List<PropertyAssignPropertyToStaff>> jsonResponse = new JsonResponse<List<PropertyAssignPropertyToStaff>>();
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllAssignedProperty", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyAssignPropertyToStaff> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyAssignPropertyToStaff>>() {
					});

			String s = "";
			for (PropertyAssignPropertyToStaff m : form) {
				byte[] csotId = Base64.getEncoder().encode(m.getCostCenterId().getBytes());
				byte[] roleId = Base64.getEncoder().encode(m.getUserRoleId().getBytes());
				byte[] userId = Base64.getEncoder().encode(m.getUserId().getBytes());
				byte[] propertyId = Base64.getEncoder().encode(m.getPropertyNameId().getBytes());

				if (m.getSatffActive())

				{
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(csotId) + "," + new String(roleId) + "," + new String(userId) + ","
						+ new String(propertyId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				if (m.getSatffActive()) {
					s = s + "&nbsp; &nbsp; <a href='javascript:void(0)'" + "' onclick='DeleteTblShift(\""
							+ new String(csotId) + ',' + new String(roleId) + ',' + new String(userId) + ','
							+ new String(propertyId) + ',' + m.getSatffActive()
							+ "\")' ><i class=\"fa fa-check-circle\" title=\"Active\"></i></a>";
				} else {
					s = s + "&nbsp; &nbsp; <a href='javascript:void(0)'" + "' onclick='DeleteTblShift(\""
							+ new String(csotId) + ',' + new String(roleId) + ',' + new String(userId) + ','
							+ new String(propertyId) + ',' + m.getSatffActive()
							+ "\")' ><i class=\"fa fa-times-circle\" title=\"InActive\"></i></a>";
				}

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewAssignedPropertyThroughAjax end");
		return response;
	}

	/*
	 * For Modal modelViewAssignmentOfSeatingPlan View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assigned-Property-modalview" })
	public @ResponseBody JsonResponse<Object> modalAssignment(Model model,
			@RequestBody PropertyAssignPropertyToStaff PropertyAssignPropertyToStaff, BindingResult result) {

		logger.info("Method : modal modelViewAssignmentOfproperty to staff starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		String encodeId = PropertyAssignPropertyToStaff.getCostCenter();
		String encodeSp = PropertyAssignPropertyToStaff.getUserRole();
		String encodeut = PropertyAssignPropertyToStaff.getUser();
		String encodePi = PropertyAssignPropertyToStaff.getPropertyNameId();
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		byte[] decodeSp = Base64.getDecoder().decode(encodeSp.getBytes());
		byte[] decodeUt = Base64.getDecoder().decode(encodeut.getBytes());
		byte[] decodePi = Base64.getDecoder().decode(encodePi.getBytes());

		String id = (new String(decodeId));
		String sp = (new String(decodeSp));
		String ut = (new String(decodeUt));
		String pi = (new String(decodePi));

		try {
			response = restClient.getForObject(env.getPropertyUrl() + "getAssignedPropertyModal?id=" + id + "&ur=" + sp
					+ "&ui=" + ut + "&pi=" + pi, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modal Amenity Item ends ");
		return response;
	}

	/*
	 * for change status
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "/view-assigned-property-to-staff-change-status" })
	public @ResponseBody JsonResponse<Object> changedStatus(Model model,
			@RequestBody PropertyAssignPropertyToStaff PropertyAssignPropertyToStaff, BindingResult result) {

		logger.info("Method : changedStatus starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(PropertyAssignPropertyToStaff.getCostCenter().getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(PropertyAssignPropertyToStaff.getUserRole().getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(PropertyAssignPropertyToStaff.getUser().getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(PropertyAssignPropertyToStaff.getPropertyNameId().getBytes());
		Boolean status = PropertyAssignPropertyToStaff.getSatffActive();
		String id = (new String(encodeByte1));
		String ur = (new String(encodeByte2));
		String userid = (new String(encodeByte3));
		String propertyId = (new String(encodeByte4));
		String createdBy = "Dj";

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(
					env.getPropertyUrl() + "changePropertyStatusById?id=" + id + "&ur=" + ur + "&userid=" + userid
							+ "&propertyId=" + propertyId + "&status=" + status + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			System.out.println("if block getmsg() not false : " + resp.getMessage());
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : changedStatus ends");
		return resp;
	}

	/*
	 * get mapping for the report pages
	 */
	@GetMapping("/view-assigned-property-to-staff-generate-report")
	public String generateAssignedPropertyToStaffReport(Model model, HttpSession session) {

		logger.info("Method : generateAssignedPropertyToStaffReport starts");

		/**
		 * get DropDown value for cost center name
		 *
		 */

		try {
			DropDownModel[] cost = restClient.getForObject(env.getPropertyUrl() + "restGetCostCenter",
					DropDownModel[].class);
			List<DropDownModel> costCenter = Arrays.asList(cost);

			model.addAttribute("costCenterList", costCenter);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * drop down for property name
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getPropertyUrl() + "getPropertyIdNames",
					DropDownModel[].class);
			List<DropDownModel> costCenter = Arrays.asList(cost);

			model.addAttribute("PropertyList", costCenter);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateAssignedPropertyToStaffReport ends");
		return "property/reports-assign-property-to-staff";
	}

}
