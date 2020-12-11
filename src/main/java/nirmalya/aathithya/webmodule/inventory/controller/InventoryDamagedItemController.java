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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryDamagedItemModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory/" })
public class InventoryDamagedItemController {
	Logger logger = LoggerFactory.getLogger(InventoryDamagedItemController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Adding new items
	 *
	 */
	@GetMapping(value = { "add-damaged-items" })
	public String addDamagedItems(Model model, HttpSession session) {
		logger.info("Method : addDamagedItems starts");
		InventoryDamagedItemModel damagedItem = new InventoryDamagedItemModel();
		InventoryDamagedItemModel damagedItemSession = (InventoryDamagedItemModel) session.getAttribute("sitem");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (damagedItemSession != null) {
			model.addAttribute("damagedItemSession", damagedItemSession);
			session.setAttribute("damagedItem", null);
		} else {
			model.addAttribute("damagedItem", damagedItem);
		}
		/*
		 * dropDown for vendor name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-damagedItem-vendorName", DropDownModel[].class);
			List<DropDownModel> vendorName = Arrays.asList(dropDownModel);
			model.addAttribute("vendorName", vendorName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addDamagedItems ends");
		return "inventory/addDamagedItem";
	}

	/*
	 * post Mapping for add Damaged Item
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-damaged-items", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addNewDamagedItems(
			@RequestBody List<InventoryDamagedItemModel> damagedItem, Model model, HttpSession session) {
		logger.info("Method : addNewDamagedItems function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		try {
			damagedItem.get(0).settCreatedBy(userId);
			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-addNew-damaged-item",
					damagedItem, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addNewDamagedItems function Ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-damaged-items-get-item" })
	public @ResponseBody JsonResponse<List<DropDownModel>> getInventoryItemAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getInventoryItemAutoSearchList starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getInventoryItemAutoSearchList?id=" + searchValue,
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

		logger.info("Method : getInventoryItemAutoSearchList ends");
		return res;
	}

	/*
	 * 
	 * GetMapping For Listing Items
	 * 
	 * 
	 */
	@GetMapping(value = { "view-damaged-items" })
	public String viewDamagedItems(Model model) {
		logger.info("Method : viewDamagedItems starts");
		/*
		 * dropDown for vendor name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-damagedItem-vendorName", DropDownModel[].class);
			List<DropDownModel> vendorName = Arrays.asList(dropDownModel);
			model.addAttribute("vendorName", vendorName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		JsonResponse<Object> damagedItem = new JsonResponse<Object>();
		model.addAttribute("damagedItem", damagedItem);
		logger.info("Method : viewDamagedItems ends");
		return "inventory/viewDamagedItem";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-damaged-items-throughAjax" })
	public @ResponseBody DataTableResponse viewDamagedItemsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : viewDamagedItemsThroughAjax starts");
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
			JsonResponse<List<InventoryDamagedItemModel>> jsonResponse = new JsonResponse<List<InventoryDamagedItemModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "view-damaged-item-list",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryDamagedItemModel> damagedItem = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryDamagedItemModel>>() {
					});
			String s = "";
			for (InventoryDamagedItemModel m : damagedItem) {
				byte[] pId = Base64.getEncoder().encode(m.gettDamagedItemId().toString().getBytes());
				// byte[] sId =
				// Base64.getEncoder().encode(m.getStatus().toString().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>";
				s = s + " &nbsp;&nbsp <a href='view-damaged-items-edit?id=" + new String(pId)
						+ "' ><i class='fa fa-edit' style=\"font-size:24px\"></i></a> &nbsp;&nbsp; ";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'style=\"font-size:24px\"></i></a> ";
				if (m.gettStatus() == true) {

					s = s + "&nbsp; &nbsp; <a href='javascript:void(0)'" + "' onclick='ChangeToScraped(\""
							+ new String(pId)
							+ "\")' ><i class=\"fa fa-check-circle\" title=\"Scraped\" style=\"font-size:24px;color:#090\"></i></a>";
				}
				if (m.gettStatus() == false) {

					s = s + "&nbsp; &nbsp; <a href='javascript:void(0)'" + "' onclick='ChangeToReSale(\""
							+ new String(pId)
							+ "\")' ><i class=\"fa fa-check-circle\" title=\"ReSale\" style=\"font-size:24px;color:#006\"></i></a>";
				}
				m.setAction(s);
				s = "";

				if (m.gettStatus()) {
					m.setStatus("ReSale");
				} else {
					m.setStatus("Scraped");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(damagedItem);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewDamagedItemsThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * 
	 * GetMapping for Edit Damaged Item
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-damaged-items-edit" })
	public String editDamagedItem(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editDamagedItem starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		InventoryDamagedItemModel damagedItem = new InventoryDamagedItemModel();
		JsonResponse<InventoryDamagedItemModel> jsonResponse = new JsonResponse<InventoryDamagedItemModel>();

		try {
			jsonResponse = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "edit-damagedItem-byId?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();

		damagedItem = mapper.convertValue(jsonResponse.getBody(), InventoryDamagedItemModel.class);
		session.setAttribute("message", "");
		model.addAttribute("damagedItem", damagedItem);
		model.addAttribute("id", damagedItem.gettDamagedItemId());

		/*
		 * dropDown for vendor name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-damagedItem-vendorName", DropDownModel[].class);
			List<DropDownModel> vendorName = Arrays.asList(dropDownModel);
			model.addAttribute("vendorName", vendorName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : editDamagedItem ends");
		return "inventory/addDamagedItem";
	}

	/**
	 * View selected Damaged Item in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-damaged-items-model" })
	public @ResponseBody JsonResponse<Object> modelviewOfDamagedItem(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewOfDamagedItem starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-damagedItem-forModel?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modelviewOfDamagedItem ends");
		return res;
	}

	/*
	 * Delete Item
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-damaged-items-delete" })
	public @ResponseBody JsonResponse<Object> deleteDamagedItem(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : deleteDamagedItem starts");
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
					environmentVaribles.getInventoryUrl() + "deleteDamagedItem?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : deleteDamagedItem ends");
		return resp;
	}

	/*
	 * Change Status to ReSale
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-damaged-items-change-statusToReSale" })
	public @ResponseBody JsonResponse<Object> changeStatusToReSale(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : changeStatusToReSale starts");
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
					environmentVaribles.getInventoryUrl() + "changeStatusToReSale?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : changeStatusToReSale ends");
		return resp;
	}

	/*
	 * Change Status To Scrapped
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-damaged-items-change-statusToScrapped" })
	public @ResponseBody JsonResponse<Object> changeStatusToScrapped(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : changeStatusToScrapped starts");
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
					environmentVaribles.getInventoryUrl() + "changeStatusToScrapped?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : changeStatusToScrapped ends");
		return resp;
	}

	/*
	 * post Mapping for Get Item Auto Complete List for search param
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-damaged-items-paramItem-autocompleteList" })

	public @ResponseBody JsonResponse<List<DropDownModel>> getParamItemList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getParamItemList starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getParamItemListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getParamItemList ends");
		return res;
	}

}
