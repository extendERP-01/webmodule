/*
 * 
 */
package nirmalya.aathithya.webmodule.inventory.controller;

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
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemCategoryModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory" })
public class InventoryItemCategoryController {
	Logger logger = LoggerFactory.getLogger(InventoryItemCategoryController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Adding new itemCategory
	 *
	 */
	@GetMapping(value = { "add-item-category" })
	public String addItem(Model model, HttpSession session) {
		logger.info("Method : addItem starts");
		InventoryItemCategoryModel itemCategoryModel = new InventoryItemCategoryModel();
		InventoryItemCategoryModel icat = (InventoryItemCategoryModel) session.getAttribute("sitemcategory");
		String message = (String) session.getAttribute("message");
		
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (icat != null) {
			model.addAttribute("itemCategoryModel", icat);
		} else {
			model.addAttribute("itemCategoryModel", itemCategoryModel);
		}
		logger.info("Method : addItem end");
		return "inventory/addItemCategory";
	}
	/*
	 * post Mapping for add ItemCategory
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-item-category" })
	public String addNewItemCategory(@ModelAttribute InventoryItemCategoryModel itemCategoryModel, Model model,
			HttpSession session) {
		logger.info("Method : addNewItemCategory starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			
		}
		try {
			itemCategoryModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "rest-addnew-itemcategory", itemCategoryModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("sitemcategory", itemCategoryModel);
			return "redirect:add-item-category";
		}
		logger.info("Method : addNewItemCategory end");
		return "redirect:view-item-category";
	}

	/*
	 * 
	 * GetMApping For Listing itemCategory
	 * 
	 * 
	 */
	@GetMapping(value = { "view-item-category" })
	public String viewItem(Model model) {
		logger.info("Method : view item starts");
		JsonResponse<Object> item = new JsonResponse<Object>();
		model.addAttribute("item", item);
		logger.info("Method : view item end");
		return "inventory/viewItemCategory";
	}

	/*
	 * view Through ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-item-category-throughAjax" })
	public @ResponseBody DataTableResponse viewItemCategory(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewItemCategory starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<InventoryItemCategoryModel>> jsonResponse = new JsonResponse<List<InventoryItemCategoryModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-item-category",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryItemCategoryModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryItemCategoryModel>>() {
					});
			String s = "";
			for (InventoryItemCategoryModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getItmCategory().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+  new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-item-category?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp; ";
				s = s + "<a href='javascript:void(0)' onclick='deleteItemCategory(\"" +  new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getItmCatActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewItemCategory ends");
		return response;
	}

	/**
	 * View selected ItemCategory in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-item-category-model" })
	public @ResponseBody JsonResponse<InventoryItemCategoryModel> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<InventoryItemCategoryModel> res = new JsonResponse<InventoryItemCategoryModel>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-item-category-byId?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modelView ends");
		return res;
	}

	/*
	 * 
	 * GetMapping for delete itemCategory of inventory
	 * 
	 */

		@SuppressWarnings("unchecked")
		@GetMapping(value = { "delete-item-category" })
		public @ResponseBody JsonResponse<Object> deleteItemCategory(Model model, @RequestParam("id") String encodeId, HttpSession session) {
			
			logger.info("Method : deleteItemCategory starts");
			byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

			String id = (new String(encodeByte));
			String userId = "";
			try {
				userId = (String)session.getAttribute("USER_ID");
			}catch(Exception e) {
				
			}
			JsonResponse<Object> resp = new JsonResponse<Object>();
		
			try {
				resp = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "delete-item-category?id=" + id+"&createdBy="+userId, JsonResponse.class);

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
			logger.info("Method : deleteItemCategory ends");
			return resp;
		}
		
	/*
	 * 
	 * 
	 * GetMApping for Edit ItemCAtegory
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "edit-item-category" })
	public String editItemCategory(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editItemCategory starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		InventoryItemCategoryModel itemCategoryModel = new InventoryItemCategoryModel();
		JsonResponse<InventoryItemCategoryModel> jsonResponse = new JsonResponse<InventoryItemCategoryModel>();

		try {
			jsonResponse = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-item-category-byId?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		itemCategoryModel = mapper.convertValue(jsonResponse.getBody(), InventoryItemCategoryModel.class);
		session.setAttribute("message", "");
		model.addAttribute("itemCategoryModel", itemCategoryModel);
		logger.info("Method : editItemCategory ends");
		return "inventory/addItemCategory";
	}
}