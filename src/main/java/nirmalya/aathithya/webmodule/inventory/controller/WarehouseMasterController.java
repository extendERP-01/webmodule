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
import nirmalya.aathithya.webmodule.inventory.model.WarehouseMasterModel;


@Controller
@RequestMapping(value = "inventory")
public class WarehouseMasterController {

	Logger logger = LoggerFactory.getLogger(WarehouseMasterController.class);

	@Autowired
	RestTemplate restClient;
	
	@Autowired
	EnvironmentVaribles env;
	
	
	@GetMapping("/add-warehouse-master")
	public String addWareHouseMaster(Model model, HttpSession session) {
		logger.info("Method : addWareHouseMaster starts");
		
		WarehouseMasterModel wareHouse = new WarehouseMasterModel();
		WarehouseMasterModel form = (WarehouseMasterModel) session.getAttribute("sWarehouse");
		
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("wareHouse", form);
		} else {
			model.addAttribute("wareHouse", wareHouse);
		}
		
		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addWareHouseMaster ends");
		return "inventory/add-warehouse-master";

	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/add-warehouse-master-get-sub-inventory")
	public @ResponseBody JsonResponse<List<DropDownModel>> getSubInventory(Model model, HttpSession session,
			@RequestBody String id) {
		logger.info("Method : getSubInventory starts");
		
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getDropDownSubInventory?id=" + id + "&action=" + "modalViewSubInventory", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getSubInventory ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-warehouse-master" })
	public String addNewWarehouse(@ModelAttribute WarehouseMasterModel wareHouse, Model model, HttpSession session) {
		logger.info("Method : addNewStore starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			wareHouse.setCreatedBy(userId);
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "restAddNewWarehouse", wareHouse,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("sWarehouse", wareHouse);
			logger.info("Method : addNewWarehouse ends");
			return "redirect:add-warehouse-master";
		}

		logger.info("Method : addNewWarehouse ends");
		return "redirect:view-warehouse-master";
	}
	
	@GetMapping("/view-warehouse-master")
	public String viewRackMaster(Model model, HttpSession session) {
		logger.info("Method : viewRackMaster starts");

		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewRackMaster ends");
		return "inventory/view-warehouse-master";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-warehouse-master-through-ajax")
	public @ResponseBody DataTableResponse viewWarehouseThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {
		logger.info("Method : viewWarehouseThroughAjax starts");

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

			JsonResponse<List<WarehouseMasterModel>> jsonResponse = new JsonResponse<List<WarehouseMasterModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllWarehouseDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<WarehouseMasterModel> warehouse = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<WarehouseMasterModel>>() {
					});

			String s = "";
			for (WarehouseMasterModel m : warehouse) {

				byte[] pId = Base64.getEncoder().encode(m.getWarehouseId().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;";
				s = s + "<a  title='Edit' href='view-warehouse-master-edit?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit'></i></a>&nbsp;";
				s = s + "<a href='javascript:void(0)' onclick='deleteWarehouse(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a>&nbsp;";

				m.setAction(s);
				s = "";

				if (m.getWhActive().equals("1")) {
					m.setWhActive("Active");
				} else {
					m.setWhActive("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(warehouse);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewWarehouseThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/view-warehouse-master-modal")
	public @ResponseBody JsonResponse<Object> viewWarehouseDetailsModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewWarehouseDetailsModal starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "viewWarehouseData?id=" + id + "&action=" + "modalViewWarehouse", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : viewWarehouseDetailsModal ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-warehouse-master-delete" })
	public @ResponseBody JsonResponse<Object> deleteWarehouse(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {
		logger.info("Method : deleteWarehouse starts");
		
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
			resp = restClient.getForObject(env.getInventoryUrl() + "deleteWarehouse?id=" + id + "&deletedBy=" + userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteWarehouse ends");
		return resp;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-warehouse-master-edit")
	public String editWarehouse(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editWarehouse starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		
		WarehouseMasterModel wareHouse = new WarehouseMasterModel();
		
		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonResponse<WarehouseMasterModel> jsonResponse = new JsonResponse<WarehouseMasterModel>();

		try {
			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "viewWarehouseData?id=" + id + "&action=" + "editViewWarehouse",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		wareHouse = mapper.convertValue(jsonResponse.getBody(), WarehouseMasterModel.class);
		
		try {
			DropDownModel[] subInv = restClient.getForObject(env.getInventoryUrl() + "editSubInvForWarehouse?id=" + wareHouse.getStore(),
					DropDownModel[].class);
			List<DropDownModel> subInvList = Arrays.asList(subInv);

			model.addAttribute("subInvList", subInvList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		session.setAttribute("message", "");

		model.addAttribute("wareHouse", wareHouse);

		logger.info("Method : editWarehouse end");
		return "inventory/add-warehouse-master";
	}
	
}
