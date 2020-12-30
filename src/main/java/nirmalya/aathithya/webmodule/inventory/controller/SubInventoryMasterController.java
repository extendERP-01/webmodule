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
import nirmalya.aathithya.webmodule.inventory.model.SubInventoryMasterModel;

@Controller
@RequestMapping(value = "inventory")
public class SubInventoryMasterController {

	Logger logger = LoggerFactory.getLogger(SubInventoryMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Default Sub-Inventory
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/add-sub-inventory-master")
	public String addSubInventoryMaster(Model model, HttpSession session) {
		logger.info("Method : addSubInventoryMaster starts");

		SubInventoryMasterModel subInventory = new SubInventoryMasterModel();
		SubInventoryMasterModel form = (SubInventoryMasterModel) session.getAttribute("sSubInventory");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("subInventory", form);
		} else {
			model.addAttribute("subInventory", subInventory);
		}

		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addSubInventoryMaster ends");
		return "inventory/add-sub-inventory-master";

	}

	/**
	 * SAVE SUB-INVENTORY
	 * 
	 * @param subInventory
	 * @param model
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-sub-inventory-master" })
	public String addNewStore(@ModelAttribute SubInventoryMasterModel subInventory, Model model, HttpSession session) {
		logger.info("Method : addNewStore starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			subInventory.setCreatedBy(userId);
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "restAddNewSubInventory", subInventory,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("sSubInventory", subInventory);
			logger.info("Method : addNewStore ends");
			return "redirect:add-sub-inventory-master";
		}

		logger.info("Method : addNewStore ends");
		return "redirect:view-sub-inventory-master";
	}

	@GetMapping("/view-sub-inventory-master")
	public String viewSubInventoryMaster(Model model, HttpSession session) {
		logger.info("Method : viewSubInventoryMaster starts");

		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSubInventoryMaster ends");
		return "inventory/view-sub-inventory-master";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-sub-inventory-master-through-ajax")
	public @ResponseBody DataTableResponse viewSubInventoryThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : viewSubInventoryThroughAjax starts");

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

			JsonResponse<List<SubInventoryMasterModel>> jsonResponse = new JsonResponse<List<SubInventoryMasterModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllSubInventoryDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SubInventoryMasterModel> subInventory = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SubInventoryMasterModel>>() {
					});

			String s = "";
			for (SubInventoryMasterModel m : subInventory) {

				byte[] pId = Base64.getEncoder().encode(m.getSubInventoryId().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;";
				s = s + "<a  title='Edit' href='view-sub-inventory-master-edit?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit'></i></a>&nbsp;";
				s = s + "<a href='javascript:void(0)' onclick='deleteSubInventory(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a>&nbsp;";

				m.setAction(s);
				s = "";

				if (m.getSubInvActive().equals("1")) {
					m.setSubInvActive("Active");
				} else {
					m.setSubInvActive("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(subInventory);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSubInventoryThroughAjax ends");
		return response;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/view-sub-inventory-master-modal")
	public @ResponseBody JsonResponse<Object> viewSubInventoryDetailsModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewSubInventoryDetailsModal starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "viewSubInventoryData?id=" + id + "&action=" + "modalViewSubInventory", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : viewSubInventoryDetailsModal ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-sub-inventory-master-delete" })
	public @ResponseBody JsonResponse<Object> deleteSubInventory(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {
		logger.info("Method : deleteSubInventory starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			resp = restClient.getForObject(env.getInventoryUrl() + "deleteSubInventory?id=" + id + "&deletedBy=" + userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteSubInventory ends");
		return resp;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-sub-inventory-master-edit")
	public String editSubInventory(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editSubInventory starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		
		SubInventoryMasterModel subInventory = new SubInventoryMasterModel();
		
		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonResponse<SubInventoryMasterModel> jsonResponse = new JsonResponse<SubInventoryMasterModel>();

		try {
			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "viewSubInventoryData?id=" + id + "&action=" + "editViewSubInventory",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		subInventory = mapper.convertValue(jsonResponse.getBody(), SubInventoryMasterModel.class);
		
		session.setAttribute("message", "");

		model.addAttribute("subInventory", subInventory);

		logger.info("Method : editSubInventory end");
		return "inventory/add-sub-inventory-master";
	}

}
