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
import nirmalya.aathithya.webmodule.employee.model.ExitInitiateModel;
import nirmalya.aathithya.webmodule.inventory.model.PhysicalVarificationWareHouseModel;
/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class PhysicalVarificationWareHouseController {
	Logger logger = LoggerFactory.getLogger(PhysicalVarificationWareHouseController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	private static final String user_Id = "USER_ID";
	@GetMapping("/add-physical-varification-warehouse")
	public String addPhysicalVarificationWareHouse(Model model, HttpSession session) {
		logger.info("Method : addExitInitiate start"); 
		
		PhysicalVarificationWareHouseModel physicalVarificationWareHouseModel = new PhysicalVarificationWareHouseModel();

		PhysicalVarificationWareHouseModel form = (PhysicalVarificationWareHouseModel) session.getAttribute("sphysicalVarificationWareHouseModel");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");
		if (form != null) {
			model.addAttribute("physicalVarificationWareHouseModel", form);
			session.setAttribute("sphysicalVarificationWareHouseModel", null);

		} else {
			model.addAttribute("physicalVarificationWareHouseModel", physicalVarificationWareHouseModel);
		}

	 
		try {
			DropDownModel[] store = restClient.getForObject(
					env.getInventoryUrl() + "getStoreForSubInv", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addPhysicalVarificationWareHouse ends");
		return "inventory/add-physical-varification-warehouse";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-physical-varification-warehouse-through-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addPhysicalVerificationAjax(
			@RequestBody List<PhysicalVarificationWareHouseModel> physicalVarificationWareHouseModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addExitInitiateAjax function starts"); 
		try {
			for (PhysicalVarificationWareHouseModel r : physicalVarificationWareHouseModel) {
				String userId = null;
				try {
					userId = (String) session.getAttribute("USER_ID");
				} catch (Exception e) {
					e.printStackTrace();
				}
				r.setCreatedBy(userId);
			}

			res = restClient.postForObject(env.getInventoryUrl() + "rest-add-physicalVerification", physicalVarificationWareHouseModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addPhysicalVerificationAjax function Ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-physical-varification-warehouse-store-subinventory" })
	public @ResponseBody JsonResponse<Object> getPhysicalVerificationTroughAjax(Model model, @RequestBody String storeId,
			BindingResult result) {
		logger.info("Method : getPhysicalVerificationTroughAjax starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "rest-get-subInventory?id=" + storeId,
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
		logger.info("Method : getPhysicalVerificationTroughAjax ends");
		return res;

	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-physical-varification-warehouse-subinventory-warehouse" })
	public @ResponseBody JsonResponse<Object> getWareHouseFromSubInventory(Model model, @RequestBody String subInventory,
			BindingResult result) {
		logger.info("Method : getWareHouseFromSubInventory starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "rest-get-warehouse?id=" + subInventory,
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
		logger.info("Method : getWareHouseFromSubInventory ends");
		return res;

	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-physical-varification-warehouse-warehouse-rack" })
	public @ResponseBody JsonResponse<Object> getRackFromWareHouse(Model model, @RequestBody String wareHouse,
			BindingResult result) {
		logger.info("Method : getRackFromWareHouse starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "rest-get-rack?id=" + wareHouse,
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
		logger.info("Method : getRackFromWareHouse ends");
		return res;

	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-physical-varification-warehouse-rack-item" })
	public @ResponseBody JsonResponse<Object> getItemFromRack(Model model, @RequestBody String rack,
			BindingResult result) {
		logger.info("Method : getItemFromRack starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "rest-get-item?id=" + rack,
					JsonResponse.class);
			//System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getItemFromRack ends");
		return res;

	}
	@GetMapping("/view-physical-varification-warehouse")
	public String viewPhysicalVarificationWareHouse( HttpSession session,Model model) {
		logger.info("Method : viewPhysicalVarificationWareHouse starts");
		
		try {
			DropDownModel[] store = restClient.getForObject(
					env.getInventoryUrl() + "getStoreForSubInv", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewPhysicalVarificationWareHouse ends");
		return "inventory/view-physical-varification-warehouse";
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-physical-varification-warehouse-throughajax")
	public @ResponseBody DataTableResponse viewPhysicalVerificationThroughAjax(Model model, HttpSession session,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2, @RequestParam String param3, @RequestParam String param4, @RequestParam String param5) {
		logger.info("Method : viewPhysicalVerificationThroughAjax starts");

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
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);

			tableRequest.setUserId(userId);
			JsonResponse<List<PhysicalVarificationWareHouseModel>> jsonResponse = new JsonResponse<List<PhysicalVarificationWareHouseModel>>();
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllPhysicalVerification", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();

			List<PhysicalVarificationWareHouseModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PhysicalVarificationWareHouseModel>>() {
					});

			String s = "";
		

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewPhysicalVerificationThroughAjax end");
		return response;
	}
}
