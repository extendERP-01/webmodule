package nirmalya.aathithya.webmodule.master.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.LocationMasterModel;

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

	@SuppressWarnings("unused")
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
			
			
			
			System.out.println("UPLOAD FILE===="+ session.getAttribute("quotationPFile"));
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
			System.out.println("DELETE FILE===="+ session.getAttribute("quotationPFile"));
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
}
