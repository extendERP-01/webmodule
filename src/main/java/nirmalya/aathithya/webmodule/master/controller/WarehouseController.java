package nirmalya.aathithya.webmodule.master.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.LocationMasterModel;
import nirmalya.aathithya.webmodule.master.model.LocationSectionModel;
import nirmalya.aathithya.webmodule.master.model.ZoneMasterModel;
import nirmalya.aathithya.webmodule.master.model.ZoneRackModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "master/" })
public class WarehouseController {
	Logger logger = LoggerFactory.getLogger(WarehouseController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@SuppressWarnings("unused")
	@GetMapping(value = { "manage-warehouse" })
	public String manageWareHouse(Model model, HttpSession session) {
		logger.info("Method : manageWareHouse starts");
		
		try {
			DropDownModel[] location = restClient.getForObject(env.getMasterUrl() + "getWarehouseLocationList", DropDownModel[].class);
			List<DropDownModel> locationList = Arrays.asList(location);
			
			model.addAttribute("locationList", locationList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		
		
	/*	try {
			LocationMasterModel[] location = restClient.getForObject(env.getMasterUrl() + "getLocationList", LocationMasterModel[].class);
			List<LocationMasterModel> locationList = Arrays.asList(location);
			
			int count = 0;
			
			for(LocationMasterModel m : locationList) {
				System.out.println(m.getLocationType());
				
				if(m.getLocationType().equals("Warehouse")) {
					//System.out.println("warehouse");
					DropDownModel dropDownModel = new DropDownModel(m.getLocationId(), m.getLocationName());
					List<DropDownModel> warehouseList = Arrays.asList(dropDownModel);
					model.addAttribute("warehouseList",warehouseList);
					model.addAttribute("locationId",m.getLocationId());
					model.addAttribute("locationCode",m.getLocationCode());
					System.out.println(warehouseList);
				}
		
			}
		
			model.addAttribute("count", count);
			System.out.println(locationList);
			model.addAttribute("locationList", locationList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}*/

		logger.info("Method : manageWareHouse ends");
		return "master/managewarehouse";
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-warehouse-get-location" })
	public @ResponseBody JsonResponse<Object> getlocationDetailAgainstId(Model model, @RequestBody String warehouseId,
			BindingResult result) {
		logger.info("Method : getlocationDetailAgainstId starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getMasterUrl() + "rest-get-locationDeatils?id=" + warehouseId,
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
		logger.info("Method : getlocationDetailAgainstId ends");
		return res;

	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-warehouse-get-location-Zone" })
	public @ResponseBody JsonResponse<Object> getlocationZonelDetailAgainstId(Model model, @RequestBody String warehouseId,
			BindingResult result) {
		logger.info("Method : getlocationZonelDetailAgainstId starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getMasterUrl() + "rest-get-locationDeatils?id=" + warehouseId,
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
		logger.info("Method : getlocationDetailAgainstId ends");
		return res;

	}
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-warehouse-zone-save")
	public @ResponseBody JsonResponse<Object> saveZoneMaster(@RequestBody ZoneMasterModel zoneMasterModel, HttpSession session) {
		logger.info("Method : saveZoneMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		zoneMasterModel.setCreatedBy(userId);
		
		
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveZoneMaster", zoneMasterModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			session.removeAttribute("quotationPFile");
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveLocationMaster starts");
		return resp;
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-warehouse-rack-save")
	public @ResponseBody JsonResponse<Object> saveRackMaster(@RequestBody ZoneRackModel zoneRackModel, HttpSession session) {
		logger.info("Method : saveRackMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		zoneRackModel.setCreatedBy(userId);
		
		
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveRackMaster", zoneRackModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			session.removeAttribute("quotationPFile");
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveRackMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-warehouse-get-warehouse-zone-details" })
	public @ResponseBody JsonResponse<Object> getWarehouseZoneDetailAgainstId(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : getWarehouseZoneDetailAgainstId starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getMasterUrl() + "rest-get-zoneDeatils?id=" + id,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			
			List<ZoneMasterModel> zoneDetails = mapper.convertValue(res.getBody(),
					new TypeReference<List<ZoneMasterModel>>() {
			});
			
			for(ZoneMasterModel m : zoneDetails) {
				try {
					ZoneRackModel[] rack = restClient.getForObject(env.getMasterUrl() + "viewRackListByZone?id="+m.getZoneId(), ZoneRackModel[].class);
					List<ZoneRackModel> rackList = Arrays.asList(rack);
					
					m.setRackList(rackList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			
			res.setBody(zoneDetails);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getWarehouseZoneDetailAgainstId ends");
		return res;

	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-warehouse-zone-edit")
	public @ResponseBody JsonResponse<Object> editZoneMaster(@RequestBody String zone, HttpSession session) {
		logger.info("Method : editZoneMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "editZoneMaster?id="+zone,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : editZoneMaster starts");
		return resp;
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-warehouse-zone-delete")
	public @ResponseBody JsonResponse<Object> deleteZoneMaster(@RequestBody String zone, HttpSession session) {
		logger.info("Method : deleteZoneMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteZoneMaster?id="+zone+"&createdBy="+userId,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteZoneMaster starts");
		return resp;
	}
}
