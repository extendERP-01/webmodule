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
import nirmalya.aathithya.webmodule.common.utils.DateFormatter;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.property.model.AssignAssetToStaffModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "property")
public class PropertyAssignAssetToStaffController {

	Logger logger = LoggerFactory.getLogger(PropertyAssignAssetToStaffController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Default 'Assign Asset To Staff' page
	 *
	 */
	@GetMapping("/add-assign-asset-to-staff")
	public String defaultAssignTabToStaff(Model model, HttpSession session) {

		logger.info("Method : defaultAssignTabToStaff starts");

		AssignAssetToStaffModel assignTabToStaff = new AssignAssetToStaffModel();
		AssignAssetToStaffModel assignTabToStaffSession = (AssignAssetToStaffModel) session
				.getAttribute("assignTabSession");
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (assignTabToStaffSession != null) {
				model.addAttribute("assignTabToStaff", assignTabToStaffSession);
				session.setAttribute("assignTabSession", null);
			} else {
				// model.addAttribute("surveyIsEdit",null);
				model.addAttribute("assignTabToStaff", assignTabToStaff);
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

			model.addAttribute("costCenter", costCenter);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

	
		logger.info("Method : defaultAssignTabToStaff ends");
		return "property/assign-asset-to-staff";
	}

 


	/*
	 * for Asset list all
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-staff-getAssetListAll" })
	public @ResponseBody JsonResponse<DropDownModel> getAssetListAll(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAssetList for fixed asset starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {

			res = restClient.getForObject(env.getPropertyUrl() + "getAssetListAsignStaff?item=" + index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getAssetList  for fixed assets ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-assign-asset-to-staff-get-user" })
	public @ResponseBody JsonResponse<DropDownModel> getUser(Model model, @RequestBody String costcenter,
			BindingResult result) {
		logger.info("Method : getUser starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "restGetUserList?id=" + costcenter,
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
		logger.info("Method : getUser ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-assign-asset-to-staff-save-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> assignAssetToStaff(
			@RequestBody List<AssignAssetToStaffModel> assignAssetToStaff, Model model, HttpSession session) {

		logger.info("Method : assignAssetToStaff function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		for (AssignAssetToStaffModel a : assignAssetToStaff) {
			a.setCreatedBy("Dj");
		}
		try {
			res = restClient.postForObject(env.getPropertyUrl() + "assignAssetToStaff", assignAssetToStaff,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : assignAssetToStaff function Ends");
		return res;
	}

	/**
	 * Default 'View Assigned Table Staff' page
	 *
	 */
	@GetMapping("/view-assigned-asset-to-staff")
	public String listAssignedAssetStaff(Model model, HttpSession session) {

		logger.info("Method : listAssignedAssetStaff starts");

		/**
		 * get DropDown value for cost center name
		 *
		 */

		try {
			DropDownModel[] cost = restClient.getForObject(env.getPropertyUrl() + "restGetCostCenter",
					DropDownModel[].class);
			List<DropDownModel> costCenter = Arrays.asList(cost);

			model.addAttribute("costCenter", costCenter);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for staff name
		 *
		 */

		try {
			DropDownModel[] staff = restClient.getForObject(env.getPropertyUrl() + "restGetStaffList",
					DropDownModel[].class);
			List<DropDownModel> staffList = Arrays.asList(staff);

			model.addAttribute("staffList", staffList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : listAssignedAssetStaff ends");
		return "property/view-assigned-asset-to-staff";
	}

	/**
	 * Web Controller for view all assigned asset to staff
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-assigned-asset-to-staff-throughAjax")
	public @ResponseBody DataTableResponse viewAllAssignedAssetToStaffThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2,
			@RequestParam String param3) {

		logger.info("Method : viewAllAssignedAssetToStaffThroughAjax starts");

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

			JsonResponse<List<AssignAssetToStaffModel>> jsonResponse = new JsonResponse<List<AssignAssetToStaffModel>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllAssignedAssetStaff", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AssignAssetToStaffModel> assignTS = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssignAssetToStaffModel>>() {
					});

			String s = "";

			for (AssignAssetToStaffModel m : assignTS) {

				byte[] encodeId = Base64.getEncoder().encode(m.getCostId().getBytes());
				byte[] encodeSid = Base64.getEncoder().encode(m.getStaffId().getBytes());
				byte[] encodeIid = Base64.getEncoder().encode(m.getItemId().getBytes());
				byte[] encodeAid = Base64.getEncoder().encode(m.getAssetId().getBytes());
				byte[] encodeDid = Base64.getEncoder()
						.encode(DateFormatter.getStringDate(m.getAssignDate()).getBytes());

				if (m.getActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
				if (m.getActive()) {
					s = s + "<a href='javascript:void(0)'" + "' onclick='changeStatus(\"" + new String(encodeId) + ','
							+ new String(encodeSid) + ',' 
							+ new String(encodeIid) + ',' + new String(encodeAid) + ',' + new String(encodeDid) + ','
							+ m.getActive()
							+ "\")' ><i class=\"fa fa-check-circle\" title=\"Active\" style=\"font-size:24px;color:#090\"></i></a>";

					m.setAction(s);
					s = "";
				} else {
					s = s + "<a href='javascript:void(0)'" + "' onclick='changeStatus(\"" + new String(encodeId) + ','
							+ new String(encodeSid) + ','
							+ new String(encodeIid) + ',' + new String(encodeAid) + ',' + new String(encodeDid) + ','
							+ m.getActive()
							+ "\")' ><i class=\"fa fa-times-circle\" title=\"Inactive\" style=\"font-size:24px;color:#e30f0f\"></i></a>";

					m.setAction(s);
					s = "";
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignTS);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewAllAssignedAssetToStaffThroughAjax ends");
		return response;
	}

	/**
	 * Web Controller for change status
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-assigned-asset-to-staff-change-status")
	public @ResponseBody JsonResponse<Object> changedAssetStatus(Model model, @RequestBody AssignAssetToStaffModel asignAssetToStaffModel, HttpSession session) {

		logger.info("Method : changedAssetStatus starts");
		
		asignAssetToStaffModel.setCreatedBy("DJ");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.postForObject(env.getPropertyUrl() + "changeAssetStatusById" ,asignAssetToStaffModel, JsonResponse.class);

		} catch (RestClientException e) {
		
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			System.out.println("if block getmsg() not false : " + resp.getMessage());
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : changedAssetStatus ends");
		return resp;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-staff-get-item" })
	public @ResponseBody JsonResponse<AssignAssetToStaffModel> getItemAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<AssignAssetToStaffModel> res = new JsonResponse<AssignAssetToStaffModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getItemListByAutoSearch?id=" + searchValue,
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
System.out.println(res);
		logger.info("Method : getItemAutoSearchList ends");
		return res;
	}  

}
