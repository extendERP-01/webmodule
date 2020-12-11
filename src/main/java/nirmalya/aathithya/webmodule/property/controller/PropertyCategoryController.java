/**
 * web Controller for property category

 */
package nirmalya.aathithya.webmodule.property.controller;

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

import nirmalya.aathithya.webmodule.property.model.PropertyCategoryModel;

@Controller
@RequestMapping(value = "property")
public class PropertyCategoryController {
	Logger logger = LoggerFactory.getLogger(PropertyCategoryController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GEt Mapping for Add property category view page
	 */
	@GetMapping("/add-property-category")
	public String addPropertyCategory(Model model, HttpSession session) {
		logger.info("Method : addPropertyCategory starts");
		PropertyCategoryModel category = new PropertyCategoryModel();

		PropertyCategoryModel form = (PropertyCategoryModel) session.getAttribute("scategory");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("category", form);
			session.setAttribute("scategory", null);

		} else {
			model.addAttribute("category", category);
		}
		logger.info("Method : addPropertyCategory end");
		return "property/addPropertyCatagory";
	}

	/*
	 * post mapping for adding new property category
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-property-category")
	public String postCategory(@ModelAttribute PropertyCategoryModel category, Model model, HttpSession session) {
		logger.info("Method : postCategory starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.postForObject(env.getPropertyUrl() + "addCategory", category, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("scategory", category);
			return "redirect:/property/add-property-category";
		}
		session.setAttribute("scategory", null);
		logger.info("Method : postCategory end");
		return "redirect:/property/view-property-category";
	}

	/*
	 * get mapping for view property category
	 */
	@GetMapping("/view-property-category")
	public String listPropertyCategory(Model model, HttpSession session) {
		logger.info("Method : listPropertyCategory starts");

		JsonResponse<Object> category = new JsonResponse<Object>();
		model.addAttribute("category", category);

		logger.info("Method : listPropertyCategory end");
		return "property/listPropertyCatagory";
	}

	/*
	 * get mapping for view property category through ajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-category-throughajax")
	public @ResponseBody DataTableResponse viewCategoryThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewCategoryThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<PropertyCategoryModel>> jsonResponse = new JsonResponse<List<PropertyCategoryModel>>();
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllcategory", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyCategoryModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyCategoryModel>>() {
					});

			String s = "";
			for (PropertyCategoryModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getPropertyCatId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View'  href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-property-category?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> ";
				/*s = s + " &nbsp;&nbsp <a href='edit-property-category?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;&nbsp; <a href= 'javascript:void(0)' "
						+ "' onclick='deleteCategoryItem(\"" +m.getPropertyCatId()
						+"\")' ><i class=\"fa fa-trash\"></i></a> ";*/
				m.setAction(s);
				s = "";

				if (m.getCategoryActive()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
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
		logger.info("Method : viewCategoryThroughAjax end");
		return response;
	}
	/*
	 * get mapping for delete category
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-category-delete")
	public  @ResponseBody JsonResponse<Object> deleteCategory(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteCategory starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		/*
		 * byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes()); String
		 * id = (new String(encodeByte));
		 */

		try {
			resp = restClient.getForObject(env.getPropertyUrl() + "deleteCategoryById?id=" + id, JsonResponse.class);

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
		logger.info("Method : deleteCategory ends");
		return resp;
	}
	/*
	 * get mapping for edit property category
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/edit-property-category")
	public String editCategory(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editCategory starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		PropertyCategoryModel category = new PropertyCategoryModel();
		JsonResponse<PropertyCategoryModel> jsonResponse = new JsonResponse<PropertyCategoryModel>();

		try {
			jsonResponse = restClient.getForObject(env.getPropertyUrl() + "getCategoryById?id=" + id,
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

		category = mapper.convertValue(jsonResponse.getBody(), PropertyCategoryModel.class);
		session.setAttribute("message", "");

		model.addAttribute("category", category);

		logger.info("Method : editCategory end");
		return "property/addPropertyCatagory";
	}

	/*
	 * get mapping for viewInModelCatData property category
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-property-category-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getCategoryById?id=" + id, JsonResponse.class);
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

}
