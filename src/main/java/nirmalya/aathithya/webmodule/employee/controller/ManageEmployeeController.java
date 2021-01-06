package nirmalya.aathithya.webmodule.employee.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeDocumentsModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeSalaryStructureModel;
import nirmalya.aathithya.webmodule.employee.model.IncomeTaxModel;
import nirmalya.aathithya.webmodule.employee.model.ManageEmployeeAddressModel;
import nirmalya.aathithya.webmodule.employee.model.ManageEmployeeModel;
import nirmalya.aathithya.webmodule.employee.model.ManageEmployeeWorkdetailsModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = { "employee/" })

public class ManageEmployeeController {
	Logger logger = LoggerFactory.getLogger(ManageEmployeeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	// Summary
	@GetMapping("/view-manage-employee")
	public String employee(Model model, HttpSession session) {

		logger.info("Method : employee starts");

		try {
			DropDownModel[] Country = restClient.getForObject(env.getEmployeeUrl() + "getCountryList",
					DropDownModel[].class);
			List<DropDownModel> counntryList = Arrays.asList(Country);

			model.addAttribute("counntryList", counntryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] State = restClient.getForObject(env.getEmployeeUrl() + "getstateList1",
					DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(State);

			model.addAttribute("stateList", stateList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] City = restClient.getForObject(env.getEmployeeUrl() + "getcityList1",
					DropDownModel[].class);
			List<DropDownModel> cityList = Arrays.asList(City);

			model.addAttribute("cityList", cityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] Gender = restClient.getForObject(env.getEmployeeUrl() + "getgenderList1",
					DropDownModel[].class);
			List<DropDownModel> genderTypeList = Arrays.asList(Gender);

			model.addAttribute("genderTypeList", genderTypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] Nationality = restClient.getForObject(env.getEmployeeUrl() + "getnationalityList1",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(Nationality);

			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] BloodGroup = restClient.getForObject(env.getEmployeeUrl() + "getbloodgroupList1",
					DropDownModel[].class);
			List<DropDownModel> bloodgroupList = Arrays.asList(BloodGroup);

			model.addAttribute("bloodgroupList", bloodgroupList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] MaritalStatus = restClient.getForObject(env.getEmployeeUrl() + "getmaritalstatusList1",
					DropDownModel[].class);
			List<DropDownModel> maritalstatusList = Arrays.asList(MaritalStatus);

			model.addAttribute("maritalstatusList", maritalstatusList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] DocumentType = restClient.getForObject(env.getEmployeeUrl() + "getdocumenttypeList1",
					DropDownModel[].class);
			List<DropDownModel> documenttypeList = Arrays.asList(DocumentType);

			model.addAttribute("documenttypeList", documenttypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] JobType = restClient.getForObject(env.getEmployeeUrl() + "getJobType1",
					DropDownModel[].class);
			List<DropDownModel> jobtypeList = Arrays.asList(JobType);

			model.addAttribute("jobtypeList", jobtypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] Department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList1",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(Department);

			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] EmploymentStatus = restClient
					.getForObject(env.getEmployeeUrl() + "getemploymentstatusList1", DropDownModel[].class);
			List<DropDownModel> employmentstatusList = Arrays.asList(EmploymentStatus);

			model.addAttribute("employmentstatusList", employmentstatusList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] degination = restClient.getForObject(env.getEmployeeUrl() + "getJobType2",
					DropDownModel[].class);
			List<DropDownModel> jobtypeList = Arrays.asList(degination);

			model.addAttribute("jobtypeList", jobtypeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] Benefits = restClient.getForObject(env.getEmployeeUrl() + "getBenefits",
					DropDownModel[].class);
			List<DropDownModel> benefitsList = Arrays.asList(Benefits);

			model.addAttribute("benefitsList", benefitsList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] Bank = restClient.getForObject(env.getEmployeeUrl() + "getBankNameList",
					DropDownModel[].class);
			List<DropDownModel> BankNameList = Arrays.asList(Bank);

			model.addAttribute("BankNameList", BankNameList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : employee ends");

		return "employee/view-manage-employee";
	}

	@PostMapping("/manage-employee-upload-file")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : employee uploadimage controller  starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			System.out.println(inputFile);
			session.setAttribute("employeePFile", inputFile);

		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : employee uploadimage controller ' ends");
		return response;
	}

	@PostMapping("/manage-employee-delete-file")
	public @ResponseBody JsonResponse<Object> deleteFile(HttpSession session) {
		logger.info("Method : deleteFile employee uploadimage controller starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			session.removeAttribute("employeePFile");
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : deleteFile employee uploadimage controller ends");
		return response;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/manage-employee-master-save")
	public @ResponseBody JsonResponse<Object> savemanangeemployee(@RequestBody ManageEmployeeModel manageEmployeeModel,
			HttpSession session) {
		logger.info("Method : saveemployee personalMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		manageEmployeeModel.setCreatedBy(userId);

		MultipartFile inputFile = (MultipartFile) session.getAttribute("employeePFile");
		byte[] bytes;
		String imageName = null;

		if (inputFile != null) {
			try {
				bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				imageName = saveAllImage(bytes, fileType[1]);
				System.out.println(imageName);

				manageEmployeeModel.setFileEmployeeimg(imageName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "saveemployeeMaster", manageEmployeeModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			session.removeAttribute("employeePFile");
			resp.setMessage("Success");
		}

		logger.info("Method : saveemployee personalMaster End");
		return resp;
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

			Path path = Paths.get(env.getFileUploadEmployee() + imageName);
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
	@PostMapping("/manage-employee-address-save")
	public @ResponseBody JsonResponse<Object> saveemployeeaddress(
			@RequestBody ManageEmployeeAddressModel manageEmployeeAddressModel, HttpSession session) {
		logger.info("Method : saveemployeeaddress starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		manageEmployeeAddressModel.setCreatedBy(userId);

		try {
			resp = restClient.postForObject(env.getEmployeeUrl() + "saveemployeeaddress", manageEmployeeAddressModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			session.removeAttribute("employeePFile");
			resp.setMessage("Success");
		}

		System.out.println("Success");

		logger.info("Method : saveemployeeaddress starts");
		return resp;
	}

	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("manage-vendor-master-data-through-ajax")
	public @ResponseBody List<ManageEmployeeModel> personalThroughAjax(Model model, HttpServletRequest request) {
		logger.info("Method : personal inf ThroughAjax starts");

		JsonResponse<List<ManageEmployeeModel>> jsonResponse = new JsonResponse<List<ManageEmployeeModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "get-personal-list", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ManageEmployeeModel> addreq = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ManageEmployeeModel>>() {
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

		logger.info("Method ; personal inf ThroughAjax ends");
		System.out.println("###########" + jsonResponse.getBody());

		return jsonResponse.getBody();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("view-manage-employee-through-ajax")
	public @ResponseBody List<ManageEmployeeAddressModel> vendorLocationThroughAjax(Model model,
			HttpServletRequest request) {
		logger.info("Method : vendorLocationThroughAjax starts");

		JsonResponse<List<ManageEmployeeAddressModel>> jsonResponse = new JsonResponse<List<ManageEmployeeAddressModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "viewEmployeeadd", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ManageEmployeeAddressModel> addreq = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ManageEmployeeAddressModel>>() {
					});

			jsonResponse.setBody(addreq);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method ; vendorLocationThroughAjax ends");
		System.out.println("###########" + jsonResponse.getBody());

		return jsonResponse.getBody();
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-employee-workdetails-save")
	public @ResponseBody JsonResponse<Object> saveemployeeworkdetails(
			@RequestBody ManageEmployeeWorkdetailsModel manageEmployeeWorkdetailsModel, HttpSession session) {
		logger.info("Method : saveemployeeworkdetails starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		manageEmployeeWorkdetailsModel.setCreatedBy(userId);

		try {
			resp = restClient.postForObject(env.getEmployeeUrl() + "saveemployeeworkdetails", manageEmployeeWorkdetailsModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			session.removeAttribute("employeePFile");
			resp.setMessage("Success");
		}

		System.out.println("Success");

		logger.info("Method : saveemployeeworkdetails starts");
		return resp;
	}
	@SuppressWarnings("unchecked")
	@GetMapping("view-manage-employee-work-ajax")
	public @ResponseBody List<ManageEmployeeWorkdetailsModel> viewmanageemployeeworkajax(Model model,
			HttpServletRequest request) {
		logger.info("Method : viewmanageemployeeworkajax starts");

		JsonResponse<List<ManageEmployeeWorkdetailsModel>> jsonResponse = new JsonResponse<List<ManageEmployeeWorkdetailsModel>>();

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "viewEmployeework", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ManageEmployeeWorkdetailsModel> addreq = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ManageEmployeeWorkdetailsModel>>() {
					});

			jsonResponse.setBody(addreq);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method ; viewmanageemployeeworkajax ends");
		System.out.println("###########" + jsonResponse.getBody());

		return jsonResponse.getBody();
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("view-delete-address-emp")
	public @ResponseBody JsonResponse<Object> deleteempaddress(Model model, @RequestParam String id,
			HttpSession session) {
		logger.info("Method : delectRequistion starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String createdBy = "";

		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			resp = restClient.getForObject(
					env.getEmployeeUrl() + "deleteaddressemp?id="+ id,
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

		logger.info("Method :  deleteempaddress ends");
		return resp;
	}
}