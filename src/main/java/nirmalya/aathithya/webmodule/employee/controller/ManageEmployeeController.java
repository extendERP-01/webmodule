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

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value ={ "employee/" })

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
				DropDownModel[] Country = restClient.getForObject(env.getEmployeeUrl() + "getCountryList", DropDownModel[].class);
				List<DropDownModel> counntryList = Arrays.asList(Country);
				
				model.addAttribute("counntryList", counntryList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] State = restClient.getForObject(env.getEmployeeUrl() + "getstateList1", DropDownModel[].class);
				List<DropDownModel> stateList = Arrays.asList(State);
				
				model.addAttribute("stateList", stateList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] City = restClient.getForObject(env.getEmployeeUrl() + "getcityList1", DropDownModel[].class);
				List<DropDownModel> cityList = Arrays.asList(City);
				
				model.addAttribute("cityList", cityList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] Gender = restClient.getForObject(env.getEmployeeUrl() + "getgenderList1", DropDownModel[].class);
				List<DropDownModel> genderTypeList = Arrays.asList(Gender);
				
				model.addAttribute("genderTypeList", genderTypeList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] Nationality = restClient.getForObject(env.getEmployeeUrl() + "getnationalityList1", DropDownModel[].class);
				List<DropDownModel> nationalityList = Arrays.asList(Nationality);
				
				model.addAttribute("nationalityList", nationalityList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			
			try {
				DropDownModel[] BloodGroup = restClient.getForObject(env.getEmployeeUrl() + "getbloodgroupList1", DropDownModel[].class);
				List<DropDownModel> bloodgroupList = Arrays.asList(BloodGroup);
				
				model.addAttribute("bloodgroupList", bloodgroupList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			
			
			try {
				DropDownModel[] MaritalStatus = restClient.getForObject(env.getEmployeeUrl() + "getmaritalstatusList1", DropDownModel[].class);
				List<DropDownModel> maritalstatusList = Arrays.asList(MaritalStatus);
				
				model.addAttribute("maritalstatusList", maritalstatusList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			try {
				DropDownModel[] DocumentType = restClient.getForObject(env.getEmployeeUrl() + "getdocumenttypeList1", DropDownModel[].class);
				List<DropDownModel> documenttypeList = Arrays.asList(DocumentType);
				
				model.addAttribute("documenttypeList", documenttypeList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			try {
				DropDownModel[] JobType = restClient.getForObject(env.getEmployeeUrl() + "getJobType1", DropDownModel[].class);
				List<DropDownModel> jobtypeList = Arrays.asList(JobType);
				
				model.addAttribute("jobtypeList", jobtypeList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			
			try {
				DropDownModel[] Department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList1", DropDownModel[].class);
				List<DropDownModel> DepartmentList = Arrays.asList(Department);
				
				model.addAttribute("DepartmentList", DepartmentList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] EmploymentStatus = restClient.getForObject(env.getEmployeeUrl() + "getemploymentstatusList1", DropDownModel[].class);
				List<DropDownModel> employmentstatusList = Arrays.asList(EmploymentStatus);
				
				model.addAttribute("employmentstatusList", employmentstatusList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] degination = restClient.getForObject(env.getEmployeeUrl() + "getJobType2", DropDownModel[].class);
				List<DropDownModel> jobtypeList = Arrays.asList(degination);
				
				model.addAttribute("jobtypeList", jobtypeList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] Benefits = restClient.getForObject(env.getEmployeeUrl() + "getBenefits", DropDownModel[].class);
				List<DropDownModel> benefitsList = Arrays.asList(Benefits);
				
				model.addAttribute("benefitsList", benefitsList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			try {
				DropDownModel[] Bank = restClient.getForObject(env.getEmployeeUrl() + "getBankNameList", DropDownModel[].class);
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

		Path path = Paths.get(env.getFileUploadEmployee() + imageName);
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

