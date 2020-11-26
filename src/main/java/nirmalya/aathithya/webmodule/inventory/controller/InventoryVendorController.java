/**
 * Defines Inventory related method call for Vendors
 */
package nirmalya.aathithya.webmodule.inventory.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.InventoryVendorModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory" })
public class InventoryVendorController {
	
	Logger logger = LoggerFactory.getLogger(InventoryVendorController.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMApping for Adding new itemCategory
	 *
	 */
	@GetMapping(value = { "add-vendors" })
	public String getVendor(Model model, HttpSession session) {
		logger.info("Method : getVendor starts");
		InventoryVendorModel vendorModel = new InventoryVendorModel();
		InventoryVendorModel vendor = (InventoryVendorModel) session.getAttribute("svendor");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (vendor != null) {
			model.addAttribute("vendorModel", vendor);
		} else {
			model.addAttribute("vendorModel", vendorModel);
		}
		/*
		 * dropDown value for item Category Name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-item-category", DropDownModel[].class);
			List<DropDownModel> itemList = Arrays.asList(dropDownModel);
			model.addAttribute("vendor", itemList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] dd = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "getCountryForVendor", DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dd);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		List<Object> str = new ArrayList<Object>();
		model.addAttribute("itemCategory", str);
		logger.info("Method : getVendor ends");
		return "inventory/addVendor";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-vendors-getState-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getState(Model model, @RequestBody String country,
			BindingResult result) {
		logger.info("Method : getState starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "getStateForVendor?id=" + country,
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
		logger.info("Method : getState ends");
		return res;
	}

	/*
	 * post Mapping for add Vendors
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-vendors-save" })
	public @ResponseBody ResponseEntity<JsonResponse<Object>> addVendors(@RequestBody InventoryVendorModel vendorModel,
			Model model, HttpSession session) {
		logger.info("Method : addVendors starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		try {
			vendorModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "rest-addNew-vendor",
					vendorModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			// session.setAttribute("message", jsonResponse.getMessage());
			// session.setAttribute("vendorModel", vendorModel);
			jsonResponse.setMessage(jsonResponse.getMessage());
		} else {
			jsonResponse.setMessage("success");
		}

		ResponseEntity<JsonResponse<Object>> response = new ResponseEntity<JsonResponse<Object>>(jsonResponse,
				HttpStatus.CREATED);
		logger.info("Method : addVendors ends");
		return response;
	}

	/*
	 * 
	 * GetMApping For Listing Vendors
	 * 
	 * 
	 */
	@GetMapping(value = { "view-vendors" })
	public String viewVendors(Model model) {
		logger.info("Method : viewVendors starts");
		JsonResponse<Object> vendors = new JsonResponse<Object>();
		model.addAttribute("vendors", vendors);
		logger.info("Method : viewVendrs ends");
		return "inventory/viewVendor";
	}

	/*
	 * view through Ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-vendors-through-ajax" })
	public @ResponseBody DataTableResponse viewVendorsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewVendorsThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<InventoryVendorModel>> jsonResponse = new JsonResponse<List<InventoryVendorModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-vendors",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryVendorModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryVendorModel>>() {
					});
			String s = "";
			for (InventoryVendorModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getVendor().getBytes());

				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;<a href='edit-vendors?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a> &nbsp;";
				s = s + "<a href='javascript:void(0)' onclick='deleteVendor(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> &nbsp;";
				s = s + "<a data-toggle='modal' title='Blacklist Vendor' href='javascript:void(0)' onclick='blackListVendor(\""
						+ new String(pId) + "\")'><i class='fa fa-times-circle'></i></a>";
				m.setAction(s);
				s = "";

				if (m.getVendorActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewVendorsThroughAjax ends");
		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "vendor-profile-rating-through-ajax" })
	public @ResponseBody DataTableResponse viewVendorsRatingThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewVendorsRatingThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<InventoryVendorModel>> jsonResponse = new JsonResponse<List<InventoryVendorModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-vendors",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryVendorModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryVendorModel>>() {
					});
			String s = "";
			for (InventoryVendorModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getVendor().getBytes());

				s = "";
				if (m.getVendorRate() == 0) {
					s = "<a data-toggle='modal' title='Rating' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-star-o' style='font-weight:bold';></i></a>";
				} else {
					s = "<a data-toggle='modal' title='Rating' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-star checked' style='font-weight:bold';></i></a>";
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
		logger.info("Method : viewVendorsRatingThroughAjax ends");
		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-vendor-contract-through-ajax" })
	public @ResponseBody DataTableResponse viewVendorsContractThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewVendorsContractThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<InventoryVendorModel>> jsonResponse = new JsonResponse<List<InventoryVendorModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-vendors",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryVendorModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryVendorModel>>() {
					});
			String s = "";
			for (InventoryVendorModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getVendor().getBytes());

				s = "";
//				if (m.getVendorContract() == null || m.getVendorContract() == ""
//						|| m.getVendorContract().equals(null)) {
					s = "<a data-toggle='modal' title='Upload Contract' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-upload'></i></a>";
//				}
					
				if(m.getVendorContract()!=null && m.getVendorContract()!="") {
					String a = "<a class='example-image-link' href='/document/invImg/"+m.getVendorContract()+"' title='"+m.getVendorContract()+"' target='_blank'><img src='../assets/images/doc-icon.png'></a>";
					m.setVendorContract(a);
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
		logger.info("Method : viewVendorsContractThroughAjax ends");
		return response;
	}

	/*
	 * Delete Vendor
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-vendors" })
	public @ResponseBody JsonResponse<Object> deleteVendor(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : deleteVendor starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "delete-vendor-byId?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteVendor ends");
		return resp;
	}

	/*
	 * 
	 * 
	 * GetMApping for Edit Vendor
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping(value = { "edit-vendors" })
	public String editVendor(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editVendors starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		InventoryVendorModel vendorModel = new InventoryVendorModel();
		JsonResponse<InventoryVendorModel> jsonResponse = new JsonResponse<InventoryVendorModel>();

		try {
			jsonResponse = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-vendor-byId?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		vendorModel = mapper.convertValue(jsonResponse.getBody(), InventoryVendorModel.class);

		try {
			DropDownModel[] state = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "editStateForVendor?id=" + vendorModel.getVendorCountry(),
					DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(state);
			model.addAttribute("stateList", stateList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		session.setAttribute("message", "");
		model.addAttribute("vendorModel", vendorModel);

		List<Object> itemcategoryList = new ArrayList<Object>();
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-item-category-byId?id=" + id,
					DropDownModel[].class);

			for (DropDownModel m : dropDownModel) {
				itemcategoryList.add(m.getName());
			}

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (itemcategoryList != null) {
			model.addAttribute("itemCategory", itemcategoryList);
		} else {
			List<Object> str = new ArrayList<Object>();
			model.addAttribute("itemCategory", str);
		}

		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "rest-get-item-category", DropDownModel[].class);
			List<DropDownModel> categoryList = Arrays.asList(dropDownModel);
			model.addAttribute("vendor", categoryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] dd = restTemplate
					.getForObject(environmentVaribles.getInventoryUrl() + "getCountryForVendor", DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dd);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : editVendors ends");
		return "inventory/addVendor";
	}

	/**
	 * View selected Vendor in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-vendors-model" })
	public @ResponseBody JsonResponse<InventoryVendorModel> modelviewOfVendor(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewOfVendor starts");

		JsonResponse<InventoryVendorModel> res = new JsonResponse<InventoryVendorModel>();
		InventoryVendorModel vendorModel = new InventoryVendorModel();
		try {
			res = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "get-vendor-for-model?id=" + index,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			vendorModel = mapper.convertValue(res.getBody(), InventoryVendorModel.class);
			String fileUrl = environmentVaribles.getBaseURL() + "document/invImg/" + vendorModel.getVendorContract();
			if(vendorModel.getVendorContract()!=null && vendorModel.getVendorContract()!="") {
				String s = "<a class='example-image-link' href='"+fileUrl+"' title='"+vendorModel.getVendorContract()+"' target='_blank'>Preview</a>";
				vendorModel.setAction(s);
			}
			res.setBody(vendorModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modelviewOfVendor ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-vendors-getVendorList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVendorList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getVendorListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getVendorList ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-vendor-contract-getVendorList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorListForContractView(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVendorListForContractView starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getVendorListByAutoSearch?id=" + searchValue,
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
		
		logger.info("Method : getVendorListForContractView ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/vendor-profile-rating-getVendorList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorListForRating(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVendorListForRating starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getVendorListByAutoSearch?id=" + searchValue,
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
		
		logger.info("Method : getVendorListForRating ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/blacklist-vendor-getVendorList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorListForBlackList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getVendorListForBlackList starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "blacklisttVendorAutoSearch?id=" + searchValue,
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
		
		logger.info("Method : getVendorListForBlackList ends");
		return res;
	}

	@PostMapping("/view-vendor-contract-uploadFile")
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

	public String saveAllImage(byte[] imageBytes, String ext) {
		logger.info("Method : saveAllImage starts");

		String imageName = null;

		try {

			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				if (ext.contentEquals("jpeg")) {
					imageName = nowTime + ".jpg";
				} else {
					imageName = nowTime + "." + ext;
				}

			}

			Path path = Paths.get(environmentVaribles.getFileUploadInventory() + imageName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllImage ends");
		return imageName;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-vendor-contract-upload-file" })
	public @ResponseBody ResponseEntity<JsonResponse<Object>> addVendorContract(@RequestBody InventoryVendorModel vendorModel,
			Model model, HttpSession session) {
		logger.info("Method : addVendorContract starts");
		
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		try {
			vendorModel.setCreatedBy(userId);
			if(vendorModel.getVendorContract()!=null && vendorModel.getVendorContract()!="") {
				MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");
				byte[] bytes;
				String imageName = null;
				if(inputFile!=null) {
					try {
						bytes = inputFile.getBytes();
						String[] fileType = inputFile.getContentType().split("/");
						if(bytes!=null) {
							imageName = saveAllImage(bytes,fileType[1]);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					vendorModel.setVendorContract(imageName);
				}
			}
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "uploadVendorContract",
					vendorModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			jsonResponse.setMessage(jsonResponse.getMessage());
		} else {
			jsonResponse.setMessage("Success");
		}

		ResponseEntity<JsonResponse<Object>> response = new ResponseEntity<JsonResponse<Object>>(jsonResponse,
				HttpStatus.CREATED);
		
		logger.info("Method : addVendorContract ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-vendors-blacklisted" })
	public @ResponseBody ResponseEntity<JsonResponse<Object>> blackListVendor(@RequestBody InventoryVendorModel vendorModel,
			Model model, HttpSession session) {
		logger.info("Method : blackListVendor starts");
		
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			
		}
		try {
			vendorModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "blacklistVendor",
					vendorModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		if (jsonResponse.getMessage() != "") {
			jsonResponse.setMessage(jsonResponse.getMessage());
		} else {
			jsonResponse.setMessage("Success");
		}
		
		ResponseEntity<JsonResponse<Object>> response = new ResponseEntity<JsonResponse<Object>>(jsonResponse,
				HttpStatus.CREATED);
		
		logger.info("Method : blackListVendor ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "vendor-profile-rating-save" })
	public @ResponseBody ResponseEntity<JsonResponse<Object>> submitVendorRate(@RequestBody InventoryVendorModel vendorModel,
			Model model, HttpSession session) {
		logger.info("Method : submitVendorRate starts");
		
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			
		}
		try {
			vendorModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "submitVendorRate",
					vendorModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		if (jsonResponse.getMessage() != "") {
			jsonResponse.setMessage(jsonResponse.getMessage());
		} else {
			jsonResponse.setMessage("Success");
		}
		
		ResponseEntity<JsonResponse<Object>> response = new ResponseEntity<JsonResponse<Object>>(jsonResponse,
				HttpStatus.CREATED);
		
		logger.info("Method : submitVendorRate ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "blacklist-vendor-through-ajax" })
	public @ResponseBody DataTableResponse viewVendorsBlacklistedThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewVendorsBlacklistedThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<InventoryVendorModel>> jsonResponse = new JsonResponse<List<InventoryVendorModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-blacklisted-vendors",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryVendorModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryVendorModel>>() {
					});
			String s = "";
			for (InventoryVendorModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getVendor().getBytes());

				s = "";
//				if (m.getVendorContract() == null || m.getVendorContract() == ""
//						|| m.getVendorContract().equals(null)) {
					s = "<a data-toggle='modal' title='Rating' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-upload'></i></a>";
//				}
					
				if(m.getVendorContract()!=null && m.getVendorContract()!="") {
					String a = "<a class='example-image-link' href='/document/invImg/"+m.getVendorContract()+"' title='"+m.getVendorContract()+"' target='_blank'><img src='../assets/images/doc-icon.png'></a>";
					m.setVendorContract(a);
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
		logger.info("Method : viewVendorsBlacklistedThroughAjax ends");
		return response;
	}

}
