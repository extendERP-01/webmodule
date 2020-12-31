package nirmalya.aathithya.webmodule.master.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.LocationMasterModel;
import nirmalya.aathithya.webmodule.master.model.LocationRoomModel;
import nirmalya.aathithya.webmodule.master.model.LocationSectionModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "master/" })
public class LocationMasterController {

	Logger logger = LoggerFactory.getLogger(LocationMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping(value = { "manage-location" })
	public String manageLocation(Model model, HttpSession session) {
		logger.info("Method : manageLocation starts");
		
		try {
			DropDownModel[] locationType = restClient.getForObject(env.getMasterUrl() + "getLocationTypeList", DropDownModel[].class);
			List<DropDownModel> locationTypeList = Arrays.asList(locationType);
			
			model.addAttribute("locationTypeList", locationTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] roomType = restClient.getForObject(env.getMasterUrl() + "getRoomTypeList", DropDownModel[].class);
			List<DropDownModel> roomTypeList = Arrays.asList(roomType);
			
			model.addAttribute("roomTypeList", roomTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] country = restClient.getForObject(env.getMasterUrl() + "getCountryListForLocation", DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(country);
			
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			LocationMasterModel[] location = restClient.getForObject(env.getMasterUrl() + "getLocationList", LocationMasterModel[].class);
			List<LocationMasterModel> locationList = Arrays.asList(location);
			
			int count = 0;
			
			for(LocationMasterModel m : locationList) {
				count = count + 1;
				if(m.getLocVirtual().equals("0")) {
					m.setLocVirtual("No");
				}
				if(m.getLocVirtual().equals("1")) {
					m.setLocVirtual("Yes");
				}
				if(m.getLocStatus().equals("0")) {
					m.setLocStatus("Inactive");
				}
				if(m.getLocStatus().equals("1")) {
					m.setLocStatus("Active");
				}
			}
			
			model.addAttribute("count", count);
			
			model.addAttribute("locationList", locationList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : manageLocation ends");
		return "master/manageLocation";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-location-get-state-list" })
	public @ResponseBody JsonResponse<Object> getStateNameForLocation(Model model, @RequestBody String tCountry,
			BindingResult result) {
		logger.info("Method : getStateNameForLocation starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		try {
			res = restClient.getForObject(env.getMasterUrl() + "getStateListForLoc?id=" + tCountry,
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
		
		logger.info("Method : getStateNameForLocation ends");
		return res;

	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-location-get-location-details" })
	public @ResponseBody JsonResponse<LocationMasterModel> getLocationDetails(Model model, @RequestBody String tCountry,
			BindingResult result) {
		logger.info("Method : getLocationDetails starts");
		
		JsonResponse<LocationMasterModel> res = new JsonResponse<LocationMasterModel>();
		
		try {
			res = restClient.getForObject(env.getMasterUrl() + "getLocationDetailsById?id=" + tCountry,
					JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();

			LocationMasterModel locDetails = mapper.convertValue(res.getBody(),
					new TypeReference<LocationMasterModel>() {
					});
			
			try {
				DropDownModel[] state = restClient.getForObject(env.getMasterUrl() + "viewStateLocListByCountry?id="+locDetails.getLocCountry(), DropDownModel[].class);
				List<DropDownModel> stateList = Arrays.asList(state);
				
				locDetails.setStateList(stateList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			try {
				DropDownModel[] city = restClient.getForObject(env.getMasterUrl() + "viewCityLocListByState?id="+locDetails.getLocState(), DropDownModel[].class);
				List<DropDownModel> cityList = Arrays.asList(city);
				
				locDetails.setCityList(cityList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			if(locDetails.getFileLocation()!=null && locDetails.getFileLocation()!="" && !locDetails.getFileLocation().equals("null")) {
				String fileLocation = env.getBaseURL() + "document/module/" + locDetails.getFileLocation();
				
				locDetails.setFileLocation(fileLocation);
			}
			
			res.setBody(locDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getLocationDetails ends");
		return res;
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-location-get-loc-floor-details" })
	public @ResponseBody JsonResponse<List<LocationMasterModel>> getLocationFloorDetails(Model model, @RequestBody String tCountry,
			BindingResult result) {
		logger.info("Method : getLocationFloorDetails starts");
		
		JsonResponse<List<LocationMasterModel>> res = new JsonResponse<List<LocationMasterModel>>();
		
		try {
			res = restClient.getForObject(env.getMasterUrl() + "getLocationFloorDetails?id=" + tCountry,
					JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<LocationMasterModel> locDetails = mapper.convertValue(res.getBody(),
					new TypeReference<List<LocationMasterModel>>() {
			});
			
			for(LocationMasterModel m : locDetails) {
				try {
					LocationSectionModel[] section = restClient.getForObject(env.getMasterUrl() + "viewSectionListByFloor?id="+m.getFloorId(), LocationSectionModel[].class);
					List<LocationSectionModel> sectionList = Arrays.asList(section);
					
					m.setSectionList(sectionList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			
			res.setBody(locDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getLocationDetails ends");
		return res;
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping(value = { "manage-location-get-loc-room-details" })
	public @ResponseBody JsonResponse<List<LocationRoomModel>> getLocationRoomDetails(Model model, @RequestBody List<String> tCountry,
			BindingResult result) {
		logger.info("Method : getLocationRoomDetails starts");
		
		JsonResponse<List<LocationRoomModel>> res = new JsonResponse<List<LocationRoomModel>>();
		
		try {
			res = restClient.postForObject(env.getMasterUrl() + "getLocationRoomDetails", tCountry,
					JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<LocationRoomModel> locDetails = mapper.convertValue(res.getBody(),
					new TypeReference<List<LocationRoomModel>>() {
			});
			
			List<DropDownModel> roomCountList = new ArrayList<DropDownModel>();
			
			try {
				DropDownModel[] roomCount = restClient.postForObject(env.getMasterUrl() + "countFloorWiseRoom",tCountry, DropDownModel[].class);
				roomCountList = Arrays.asList(roomCount);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if(tCountry.size() > 0) {
				for(int i = 0; i < locDetails.size(); i++) {
					try {
						LocationRoomModel[] room = restClient.getForObject(env.getMasterUrl() + "viewRoomListBySection?id="+locDetails.get(i).getSectionId(), LocationRoomModel[].class);
						List<LocationRoomModel> roomList = Arrays.asList(room);
						
						locDetails.get(i).setRoomList(roomList);
						int c  = 0;
						for(LocationRoomModel a : roomList) {
							c = c + 1;
						}
						locDetails.get(i).setRoomCount(c);
					} catch (RestClientException e) {
						e.printStackTrace();
					}
					
				}
				
				for(DropDownModel m : roomCountList) {
					for(LocationRoomModel a : locDetails) {
						if(m.getKey().contains(a.getFloorId())) {
							a.setFloorCount(Integer.parseInt(m.getName()));
						}
					}
				}
			}
			
			res.setBody(locDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getLocationRoomDetails ends");
		return res;
		
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "manage-location-get-location-listing" })
	public @ResponseBody List<LocationMasterModel> getLocationListing(Model model, HttpServletRequest request) {
		logger.info("Method : getLocationListing starts");
		
		JsonResponse<List<LocationMasterModel>> res = new JsonResponse<List<LocationMasterModel>>();
		
		try {
			res = restClient.getForObject(env.getMasterUrl() + "getLocationListing",
					JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();

			List<LocationMasterModel> location = mapper.convertValue(res.getBody(),
					new TypeReference<List<LocationMasterModel>>() {
					});
			
			for(LocationMasterModel m : location) {
				if(m.getLocVirtual().contentEquals("1")) {
					m.setLocVirtual("Yes");
				} else {
					m.setLocVirtual("No");
				}
				
				if(m.getLocStatus().contentEquals("1")) {
					m.setLocStatus("Active");
				} else {
					m.setLocStatus("Inactive");
				}
			}
			
			res.setBody(location);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		System.out.println(res.getBody());
		logger.info("Method : getLocationListing ends");
		return res.getBody();
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-location-get-city-list" })
	public @ResponseBody JsonResponse<Object> getCityForLocation(Model model, @RequestBody String tCountry,
			BindingResult result) {
		logger.info("Method : getCityForLocation starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		try {
			res = restClient.getForObject(env.getMasterUrl() + "getCityForLocation?id=" + tCountry,
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
		
		logger.info("Method : getCityForLocation ends");
		return res;
		
	}
	
	@PostMapping("/manage-location-upload-file")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("quotationPFile", inputFile);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}
	
	@PostMapping("/manage-location-delete-file")
	public @ResponseBody JsonResponse<Object> deleteFile(HttpSession session) {
		logger.info("Method : deleteFile controller function 'post-mapping' starts");
		
		JsonResponse<Object> response = new JsonResponse<Object>();
		
		try {
			session.removeAttribute("quotationPFile");
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : deleteFile controller function 'post-mapping' ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-save")
	public @ResponseBody JsonResponse<Object> saveLocationMaster(@RequestBody LocationMasterModel location, HttpSession session) {
		logger.info("Method : saveLocationMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		location.setCreatedBy(userId);
		
		MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");
		byte[] bytes;
		String imageName = null;
		
		if(inputFile!=null) {
			try {
				bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				imageName = saveAllImage(bytes,fileType[1]);
				
				location.setFileLocation(imageName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveLocationMaster", location,
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
	@PostMapping("/manage-location-save-floor")
	public @ResponseBody JsonResponse<Object> saveFloorMaster(@RequestBody LocationMasterModel location, HttpSession session) {
		logger.info("Method : saveLocationMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		location.setCreatedBy(userId);
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveFloorMaster", location,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveLocationMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-save-room")
	public @ResponseBody JsonResponse<Object> saveRoomMaster(@RequestBody LocationRoomModel location, HttpSession session) {
		logger.info("Method : saveRoomMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		location.setCreatedBy(userId);
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveRoomMaster", location,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveRoomMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-save-section")
	public @ResponseBody JsonResponse<Object> saveSectionMaster(@RequestBody LocationSectionModel location, HttpSession session) {
		logger.info("Method : saveSectionMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		location.setCreatedBy(userId);
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveSectionMaster", location,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveSectionMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-get-floor-delete")
	public @ResponseBody JsonResponse<Object> deleteFloorMaster(@RequestBody String floor, HttpSession session) {
		logger.info("Method : deleteFloorMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteFloorMaster?id="+floor+"&createdBy="+userId,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteFloorMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-get-section-delete")
	public @ResponseBody JsonResponse<Object> deleteSectionMaster(@RequestBody String floor, HttpSession session) {
		logger.info("Method : deleteSectionMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteSectionMaster?id="+floor+"&createdBy="+userId,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteSectionMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-get-room-delete")
	public @ResponseBody JsonResponse<Object> deleteRoomMaster(@RequestBody String floor, HttpSession session) {
		logger.info("Method : deleteRoomMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteRoomMaster?id="+floor+"&createdBy="+userId,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteRoomMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-get-location-delete")
	public @ResponseBody JsonResponse<Object> deleteLocationMaster(@RequestBody List<DropDownModel> locationList, HttpSession session) {
		logger.info("Method : deleteLocationMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(DropDownModel m : locationList) {
			m.setName(userId);
		}
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "deleteLocationMaster",locationList,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteLocationMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-get-floor-edit")
	public @ResponseBody JsonResponse<Object> editFloorMaster(@RequestBody String floor, HttpSession session) {
		logger.info("Method : editFloorMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "editFloorMaster?id="+floor,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteFloorMaster starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-get-section-edit")
	public @ResponseBody JsonResponse<Object> editSectionMaster(@RequestBody String floor, HttpSession session) {
		logger.info("Method : editSectionMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "editSectionMaster?id="+floor,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : editSectionMaster starts");
		return resp;
	}
	
	public String saveAllImage(byte[] imageBytes, String ext) {
		logger.info("Method : saveAllImage starts");
		
		String imageName = null;
		
		try {
			
			if(imageBytes!=null) {
				long nowTime = new Date().getTime();
				if(ext.contentEquals("jpeg")) {
					imageName = nowTime+".jpg";
				} else {
					imageName = nowTime+"."+ext;
				}
				
			}

			Path path = Paths.get(env.getFileUploadMaster() + imageName);
			if(imageBytes !=null) {
				Files.write(path, imageBytes);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : saveAllImage ends");
		return imageName;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-location-get-location-file-delete")
	public @ResponseBody JsonResponse<Object> deleteLocationFile(@RequestBody String floor, HttpSession session) {
		logger.info("Method : deleteLocationFile starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteLocationFile?id="+floor+"&createdBy="+userId,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : deleteLocationFile starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("manage-location-report-excel")
	public ModelAndView downloadExcelReport(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("id") String encodedPraram1) {
		logger.info("Method : downloadExcelReport start");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String param1 = (new String(encodeByte1));
		
		List<String> locationList = new ArrayList<String>();
		
		String data[] = param1.split(",");
		
		for(int i = 0; i < data.length; i++) {
			locationList.add(data[i]);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		
		try {
			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getLocationExcelData", locationList,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<LocationMasterModel> location = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<LocationMasterModel>>() {
					});

			
			map.put("location", location);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=LocationExcelReport.xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("LocationMasterController -> downloadExcelReport GET", e);
		}
		logger.info("Method : downloadExcelReport ends");
		return new ModelAndView(new ExcelLocationReport(), map);
	}
}
