/*
 * web Controller for Seating plan Assignment
 */
package nirmalya.aathithya.webmodule.property.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

import nirmalya.aathithya.webmodule.property.model.PropertyAssignmentOfSeatingPlanModel;

/**
 * @author Nirmalya Labs
 *
 */

@Controller
@RequestMapping(value = "property")
public class PropertySeatingPlanAssignmentController {

	Logger logger = LoggerFactory.getLogger(PropertySeattingPlanController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add assignment of planning
	 */
	@GetMapping("/assign-seatingplan-to-propertytype")
	public String add_AssignmentOf_SettingPlan(Model model, HttpSession session) {

		logger.info("Method : add-AssignmentOf-SeatingPlan starts");

		PropertyAssignmentOfSeatingPlanModel assignmentOfSeatingPlanModel = new PropertyAssignmentOfSeatingPlanModel();
		PropertyAssignmentOfSeatingPlanModel sessiontheme = (PropertyAssignmentOfSeatingPlanModel) session
				.getAttribute("assignSeating");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessiontheme != null) {
			model.addAttribute("assignmentOfSeatingPlan", sessiontheme);
			session.setAttribute("assignSeating", null);
		} else {
			model.addAttribute("assignmentOfSeatingPlan", assignmentOfSeatingPlanModel);
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyNameList", propertyNameList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down User Type
		 */
		try {
			DropDownModel[] userType = restClient.getForObject(env.getPropertyUrl() + "getUserType",
					DropDownModel[].class);
			List<DropDownModel> userTypeList = Arrays.asList(userType);
			model.addAttribute("userTypeList", userTypeList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "property/AddAssignmentOfSeatingPlan";
	}

	/*
	 * post mapping for assigning new Seating plan
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/assign-seatingplan-to-propertytype-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addLaundryServiceProperties(
			@RequestBody List<PropertyAssignmentOfSeatingPlanModel> assignmentOfSeatingPlanModel, Model model,
			HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : restAddAssignmentOfSeatingPlanModel function starts");

		try {
			byte[] bytes = (byte[]) session.getAttribute("menuItemsMasterFile");
			long nowTime = new Date().getTime();
			String imageName = nowTime + ".png";

			for (PropertyAssignmentOfSeatingPlanModel r : assignmentOfSeatingPlanModel) {
				r.setsPlanImage(imageName);
				r.setCreatedBy("DJ");

			}
			
			res = restClient.postForObject(env.getPropertyUrl() + "restAddAssignmentOfSeatingPlanModel",
					assignmentOfSeatingPlanModel, JsonResponse.class);

		

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : restAddAssignmentOfSeatingPlanModel function Ends");
		return res;
	}
/*
 * forget sitting plan name list
 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-seatingplan-to-propertytype-getSeatingPlanNameList" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyTypeList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getSeatingPlanNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getSeatingPlanName?proCat=" + index,
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

		logger.info("Method : getSeatingPlanNameList ends");
		return res;
	}

	/*
	 * Get Mapping for view assignment of planning
	 */
	@GetMapping("/view-assigned-seatingplan")
	public String list_AssignmentOf_SettingPlan(Model model, HttpSession session) {

		logger.info("Method : ListAssignmentOfSeatingPlan starts");

		/*
		 * for viewing drop down list of property type
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyId",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyIdList", propertyNameList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list of user type
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getUserType",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("UserTypeList", propertyNameList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		/*
		 * for Drop down Seating plan
		 */

		try {
			DropDownModel[] propertyType = restClient.getForObject(env.getPropertyUrl() + "getSeatingPlan",
					DropDownModel[].class);
			List<DropDownModel> seatingPlanList = Arrays.asList(propertyType);
			model.addAttribute("seatingPlanList", seatingPlanList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : ListAssignmentOfSeatingPlan ends");

		return "property/ListAssignmentOfSeatingPlan";
	}

	/*
	 * For view Amenity item for dataTable Ajax call
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-assigned-seatingplan-ThAjax")
	public @ResponseBody DataTableResponse viewThemeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2 ,@RequestParam String param3) {

		logger.info("Method : viewAssignmentOfSeatingPlan ThrowAjax statrs");

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

			JsonResponse<List<PropertyAssignmentOfSeatingPlanModel>> jsonResponse = new JsonResponse<List<PropertyAssignmentOfSeatingPlanModel>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllAssignmentSeatingPlan", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyAssignmentOfSeatingPlanModel> assignment = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyAssignmentOfSeatingPlanModel>>() {
					});
			String s = "";

			for (PropertyAssignmentOfSeatingPlanModel m : assignment) {
				byte[] encodeId = Base64.getEncoder().encode(m.getPropertyType().getBytes());
				byte[] encodeSp = Base64.getEncoder().encode(m.getSeatingPlan().getBytes());
				byte[] encodeUt = Base64.getEncoder().encode(m.getUserType().getBytes());
				s = "";
				s = s + "<a href='edit-assigned-setaingplan?id=" + new String(encodeId) + "&sp=" + new String(encodeSp)
						+ "&ut=" + new String(encodeUt)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeSp) + ',' + new String(encodeUt)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				
				
				m.setsPlanImage(s);

				if (m.getpSplanActive()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignment);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : view throw viewAssignmentOfSeatingPlan ends");

		return response;
	}

	/*
	 * for Edit Seating Plan get mapping
	 */

	@GetMapping("/edit-assigned-setaingplan")
	public String editTheme(Model model, @RequestParam("id") String encodeId, @RequestParam("sp") String encodeSp,
			@RequestParam("ut") String encodeUt, HttpSession session) {

		logger.info("Method in web: edit-setaing-plan starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		byte[] decodeSp = Base64.getDecoder().decode(encodeSp.getBytes());
		byte[] decodeUt = Base64.getDecoder().decode(encodeUt.getBytes());

		String id = (new String(decodeId));
		String sp = (new String(decodeSp));
		String ut = (new String(decodeUt));

		try {

			PropertyAssignmentOfSeatingPlanModel[] propertyAssignmentOfSeatingPlanModel = restClient.getForObject(
					env.getPropertyUrl() + "getAssignmentById?id=" + id + "&sp=" + sp + "&ut=" + ut,
					PropertyAssignmentOfSeatingPlanModel[].class);

			List<PropertyAssignmentOfSeatingPlanModel> assignmentOfSeatingPlan = Arrays
					.asList(propertyAssignmentOfSeatingPlanModel);
			if (assignmentOfSeatingPlan != null) {
				assignmentOfSeatingPlan.get(0).setId("edit");
			}
			System.out.println(assignmentOfSeatingPlan);
			model.addAttribute("assignmentOfSeatingPlan", assignmentOfSeatingPlan);

			model.addAttribute("Edit", "For Edit");
			session.setAttribute("message", "");

			
		
		/*
		 * for Drop down Property Type id
		 */

		try {
			DropDownModel[] propertyType = restClient.getForObject(env.getPropertyUrl() + "getPropertyIdEdit?proCat="+assignmentOfSeatingPlan.get(0).getPropertyCategory(),
					DropDownModel[].class);
			List<DropDownModel> propertyTypeList = Arrays.asList(propertyType);
			model.addAttribute("propertyTypeList", propertyTypeList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list for property type
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for Drop down Seating plan
		 */

		try {
			DropDownModel[] propertyType = restClient.getForObject(env.getPropertyUrl() + "getSeatingPlan",
					DropDownModel[].class);
			List<DropDownModel> seatingPlanList = Arrays.asList(propertyType);
			model.addAttribute("seatingPlanList", seatingPlanList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down User Type
		 */
		try {
			DropDownModel[] userType = restClient.getForObject(env.getPropertyUrl() + "getUserType",
					DropDownModel[].class);
			List<DropDownModel> userTypeList = Arrays.asList(userType);
			model.addAttribute("userTypeList", userTypeList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method in web: edit-setaing-planends");

		return "property/AddAssignmentOfSeatingPlan";
	}

	/*
	 * For Modal modelViewAssignmentOfSeatingPlan View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assigned-seatingplan-modalview" })
	public @ResponseBody JsonResponse<List<PropertyAssignmentOfSeatingPlanModel>> modalAssignment(Model model,
			@RequestBody PropertyAssignmentOfSeatingPlanModel propertyAssignmentOfSeatingPlanModel,
			BindingResult result) {

		logger.info("Method : modal modelViewAssignmentOfSeatingPlan starts");

		

		String encodeId = propertyAssignmentOfSeatingPlanModel.getPropertyType();
		String encodeSp = propertyAssignmentOfSeatingPlanModel.getSeatingPlan();
		String encodeut = propertyAssignmentOfSeatingPlanModel.getUserType();
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		byte[] decodeSp = Base64.getDecoder().decode(encodeSp.getBytes());
		byte[] decodeUt = Base64.getDecoder().decode(encodeut.getBytes());

		String id = (new String(decodeId));
		String sp = (new String(decodeSp));
		String ut = (new String(decodeUt));
		JsonResponse<List<PropertyAssignmentOfSeatingPlanModel>> response = new JsonResponse<List<PropertyAssignmentOfSeatingPlanModel>>();
		try {
			response = restClient.getForObject(
					env.getPropertyUrl() + "getAssignmentByIdModal?id=" + id + "&sp=" + sp + "&ut=" + ut,
					JsonResponse.class);
			System.out.println(response);
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
	 * Ajax call for file uploading
	 */
	@PostMapping("/uploadFileSPlan")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("menuItemsMasterFile", inputFile.getBytes());
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}
	@GetMapping("/view-assigned-seatingplan-generate-report")
	public String generateAssignedAssetReport(Model model, HttpSession session) {
		
		logger.info("Method : generateAssignedSeatingPlanReport starts");
		
		/*
		 * for viewing drop down list of property type
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyId",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyIdList", propertyNameList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list of user type
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getUserType",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("UserTypeList", propertyNameList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		/*
		 * for Drop down Seating plan
		 */

		try {
			DropDownModel[] propertyType = restClient.getForObject(env.getPropertyUrl() + "getSeatingPlan",
					DropDownModel[].class);
			List<DropDownModel> seatingPlanList = Arrays.asList(propertyType);
			model.addAttribute("seatingPlanList", seatingPlanList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		logger.info("Method : generateAssignedSeatingPlanReport ends");
		return "property/reports-assign-seatingPlan";
	}
	
}
