/**
 * Web Controller For AssignConsumedItems
 */
package nirmalya.aathithya.webmodule.property.controller;

import java.util.Arrays;
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
import nirmalya.aathithya.webmodule.property.model.PropertyAssignConsumedItemModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "property")
public class PropertyAssignedConsumedItemController {
	Logger logger = LoggerFactory.getLogger(PropertyAssignedConsumedItemController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMApping for Adding
	 *
	 */
	@GetMapping("add-assign-consumed")
	public String addAsignConsumedItem(Model model, HttpSession session) {

		logger.info("Method : addAsignConsumedItem starts");

		try {
			DropDownModel[] propertyCategory = restClient.getForObject(env.getPropertyUrl() + "get-propertyCategory",
					DropDownModel[].class);
			List<DropDownModel> propertyCategoryList = Arrays.asList(propertyCategory);

			model.addAttribute("propertyCategoryList", propertyCategoryList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		// DRop DowN endS

		PropertyAssignConsumedItemModel propertyAssignConsumedItemModel = new PropertyAssignConsumedItemModel();
		PropertyAssignConsumedItemModel form = (PropertyAssignConsumedItemModel) session.getAttribute("sitems");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("propertyAssignConsumedItemModel", form);
		} else {
			model.addAttribute("propertyAssignConsumedItemModel", propertyAssignConsumedItemModel);
		}

		logger.info("Method : addAsignConsumedItem ends");

		return "property/addAssignConsumedItemToProperty";

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-assign-consumed-get-amenityName-through-ajax" })
	public @ResponseBody JsonResponse<Object> getAmenityName(Model model, @RequestBody String propertyNameId,
			BindingResult result) {
		logger.info("Method : getAmenityName starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "get-amenityName?id=" + propertyNameId,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			// ItemModel itemModel = new ItemModel();
			// res.setBody(itemModel);
			res.setMessage("success");
		}
		logger.info("Method : getAmenityName ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-assign-consumed-get-itemCategoryName-through-ajax" })
	public @ResponseBody JsonResponse<Object> getItemCategoryName(Model model, @RequestBody String amenity,
			BindingResult result) {
		logger.info("Method : getAmenityName starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "get-itemcategory-name?id=" + amenity,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			// ItemModel itemModel = new ItemModel();
			// res.setBody(itemModel);
			res.setMessage("success");
		}
		logger.info("Method : getAmenityName ends");
		return res;
	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-assign-consumed-get-itemCategory-through-ajax" })
	public @ResponseBody JsonResponse<Object> getItemCategory(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : getItemCategory starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "get-itemSubCategory?id=" + itemCategory,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			// ItemModel itemModel = new ItemModel();
			// res.setBody(itemModel);
			res.setMessage("success");
		}
		logger.info("Method : getItemCategory ends");
		return res;
	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-assign-consumed-get-itemName-through-ajax" })
	public @ResponseBody JsonResponse<Object> getItemName(Model model, @RequestBody String itemSubCategory,
			BindingResult result) {
		logger.info("Method : getItemName starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		// System.out.println(itemSubCategory);
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "get-itemName?id=" + itemSubCategory,
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
		logger.info("Method : getItemName ends");
		return res;

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-assign-consumed-get-propertyName-through-ajax" })
	public @ResponseBody JsonResponse<Object> propertyName(Model model, @RequestBody String propertyCategory,
			BindingResult result) {
		logger.info("Method : propertyName starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		// System.out.println("propertyCategory"+propertyCategory);
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "get-propertyName?id=" + propertyCategory,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res);
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : propertyName ends");
		return res;
	}

	/*
	 * get Mapping for view
	 * 
	 */

	@GetMapping("/view-assign-consumed")
	public String viewAssignedConsume(Model model, HttpSession session) {

		logger.info("Method : viewAssignedConsume starts");

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getPropertyUrl() + "rest-get-propertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyName = Arrays.asList(dropDownModel);
			model.addAttribute("propertyName", propertyName);
			System.out.println(propertyName);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getPropertyUrl() + "rest-get-ItemName",
					DropDownModel[].class);
			List<DropDownModel> itemName = Arrays.asList(dropDownModel);
			model.addAttribute("itemName", itemName);
			System.out.println(itemName);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewAssignedConsume ends");

		return "property/viewAssignConsumedItemToProperty";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-assign-consumed" })
	public String addAssignConsumed(@ModelAttribute PropertyAssignConsumedItemModel propertyAssignConsumedItemModel,
			Model model, HttpSession session) {
		logger.info("Method : addGoodsReceiceNote starts");
		System.out.println("propertyAssignConsumedItemModel" + propertyAssignConsumedItemModel);
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			propertyAssignConsumedItemModel.setCreatedBy("u0001");
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "rest-add-assign-consumed",
					propertyAssignConsumedItemModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		// System.out.println(jsonResponse.getMessage());

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("propertyAssignConsumedItemModel", propertyAssignConsumedItemModel);
			return "redirect:add-assign-consumed";
		}
		logger.info("Method : addGoodsReceiceNote ends");
		return "redirect:view-assign-consumed";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-assign-consumed-throughAjax" })
	public @ResponseBody DataTableResponse viewAssignConsumedThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {
		logger.info("Method : viewAssignConsumedThroughAjax starts");
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

			JsonResponse<List<PropertyAssignConsumedItemModel>> jsonResponse = new JsonResponse<List<PropertyAssignConsumedItemModel>>();
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "get-all-assign-consumeditem", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();
			List<PropertyAssignConsumedItemModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyAssignConsumedItemModel>>() {
					});

			// System.out.println(form);
			String s = "";

			for (PropertyAssignConsumedItemModel m : form) {

				s = "";
				s = s + "<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" + m.getPropertyNameId() + ","
						+ m.getItemNameId()
						+ "\")'><i class=\"fa fa-trash\"></i></a>&nbsp;&nbsp;"
						+ "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ m.getPropertyNameId() + ',' + m.getItemNameId()
						+ "\")'><i class='fa fa-search search'></i></a>&nbsp;&nbsp;";
				/*
				 * +"<a href='javascript:void' onclick='pdfCreate(\"" +
				 * m.getPropertyNameId()+','+m.getItemNameId() +
				 * "\")'><i class='fa fa-download' style=\"font-size:24px\"></i></a>";
				 */
				m.setAction(s);
				s = "";

				// System.out.println(m.getItemName()+m.getItemNameId()+m.getPropertyName()+m.getPropertyNameId());

				if (m.getAssignActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("InActive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewAssignConsumedThroughAjax ends");
		return response;
	}

	/*
	 * For Delete Assigned Asset
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/delete-assign-consumeditem")
	public @ResponseBody JsonResponse<Object> deleteAssign(@RequestParam String id, @RequestParam String ac,
			HttpSession session) {
		logger.info("Method : deleteAssign Starts");

		String createdBy = "u0001";
		JsonResponse<Object> resp = new JsonResponse<Object>();

		// System.out.println("In web controller rest part " + id + " ... " + ac);
		try {
			resp = restClient.getForObject(env.getPropertyUrl() + "deleteAssignConsumedItem?id=" + id + "&ac=" + ac
					+ "&createdBy=" + createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteAssign ends");

		return resp;
	}

	/*
	 * For Modal model View Assigned Asset
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assign-consumed-modal-view" })
	public @ResponseBody JsonResponse<Object> modalViewAssignConsumed(Model model,
			@RequestBody PropertyAssignConsumedItemModel propertyAssignConsumedItemModel, BindingResult result) {

		logger.info("Method : modal View AssignConsumed Starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response = restClient.getForObject(env.getPropertyUrl() + "modalViewAssignConsumed?id="
					+ propertyAssignConsumedItemModel.getPropertyNameId() + "&an="
					+ propertyAssignConsumedItemModel.getItemNameId(), JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {

			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modal AssignConsumed ends ");
		return response;
	}

	@GetMapping("/view-assign-consumed-generate-report")
	public String generatePropertyItemAssignReport(Model model, HttpSession session) {

		logger.info("Method : generatePropertyItemAssignReport starts");
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getPropertyUrl() + "rest-get-propertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyName = Arrays.asList(dropDownModel);
			model.addAttribute("propertyName", propertyName);
			System.out.println(propertyName);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getPropertyUrl() + "rest-get-ItemName",
					DropDownModel[].class);
			List<DropDownModel> itemName = Arrays.asList(dropDownModel);
			model.addAttribute("itemName", itemName);
			// System.out.println(itemName);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : generatePropertyItemAssignReport ends");
		return "property/pdfReportPropertyAssign";
	}

}
