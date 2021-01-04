package nirmalya.aathithya.webmodule.master.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.LocationMasterModel;
import nirmalya.aathithya.webmodule.master.model.VendorLocationMasterModel;
import nirmalya.aathithya.webmodule.master.model.VendorMasterModel;
import nirmalya.aathithya.webmodule.master.model.ZoneMasterModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "master/" })
public class VendorMasterController {
	Logger logger = LoggerFactory.getLogger(VendorMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@GetMapping(value = { "manage-vendor-master" })
	public String manageVendor(Model model, HttpSession session) {
		logger.info("Method : manageVenor starts");
		try {
			DropDownModel[] locationType = restClient.getForObject(env.getMasterUrl() + "getLocationTypeList", DropDownModel[].class);
			List<DropDownModel> locationTypeList = Arrays.asList(locationType);
			
			model.addAttribute("locationTypeList", locationTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] category = restClient.getForObject(env.getMasterUrl() + "getCategoryList", DropDownModel[].class);
			List<DropDownModel> categoryTypeList = Arrays.asList(category);
			
			model.addAttribute("categoryTypeList", categoryTypeList);
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
		return "master/manageVendor";
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-vendor-master-save")
	public @ResponseBody JsonResponse<Object> saveVendorMaster(@RequestBody VendorMasterModel vendorMasterModel, HttpSession session) {
		logger.info("Method : saveVendorMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		vendorMasterModel.setCreatedBy(userId);
		
		MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");
		byte[] bytes;
		String imageName = null;
		
		if(inputFile!=null) {
			try {
				bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				imageName = saveAllImage(bytes,fileType[1]);
				
				vendorMasterModel.setFileVendor(imageName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			System.out.println(vendorMasterModel);
			resp = restClient.postForObject(env.getMasterUrl() + "saveVendorMaster", vendorMasterModel,
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
		
		logger.info("Method : saveVendorMaster starts");
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
	@PostMapping("/manage-vendor-master-location-save")
	public @ResponseBody JsonResponse<Object> saveVendorLocationMaster(@RequestBody VendorLocationMasterModel vendorLocationMasterModel, HttpSession session) {
		logger.info("Method : saveVendorLocationMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		vendorLocationMasterModel.setCreatedBy(userId);
		
		
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveVendorLocationMaster", vendorLocationMasterModel,
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
		
		logger.info("Method : saveVendorLocationMaster starts");
		return resp;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "manage-vendor-master-get-state-list" })
	public @ResponseBody JsonResponse<Object> getStateNameForLocationVendor(Model model, @RequestBody String tCountry,
			BindingResult result) {
		logger.info("Method : getStateNameForLocation starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getMasterUrl() + "getStateListForLoc?id=" + tCountry, JsonResponse.class);
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
	@GetMapping("manage-vendor-master-data-through-ajax")
	public @ResponseBody List<VendorMasterModel> vendorThroughAjax(Model model, HttpServletRequest request) {
		logger.info("Method : vendorThroughAjax starts");

		JsonResponse<List<VendorMasterModel>> jsonResponse = new JsonResponse<List<VendorMasterModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getMasterUrl() + "get-vendor-list", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<VendorMasterModel> addreq = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<VendorMasterModel>>() {
					});
		/*	for (AddRecruitentModel m : addreq) {
				if (m.getActivityStatus() == "1") {
					m.setActivityStatus("Created");
				} else if (m.getActivityStatus() == "2") {
					m.setActivityStatus("Active");
				} else if (m.getActivityStatus() == "3") {
					m.setActivityStatus("Closed");
				}
			}*/
			jsonResponse.setBody(addreq);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method ; vendorThroughAjax ends");
		System.out.println("###########" + jsonResponse.getBody());

		return jsonResponse.getBody();
	}
	@SuppressWarnings("unchecked")
	@GetMapping("manage-vendor-master-through-ajax")
	public @ResponseBody List<VendorLocationMasterModel> vendorLocationThroughAjax(Model model, HttpServletRequest request) {
		logger.info("Method : vendorLocationThroughAjax starts");

		JsonResponse<List<VendorLocationMasterModel>> jsonResponse = new JsonResponse<List<VendorLocationMasterModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getMasterUrl() + "get-vendor-location-list", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<VendorLocationMasterModel> addreq = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<VendorLocationMasterModel>>() {
					});
		/*	for (AddRecruitentModel m : addreq) {
				if (m.getActivityStatus() == "1") {
					m.setActivityStatus("Created");
				} else if (m.getActivityStatus() == "2") {
					m.setActivityStatus("Active");
				} else if (m.getActivityStatus() == "3") {
					m.setActivityStatus("Closed");
				}
			}*/
			jsonResponse.setBody(addreq);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method ; vendorLocationThroughAjax ends");
		System.out.println("###########" + jsonResponse.getBody());

		return jsonResponse.getBody();
	}
}
