/**
 * web Controller for property asset code
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

import nirmalya.aathithya.webmodule.asset.model.AssetModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.property.model.AssetItemModel;
import nirmalya.aathithya.webmodule.property.model.PropertyAssetCodeModel;

/**
 * @author Nirmalya Labs
 *
 */

@Controller
@RequestMapping(value = "property")

public class PropertyAssetCodeController {
	Logger logger = LoggerFactory.getLogger(PropertyAssetCodeController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GEt Mapping for Add asset code view page
	 */
	@GetMapping("/add-assetcode")
	public String addPropertyAssetCode(Model model, HttpSession session) {
		logger.info("Method : addPropertyAssetCode starts");
		PropertyAssetCodeModel assetCode = new PropertyAssetCodeModel();

		PropertyAssetCodeModel form = (PropertyAssetCodeModel) session.getAttribute("sassetCode");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("assetCode", form);
			session.setAttribute("sassetCode", null);
		} else {
			model.addAttribute("assetCode", assetCode);
		}
		/*
		 * dropdown data
		 */
		try {
			AssetItemModel[] item = restClient.getForObject(env.getPropertyUrl() + "getItemNameForAsset", AssetItemModel[].class);
			List<AssetItemModel> itemList = Arrays.asList(item);
			model.addAttribute("itemList", itemList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/** Store **/
		try {
			DropDownModel[] store = restClient.getForObject(env.getPropertyUrl() + "getStoreForAsset", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addPropertyAssetCode end");
		return "property/addAssetCode";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assetcode-get-item-category" })
	public @ResponseBody JsonResponse<DropDownModel> getItemCategoryForAsset(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getItemCategoryForAsset starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getItemCategoryForAsset?id=" + searchValue,
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

		logger.info("Method : getItemCategoryForAsset ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assetcode-get-barcode" })
	public @ResponseBody JsonResponse<DropDownModel> getItemBarcodeForAsset(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getItemBarcodeForAsset starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getItemBarcodeForAsset?id=" + searchValue,
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
		
		logger.info("Method : getItemBarcodeForAsset ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-assetcode-get-item" })
	public @ResponseBody JsonResponse<AssetModel> getItemForAsset(Model model, @RequestBody DropDownModel searchValue,
			BindingResult result) {
		logger.info("Method : getItemCategoryForAsset starts");
		
		JsonResponse<AssetModel> res = new JsonResponse<AssetModel>();
		
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getItemForAsset?id=" + searchValue.getName() + "&cat=" + searchValue.getKey(),
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
		
		logger.info("Method : getItemForAsset ends");
		return res;
	}

	/*
	 * post Mapping for submit data
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-assetcode")
	public String postAssetCode(@ModelAttribute PropertyAssetCodeModel assetCode, Model model, HttpSession session) {
		logger.info("Method : postAssetCode starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		assetCode.setCreatedBy("Dj");
		try {
			resp = restClient.postForObject(env.getPropertyUrl() + "addAssetCode", assetCode, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sassetCode", assetCode);
			return "redirect:/property/add-assetcode";

		}
		session.setAttribute("sassetCode", null);
		logger.info("Method : postAssetCode end");
		return "redirect:/property/view-assetcode";
	}
	/*
	 * GEt Mapping for view property asset code form
	 */

	@GetMapping("/view-assetcode")
	public String listPropertyAssetCode(Model model, HttpSession session) {
		logger.info("Method : listPropertyAssetCode starts");

		logger.info("Method : listPropertyAssetCode end");
		return "property/listAssetCode";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-assetcode-throughajax")
	public @ResponseBody DataTableResponse viewAssetCodeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAssetCodeThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<PropertyAssetCodeModel>> jsonResponse = new JsonResponse<List<PropertyAssetCodeModel>>();
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllAssetCode", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyAssetCodeModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyAssetCodeModel>>() {
					});

			String s = "";
			for (PropertyAssetCodeModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getAssetCodeId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;<a href='edit-assetcode?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;<a href='javascript:void(0)'\"+ \"' onclick='deleteAsset(  \""
						+ new String(pId) + "\")' ><i class=\"fa fa-trash\"></i></a> ";

				m.setAction(s);
				s = "";

				if (m.getWorkingStatus()) {
					m.setWorkingStatusName("Active");
				} else {
					m.setWorkingStatusName("Inactive");
				}

				if (m.getAssetactive()) {
					m.setAssetStatus("Active");
				} else {
					m.setAssetStatus("Inactive");
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
		logger.info("Method : viewAssetCodeThroughAjax end");
		return response;
	}

	/*
	 * GEt Mapping for delete asset code
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-assetcode-delete")
	public @ResponseBody JsonResponse<Object> deleteAssetCode(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteAssetCode starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		String createdBy = "DJ";
		try {
			resp = restClient.getForObject(
					env.getPropertyUrl() + "deleteAssetCodeById?id=" + id1 + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteAssetCode ends");

		return resp;
	}
	/*
	 * GEt Mapping for edit asset code
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/edit-assetcode")
	public String editAssetCode(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editAssetCode starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		PropertyAssetCodeModel assetCode = new PropertyAssetCodeModel();
		JsonResponse<PropertyAssetCodeModel> jsonResponse = new JsonResponse<PropertyAssetCodeModel>();

		try {
			jsonResponse = restClient.getForObject(
					env.getPropertyUrl() + "getAssetCodeById?id=" + id + "&Action=editAssetCode", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		assetCode = mapper.convertValue(jsonResponse.getBody(), PropertyAssetCodeModel.class);
		session.setAttribute("message", "");

		model.addAttribute("assetCode", assetCode);
		/*
		 * drop down data
		 */
		try {
			AssetItemModel[] item = restClient.getForObject(env.getPropertyUrl() + "getItemNameForAsset", AssetItemModel[].class);
			List<AssetItemModel> itemList = Arrays.asList(item);
			model.addAttribute("itemList", itemList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/** Store **/
		try {
			DropDownModel[] store = restClient.getForObject(env.getPropertyUrl() + "getStoreForAsset", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : editAssetCode end");
		return "property/addAssetCode";
	}
	/*
	 * post Mapping for viewInModelDataHotel
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assetcode-modal" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(
					env.getPropertyUrl() + "getAssetCodeById?id=" + id + "&Action=" + "ModelViewAsset",
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
	@PostMapping(value = { "add-assetcode-get-classification-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesSubGroup(Model model,
			@RequestBody String tAccountGroupType, BindingResult result) {
		logger.info("Method : getSalesSubGroup starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "rest-getSalesSubGroup?id=" + tAccountGroupType,
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
		logger.info("Method : getSalesSubGroup ends");
		return res;
	}

}
