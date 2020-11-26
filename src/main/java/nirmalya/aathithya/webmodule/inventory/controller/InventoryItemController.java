/**
 * Defines Inventory related method call for Item
 */
package nirmalya.aathithya.webmodule.inventory.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory/" })
public class InventoryItemController {
	Logger logger = LoggerFactory.getLogger(InventoryItemController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Adding new items
	 *
	 */
	@GetMapping(value = { "add-items" })
	public String addItems(Model model, HttpSession session) {
		logger.info("Method : addItems starts");
		InventoryItemModel itemModel = new InventoryItemModel();
		InventoryItemModel item = (InventoryItemModel) session.getAttribute("sitem");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (item != null) {
			model.addAttribute("itemModel", item);
			session.setAttribute("itemModel", null);
		} else {
			model.addAttribute("itemModel", itemModel);
		}

		/*
		 * dropDown for item Type add-items-getSubGroup
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "rest-get-itemType", DropDownModel[].class);
			List<DropDownModel> itemGroupList = Arrays.asList(dropDownModel);
			model.addAttribute("itemGroupList", itemGroupList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for sac codes
		 */
		try {
			DropDownModel[] sacCodes = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-saccode-for-invitems", DropDownModel[].class);
			List<DropDownModel> sacCodeList = Arrays.asList(sacCodes);
			model.addAttribute("sacCodeList", sacCodeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for itemCategory
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for production Type List
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-productiontype", DropDownModel[].class);
			List<DropDownModel> productionList = Arrays.asList(dropDownModel);
			model.addAttribute("productionList", productionList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Serve Type
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "rest-get-serveType", DropDownModel[].class);
			List<DropDownModel> serveTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serveTypeList", serveTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Service Type
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-serviceType", DropDownModel[].class);
			List<DropDownModel> serviceTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serviceTypeList", serviceTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addItems ends");
		return "inventory/addItem";
	}

	/*
	 * post Mapping for add Items
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-items" })
	public @ResponseBody JsonResponse<Object> postAddItems(@RequestBody List<InventoryItemModel> itemModel, Model model,
			HttpSession session) {
		logger.info("Method : postAddItems starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		String imageName = null;
		byte[] bytes = (byte[]) session.getAttribute("ItemImageFile");
		if (bytes != null) {
			long nowTime = new Date().getTime();
			imageName = nowTime + ".png";
			itemModel.get(0).setItemImg(imageName);
			System.out.println("image name = " + imageName);
			session.setAttribute("ItemImageFile", null);
		} else {
			imageName = (String) session.getAttribute("imageNameFromDnForEdit");
			itemModel.get(0).setItemImg(imageName);
			session.setAttribute("imageNameFromDnForEdit", null);
		}

		try {
			itemModel.get(0).setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-addNew-item",
					itemModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if ((jsonResponse.getCode() == null || jsonResponse.getCode() == "")
				&& (jsonResponse.getMessage() == null || jsonResponse.getMessage() == "")) {

			Path path = Paths.get(environmentVaribles.getFileUploadItem() + imageName);
			if (bytes != null) {
				try {
					Files.write(path, bytes);
				} catch (IOException e1) {

					e1.printStackTrace();
				}

				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				Integer height = 50;
				Integer width = 50;

				try {
					BufferedImage img = ImageIO.read(in);
					if (height == 0) {
						height = (width * img.getHeight()) / img.getWidth();
					}
					if (width == 0) {
						width = (height * img.getWidth()) / img.getHeight();
					}

					Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
					BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					ImageIO.write(imageBuff, "jpg", buffer);

					byte[] thumb = buffer.toByteArray();
					Path pathThumb = Paths.get(environmentVaribles.getFileUploadItem() + "thumb\\" + imageName);
					Files.write(pathThumb, thumb);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		if (jsonResponse.getMessage() != "") {
			jsonResponse.setMessage("Failed");
		} else {
			jsonResponse.setMessage("Success");
		}
		logger.info("Method : postAddItems ends");
		return jsonResponse;
	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-items-getSubCategory-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getItemCategory(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : getItemCategory starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-itemSubCategory?id=" + itemCategory,
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
		logger.info("Method : getItemCategory ends");
		return res;
	}

	/*
	 * 
	 * GetMapping For Listing Items
	 * 
	 * 
	 */
	@GetMapping(value = { "view-items" })
	public String viewItems(Model model) {
		logger.info("Method : viewItems starts");
		/*
		 * dropDown of itemName for search param1
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "param-itemName", DropDownModel[].class);
			List<DropDownModel> itemNameList = Arrays.asList(dropDownModel);
			model.addAttribute("itemNameList", itemNameList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value of Serve Type for search Param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "param-serveType", DropDownModel[].class);
			List<DropDownModel> serveTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serveTypeList", serveTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value of Serve Type for search Param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "param-itemCategory", DropDownModel[].class);
			List<DropDownModel> categoryList = Arrays.asList(dropDownModel);
			model.addAttribute("categoryList", categoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value of Serve Type for search Param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "param-storeName", DropDownModel[].class);
			List<DropDownModel> storeName = Arrays.asList(dropDownModel);
			model.addAttribute("storeName", storeName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		JsonResponse<Object> vendors = new JsonResponse<Object>();
		model.addAttribute("vendors", vendors);
		logger.info("Method : viewItems ends");
		return "inventory/viewItem";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-items-throughAjax" })
	public @ResponseBody DataTableResponse viewItemListThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {
		logger.info("Method : viewItemListThroughAjax starts");
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
			JsonResponse<List<InventoryItemModel>> jsonResponse = new JsonResponse<List<InventoryItemModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-Items",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryItemModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryItemModel>>() {
					});
			String s = "";
			for (InventoryItemModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getItem().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-item?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp; ";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getItemActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
				if (m.gettPurchaseSubGroup() == null) {
					m.settPurchaseSubGroup("NA");
				}
				if (m.getTsalesSubGroup() == null) {
					m.setTsalesSubGroup("NA");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewItemListThroughAjax ends");
		return response;
	}

	/*
	 * Delete Item
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-item-byId" })
	public @ResponseBody JsonResponse<Object> deleteItem(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : deleteItem starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "delete-item?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : deleteItem ends");
		return resp;
	}

	/*
	 * 
	 * 
	 * GetMapping for Edit Item
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "edit-item" })
	public String editItem(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editItem starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		List<InventoryItemModel> itemModel = new ArrayList<InventoryItemModel>();
		JsonResponse<List<InventoryItemModel>> jsonResponse = new JsonResponse<List<InventoryItemModel>>();

		try {
			jsonResponse = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "edit-item-byId?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();

		itemModel = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<InventoryItemModel>>() {
		});

		// itemModel = mapper.convertValue(jsonResponse.getBody(), new
		// TypeReference<List<InventoryItemModel>>());
		session.setAttribute("message", "");
		if (itemModel.get(0).getItemImg() == null || itemModel.get(0).getItemImg() == "") {
			itemModel.get(0).setItemImg(null);
		} else {
			String image = itemModel.get(0).getItemImg();
			session.setAttribute("imageNameFromDnForEdit", image);
			String variable = environmentVaribles.getBaseUrlPath();
			model.addAttribute("image", variable + "document/item/" + image + "");
			model.addAttribute("image", variable + "document/item/" + image + "");
			model.addAttribute("action", variable + "document/item/thumb/" + image + "");

		}

		model.addAttribute("itemModel", itemModel);

		/*
		 * dropDown for item Type add-items-getSubGroup-throughAjax
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "rest-get-itemType", DropDownModel[].class);
			List<DropDownModel> itemGroupList = Arrays.asList(dropDownModel);
			model.addAttribute("itemGroupList", itemGroupList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for production Type List
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-productiontype", DropDownModel[].class);
			List<DropDownModel> productionList = Arrays.asList(dropDownModel);
			model.addAttribute("productionList", productionList);
			System.out.println("productionList" + productionList);
			model.addAttribute("Edit", "For Edit");
			System.out.println("itemModel" + itemModel);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for sac codes
		 */
		try {
			DropDownModel[] sacCodes = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-saccode-for-invitems", DropDownModel[].class);
			List<DropDownModel> sacCodeList = Arrays.asList(sacCodes);
			model.addAttribute("sacCodeList", sacCodeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for itemCategory
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for itemSubCategory
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
					+ "get-edit-item-itemSubCategory?id=" + itemModel.get(0).getItemCategory(), DropDownModel[].class);
			List<DropDownModel> itemSubCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemSubCategoryList", itemSubCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Serve Type
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "rest-get-serveType", DropDownModel[].class);
			List<DropDownModel> serveTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serveTypeList", serveTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Service Type
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-serviceType", DropDownModel[].class);
			List<DropDownModel> serviceTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serviceTypeList", serviceTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Purchase Sub Group
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
					+ "get-edit-item-purchase?id=" + itemModel.get(0).gettAccountGroupType(), DropDownModel[].class);
			List<DropDownModel> purchaseEditList = Arrays.asList(dropDownModel);
			model.addAttribute("purchaseEditList", purchaseEditList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Sales Sub Group
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
					+ "get-edit-item-sales?id=" + itemModel.get(0).gettAccountGroupType(), DropDownModel[].class);
			List<DropDownModel> getsalesSubGroupList = Arrays.asList(dropDownModel);
			model.addAttribute("getsalesSubGroupList", getsalesSubGroupList);

		} catch (RestClientException e) {
			e.printStackTrace();

		}
		logger.info("Method : editItem ends");
		return "inventory/addItem";
	}

	/**
	 * View selected Item in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-items-model" })
	public @ResponseBody JsonResponse<Object> modelviewOfItem(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewOfItem starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-item-forModel?id=" + index,
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
		logger.info("Method : modelviewOfItem ends");
		return res;
	}

	/*
	 * Method For Pdf
	 * 
	 */
	@GetMapping("/view-items-generate-report")
	public String generateItemReport(Model model, HttpSession session) {

		logger.info("Method : generateItemReport starts");
		/*
		 * dropDown of itemName for search param1
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "param-itemName", DropDownModel[].class);
			List<DropDownModel> itemNameList = Arrays.asList(dropDownModel);
			model.addAttribute("itemNameList", itemNameList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value of Serve Type for search Param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "param-serveType", DropDownModel[].class);
			List<DropDownModel> serveTypeList = Arrays.asList(dropDownModel);
			model.addAttribute("serveTypeList", serveTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value of Serve Type for search Param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-itemCategory", DropDownModel[].class);
			List<DropDownModel> categoryList = Arrays.asList(dropDownModel);
			model.addAttribute("categoryList", categoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			String userId = (String) session.getAttribute("USER_ID");
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-store?userId=" + userId, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dropDownModel);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateItemReport ends");
		return "inventory/generateItemReport";
	}

	/*
	 * 
	 * Method for autoComplete
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-items-getItemNameAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getItemNameList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getItemNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getItemNameListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getItemNameList ends");
		return res;
	}

	/*
	 * 
	 * Method for autoComplete
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-items-generateItemNameAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> generateItemNameList(@RequestParam String searchValue,@RequestParam String icat ) {
		logger.info("Method : generateItemNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl()
					+ "generateItemNameListByAutoSearch?id=" + searchValue+"&icat="+icat, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : generateItemNameList ends");
		return res;
	}

	/*
	 * gget Purchase Subgroup by the onChange of AccountSubGroup selected in addItem
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-items-getPurchaseSubGroup-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseSubGroup(Model model,
			@RequestBody String tAccountGroupType, BindingResult result) {
		logger.info("Method : getPurchaseSubGroup starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-getPurchaseSubGroup?id=" + tAccountGroupType,
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
		logger.info("Method : getPurchaseSubGroup ends");
		return res;
	}

	/*
	 * 
	 * get Sales Subgroup by the onChange of AccountSubGroup selected in addItem
	 * form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-items-getSalesSubGroup-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesSubGroup(Model model,
			@RequestBody String tAccountGroupType, BindingResult result) {
		logger.info("Method : getSalesSubGroup starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-getSalesSubGroup?id=" + tAccountGroupType,
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

	/**
	 * get getPipeSize by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-items-getPipe-size-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getPipeSize(@RequestParam String itemCategory,
			@RequestParam String itemSubCategory) {
		logger.info("Method : getPipeSize starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "rest-get-itemPipeSize?id="
					+ itemCategory + "&itemSubCategory=" + itemSubCategory, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getPipeSize ends");
		return res;
	}

	/*
	 * for file upload
	 */
	@PostMapping("/add-items-uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("ItemImageFile", inputFile.getBytes());
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

}
