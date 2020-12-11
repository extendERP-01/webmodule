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
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemSubCategoryModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class InventoryItemSubCategoryController {

	Logger logger = LoggerFactory.getLogger(InventoryItemSubCategoryController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMApping for Adding new itemSubCategory
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/add-item-subcategory")
	public String addItemSubCategory(Model model, HttpSession session) {
		logger.info("Method : addItemSubCategory starts");

		try {
			DropDownModel[] inventory = restClient.getForObject(env.getInventoryUrl() + "get-itemCategory",
					DropDownModel[].class);
			List<DropDownModel> tblmstr = Arrays.asList(inventory);

			model.addAttribute("tblmstr", inventory);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		// DRop DowN endS

		InventoryItemSubCategoryModel itemSubCategoryModel = new InventoryItemSubCategoryModel();
		InventoryItemSubCategoryModel form = (InventoryItemSubCategoryModel) session.getAttribute("sitems");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("itemSubCategoryModel", form);
		} else {
			model.addAttribute("itemSubCategoryModel", itemSubCategoryModel);
		}

		logger.info("Method : addItemSubCategory ends");

		return "inventory/addItemSubCategory";

	}

	/*
	 * post Mapping for add ItemSubCategory
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("add-item-subcategory")
	public String addNewItemSubCategory(@ModelAttribute InventoryItemSubCategoryModel itemSubCategoryModel, Model model,
			HttpSession session) {

		logger.info("Method : addItemSubCategory starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			
		}
		try {
			itemSubCategoryModel.setCreatedBy(userId);
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "rest-addNew-item-SubCategory",
					itemSubCategoryModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("itemSubCategoryModel", itemSubCategoryModel);
			return "redirect:/inventory/add-item-subcategory";
		}

		logger.info("Method : addItemSubCategory ends");
		return "redirect:/inventory/view-item-subcategory";
	}

	/*
	 * get Mapping for view ItemSubCategory
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-item-subcategory")
	public String viewSubCategory(Model model, HttpSession session) {

		logger.info("Method : viewSubCategory starts");

		try {
			DropDownModel[] inventory = restClient.getForObject(env.getInventoryUrl() + "get-itemCategory",
					DropDownModel[].class);
			List<DropDownModel> tblmstr = Arrays.asList(inventory);

			model.addAttribute("tblmstr", inventory);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSubCategory ends");

		return "inventory/viewItemSubCategory";
	}

	/*
	 * get Mapping for view ItemSubCategory Through Ajax
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-item-subcategory-throughAjax")
	public @ResponseBody DataTableResponse viewSubCatThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewFloorThroughAjax starts");

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

			JsonResponse<List<InventoryItemSubCategoryModel>> jsonResponse = new JsonResponse<List<InventoryItemSubCategoryModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-All-Item-SubCategory", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryItemSubCategoryModel> itemSubCategory = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryItemSubCategoryModel>>() {
					});

			String s = "";

			for (InventoryItemSubCategoryModel m : itemSubCategory) {

				byte[] pId = Base64.getEncoder().encode(m.getItmSubCategoryId().getBytes());

				s = "";
				s = s + "<a href='edit-item-subcategory?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
				s = s + "<a href='javascript:void(0)' onclick='DeleteSubCategory(\"" + m.getItmSubCategoryId()
						+ "\")'><i class='fa fa-trash'></i></a> &nbsp;&nbsp;";
				s = s + "<a data-toggle='modal' title='View' href='javascript:void' onclick='viewInModel(\""
						+ m.getItmSubCategoryId()
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

				if (m.getItmSubActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("InActive");
				}

				/*
				 * s = s + "<a href='deleteFloor?id="+m.getFloorId()+"' >Delete</a>";
				 * m.setDelete(s); s = "";
				 */
			}
			// System.out.println("Total=="+jsonResponse.getTotal());
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(itemSubCategory);

		} catch (Exception e) {

			e.printStackTrace();
		}

		logger.info("Method : viewFloorThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * 
	 * GetMApping for Edit ItemSubCategory
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-item-subcategory")
	public String editItemSubCategory(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editItemSubCategory starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		InventoryItemSubCategoryModel itemSubCategoryModel = new InventoryItemSubCategoryModel();
		JsonResponse<InventoryItemSubCategoryModel> jsonResponse = new JsonResponse<InventoryItemSubCategoryModel>();

		try {
			DropDownModel[] inventory = restClient.getForObject(env.getInventoryUrl() + "get-itemCategory",
					DropDownModel[].class);
			List<DropDownModel> tblmstr = Arrays.asList(inventory);

			model.addAttribute("tblmstr", inventory);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "get-item-sub-category-byId?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper1 = new ObjectMapper();
		itemSubCategoryModel = mapper1.convertValue(jsonResponse.getBody(), InventoryItemSubCategoryModel.class);
		// System.out.println("SubcategoryData : " + itemSubCategoryModel);
		session.setAttribute("message", "");
		model.addAttribute("itemSubCategoryModel", itemSubCategoryModel);

		logger.info("Method : editItemSubCategory ends");
		return "inventory/addItemSubCategory";
	}

	/*
	 * Post Mapping for Modalview ItemSubCategory
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-item-subcategory-modal" })
	public @ResponseBody JsonResponse<Object> modalItemSub(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalItemSub starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-item-sub-category-byId?id=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			// System.out.println("if block getmsg() not false : " + res.getMessage());
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		// System.out.println("response from rest :" + res);
		logger.info("Method : modalItemSub ends");
		return res;
	}

	/*
	 * get Mapping for Delete ItemSubCategory
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-item-sub-category" })
	public @ResponseBody JsonResponse<Object> deleteItemSubCategory(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteItemSubCategory Starts");

		String userId = "";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(
					env.getInventoryUrl() + "delete-item-sub-category?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : deleteItemSubCategory ends");

		// System.out.println(resp);
		return resp;

	}

}
