
/**
 * web Controller for Amenity Item
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
import nirmalya.aathithya.webmodule.property.model.PropertyAmenityItemModel;

/**
 * @author Nirmalya Labs
 *
 */

@Controller
@RequestMapping(value = "property")
public class PropertyAmenityItemController {

	Logger logger = LoggerFactory.getLogger(PropertySeattingPlanController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GEt Mapping for Add amenity view page
	 */
	@GetMapping("/add-amenity-item")
	public String addPropertyTheme(Model model, HttpSession session) {

		logger.info("Method : addProperty Item  starts");

		PropertyAmenityItemModel amenityItem = new PropertyAmenityItemModel();
		PropertyAmenityItemModel sessionAmenityItem = (PropertyAmenityItemModel) session.getAttribute("amenityItem");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionAmenityItem != null) {
			model.addAttribute("amenityItem", sessionAmenityItem);
			session.setAttribute("amenityItem", null);
		} else {
			model.addAttribute("amenityItem", amenityItem);
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
		 * for viewing drop down list for item Category
		 */
		try {
			DropDownModel[] iteCategory = restClient.getForObject(env.getPropertyUrl() + "getItemCategoryName",
					DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(iteCategory);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addPropertyAmenity Item end");
		return "property/AddAmenityItem";
	}

	/*
	 * post mapping for adding new Amenity Item
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-amenity-item")
	public String addAmenityItem(@ModelAttribute PropertyAmenityItemModel amenityItem, Model model,
			HttpSession session) {

		logger.info("Method : addAmenityItem starts");

		amenityItem.setCreatedBy("Dj");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getPropertyUrl() + "restAddAmenityItem", amenityItem,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("amenityItem", amenityItem);
			return "redirect:/property/add-amenity-item";
		}
		logger.info("Method : addAmenityItem ends");
		return "redirect:/property/view-amenity-item";
	}

	/*
	 * drop down for property type list by property category
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-amenity-item-getPropertyType" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyType(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getProperty TypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getPropertyType?proCat=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getProperty TypeList ends");
		return res;
	}
	/*
	 * for drop down Amenity list by property type
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-amenity-item-getAmenitiesList" })
	public @ResponseBody JsonResponse<DropDownModel> getAmenitiesList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAmenityTypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getAmenityName?proType=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAmenityTypeList ends");
		return res;
	}

	/*
	 * for drop down Sub Category item list by item category
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-amenity-item-getSubCategoryItemList" })
	public @ResponseBody JsonResponse<DropDownModel> getSubCategoryItemList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getSubCategoryItemList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getSubCategoryItemList?proCategoryId=" + index,
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

		logger.info("Method : getSubCategoryItemList ends");
		return res;
	}

	/*
	 * for drop down item list by item sub category
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-amenity-item-getItemList" })
	public @ResponseBody JsonResponse<DropDownModel> getItemList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getSubCategoryItemList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getItemList?ItemSubCat=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getSubCategoryItemList ends");
		return res;
	}

	/*
	 * Get Mapping View Amenity Item
	 */
	@GetMapping("/view-amenity-item")
	public String lstpropertyTheme(Model model, HttpSession session) {

		logger.info("Method : lstPropertyTheme starts");

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
		 * for Drop down amenity
		 */

		try {
			DropDownModel[] amenity = restClient.getForObject(env.getPropertyUrl() + "getamenity",
					DropDownModel[].class);
			List<DropDownModel> amenityList = Arrays.asList(amenity);
			model.addAttribute("amenityList", amenityList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : lstPropertyTheme ends");

		return "property/ListAmenityItem";
	}

	/*
	 * For view Amenity item for dataTable Ajax call
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-amenity-item-throughajax")
	public @ResponseBody DataTableResponse viewThemeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewAmenity item ThrowAjax statrs");

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

			JsonResponse<List<PropertyAmenityItemModel>> jsonResponse = new JsonResponse<List<PropertyAmenityItemModel>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllAmenityItem", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyAmenityItemModel> AmenityItem = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyAmenityItemModel>>() {
					});

			String s = "";

			for (PropertyAmenityItemModel m : AmenityItem) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getPropertyType().getBytes());
				byte[] encodeAm = Base64.getEncoder().encode(m.getAmenities().getBytes());
				byte[] encodeItm = Base64.getEncoder().encode(m.getItem().getBytes());

				s = s + "<a href='edit-amenity-item?id=" + new String(encodeId) + "&am=" + new String(encodeAm)
						+ "&itm=" + new String(encodeItm)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeAm) + ',' + new String(encodeItm)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getAmntyItemActive()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(AmenityItem);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : view throw ajax Theme ends");

		return response;
	}

	/*
	 * for Edit amenity item get mapping
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-amenity-item")
	public String editTheme(Model model, @RequestParam("id") String encodeId, @RequestParam("am") String encodeAm,
			@RequestParam("itm") String encodeItm, HttpSession session) {

		logger.info("Method in web: edit Amenity Item starts");

		PropertyAmenityItemModel amenityItem = new PropertyAmenityItemModel();
		JsonResponse<PropertyAmenityItemModel> jsonResponse = new JsonResponse<PropertyAmenityItemModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		byte[] decodeAm = Base64.getDecoder().decode(encodeAm.getBytes());
		byte[] decodeItm = Base64.getDecoder().decode(encodeItm.getBytes());

		String id = (new String(decodeId));
		String am = (new String(decodeAm));
		String itm = (new String(decodeItm));

		try {

			jsonResponse = restClient.getForObject(
					env.getPropertyUrl() + "getAmenityItemById?id=" + id + "&am=" + am + "&itm=" + itm,
					JsonResponse.class);
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
		 * for viewing drop down list for item Category
		 */
		try {
			DropDownModel[] iteCategory = restClient.getForObject(env.getPropertyUrl() + "getItemCategoryName",
					DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(iteCategory);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for Drop down Property Type id
		 */

		try {
			DropDownModel[] propertyType = restClient.getForObject(env.getPropertyUrl() + "getpropertyTypeId",
					DropDownModel[].class);
			List<DropDownModel> propertyTypeList = Arrays.asList(propertyType);
			model.addAttribute("propertyTypeList", propertyTypeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for Drop down amenity
		 */

		try {
			DropDownModel[] amenity = restClient.getForObject(env.getPropertyUrl() + "getamenity",
					DropDownModel[].class);
			List<DropDownModel> amenityList = Arrays.asList(amenity);
			model.addAttribute("amenityList", amenityList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for Drop down Item SubCategory
		 */

		try {
			DropDownModel[] SubCategory = restClient.getForObject(env.getPropertyUrl() + "getSubCategory",
					DropDownModel[].class);
			List<DropDownModel> SubCategoryList = Arrays.asList(SubCategory);
			model.addAttribute("SubCategoryList", SubCategoryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for Drop down Item Name List
		 */

		try {
			DropDownModel[] itemName = restClient.getForObject(env.getPropertyUrl() + "getItemNameList",
					DropDownModel[].class);
			List<DropDownModel> itemNameList = Arrays.asList(itemName);
			model.addAttribute("itemNameList", itemNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		amenityItem = mapper.convertValue(jsonResponse.getBody(), PropertyAmenityItemModel.class);
		session.setAttribute("message", "");

		model.addAttribute("amenityItem", amenityItem);

		logger.info("Method in web: edit Amenity Item ends");

		return "property/EditAmenityItem";
	}

	/*
	 * for edit amenity item
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/edit-amenityItem")
	public String editAmenityItem(@ModelAttribute PropertyAmenityItemModel amenityItem, Model model,
			HttpSession session) {

		logger.info("Method : edit AmenityItem starts");

		@SuppressWarnings("unused")
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getPropertyUrl() + "restEditAmenityItem", amenityItem,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : addAmenityItem ends");

		return "redirect:/property/view-amenity-item";
	}

	/*
	 * For Modal Amenity item View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-amenity-item-modal" })
	public @ResponseBody JsonResponse<Object> modalTheme(Model model, @RequestBody PropertyAmenityItemModel amenityItem,
			BindingResult result) {

		logger.info("Method : modal Amenity Item starts");

		JsonResponse<Object> response = new JsonResponse<Object>();
		String encodeId = amenityItem.getPropertyType();
		String encodeAm = amenityItem.getAmenities();
		String encodeItm = amenityItem.getItem();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		byte[] decodeAm = Base64.getDecoder().decode(encodeAm.getBytes());
		byte[] decodeItm = Base64.getDecoder().decode(encodeItm.getBytes());

		String id = (new String(decodeId));
		String am = (new String(decodeAm));
		String itm = (new String(decodeItm));

		try {

			response = restClient.getForObject(
					env.getPropertyUrl() + "getAmenityItemById?id=" + id + "&am=" + am + "&itm=" + itm,
					JsonResponse.class);
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
}
