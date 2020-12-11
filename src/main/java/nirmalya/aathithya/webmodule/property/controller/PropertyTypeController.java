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
import nirmalya.aathithya.webmodule.property.model.PropertyTypeForm;

@Controller
@RequestMapping("property")
public class PropertyTypeController {

	Logger logger = LoggerFactory.getLogger(PropertyTypeController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/add-propertytype")
	public String addPropertyType(Model model, HttpSession session) {
		logger.info("Method : add_Property_Type starts");
		session.setAttribute("title", "Add Property Type");
		PropertyTypeForm add_PropetyType = new PropertyTypeForm();

		PropertyTypeForm edit_PropertyType = (PropertyTypeForm) session.getAttribute("edit_Property");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (edit_PropertyType != null) {
			model.addAttribute("addPropetyType", edit_PropertyType);
		} else {
			model.addAttribute("addPropetyType", add_PropetyType);
		}
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : add_Property_Type endss");
		return "property/AddPropertyType";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-propertytype-getAmenityTypeList" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyTypeList(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : getAmenityTypeList starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getAmenityName01?proCat=" + index, JsonResponse.class);
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
		
		logger.info("Method : getAmenityTypeList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-propertytype")
	public String submitPropertyType(@ModelAttribute PropertyTypeForm propertyTypeForm, Model model, HttpSession session) {
		logger.info("Method : submitPropertyType starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			String s = "";
			
			for(String m : propertyTypeForm.getAmenitiesList()) {
				s= s + m + ",";
			}
			
			if(s !="") {
				s = s.substring(0, s.length()-1);
				propertyTypeForm.setAmenities(s);
			}
			propertyTypeForm.setCreatedBy("DJ");
			resp = restClient.postForObject(env.getPropertyUrl() + "restAddPropertyType", propertyTypeForm,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("edit_Property", propertyTypeForm);
			return "redirect:/property/add-propertytype";
		}
		session.setAttribute("edit_Property", null);
		
		logger.info("Method : edit_Property ends");
		return "redirect:/property/view-propertytype";
	}

	
	@GetMapping("/view-propertytype")
	public String listPropertyType(Model model, HttpSession session) {
		logger.info("Method : list_Property_Type starts");
		session.setAttribute("title", "view Property Type");
		logger.info("Method : list_Property_Type ends");
		return "property/ListPropertyType";
	}
	
	// ajax call for show list in view page
	@SuppressWarnings("unchecked")
	@GetMapping("/view-propertytype-throughajax")
	public @ResponseBody DataTableResponse viewPropertyTypeAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewPropertyTypeAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);


			JsonResponse<List<PropertyTypeForm>> jsonResponse = new JsonResponse<List<PropertyTypeForm>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllPropertyType", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyTypeForm> property = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyTypeForm>>() {
					});

			String s = "";
			String encoded_value = "";
			for (PropertyTypeForm m : property) {
				byte[] propertyId = Base64.getEncoder().encode(m.getPropertyType().getBytes());

				encoded_value = new String(propertyId);
				s = "";
				s = s + "<a href='editPropertyType?id=" + encoded_value
						+ "' ><i class='fa fa-edit edit'></i></a>";
			s = s + "<a data-toggle='modal' title='View'  href='javascript:void(0)' onclick='deletePropertyType(\""
						+ encoded_value + "\")'><i class='fa fa-trash trash'></i></a>";
				
				s = s + "<a data-toggle='modal' title='View'  href='javascript:void(0)' onclick='viewInModel(\""
						+ encoded_value + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

				if (m.getpTypeActive()) {
					m.setActivityStatus("Active");
				} else {
					m.setActivityStatus("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(property);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewPropertyTypeAjax ends");
		return response;
	}

	

	// for modal case
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-propertytype-modal" })
	public @ResponseBody JsonResponse<Object> modalAmenityManagement(Model model, @RequestBody String index,
			BindingResult result) {
		
		logger.info("Method : modalPropertyType starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());

		String propertyId = (new String(encodeByte));

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getPropertyTypeById?id=" + propertyId, JsonResponse.class);
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
		
		logger.info("Method : modalPropertyType ends");
		return res;
	}

	// for edit case
	@SuppressWarnings("unchecked")
	@GetMapping("/editPropertyType")
	public String editPropertyType(Model model, @RequestParam String id, HttpSession session) {
		logger.info("Method : editPropertyType starts");
		session.setAttribute("title", "Edit Property Type");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());

		String propertyId = (new String(encodeByte));
		
		PropertyTypeForm addPropertyType = new PropertyTypeForm();
		JsonResponse<PropertyTypeForm> res = new JsonResponse<PropertyTypeForm>();
        
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getPropertyTypeByIdEdit?id=" + propertyId,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			addPropertyType = mapper.convertValue(res.getBody(), PropertyTypeForm.class);
			
			model.addAttribute("addPropetyType" ,addPropertyType);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		addPropertyType.setAmenitiesList(Arrays.asList(addPropertyType.getAmenities().split("\\s*,\\s*")));
		System.out.println("addPropertyType >>>>>" +addPropertyType);
		
		
		session.setAttribute("message", "");


		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);

			
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		try {
			DropDownModel[] amenityName = restClient.getForObject(env.getPropertyUrl() + "getAmenityNameEdit?proCat=" + addPropertyType.getPropertyCategory(),
					DropDownModel[].class);
			List<DropDownModel> amenityNameList = Arrays.asList(amenityName);

			res = restClient.getForObject(env.getPropertyUrl() + "getAmenityName01?proCat=" + addPropertyType.getPropertyCategory(), JsonResponse.class);
			model.addAttribute("amenityNameList", amenityNameList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : editPropertyType ends");
		return "property/AddPropertyType";
	}
	
	//delete Property Type
	@SuppressWarnings("unchecked")
	@GetMapping("/view-propertytype-deletPropertyType")
	public @ResponseBody JsonResponse<Object> deletePropertyType(Model model, @RequestParam String id, HttpSession session) {
		
		logger.info("Method : deletePropertyType starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());

		String propertyId = (new String(encodeByte));
		String createdBy = "Dj";
		try {
			resp = restClient.getForObject(env.getPropertyUrl() + "deletePropertyById?id=" + propertyId+"&createdBy="+createdBy, JsonResponse.class);

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
		logger.info("Method : deletePropertyType ends");
		//return "property/ListPropertyType";
		return resp;
	}
	

}
