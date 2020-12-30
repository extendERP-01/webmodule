
/*
 * for Property Theme web Controller
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
import nirmalya.aathithya.webmodule.property.model.PropertyThemeModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "property")
public class PropertyThemeController {

	Logger logger = LoggerFactory.getLogger(PropertyThemeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add Theme view page
	 */
	@GetMapping("/add-property-theme")
	public String addPropertyTheme(Model model, HttpSession session) {

		logger.info("Method : addPropertyTheme starts");

		PropertyThemeModel theme = new PropertyThemeModel();
		PropertyThemeModel sessiontheme = (PropertyThemeModel) session.getAttribute("stheme");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessiontheme != null) {
			model.addAttribute("theme", sessiontheme);
			session.setAttribute("stheme", null);
		} else {
			model.addAttribute("theme", theme);
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

		logger.info("Method : addPropertyTheme ends");

		return "property/AddPropertyTheme";
	}

	/*
	 * Post Mapping for adding new Theme
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-property-theme")
	public String addTheme(@ModelAttribute PropertyThemeModel theme, Model model, HttpSession session) {

		logger.info("Method : addTheme starts");
		theme.setCreatedBy("DJ");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getPropertyUrl() + "restAddTheme", theme, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("stheme", theme);
			return "redirect:/property/add-property-theme";
		}
		logger.info("Method : addTheme ends");

		return "redirect:/property/view-property-theme";
	}

	/*
	 * Get Mapping View Property Theme
	 */
	@GetMapping("/view-property-theme")
	public String lstpropertyTheme(Model model, HttpSession session) {

		logger.info("Method : lstPropertyTheme starts");

		JsonResponse<Object> theme = new JsonResponse<Object>();
		model.addAttribute("theme", theme);

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

		logger.info("Method : lstPropertyTheme ends");

		return "property/ListPropertyTheme";
	}

	/*
	 * For viewTheme for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-theme-ThroughAjax")
	public @ResponseBody DataTableResponse viewThemeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewThemeThrowAjax statrs");

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

			JsonResponse<List<PropertyThemeModel>> jsonResponse = new JsonResponse<List<PropertyThemeModel>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllThemes", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyThemeModel> theme = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyThemeModel>>() {
					});

			String s = "";

			for (PropertyThemeModel m : theme) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getTheme().getBytes());
				s = s + "<a href='edit-theme?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletePropertyTheme(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getThmActive()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(theme);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : view throw ajax Theme ends");

		return response;
	}

	/*
	 * for Edit Property Theme
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-theme")
	public String editTheme(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : edit Theme starts");

		PropertyThemeModel theme = new PropertyThemeModel();
		JsonResponse<PropertyThemeModel> jsonResponse = new JsonResponse<PropertyThemeModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getPropertyUrl() + "getThemeById?id=" + id, JsonResponse.class);
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

		theme = mapper.convertValue(jsonResponse.getBody(), PropertyThemeModel.class);
		session.setAttribute("message", "");

		model.addAttribute("theme", theme);

		logger.info("Method : edit Theme ends");

		return "property/AddPropertyTheme";
	}

	/*
	 * For Delete Theme
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-theme-deleteTheme")
	public @ResponseBody JsonResponse<Object> deleteTheme(@RequestParam String id, HttpSession session) {

		logger.info("Method : delete Theme ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "DJ";
		try {
			resp = restClient.getForObject(env.getPropertyUrl() + "deleteThemeById?id=" + id1+"&createdBy="+createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteProperty Theme ends");
		// return "property/ListPropertyType";
		return resp;
	}

	
	/*
	 * For Modal Theme View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-property-theme-modalViewTheme" })
	public @ResponseBody JsonResponse<Object> modalTheme(Model model, @RequestBody String index, BindingResult result) {

		logger.info("Method : modalTheme ends");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getThemeById?id=" + id, JsonResponse.class);
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
		logger.info("Method :modalTheme ends");
		return res;
	}

}
