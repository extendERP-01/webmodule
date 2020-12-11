package nirmalya.aathithya.webmodule.asset.controller;

import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.asset.model.AssetFuelConsumptionModel;
import nirmalya.aathithya.webmodule.asset.model.AssetMaintenanceHistoryModel;
import nirmalya.aathithya.webmodule.asset.model.AssetVehicleModel;
import nirmalya.aathithya.webmodule.asset.model.AssignVehicleToDriverModel;
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
public class AssetReportController {
	
	Logger logger = LoggerFactory.getLogger(AssetReportController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Maintenance Report' page
	 *
	 */
	@GetMapping("/maintenance-report")
	public String generateMaintenanceReport(Model model, HttpSession session) {
		logger.info("Method : generateMaintenanceReport starts");
		
		logger.info("Method : generateMaintenanceReport ends");
		return "asset/generate-maintenance-report";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "maintenance-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewSaleReport(HttpSession session,HttpServletRequest request, Model model,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param1") String param1) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssetMaintenanceHistoryModel>> jsonResponse = new JsonResponse<List<AssetMaintenanceHistoryModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getMaintenanceReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetMaintenanceHistoryModel> assetMaintenanceList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetMaintenanceHistoryModel>>() {
				});
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(assetMaintenanceList);
		
		return response;
	}
	
	/**
	 * View Default 'Vehicle Asset Summary Report' page
	 *
	 */
	@GetMapping("/vehicle-asset-summary-report")
	public String generateVehicleAssetSummaryReport(Model model, HttpSession session) {
		logger.info("Method : generateVehicleAssetSummaryReport starts");
		
		logger.info("Method : generateVehicleAssetSummaryReport ends");
		return "asset/generate-vehicle-asset-summary-report";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/vehicle-asset-summary-report-get-vehicle" })
	public @ResponseBody JsonResponse<DropDownModel> getVehicleAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleForReport?id=" + searchValue,
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
	@GetMapping(value = { "vehicle-asset-summary-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewVehicleAssetSummaryReport(HttpSession session,HttpServletRequest request, Model model,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param1") String param1) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssetVehicleModel>> jsonResponse = new JsonResponse<List<AssetVehicleModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleAssetSummaryReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetVehicleModel> assetList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetVehicleModel>>() {
				});
		
		for(AssetVehicleModel m : assetList) {
			if(m.getAssignStatus()) {
				m.setStatus("Free");
			} else {
				m.setStatus("Assigned");
			}
			
			if(m.getAssignType()) {
				m.setType("Reserved");
			} else {
				m.setType("Running");
			}
			
			if(m.getRemoveDate()=="" || m.getRemoveDate()==null) {
				m.setRemoveDate("N/A");
			}
			
			if(m.getComment()=="" || m.getComment()==null) {
				m.setComment("N/A");
			}
		}
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(assetList);
		
		return response;
	}
	
	/**
	 * View Default 'Vehicle Current Asset Report' page
	 *
	 */
	@GetMapping("/vehicle-current-asset-report")
	public String generateVehicleCurrentAssetReport(Model model, HttpSession session) {
		logger.info("Method : generateVehicleCurrentAssetReport starts");
		
		logger.info("Method : generateVehicleCurrentAssetReport ends");
		return "asset/generate-vehicle-current-asset-report";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/vehicle-current-asset-report-get-vehicle" })
	public @ResponseBody JsonResponse<DropDownModel> getVehicleCurrentAssetAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleCurrentAssetAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleForReport?id=" + searchValue,
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

		logger.info("Method : getVehicleCurrentAssetAutoSearchList ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "vehicle-current-asset-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewVehicleCurrentAssetSummaryReport(HttpSession session,HttpServletRequest request, Model model,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param1") String param1) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssetVehicleModel>> jsonResponse = new JsonResponse<List<AssetVehicleModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleCurrentAssetReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetVehicleModel> assetList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetVehicleModel>>() {
				});
		
		for(AssetVehicleModel m : assetList) {
			if(m.getAssignType()) {
				m.setType("Reserved");
			} else {
				m.setType("Running");
			}
			
			if(m.getRemoveDate()=="" || m.getRemoveDate()==null) {
				m.setRemoveDate("N/A");
			}
		}
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(assetList);
		System.out.println("Curr Asset: "+response);
		return response;
	}
	
	/**
	 * View Default 'Fuel Consumption Report' page
	 *
	 */
	@GetMapping("/fuel-consumption-report")
	public String generateFuelConsumptionReport(Model model, HttpSession session) {
		logger.info("Method : generateFuelConsumptionReport starts");
		
		logger.info("Method : generateFuelConsumptionReport ends");
		return "asset/generate-fuel-consumption-report";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/fuel-consumption-report-get-vehicle" })
	public @ResponseBody JsonResponse<DropDownModel> getVehicleFuelConsumptionAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleFuelConsumptionAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleForReport?id=" + searchValue,
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

		logger.info("Method : getVehicleFuelConsumptionAutoSearchList ends");
		return res;
	}
	
	/**
	 * View Default 'Assigned Driver Report' page
	 *
	 */
	@GetMapping("/assigned-driver-report")
	public String generateAssignedDriverReport(Model model, HttpSession session) {
		logger.info("Method : generateAssignedDriverReport starts");
		
		logger.info("Method : generateAssignedDriverReport ends");
		return "asset/generate-assigned-driver-report";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "fuel-consumption-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewVehicleFuelConsumptionReport(HttpSession session,HttpServletRequest request, Model model,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param1") String param1) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssetFuelConsumptionModel>> jsonResponse = new JsonResponse<List<AssetFuelConsumptionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleFuelConsmpReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetFuelConsumptionModel> fuel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetFuelConsumptionModel>>() {
				});
		for(AssetFuelConsumptionModel m : fuel) {
			String s = String.format("%.2f", m.getTotalAmount());
			m.setAmtString("<span class='amtString'>"+s+"</span>");
		}
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(fuel);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assigned-driver-report-get-vehicle" })
	public @ResponseBody JsonResponse<DropDownModel> getVehicleDriverAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleDriverAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleForReport?id=" + searchValue,
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

		logger.info("Method : getVehicleDriverAutoSearchList ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "assigned-driver-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewVehicleDriverReport(HttpSession session,HttpServletRequest request, Model model,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param1") String param1) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssignVehicleToDriverModel>> jsonResponse = new JsonResponse<List<AssignVehicleToDriverModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleDriverReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssignVehicleToDriverModel> driver = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssignVehicleToDriverModel>>() {
				});
		for(AssignVehicleToDriverModel m : driver) {
			if(m.getAssignStatus()) {
				m.setStatus("Assigned");
			} else {
				m.setStatus("Free");
			}
			
			if(m.getRemoveDate()=="" || m.getRemoveDate()==null) {
				m.setRemoveDate("N/A");
			}
			if(m.getComment()=="" || m.getComment()==null) {
				m.setComment("N/A");
			}
		}
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(driver);
		return response;
	}
	
	/**
	 * View Default 'Assigned Driver Report' page
	 *
	 */
	@GetMapping("/view-assigned-asset-to-vehicle-report")
	public String generateAssignedAssetVehicleReport(Model model, HttpSession session) {
		logger.info("Method : generateAssignedAssetVehicleReport starts");
		
		logger.info("Method : generateAssignedAssetVehicleReport ends");
		return "asset/generate-assigned-asset-vehicle-report";
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-assigned-asset-to-vehicle-report-through-ajax" })
	public @ResponseBody DataTableResponse previewvehicleAssignSummaryReport(HttpSession session,HttpServletRequest request, Model model,@RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssetVehicleModel>> jsonResponse = new JsonResponse<List<AssetVehicleModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleAssetAssignReportPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetVehicleModel> assetAssignVehicle = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetVehicleModel>>() {
				});
		
			for(AssetVehicleModel m: assetAssignVehicle){
				if(m.getRemoveDate()=="" || m.getRemoveDate()==null) {
					m.setRemoveDate("N/A");
				}
				if(m.getComment()=="" || m.getComment()==null) {
					m.setComment("N/A");
				}
			}
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(assetAssignVehicle);
		
		return response;
	}/**
	 * Web Controller - Get driver List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-assigned-asset-to-vehicle-report-autocomplete-asset" })
	public @ResponseBody JsonResponse<DropDownModel> getVehicleAssetAutoCompleteAssetReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleAssetAutoCompleteAssetReport starts");
		System.out.println(searchValue);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleAssetAssignAutoCompleteAssetReport?id=" + searchValue,
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

		logger.info("Method : getVehicleAssetAutoCompleteAssetReport ends");
		return res;
	}
	/**
	 * View Default 'Fuel Consumption Report' page
	 *
	 */
	@GetMapping("/fuel-consumption-generate-vehicle-report")
	public String generateFuelConsumptionVehicleReport(Model model, HttpSession session) {
		logger.info("Method : generateFuelConsumptionVehicleReport starts");
		try {
			DropDownModel[] store = restClient.getForObject(env.getAssetUrl()+ "getStoreForFuelConsmption", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : generateFuelConsumptionVehicleReport ends");
		return "asset/generate-fuel-consumption-vehicle-report";
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "fuel-consumption-generate-vehicle-report-through-ajax" })
	public @ResponseBody DataTableResponse previewVehicleFuelReport(HttpSession session,HttpServletRequest request, Model model,
			 @RequestParam("param1") String param1,@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param4") String param4,
			 @RequestParam("param5") String param5) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssetFuelConsumptionModel>> jsonResponse = new JsonResponse<List<AssetFuelConsumptionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getGenerateFuelConsumptionVehicleReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetFuelConsumptionModel> fuelConsumption = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetFuelConsumptionModel>>() {
				});
		
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(fuelConsumption);
		return response;
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/fuel-consumption-generate-vehicle-report-auto-complete-vehicleNo" })
	public @ResponseBody JsonResponse<DropDownModel> getVehicleNoAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleNoAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		System.out.println(env.getAssetUrl() + "getVehicleNoAutoCompleteForAssetReport?id=" + searchValue);
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getVehicleNoAutoCompleteForAssetReport?id=" + searchValue,
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

		logger.info("Method : getVehicleNoAutoSearchList ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/fuel-consumption-generate-vehicle-report-auto-complete-coupnNo" })
	public @ResponseBody JsonResponse<DropDownModel> getCouponNoAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getCouponNoAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getCouponNoAutoCompleteForReport?id=" + searchValue,
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

		logger.info("Method : getCouponNoAutoSearchList ends");
		return res;
	}
	/**
	 * View Default 'Fuel Consumption Report' page
	 *
	 */
	@GetMapping("/fuel-consumption-generate-vehicle-diesel-milage-report")
	public String generateFuelConsumptionDieselMilageVehicleReport(Model model, HttpSession session) {
		logger.info("Method : generateFuelConsumptionDieselMilageVehicleReport starts");
		try {
			DropDownModel[] store = restClient.getForObject(env.getAssetUrl()+ "getStoreForFuelConsmption", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : generateFuelConsumptionDieselMilageVehicleReport ends");
		return "asset/generate-fuel-consumption-diesel-milage-vehicle-report";
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "fuel-consumption-generate-vehicle-diesel-milage-report-through-ajax" })
	public @ResponseBody DataTableResponse previewVehicleFuelMilageReport(HttpSession session,HttpServletRequest request, Model model,
			 @RequestParam("param1") String param1,@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param4") String param4,
			 @RequestParam("param5") String param5) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<AssetFuelConsumptionModel>> jsonResponse = new JsonResponse<List<AssetFuelConsumptionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getGenerateFuelConsumptionVehicleMilageReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetFuelConsumptionModel> fuelConsumptionMilage = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetFuelConsumptionModel>>() {
				});
		
		for(AssetFuelConsumptionModel m : fuelConsumptionMilage) {
			Double totalKM = 0.0;
			Double totalFHR = 0.0;
			Double totalBHR = 0.0;
			if(m.getBackHrMeter()==0.0) {
				m.setBhr(0.0);
			}
			totalKM = m.getKmMilage() * m.getQuantity();
			totalFHR = m.getFhr() * m.getQuantity();
			if(m.getKmMilage()==0.0) {
				if(m.getBhr()==0.0) {
					totalBHR = 0.0;
				} else {
					totalBHR = m.getQuantity() / m.getBhr();
				}
			} else {
				totalBHR = m.getBhr() * m.getQuantity();
			}
			
			String kmValue = Math.round(totalKM)+"/"+m.getKmMilage();
			String fhrValue = Math.round(totalFHR)+"/"+m.getFhr();
			String bhrValue = Math.round(totalBHR)+"/"+m.getBhr();
			m.setKmValue(kmValue);
			m.setFhrValue(fhrValue);
			m.setBhrValue(bhrValue);
		}
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(fuelConsumptionMilage);
		return response;
	}
}
