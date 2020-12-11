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
import nirmalya.aathithya.webmodule.property.model.PropertyAssignAssetModel;

/**
 * @author Nirmalya Labs
 *
 */

@Controller
@RequestMapping(value = "property")
public class PropertyAssignAssetController {

	Logger logger = LoggerFactory.getLogger(PropertyAssignAssetController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * get mapping for assign new asset
	 */
	@GetMapping("/add-assign-asset-to-property")
	public String add_AssignAssetTo_Property(Model model, HttpSession session) {

		logger.info("Method : add asssign to asset");

		PropertyAssignAssetModel assignAsset = new PropertyAssignAssetModel();
		PropertyAssignAssetModel sessionassignAsset = (PropertyAssignAssetModel) session.getAttribute("assignAsset");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionassignAsset != null) {
			model.addAttribute("assignAsset", sessionassignAsset);
			session.setAttribute("assignAsset", null);
		} else {
			model.addAttribute("assignAsset", assignAsset);
		}

		/*
		 * for viewing drop down list for property type
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			System.out.println("propertyNameList: "+propertyNameList);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "property/AddAssignAssetToProperty";
	}

	
	/*
	 * for drop down property Type list
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-property-getPropertyTypeList" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyTypeList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getProperty TypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		//this method defines in amenity item rest controller
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getPropertyTypeName?proCat=" + index,
					JsonResponse.class);
			System.out.println("res : "+res);
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
	 * for item name list for fixed asset
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-property-getItemListFixed" })
	public @ResponseBody JsonResponse<DropDownModel> getItemList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getSubCategoryItemList for fixed asset starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {

			res = restClient.getForObject(env.getPropertyUrl() + "getItemListFixed?ItemSubCat=" + index,
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

		logger.info("Method : getSubCategoryItemList  for fixed assets ends");
		return res;
	}

	/*
	 * for drop down Amenity by property type
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-property-getAmenity" })
	public @ResponseBody JsonResponse<DropDownModel> getAmenity(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :getAmenity TypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getAmenity?proCat=" + index, JsonResponse.class);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAmenity TypeList ends");
		return res;
	}

	/*
	 * for drop down Amenity by property type
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-property-getSubCategoryItem" })
	public @ResponseBody JsonResponse<DropDownModel> getSubCategoryItem(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :getSubCategoryItem TypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getSubCategoryItem?itmCat=" + index,
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

		logger.info("Method : getSubCategoryItem TypeList ends");
		return res;
	}

	/*
	 * for drop down Amenity by property type
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-property-getItemCategoryByAmenity" })
	public @ResponseBody JsonResponse<DropDownModel> getItemCategoryByAmenity(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :getItemCategoryByAmenity  starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getItemCategoryByAmenity?amenity=" + index,
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

		logger.info("Method : getItemCategoryByAmenity ends");
		return res;
	}
	
	/*
	 * for Asset list fixed
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-property-property-getAssetList" })
	public @ResponseBody JsonResponse<DropDownModel> getAssetList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAssetList for fixed asset starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {

			res = restClient.getForObject(env.getPropertyUrl() + "getAssetList?item=" + index, JsonResponse.class);

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
	/*
	 * for Asset list all
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assign-asset-to-property-getAssetListAll" })
	public @ResponseBody JsonResponse<DropDownModel> getAssetListAll(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAssetList for fixed asset starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {

			res = restClient.getForObject(env.getPropertyUrl() + "getAssetListAll?item=" + index, JsonResponse.class);

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


	/*
	 * post mapping for Assign new ASset
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-assign-asset-to-property")
	public String addAmenityItem(@ModelAttribute PropertyAssignAssetModel assignAsset, Model model,
			HttpSession session) {

		logger.info("Method : AddAssignAsset starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String s = "";

		for (String m : assignAsset.getAssetsList()) {
			s = s + m + ",";
		}

		if (s != "") {
			s = s.substring(0, s.length() - 1);
			assignAsset.setAssetsName(s);
		}
		assignAsset.setCreatedBy("Dj");
		try {
			resp = restClient.postForObject(env.getPropertyUrl() + "restAddAssignAsset", assignAsset,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("assignAsset", assignAsset);
			return "redirect:/property/add-assign-asset-to-property";
		}
		logger.info("Method : AddAssignAsset ends");
		return "redirect:/property/view-assign-asset-to-property";
	}

	/*
	 * get mapping for view assigned asset
	 */

	@GetMapping("/view-assign-asset-to-property")
	public String list_AssignAssetTo_Property(Model model, HttpSession session) {

		logger.info("Method : lstPropertyTheme starts");

		/*
		 * for Drop down Property Type id
		 */

		try {
			DropDownModel[] propertyType = restClient.getForObject(env.getPropertyUrl() + "getPropertyIdNames",
					DropDownModel[].class);
			List<DropDownModel> PropertyIdNames = Arrays.asList(propertyType);
			model.addAttribute("PropertyIdNames", PropertyIdNames);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list item Names for searching
		 */
		try {
			DropDownModel[] categoryName = restClient.getForObject(env.getPropertyUrl() + "getItemNameList",
					DropDownModel[].class);
			List<DropDownModel> itemNames = Arrays.asList(categoryName);
			model.addAttribute("itemNames", itemNames);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : lstPropertyTheme ends");

		return "property/ListAssignedAssetToProperty";
	}

	/*
	 * For view Assign asset for dataTable Ajax call
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-assign-asset-to-property-ThroughAjax")
	public @ResponseBody DataTableResponse viewThemeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : view Assigned Asset ThrowAjax statrs");

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

			JsonResponse<List<PropertyAssignAssetModel>> jsonResponse = new JsonResponse<List<PropertyAssignAssetModel>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllAssignedAsset", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			if(jsonResponse.getMessage()==null)
			{
			List<PropertyAssignAssetModel> asignAsset = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyAssignAssetModel>>() {
					});
			

			String s = "";

			for (PropertyAssignAssetModel m : asignAsset) {
				s = "";

				byte[] pId = Base64.getEncoder().encode( m.getProperty().getBytes());
				byte[] encodeAN = Base64.getEncoder().encode(m.getAssetsName().getBytes());
				

				s = s + "<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" + new String(pId) + ","
						+new String(encodeAN)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + ',' + new String(encodeAN)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}
			

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(asignAsset);
			}
			else
			{
				JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
				res.setMessage("No record Found..");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : view throw ajax Assigned Asset ends");

		return response;
	}

	/*
	 * For Modal model View Assigned Asset
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assign-asset-to-property-modal" })
	public @ResponseBody JsonResponse<Object> modalTheme(Model model,
			@RequestBody PropertyAssignAssetModel propertyAssignAssetModel, BindingResult result) {

		logger.info("Method : modal View Assigned Asset Starts");

		JsonResponse<Object> response = new JsonResponse<Object>();
		String encodeId=propertyAssignAssetModel.getProperty();
		String eccodeAn=propertyAssignAssetModel.getAssetsName();
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		byte[] decodeAm = Base64.getDecoder().decode(eccodeAn.getBytes());
		
		String id = (new String(decodeId));
		String am = (new String(decodeAm));
		

		try {

			response = restClient.getForObject(env.getPropertyUrl() + "getAssignedAssetById?id="
					+ id+ "&an=" + am,
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
		logger.info("Method : modal Assigned Asset ends ");
		return response;
	}

	/*
	 * For Delete Assigned Asset
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-assign-asset-to-property-delete")
	public @ResponseBody JsonResponse<Object> deleteSetting(@RequestParam String id, @RequestParam String ac,
			HttpSession session) {
		logger.info("Method : delete Assigned Asset Starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());
		byte[] decodeAm = Base64.getDecoder().decode(ac.getBytes());
		
		String pId = (new String(decodeId));
		String am = (new String(decodeAm));
		String createdBy = "DJ";
		try {
			resp = restClient.getForObject(env.getPropertyUrl() + "deleteAssignedAsset?id=" + pId + "&ac=" + am + "&createdBy=" + createdBy,
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
		logger.info("Method : delete Assigned Asset ends");


		return resp;
	}
	
	@GetMapping("/view-assign-asset-to-property-report")
	public String generateAssignedAssetReport(Model model, HttpSession session) {
		
		logger.info("Method : generateAssignedAssetReport starts");
		
		/*
		 * for Drop down Property Type id
		 */

		try {
			DropDownModel[] propertyType = restClient.getForObject(env.getPropertyUrl() + "getPropertyIdNames",
					DropDownModel[].class);
			List<DropDownModel> PropertyIdNames = Arrays.asList(propertyType);
			model.addAttribute("PropertyIdNames", PropertyIdNames);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list item Names for searching
		 */
		try {
			DropDownModel[] categoryName = restClient.getForObject(env.getPropertyUrl() + "getItemNameList",
					DropDownModel[].class);
			List<DropDownModel> itemNames = Arrays.asList(categoryName);
			model.addAttribute("itemNames", itemNames);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		logger.info("Method : generateAssignedAssetReport ends");
		return "property/report-assigned-asset";
	}
	

}
