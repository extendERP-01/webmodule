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
import nirmalya.aathithya.webmodule.property.model.AmenityForm;


@Controller
@RequestMapping("property")
public class AmenityController {
  
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	Logger logger = LoggerFactory.getLogger(AmenityController.class);
	/*
	 * get mapping for add amenity
	 */
	@GetMapping("/add-amenity-form")
	public String add_Amenity_Form(Model model, HttpSession session) {
		logger.info("Method : add_Amenity_Form starts");
		AmenityForm addAmenityForm = new AmenityForm();
		AmenityForm editAmenityForm = (AmenityForm) session.getAttribute("edit_amenity");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (editAmenityForm != null) {
			model.addAttribute("addAmenityForm", editAmenityForm);
			session.setAttribute("edit_amenity", null);
		} else {
			model.addAttribute("addAmenityForm", addAmenityForm);
		}
		try {
			DropDownModel[] iteCategory = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(iteCategory);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : add_Amenity_Form ends");
		return "property/AddAmenityForm";
	}
/*
 * post mapping for add amenity
 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-amenity-form")
	public String addUserForm(@ModelAttribute AmenityForm amenityForm, Model model, HttpSession session) {
		
		logger.info("Method : addUserForm starts"); 
		JsonResponse<Object> resp = new JsonResponse<Object>();
		amenityForm.setCreatedBy("DJ");
		try {
			resp = restClient.postForObject(env.getPropertyUrl() + "restAddAmenity", amenityForm, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("edit_amenity", amenityForm);
			return "redirect:/property/add-amenity-form";
		}
		session.setAttribute("edit_amenity", null);
		logger.info("Method : addUserForm ends"); 
		
		return "redirect:/property/view-amenity-form";
	}
/*
 * view all amenity form
 */
	@GetMapping("/view-amenity-form")
	public String list_Amenity_Form(Model model, HttpSession session) {
		logger.info("Method : list_Amenity_Form starts"); 
		
		try {
			DropDownModel[] iteCategory = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(iteCategory);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : list_Amenity_Form ends"); 
		
		return "property/ListAmenityForm";
	}

	/*
	 *  ajax call for show list in view page
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-amenity-form-throughAjax")
	public @ResponseBody DataTableResponse viewAmenityManagementAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2) {
		logger.info("Method : viewAmenityManagementAjax stares"); 
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

			JsonResponse<List<AmenityForm>> jsonResponse = new JsonResponse<List<AmenityForm>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllAmenity", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AmenityForm> amenity = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AmenityForm>>() {
					});

			String s = "";

			for (AmenityForm m : amenity) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getAmenitiesId().getBytes());
				
				s = s + "<a href='editamenity?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteAmenity(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				
				m.setAction(s);
				s = "";

				if (m.getAmntsActive()) {
					m.setActivity_status("Active");
				} else {
					m.setActivity_status("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(amenity);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewAmenityManagementAjax ends");
		return response;
	}

	/*
	 *  for edit case
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/editamenity")
	public String editUser(Model model, @RequestParam String id, HttpSession session) {

		logger.info("Method : editUser ends");
		
		AmenityForm addAmenity = new AmenityForm();
		JsonResponse<AmenityForm> jsonResponse = new JsonResponse<AmenityForm>();


		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));

		try {
			jsonResponse = restClient.getForObject(env.getPropertyUrl() + "getAmenityById?id=" + id1,
					JsonResponse.class);

		} catch (RestClientException e) {
			
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		addAmenity = mapper.convertValue(jsonResponse.getBody(), AmenityForm.class);
		session.setAttribute("message", "");

		try {
			DropDownModel[] iteCategory = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(iteCategory);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("addAmenityForm", addAmenity);
		
		logger.info("Method : editUser ends");
		return "property/AddAmenityForm";
	}

	/*
	 *  for modal case
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-amenity-form-modal" })
	public @ResponseBody JsonResponse<Object> modalAmenityManagement(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalAmenityManagement starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getAmenityById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modalAmenityManagement ends");
		return res;
	}

	
	/*
	 * for delete case
	 */
	@SuppressWarnings("unchecked")
	
	@GetMapping("/view-amenity-form-delete")
	public @ResponseBody JsonResponse<Object> deleteTheme(@RequestParam String id, HttpSession session) {

		logger.info("Method : view-Amenity-Form-delete starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "DJ";
		try {
			resp = restClient.getForObject(env.getPropertyUrl() + "deleteAmenityById?id=" + id1+"&createdBy="+createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : view-Amenity-Form-delete ends");
		
		return resp;
	}
}
