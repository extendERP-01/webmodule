/**
 * Define ItemServeType Controller
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemServeTypeModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class InventoryItemServeTypeController {

	Logger logger = LoggerFactory.getLogger(InventoryItemSubCategoryController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMApping for Adding new itemServeType
	 *
	 */
	@GetMapping("add-item-servetype")
	public String addItemServeType(Model model, HttpSession session) {

		logger.info("Method : addItemServeType starts");

		InventoryItemServeTypeModel itemServeTypeModel = new InventoryItemServeTypeModel();
		InventoryItemServeTypeModel form = (InventoryItemServeTypeModel) session.getAttribute("sitems");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("itemServeTypeModel", form);
		} else {
			model.addAttribute("itemServeTypeModel", itemServeTypeModel);
		}
		logger.info("Method : addItemServeType ends");

		return "inventory/addItemServeType";
	}

	/*
	 * post Mapping for add ItemServeType
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("add-item-servetype")
	public String addNewItemServeType(@ModelAttribute InventoryItemServeTypeModel itemServeTypeModel, Model model,
			HttpSession session) {

		logger.info("Method : addNewItemServeType starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		try {
			itemServeTypeModel.setCreatedBy("u0001");
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "addNewItemServeType", itemServeTypeModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("itemServeTypeModel", itemServeTypeModel);
			return "redirect:/inventory/add-item-servetype";
		}

		logger.info("Method : addNewItemServeType ends");
		return "redirect:view-item-servetype";

	}

	/*
	 * get Mapping for view ItemServeType
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-item-servetype")
	public String viewItemServeType(Model model, HttpSession session) {

		logger.info("Method : viewItemServeType starts");
		logger.info("Method : viewItemServeType ends");

		return "inventory/viewItemServeType";
	}

	/*
	 * get Mapping for view ItemServeType Through Ajax
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-item-servetype-throughAjax")
	public @ResponseBody DataTableResponse viewItemServeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1 /* , @RequestParam String param2 */) {

		logger.info("Method : viewItemServeThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			// tableRequest.setParam2(param2);

			JsonResponse<List<InventoryItemServeTypeModel>> jsonResponse = new JsonResponse<List<InventoryItemServeTypeModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllItemServeType", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryItemServeTypeModel> itemServeTypeModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryItemServeTypeModel>>() {
					});

			String s = "";

			for (InventoryItemServeTypeModel m : itemServeTypeModel) {

				byte[] pId = Base64.getEncoder().encode(m.getItmServeTypeId().getBytes());

				s = "";
				s = s + "<a href='edit-item-servetype?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
				s = s + "<a href='javascript:void(0)' onclick='DeleteServeType(\"" + m.getItmServeTypeId()
						+ "\")'><i class='fa fa-trash'></i></a> &nbsp;&nbsp;";
				s = s + "<a data-toggle='modal' title='View' href='javascript:void' onclick='viewInModel(\""
						+ m.getItmServeTypeId()
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

				if (m.getItmServeTypeActive()) {
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
			response.setData(itemServeTypeModel);

		} catch (Exception e) {

			e.printStackTrace();
		}

		logger.info("Method : viewItemServeThroughAjax ends");
		return response;
	}

	/*
	 * get Mapping for Delete ItemServeType
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-item-servetype" })
	public @ResponseBody JsonResponse<Object> deleteItemServeType(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteItemServeType Starts");

		String createdBy = "u0001";
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(
					env.getInventoryUrl() + "deleteItemServeType?id=" + id + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteItemServeType ends");

		// System.out.println(resp);
		return resp;

	}

	/*
	 * 
	 * 
	 * GetMApping for Edit ItemServeType
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-item-servetype")

	public String editItemServeType(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editItemServeType starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		InventoryItemServeTypeModel itemServeTypeModel = new InventoryItemServeTypeModel();
		JsonResponse<InventoryItemServeTypeModel> jsonResponse = new JsonResponse<InventoryItemServeTypeModel>();

		try {
			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "getItemServeTypeById?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		itemServeTypeModel = mapper.convertValue(jsonResponse.getBody(), InventoryItemServeTypeModel.class);
		// System.out.println("SubcategoryData : " + itemSubCategoryModel);
		session.setAttribute("message", "");
		model.addAttribute("itemServeTypeModel", itemServeTypeModel);

		logger.info("Method : editItemSubCategory ends");
		return "inventory/addItemServeType";
	}

	/*
	 * Post Mapping for Modalview ItemServeType
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-item-servetype-modal" })
	public @ResponseBody JsonResponse<Object> modalItemServeType(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalItemServeType starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getItemServeTypeById?id=" + index,
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
		logger.info("Method : modalItemServeType ends");
		return res;
	}

}
