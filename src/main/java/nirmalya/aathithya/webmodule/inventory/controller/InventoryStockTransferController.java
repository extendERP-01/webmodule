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
import nirmalya.aathithya.webmodule.inventory.model.InventoryStockTransferModel;
import nirmalya.aathithya.webmodule.inventory.model.StockItemModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory/" })
public class InventoryStockTransferController {
	Logger logger = LoggerFactory.getLogger(InventoryStockTransferController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Add KRA Measure
	 */
	@GetMapping("/add-item-stock-transfer")
	public String addItemStockTransferDetail(Model model, HttpSession session) {

		logger.info("Method : addItemStockTransferDetail starts");

		InventoryStockTransferModel stockTransfer = new InventoryStockTransferModel();
		InventoryStockTransferModel stockTransferSession = (InventoryStockTransferModel) session
				.getAttribute("sessionMeasureDetails");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (stockTransferSession != null) {
			model.addAttribute("stockTransfer", stockTransferSession);
			session.setAttribute("stockTransferSession", null);
		} else {
			model.addAttribute("stockTransfer", stockTransfer);
		}
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/*
		 * dropDown value for From Store
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-stockTransfer-fromStore?id="+userId, DropDownModel[].class);
			List<DropDownModel> fromStoreList = Arrays.asList(dropDownModel);
			model.addAttribute("fromStoreList", fromStoreList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for To Store
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-stockTransfer-toStore", DropDownModel[].class);
			List<DropDownModel> toStoreList = Arrays.asList(dropDownModel);
			model.addAttribute("toStoreList", toStoreList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addItemStockTransferDetail ends");
		return "inventory/addStockTransferDetail";
	}

	/*
	 * post Mapping for Get Item Auto Complete List
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-item-stock-transfer-getItemAutocompleteList" })

	public @ResponseBody JsonResponse<List<StockItemModel>> getItemAutoCompleteList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoCompleteList starts");

		JsonResponse<List<StockItemModel>> res = new JsonResponse<List<StockItemModel>>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getItemAutoCompleteList?id=" + searchValue,
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

		logger.info("Method : getItemAutoCompleteList ends");
		return res;
	}

	/*
	 * 
	 * 
	 * Method For Get Available quantity
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-item-stock-transfer-getItemDetail-throughAjax" })
	public @ResponseBody JsonResponse<InventoryStockTransferModel> getItemDetail(@RequestParam("item") String item) {
		logger.info("Method : getItemDetail starts");

		JsonResponse<InventoryStockTransferModel> res = new JsonResponse<InventoryStockTransferModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "getItemDetail?id=" + item,
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
		logger.info("Method : getItemDetail ends");
		return res;
	}

	/*
	 * post Mapping for add Stock Transfer
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-item-stock-transfer", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveStockTransfer(
			@RequestBody List<InventoryStockTransferModel> inventoryStockTransferModel, Model model,
			HttpSession session) {
		logger.info("Method : saveStockTransfer function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		Double grandTotal = 0.0;
		for (InventoryStockTransferModel m : inventoryStockTransferModel) {

			try {
				DropDownModel[] dropDownModel = restTemplate.getForObject(
						environmentVaribles.getInventoryUrl() + "get-taxRate?id=" + m.gettItem(),
						DropDownModel[].class);
				List<DropDownModel> toStoreList = Arrays.asList(dropDownModel);
				model.addAttribute("toStoreList", toStoreList);
				for (DropDownModel n : toStoreList) {
					m.setTaxRate(n.getKey());
				}

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			m.setTotal(m.getPrice() * m.gettItemQuantity());
			m.setCreatedBy(userId);
			grandTotal = grandTotal + m.getTotal();
			m.setSubTotal(grandTotal);
		}
		try {

			res = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-add-stock-transfer",
					inventoryStockTransferModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveStockTransfer function Ends");
		return res;
	}
	/*
	 * 
	 * GetMApping For Listing goods receive note
	 * 
	 * 
	 */

	@GetMapping(value = { "view-item-stock-transfer" })
	public String viewStockTransfer(Model model,HttpSession session) {
		logger.info("Method : viewStockTransfer starts");

String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/*
		 * dropDown value for From Store
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-stockTransfer-fromStore?id="+userId, DropDownModel[].class);
			List<DropDownModel> fromStoreList = Arrays.asList(dropDownModel);
			model.addAttribute("fromStoreList", fromStoreList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for To Store
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-stockTransfer-toStore", DropDownModel[].class);
			List<DropDownModel> toStoreList = Arrays.asList(dropDownModel);
			model.addAttribute("toStoreList", toStoreList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewStockTransfer ends");
		return "inventory/viewStockTransferDtl";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-item-stock-transfer-throughAjax" })
	public @ResponseBody DataTableResponse viewStockThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3, HttpSession session) {
		logger.info("Method : viewStockThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setUserId(userId);

			JsonResponse<List<InventoryStockTransferModel>> jsonResponse = new JsonResponse<List<InventoryStockTransferModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-alls-stock-transfer",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryStockTransferModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryStockTransferModel>>() {
					});
			String s = "";

			for (InventoryStockTransferModel m : form) {

				byte[] pId = Base64.getEncoder().encode(m.gettStockTransferId().getBytes());

				s = "";
				
				
				if (m.gettTransferStatus()) {
					m.setStatus("Received");
					s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;";
				} else {
					m.setStatus("Not Received");
					s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;";
					s = s + "<a href='edit-item-stock-transfer?id=" + new String(pId)
							+ "' ><i class='fa fa-edit'></i></a> &nbsp;";
					s = s + "<a href='javascript:void(0)' onclick='DeleteStock(\"" + new String(pId)
							+ "\")'><i class='fa fa-trash'></i></a>&nbsp;";
					s = s + "<a href='javascript:void(0)' onclick='changeStockStatus(\"" + new String(pId)
							+ "\")'><i class='fa fa-times-circle'></i></a>&nbsp;";
				}
				
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewStockThroughAjax ends");
		return response;
	}

	/**
	 * View selected GoodsReceive in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-item-stock-transfer-model" })
	public @ResponseBody JsonResponse<Object> modelviewStockTransfer(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewStockTransfer starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {

			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "gets-stock-modals?id=" + index,
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
		logger.info("Method : modelviewStockTransfer ends");
		return res;
	}

	/*
	 * 
	 * method to delete goods receive
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-item-stock-transfer" })
	public @ResponseBody JsonResponse<Object> deleteStockTransfer(@RequestParam("id") String encodeId,
			HttpSession session) {
		logger.info("Method : deleteStockTransfer Starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		String userId = null;
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "delete-stock-transfer?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : deleteStockTransfer ends");
		return resp;

	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-item-stock-transfer-change-status" })
	public @ResponseBody JsonResponse<Object> changeStockStatus(@RequestParam("id") String encodeId,
			HttpSession session) {
		logger.info("Method : changeStockStatus Starts");
		
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		
		String userId = null;
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "changeStockStatus?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : changeStockStatus ends");
		return resp;
		
	}

	@GetMapping(value = { "edit-item-stock-transfer" })
	public String editStockTransfer(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editStockTransfer starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		JsonResponse<InventoryStockTransferModel> jsonResponse = new JsonResponse<InventoryStockTransferModel>();

		try {
			InventoryStockTransferModel[] inventoryStockTransferModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "edit-stock-by-id?id=" + id,
					InventoryStockTransferModel[].class);

			List<InventoryStockTransferModel> stockTransferModel = Arrays.asList(inventoryStockTransferModel);

			String userId = "";
			
			try {
				userId = (String) session.getAttribute("USER_ID");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			/*
			 * dropDown value for From Store
			 */
			try {
				DropDownModel[] dropDownModel = restTemplate.getForObject(
						environmentVaribles.getInventoryUrl() + "get-stockTransfer-fromStore?id="+userId, DropDownModel[].class);
				List<DropDownModel> fromStoreList = Arrays.asList(dropDownModel);
				model.addAttribute("fromStoreList", fromStoreList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			/*
			 * dropDown value for To Store
			 */
			try {
				DropDownModel[] dropDownModel = restTemplate.getForObject(
						environmentVaribles.getInventoryUrl() + "get-stockTransfer-toStore", DropDownModel[].class);
				List<DropDownModel> toStoreList = Arrays.asList(dropDownModel);
				model.addAttribute("toStoreList", toStoreList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}

			model.addAttribute("id", stockTransferModel.get(0).gettStockTransferId());
			session.setAttribute("message", "");
			model.addAttribute("stockTransferModel", stockTransferModel);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editStockTransfer ends");
		return "inventory/addStockTransferDetail";

	}

}
