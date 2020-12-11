package nirmalya.aathithya.webmodule.asset.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.asset.model.AssetFuelConsumptionModel;
import nirmalya.aathithya.webmodule.asset.model.AssetMaintenanceHistoryModel;
import nirmalya.aathithya.webmodule.asset.model.AssetVehicleModel;
import nirmalya.aathithya.webmodule.asset.model.AssignVehicleToDriverModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "asset")
public class AssetPDFController {
	
	Logger logger = LoggerFactory.getLogger(AssetReportController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "maintenance-report-download" })
	public void generateAssignedAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3, @RequestParam("param1") String encodedParam1) {

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param1 = (new String(encodeByte1));
		
		JsonResponse<List<AssetMaintenanceHistoryModel>> jsonResponse = new JsonResponse<List<AssetMaintenanceHistoryModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getMaintenanceReportPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetMaintenanceHistoryModel> maintenanceList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetMaintenanceHistoryModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
		Double gTotal = 0.0;
		for(AssetMaintenanceHistoryModel m : maintenanceList) {
			gTotal = gTotal + m.getPrice();
		}
		
		if(maintenanceList.size()>=1) {
			maintenanceList.get(0).setgTotal(gTotal);
		}
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Maintenance?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String variable = env.getBaseUrlPath();
		
		String logo = logoList.get(0).getName();
	    data.put("logoImage",variable+"document/hotel/"+logo+"");
	    data.put("fromDate", param2);
		data.put("toDate", param3);
		data.put("maintenanceList", maintenanceList);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=MaintenanceReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/maintenanceReportPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "vehicle-asset-summary-report-download" })
	public void generateVehicleAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3, @RequestParam("param1") String encodedParam1) {

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param1 = (new String(encodeByte1));
		
		JsonResponse<List<AssetVehicleModel>> jsonResponse = new JsonResponse<List<AssetVehicleModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleAssetSummaryReportPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetVehicleModel> assetList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetVehicleModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
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
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-VehicleAssetSummary?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String variable = env.getBaseUrlPath();
		
		String logo = logoList.get(0).getName();
	    data.put("logoImage",variable+"document/hotel/"+logo+"");
	    data.put("fromDate", param2);
		data.put("toDate", param3);
		data.put("assetList", assetList);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=VehicleAssetSummaryReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/vehicleAssetSummaryReportPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "vehicle-current-asset-report-download" })
	public void generateVehicleCurrentAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3, @RequestParam("param1") String encodedParam1) {

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param1 = (new String(encodeByte1));
		
		JsonResponse<List<AssetVehicleModel>> jsonResponse = new JsonResponse<List<AssetVehicleModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleCurrentAssetReportPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetVehicleModel> assetList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetVehicleModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
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
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-VehicleAssetSummary?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String variable = env.getBaseUrlPath();
		
		String logo = logoList.get(0).getName();
	    data.put("logoImage",variable+"document/hotel/"+logo+"");
	    data.put("fromDate", param2);
		data.put("toDate", param3);
		data.put("assetList", assetList);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=VehicleCurrentAssetReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/vehicleCurrentAssetReportPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "fuel-consumption-report-download" })
	public void generateFuelConsumptionPdf(HttpServletResponse response, Model model,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3, @RequestParam("param1") String encodedParam1) {

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param1 = (new String(encodeByte1));
		
		JsonResponse<List<AssetFuelConsumptionModel>> jsonResponse = new JsonResponse<List<AssetFuelConsumptionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleFuelConsmpReportPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetFuelConsumptionModel> fuel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetFuelConsumptionModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
		Double gTotal = 0.0;
		Double gQty = 0.0;
		for(AssetFuelConsumptionModel m : fuel) {
			gTotal = gTotal + m.getTotalAmount();
			gQty = gQty + m.getQuantity();
		}
		
		if(fuel.size()>=1) {
			fuel.get(0).setgTotal(gTotal);
			fuel.get(0).setgQty(gQty);
		}
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Fuel?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String variable = env.getBaseUrlPath();
		
		String logo = logoList.get(0).getName();
	    data.put("logoImage",variable+"document/hotel/"+logo+"");
	    data.put("fromDate", param2);
		data.put("toDate", param3);
		data.put("fuel", fuel);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=FuelConsumptionReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/fuelConsumptionReportPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "assigned-driver-report-download" })
	public void generateAssignedPdf(HttpServletResponse response, Model model,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3, @RequestParam("param1") String encodedParam1) {

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param1 = (new String(encodeByte1));
		
		JsonResponse<List<AssignVehicleToDriverModel>> jsonResponse = new JsonResponse<List<AssignVehicleToDriverModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam1(param1);
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleDriverReportPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssignVehicleToDriverModel> driver = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssignVehicleToDriverModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
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
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Fuel?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String variable = env.getBaseUrlPath();
		
		String logo = logoList.get(0).getName();
	    data.put("logoImage",variable+"document/hotel/"+logo+"");
	    data.put("fromDate", param2);
		data.put("toDate", param3);
		data.put("driver", driver);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=AssignedDriverReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/assignedDriverReportPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-assigned-asset-to-vehicle-report-download" })
	public void generateAssignedAssetVehiclePdf(HttpServletResponse response, Model model, @RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3, HttpSession session) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		
		
		JsonResponse<List<AssignVehicleToDriverModel>> jsonResponse = new JsonResponse<List<AssignVehicleToDriverModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getVehicleAssetAssignReportPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetVehicleModel> assetVehicle = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetVehicleModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
		
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Fuel?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
//		String variable = env.getBaseUrlPath();
		
//		String logo = logoList.get(0).getName();
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		String printedBy = (String) session.getAttribute("USER_NAME");
		if (assetVehicle.size() != 0) {

			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("assetVehicle", "");
		}

		for(AssetVehicleModel m: assetVehicle){
			if(m.getRemoveDate()=="" || m.getRemoveDate()==null) {
				m.setRemoveDate("N/A");
			}
			if(m.getComment()=="" || m.getComment()==null) {
				m.setComment("N/A");
			}
		}
		data.put("assetVehicle", assetVehicle);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=AssignedAssetVehicleReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/assignedAssetVehicleReportPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "fuel-consumption-generate-vehicle-report-download" })
	public void generateFuelConsumptionVehiclePdf(HttpServletResponse response, Model model, @RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4,
			@RequestParam("param5") String encodedParam5, HttpSession session) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		
		
		JsonResponse<List<AssetFuelConsumptionModel>> jsonResponse = new JsonResponse<List<AssetFuelConsumptionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getGenerateFuelConsumptionVehicleReportForPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetFuelConsumptionModel> fuelConsmptionVehicle = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetFuelConsumptionModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
		
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Fuel?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
//		String variable = env.getBaseUrlPath();
//		
//		String logo = logoList.get(0).getName();
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		String printedBy = (String) session.getAttribute("USER_NAME");
		if (fuelConsmptionVehicle.size() != 0) {

			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("fuelConsmptionVehicle", "");
		}
		
		data.put("fuelConsmptionVehicle", fuelConsmptionVehicle);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=VehicleFuelConsumptionReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/fuelConsumptionForVehiclePDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "fuel-consumption-generate-vehicle-diesel-milage-report-download" })
	public void generateFuelConsumptionVehicleMilagePdf(HttpServletResponse response, Model model, @RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4,
			@RequestParam("param5") String encodedParam5, HttpSession session) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		
		
		JsonResponse<List<AssetFuelConsumptionModel>> jsonResponse = new JsonResponse<List<AssetFuelConsumptionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		
		
		try {
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getGenerateFuelConsumptionVehicleMilageReportForPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AssetFuelConsumptionModel> fuelConsmptionVehicleMilage = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AssetFuelConsumptionModel>>() {
				});
		for(AssetFuelConsumptionModel m : fuelConsmptionVehicleMilage) {
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
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Fuel?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
//		String variable = env.getBaseUrlPath();
//		
//		String logo = logoList.get(0).getName();
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		String printedBy = (String) session.getAttribute("USER_NAME");
		if (fuelConsmptionVehicleMilage.size() != 0) {

			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("fuelConsmptionVehicleMilage", "");
		}
		
		data.put("fuelConsmptionVehicleMilage", fuelConsmptionVehicleMilage);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=VehicleFuelConsumptionMilageReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("asset/fuelConsumptionForVehicleMilagePDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
