package nirmalya.aathithya.webmodule.inventory.controller;

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
import nirmalya.aathithya.webmodule.inventory.model.InventoryManageCostcenterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "inventory")
public class InventoryManageCostCenterController {

	Logger logger = LoggerFactory.getLogger(InventoryManageCostCenterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add Theme view page
	 */
	@GetMapping("/add-costcenter")
	public String addManageCostcenter(Model model, HttpSession session) {

		logger.info("Method : addManageCostcenter starts");

		InventoryManageCostcenterModel costcenter = new InventoryManageCostcenterModel();
		InventoryManageCostcenterModel sessiontheme = (InventoryManageCostcenterModel) session
				.getAttribute("scostcenter");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessiontheme != null) {
			model.addAttribute("costcenter", sessiontheme);
			session.setAttribute("scostcenter", null);
		} else {
			model.addAttribute("costcenter", costcenter);
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

		logger.info("Method : addManageCostcenter ends");

		return "inventory/costcenter-manage";
	}

	/*
	 * Post Mapping for adding new Theme
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-costcenter")
	public String addManageCostcenterPost(@ModelAttribute InventoryManageCostcenterModel inventoryManageCostcenterModel,
			Model model, HttpSession session) {

		logger.info("Method : addManageCostcenterPost starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String s = "";

		for (String m : inventoryManageCostcenterModel.getPropertyCategory()) {
			s = s + m + ",";
		}

		if (s != "") {
			s = s.substring(0, s.length() - 1);
			inventoryManageCostcenterModel.setPropertyCategoryName(s);
			;
		}
		inventoryManageCostcenterModel.setCreatedBy(userId);
		try {

			resp = restClient.postForObject(env.getInventoryUrl() + "restAddCostcenterManage",
					inventoryManageCostcenterModel, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("stheme", inventoryManageCostcenterModel);
			return "redirect:/inventory/add-costcenter";
		}
		logger.info("Method : addManageCostcenterPost ends");

		return "redirect:/inventory/view-costcenter";
	}

	/*
	 * Get Mapping View Property Theme
	 */
	@GetMapping("/view-costcenter")
	public String listManageCostcenter(Model model, HttpSession session) {

		logger.info("Method in controller: listManageCostcenter starts");

		JsonResponse<Object> costcenter = new JsonResponse<Object>();
		model.addAttribute("costcenter", costcenter);

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

		logger.info("Method in controller: listManageCostcenter ends");

		return "inventory/list-manage-costcenter";
	}

	/*
	 * For viewTheme for dataTable Ajaxcall
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-costcenter-ThroughAjax")
	public @ResponseBody DataTableResponse viewManageCostcenterThroughAjax(Model model, HttpServletRequest request,

			@RequestParam String param1) {

		logger.info("Method in Controller: viewManageCostcenterThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			/* tableRequest.setParam2(param2); */

			JsonResponse<List<InventoryManageCostcenterModel>> jsonResponse = new JsonResponse<List<InventoryManageCostcenterModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getManageCostCenter", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryManageCostcenterModel> theme = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryManageCostcenterModel>>() {
					});

			String s = "";

			for (InventoryManageCostcenterModel m : theme) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getCostcenterId().getBytes());
				s = s + "<a href='edit-costcenter?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteManageCostcenter(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());

			response.setDraw(Integer.parseInt(draw));
			response.setData(theme);

		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method in Controller: viewManageCostcenterThroughAjax ends");

		return response;
	}

	/*
	 * for Edit manage cost center
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-costcenter")
	public String editCostcenter(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : edit editCostcenter starts");

		InventoryManageCostcenterModel costcenter = new InventoryManageCostcenterModel();
		JsonResponse<InventoryManageCostcenterModel> jsonResponse = new JsonResponse<InventoryManageCostcenterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "getManageCostcenter?id=" + id,
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
		ObjectMapper mapper = new ObjectMapper();

		costcenter = mapper.convertValue(jsonResponse.getBody(), InventoryManageCostcenterModel.class);
		costcenter.setPropertyCategory(Arrays.asList(costcenter.getPropertyCategoryName().split("\\s*,\\s*")));
		model.addAttribute("costcenter", costcenter);

		logger.info("Method : edit costcenter ends");

		return "inventory/costcenter-manage";
	}

	/*
	 * For Delete Theme
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-costcenter-delete")
	public @ResponseBody JsonResponse<Object> deleteManageCostcenter(@RequestParam String id, HttpSession session) {

		logger.info("Method : deleteManageCostcenter ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		try {
			resp = restClient.getForObject(
					env.getInventoryUrl() + "deleteManageCostcenter?id=" + id1 + "&createdBy=" + userId,
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
		logger.info("Method : deleteManageCostcenter ends");
		return resp;
	}

	/*
	 * For Modal manage Cost center
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "/view-costcenter-modalView" })
	public @ResponseBody JsonResponse<Object> modalTheme(Model model, @RequestBody String index, BindingResult result) {

		logger.info("Method : view-manage-costcenter-modalView ends");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getManageCostcenterModal?id=" + id,
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
		logger.info("Method :view-manage-costcenter-modalView ends");
		return res;
	}

}
