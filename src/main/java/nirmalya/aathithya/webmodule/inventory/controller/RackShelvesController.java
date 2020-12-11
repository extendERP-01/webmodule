package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList;
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
import nirmalya.aathithya.webmodule.inventory.model.AssignedItemModel;
import nirmalya.aathithya.webmodule.inventory.model.RackShelvesModel;

@Controller
@RequestMapping(value = "inventory")
public class RackShelvesController {

	Logger logger = LoggerFactory.getLogger(RackShelvesController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/assign-item-to-rack")
	public String defaultAssignItemToRack(Model model, HttpSession session) {
		logger.info("Method : defaultAssignItemToRack starts");

		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultAssignItemToRack ends");
		// return "inventory/assign-item-to-rack";
		return "inventory/assign-item-to-shelfs";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/assign-item-to-rack-get-sub-inventory")
	public @ResponseBody JsonResponse<List<DropDownModel>> getSubInventory(Model model, HttpSession session,
			@RequestBody String id) {
		logger.info("Method : getSubInventory starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getDropDownSubInventory?id=" + id,
					JsonResponse.class);
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
	@PostMapping("/assign-item-to-rack-get-warehouse")
	public @ResponseBody JsonResponse<List<DropDownModel>> getWarehouse(Model model, HttpSession session,
			@RequestBody String id) {
		logger.info("Method : getWarehouse starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getDropDownWarehouse?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getWarehouse ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/assign-item-to-rack-get-rack")
	public @ResponseBody JsonResponse<List<DropDownModel>> getRack(Model model, HttpSession session,
			@RequestBody String id) {
		logger.info("Method : getWarehouse starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getDropDownRack?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getWarehouse ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/assign-item-to-rack-get-shelfs")
	public @ResponseBody JsonResponse<List<DropDownModel>> getShelfs(Model model, HttpSession session,
			@RequestBody String id) {
		logger.info("Method : getShelfs starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getDropDownShelfs?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getShelfs ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/assign-item-to-rack-get-item")
	public @ResponseBody JsonResponse<List<DropDownModel>> getItem(Model model, HttpSession session,
			@RequestBody String id) {
		logger.info("Method : getItem starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getDropDownItem", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getItem ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/assign-item-to-rack-get-previous-entry-details")
	public @ResponseBody JsonResponse<List<RackShelvesModel>> getPreviousDetails(Model model, HttpSession session,
			@RequestParam String whId, @RequestParam String store, @RequestParam String subInventoryId) {
		logger.info("Method : getPreviousDetails starts");

		JsonResponse<List<RackShelvesModel>> res = new JsonResponse<List<RackShelvesModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPreviousDetails?whId=" + whId + "&storeId="
					+ store + "&subInvId=" + subInventoryId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		ObjectMapper mapper = new ObjectMapper();
		List<RackShelvesModel> rack = mapper.convertValue(res.getBody(), new TypeReference<List<RackShelvesModel>>() {
		});
		List<DropDownModel> shelvesList = null;
		List<DropDownModel> RackList = null;
		try {
			DropDownModel[] Rack = restClient.getForObject(env.getInventoryUrl() + "editRackByWareHouse?id=" + whId,
					DropDownModel[].class);
			RackList = Arrays.asList(Rack);

			model.addAttribute("RackList", RackList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] shelves = restClient.getForObject(env.getInventoryUrl() + "editShelves",
					DropDownModel[].class);
			shelvesList = Arrays.asList(shelves);

			model.addAttribute("shelvesList", shelvesList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<List<DropDownModel>> ress = new JsonResponse<List<DropDownModel>>();

		try {
			ress = restClient.getForObject(env.getInventoryUrl() + "getDropDownItem", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		List<DropDownModel> Item = mapper.convertValue(ress.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		for (RackShelvesModel a : rack) {
			a.setRackList(RackList);
			a.setShelvesList(shelvesList);
			a.setItemList(Item);
		}
		res.setBody(rack);
		logger.info("Method : getPreviousDetails ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/assign-item-to-rack-save")
	public @ResponseBody JsonResponse<Object> assignItemToShelves(Model model, HttpSession session,
			@RequestBody List<RackShelvesModel> assigneItem) {
		logger.info("Method : assignItemToShelves starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {

			for (RackShelvesModel m : assigneItem) {
				m.setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getInventoryUrl() + "assignItemToShelves", assigneItem,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : assignItemToShelves ends");
		return res;
	}

	@GetMapping("/view-assigned-item-to-rack")
	public String defaultViewAssignedItemToRack(Model model, HttpSession session) {
		logger.info("Method : defaultViewAssignedItemToRack starts");

		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultViewAssignedItemToRack ends");
		return "inventory/view-assigned-item-to-rack";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-assigned-item-to-rack-through-ajax")
	public @ResponseBody DataTableResponse viewAssignedItemThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {
		logger.info("Method : viewAssignedItemThroughAjax starts");

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

			JsonResponse<List<RackShelvesModel>> jsonResponse = new JsonResponse<List<RackShelvesModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllRackDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<RackShelvesModel> rack = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<RackShelvesModel>>() {
					});

			String s = "";
			for (RackShelvesModel m : rack) {

				byte[] pId = Base64.getEncoder().encode(m.getRackId().getBytes());
				byte[] storeId = Base64.getEncoder().encode(m.getStore().getBytes());
				byte[] subId = Base64.getEncoder().encode(m.getSubInventory().getBytes());
				byte[] whId = Base64.getEncoder().encode(m.getWarehouse().getBytes());

				s = "";
//				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
//						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;";

				s = s + "<a  title='Edit' href='view-assigned-item-to-rack-edit?id=" + new String(pId) + "&storeId="
						+ new String(storeId) + "&subId=" + new String(subId) + "&whId=" + new String(whId)
						+ "' '><i class='fa fa-edit edit'></i></a>&nbsp;";

//				s = s + "<a href='javascript:void(0)' onclick='deleteWarehouse(\"" + new String(pId)
//						+ "\")'><i class='fa fa-trash'></i></a>&nbsp;";

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(rack);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewAssignedItemThroughAjax ends");
		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-assigned-item-to-rack-edit")
	public String editAssignedItem(Model model, @RequestParam("id") String encodeId,
			@RequestParam("storeId") String encodestoreId, @RequestParam("subId") String encodesubId,
			@RequestParam("whId") String encodewhId, HttpSession session) {
		logger.info("Method : editAssignedItem starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		byte[] encodeByte1 = Base64.getDecoder().decode(encodestoreId.getBytes());
		String storeId = (new String(encodeByte1));
		byte[] encodeByte2 = Base64.getDecoder().decode(encodestoreId.getBytes());
		String subId = (new String(encodeByte2));
		byte[] encodeByte3 = Base64.getDecoder().decode(encodewhId.getBytes());
		String whId = (new String(encodeByte3));

		List<RackShelvesModel> rackDtls = new ArrayList<RackShelvesModel>();

		try {
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getStoreForSubInv",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);

			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<List<RackShelvesModel>> jsonResponse = new JsonResponse<List<RackShelvesModel>>();

		try {
			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "editRackDetails?id=" + id + "&storeId="
					+ storeId + "&subId=" + subId + "&whId" + whId, JsonResponse.class);
			model.addAttribute("id", "id");
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		rackDtls = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<RackShelvesModel>>() {
		});
		try {
			DropDownModel[] subInv = restClient.getForObject(
					env.getInventoryUrl() + "editSubInvForWarehouse?id=" + rackDtls.get(0).getStore(),
					DropDownModel[].class);
			List<DropDownModel> subInvList = Arrays.asList(subInv);

			model.addAttribute("subInvList", subInvList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] warehouse = restClient.getForObject(
					env.getInventoryUrl() + "editWarehouseForRack?id=" + rackDtls.get(0).getSubInventory(),
					DropDownModel[].class);
			List<DropDownModel> warehouseList = Arrays.asList(warehouse);

			model.addAttribute("warehouseList", warehouseList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] Rack = restClient.getForObject(
					env.getInventoryUrl() + "editRackByWareHouse?id=" + rackDtls.get(0).getWarehouse(),
					DropDownModel[].class);
			List<DropDownModel> RackList = Arrays.asList(Rack);

			model.addAttribute("RackList", RackList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * try { DropDownModel[] shelves = restClient.getForObject(
		 * env.getInventoryUrl() + "editShelvesByRack?id=" +
		 * rackDtls.get(0).getRackId(), DropDownModel[].class); List<DropDownModel>
		 * shelvesList = Arrays.asList(shelves);
		 * 
		 * model.addAttribute("shelvesList", shelvesList); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		List<DropDownModel> shelvesList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] shelves = restClient.getForObject(env.getInventoryUrl() + "editShelves",
					DropDownModel[].class);
			shelvesList = Arrays.asList(shelves);

			model.addAttribute("shelvesList", shelvesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<DropDownModel> itemList = new ArrayList<DropDownModel>();
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getDropDownItem", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		itemList = mapper.convertValue(res.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("itemList", itemList);

		List<Object> selectedItemList = new ArrayList<Object>();
		List<Object> selItemList = new ArrayList<Object>();
		try {

			for (RackShelvesModel a : rackDtls) {
				AssignedItemModel[] selectedItem = restClient.getForObject(
						env.getInventoryUrl() + "getSelectedItem?id=" + a.getShelf(), AssignedItemModel[].class);
				List<AssignedItemModel> sItemList = Arrays.asList(selectedItem);
				List<String> newItemList = new ArrayList<String>();
				for (AssignedItemModel m : sItemList) {
					newItemList.add(m.getKey());
				}
				selectedItemList.add(newItemList);
				selItemList.add(sItemList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("selectedItemList", selectedItemList);
		model.addAttribute("selItemList", selItemList);

		session.setAttribute("message", "");

		if (rackDtls.size() > 0) {
			model.addAttribute("rackId", rackDtls.get(0).getRackId());
//			double db = (double) rackDtls.get(0).getShelvesCapacity();
//			int cp = (int) db;
//			model.addAttribute("shelvesCapacity", cp);
		} else {
			model.addAttribute("rackId", "");
//			model.addAttribute("shelvesCapacity", "");
		}

		model.addAttribute("rackDtls", rackDtls);

		logger.info("Method : editAssignedItem end");
		return "inventory/assign-item-to-shelfs";
	}
}
