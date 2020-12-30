/**
 * web Controller for property master form
 */
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
import nirmalya.aathithya.webmodule.property.model.PropertyMasterModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "property")
public class PropertyMasterController {
	Logger logger = LoggerFactory.getLogger(PropertyMasterController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * GEt Mapping for Add property form view page
	 * 
	 */
	@GetMapping("/add-property-master")
	public String addPropertyMaster(Model model, HttpSession session) {
		logger.info("Method : addPropertyMaster starts");
		PropertyMasterModel propertyMaster = new PropertyMasterModel();

		PropertyMasterModel form = (PropertyMasterModel) session.getAttribute("spropertyMaster");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("propertyMaster", form);
			session.setAttribute("spropertyMaster", null);
		} else {
			model.addAttribute("propertyMaster", propertyMaster);
		}
		/*
		 * dropdown data for property category
		 */
		try {
			DropDownModel[] propertyCategory = restClient.getForObject(env.getPropertyUrl() + "getCategoryName",
					DropDownModel[].class);

			List<DropDownModel> categoryList = Arrays.asList(propertyCategory);
			model.addAttribute("categoryList", categoryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * property floor
		 */

		try {
			DropDownModel[] propertyFloor = restClient.getForObject(env.getPropertyUrl() + "getFloorName",
					DropDownModel[].class);

			List<DropDownModel> floorList = Arrays.asList(propertyFloor);
			model.addAttribute("floorList", floorList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addPropertyMaster end");
		return "property/addPropertyForm";
	}
	
	/*
	 * post Mapping for submit data
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-property-master")
	public String postPropertyMaster(@ModelAttribute PropertyMasterModel propertyMaster, Model model,
			HttpSession session) {
		logger.info("Method : postPropertyMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			propertyMaster.setPropertyCreatedBy("u0001");
			resp = restClient.postForObject(env.getPropertyUrl() + "addPropertyMaster", propertyMaster,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("spropertyMaster", propertyMaster);
			return "redirect:/property/add-property-master";

		}
		session.setAttribute("spropertyMaster", null);
		logger.info("Method : postPropertyMaster end");
		return "redirect:/property/view-property-master";
	}

	/**
	 * GetMapping for view property master
	 */
	@GetMapping("/view-property-master")
	public String listPropertyMaster(Model model, HttpSession session) {
		logger.info("Method : listPropertyMaster starts");
		/*
		 * dropdown data for property category
		 */
		try {
			DropDownModel[] propertyCategory = restClient.getForObject(env.getPropertyUrl() + "getCategoryName",
					DropDownModel[].class);

			List<DropDownModel> categoryList = Arrays.asList(propertyCategory);
			model.addAttribute("categoryList", categoryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] propertyTypeSearch = restClient.getForObject(env.getPropertyUrl() + "getPropTypSearchName",
					DropDownModel[].class);

			List<DropDownModel> typeSearchList = Arrays.asList(propertyTypeSearch);
			model.addAttribute("typeSearchList", typeSearchList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] property = restClient.getForObject(env.getPropertyUrl() + "getPropName",
					DropDownModel[].class);

			List<DropDownModel> nameList = Arrays.asList(property);
			model.addAttribute("nameList", nameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : listPropertyMaster end");
		return "property/listPropertyForm";
	}

	/**
	 * GetMapping for view property master throughajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-master-throughajax")
	public @ResponseBody DataTableResponse viewPropertyMasterThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2,@RequestParam String param3,
			@RequestParam String param4,@RequestParam String param5,@RequestParam String param6) {
		logger.info("Method : viewPropertyMasterThroughAjax starts");

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
			tableRequest.setParam5(param5);
			tableRequest.setParam6(param6);

			JsonResponse<List<PropertyMasterModel>> jsonResponse = new JsonResponse<List<PropertyMasterModel>>();
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllPropertyMaster", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyMasterModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyMasterModel>>() {
					});

			String s = "";
			for (PropertyMasterModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getPropertyId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-property-master?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;&nbsp; <a href='javascript:void(0)'\"+ \"' onclick='deleteMaster(  \""
						+ new String(pId) + "\")' ><i class=\"fa fa-trash\"></i></a> ";

				m.setAction(s);
				s = "";

				if (m.getCleanStatus()) {
					m.setCleanStatusName("Clean");
				} else {
					m.setCleanStatusName("Dirty");

				}

				if (m.getPropertyActive()) {
					m.setPropertyStatus("Active");
				} else {
					m.setPropertyStatus("Inactive");
				}
				
				if (m.getpBookingStatus()) {
					m.setBookingStatusName("Active");
				} else {
					m.setBookingStatusName("Inactive");
				}
				if (m.getReservationStatus()) {
					m.setReservationStatusName("Active");
				} else {
					m.setReservationStatusName("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewPropertyMasterThroughAjax end");
		return response;
	}

	/*
	 * GetMapping for delete property master
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-master-delete-property-master")
	public @ResponseBody JsonResponse<Object> deletePropertyMaster(@RequestParam String id, HttpSession session) {
		logger.info("Method : deletePropertyMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));

		try {String createdBy="u0002";
			resp = restClient.getForObject(env.getPropertyUrl() + "deletePropertyMasterById?id=" + id1+ "&createdBy="+createdBy,
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
		logger.info("Method : deletePropertyMaster ends");

		return resp;
	}
	/*
	 * GEt Mapping for edit property master
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/edit-property-master")
	public String editPropertyMaster(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editPropertyMaster starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		PropertyMasterModel propertyMaster = new PropertyMasterModel();
		JsonResponse<PropertyMasterModel> jsonResponse = new JsonResponse<PropertyMasterModel>();

		try {
			jsonResponse = restClient.getForObject(
					env.getPropertyUrl() + "getPropertyMasterById?id=" + id + "&Action=editPropertyMaster",
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

		propertyMaster = mapper.convertValue(jsonResponse.getBody(), PropertyMasterModel.class);

		session.setAttribute("message", "");

		model.addAttribute("propertyMaster", propertyMaster);
		/*
		 * dropdown data for property category
		 */
		try {
			DropDownModel[] propertyCategory = restClient.getForObject(env.getPropertyUrl() + "getCategoryName",
					DropDownModel[].class);

			List<DropDownModel> categoryList = Arrays.asList(propertyCategory);
			model.addAttribute("categoryList", categoryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] propertyType = restClient.getForObject(
					env.getPropertyUrl() + "getTypeNameEdit?proCat=" + propertyMaster.getPropertyCategory(),
					DropDownModel[].class);
			List<DropDownModel> typeList = Arrays.asList(propertyType);

			model.addAttribute("typeList", typeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * property floor
		 */

		try {
			DropDownModel[] propertyFloor = restClient.getForObject(env.getPropertyUrl() + "getFloorName",
					DropDownModel[].class);

			List<DropDownModel> floorList = Arrays.asList(propertyFloor);
			model.addAttribute("floorList", floorList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : editPropertyMaster end");
		return "property/addPropertyForm";
	}
	/*
	 * post Mapping for viewInModelData in Propety Master
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-property-master-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		

		try {
			res = restClient.getForObject(
					env.getPropertyUrl() + "getPropertyMasterById?id=" + id + "&Action=" + "ModelViewMaster",
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

		logger.info("Method : modelView end");
		return res;
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-property-master-getPropertyTypList" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyTypeList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getProperty TypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getPropertyTypName?proCat=" + index,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			System.out.println("if block getmsg() not false : " + res.getMessage());
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getProperty TypeList ends");
		return res;
	}

	/**
	 * for drop down property Name list
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-property-master-getPropertNameList" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyNameList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getPropertyNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getPropertyNameList?proTyp=" + index,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			System.out.println("if block getmsg() not false : " + res.getMessage());
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getPropertyNameList ends");
		return res;
	}

}
