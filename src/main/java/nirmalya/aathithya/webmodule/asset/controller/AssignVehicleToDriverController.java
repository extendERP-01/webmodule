package nirmalya.aathithya.webmodule.asset.controller;

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

import nirmalya.aathithya.webmodule.asset.model.AssignVehicleToDriverModel;
import nirmalya.aathithya.webmodule.asset.model.ItemAssetModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "asset")
public class AssignVehicleToDriverController {

	Logger logger = LoggerFactory.getLogger(AssignVehicleToDriverController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Driver Vehicle Assign' page
	 *
	 */
	@GetMapping("/assign-vehicle-to-driver")
	public String assignVehicleDriver(Model model, HttpSession session) {
		logger.info("Method : assignVehicleDriver starts");
		
		logger.info("Method : assignVehicleAsset ends");
		return "asset/assign-vehicle-to-driver";
	}
	
	/**
	 * View Default 'Driver Vehicle Assign' View page
	 *
	 */
	@GetMapping("/view-assigned-vehicle-to-driver")
	public String viewAssignedVehicleDriver(Model model, HttpSession session) {
		logger.info("Method : viewAssignedVehicleDriver starts");
		
		logger.info("Method : viewAssignedVehicleDriver ends");
		return "asset/view-assigned-vehicle-to-driver";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-vehicle-to-driver-get-employee" })
	public @ResponseBody JsonResponse<DropDownModel> getDriverAutoSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getDriverAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getDriverAutoSearch?id=" + searchValue,
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
		logger.info("Method : getDriverAutoSearch ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-vehicle-to-driver-get-vehicle" })
	public @ResponseBody JsonResponse<ItemAssetModel> getVehicleAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleAutoSearchList starts");

		JsonResponse<ItemAssetModel> res = new JsonResponse<ItemAssetModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleAutoSearchList?id=" + searchValue,
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
		logger.info("Method : getVehicleAutoSearchList ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assigned-vehicle-to-driver-get-vehicle" })
	public @ResponseBody JsonResponse<ItemAssetModel> getVehicleAutoSearchListForSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleAutoSearchListForSearch starts");

		JsonResponse<ItemAssetModel> res = new JsonResponse<ItemAssetModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleAutoSearchListForSearch?id=" + searchValue,
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
		logger.info("Method : getVehicleAutoSearchListForSearch ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "assign-vehicle-to-driver-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> assignVehicleToDriver(@RequestBody List<AssignVehicleToDriverModel> assignDriver,
			Model model, HttpSession session) {
		logger.info("Method : assignVehicleToDriver function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < assignDriver.size(); i++) {
				assignDriver.get(i).setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getAssetUrl() + "assignVehicleToDriver", assignDriver, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : assignVehicleToDriver function Ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-assigned-vehicle-to-driver-through-ajax")
	public @ResponseBody DataTableResponse viewAssignedDriverThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3, @RequestParam String param4) {
		logger.info("Method : viewAssignedDriverThroughAjax starts");

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
			tableRequest.setParam4(param4);

			JsonResponse<List<AssignVehicleToDriverModel>> jsonResponse = new JsonResponse<List<AssignVehicleToDriverModel>>();

			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getAssignedVehicleDriverDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AssignVehicleToDriverModel> assignedDriver = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssignVehicleToDriverModel>>() {
					});

			String s = "";

			for (AssignVehicleToDriverModel m : assignedDriver) {
				
				byte[] vId = Base64.getEncoder().encode(m.getvAssetId().getBytes());
				byte[] id = Base64.getEncoder().encode(m.getDriverId().getBytes());
				byte[] dateId = Base64.getEncoder().encode(m.getCreatedOnDate().getBytes());
				byte[] timeId = Base64.getEncoder().encode(m.getCreatedOnTime().getBytes());
				
				if(m.getAssignStatus()) {
					m.setStatus("Assigned");
					
					s = s + "<a href='javascript:void' title='Unassign' onclick='changeStatusModal(\""
							+new String(id)+"\",\""+ new String(vId) +"\",\""+ new String(dateId) +"\",\""+ new String(timeId) + "\")'><button class='btn btn-danger'><i class='fa fa-close' style='font-size:20px;'></i></button></a>&nbsp;";
					m.setAction(s);
					s = "";
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignedDriver);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewAssignedDriverThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assigned-vehicle-to-driver-change-status" })
	public @ResponseBody JsonResponse<Object> changeAssignedDriverStatus(Model model, @RequestBody AssignVehicleToDriverModel searchValue,
			BindingResult result) {
		logger.info("Method : changeAssignedDriverStatus starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.postForObject(env.getAssetUrl() + "changeAssignedDriverStatus", searchValue,
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

		logger.info("Method : changeAssignedDriverStatus ends");
		return res;
	}
}
