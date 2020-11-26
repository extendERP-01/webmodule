package nirmalya.aathithya.webmodule.employee.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.ReadExcelData;
import nirmalya.aathithya.webmodule.employee.model.HrmsDailyAttendanceExcelModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsDailyAttendanceExcelUploadController {
	Logger logger = LoggerFactory.getLogger(HrmsDailyAttendanceExcelUploadController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	ReadExcelData readExcelData;

	/**
	 * Default 'add production planning ' page
	 *
	 */

	@GetMapping("/daily-attendance-excel-upload")
	public String dailyAttExcelUpload(Model model, HttpSession session) {

		logger.info("Method : dailyAttExcelUpload starts");

		logger.info("Method : dailyAttExcelUpload ends");

		return "employee/upload-daily-attendance-excel";
	}

	/*
	 * for file upload
	 */
	@PostMapping("/daily-attendance-excel-upload-uploadExcelFile")
	public @ResponseBody JsonResponse<Object> uploadExcelFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadExcelFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("employeeExcelFile", inputFile.getBytes());
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadExcelFile controller function 'post-mapping' ends");
		return response;
	}

	/*
	 * Post Mapping for adding excel data
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@PostMapping("/daily-attendance-excel-upload-save")
	public @ResponseBody JsonResponse<Object> saveAllExcelAttendanceData(@RequestBody String currDate, Model model,
			HttpSession session) {

		logger.info("Method : saveAllExcelData Post starts");
		JsonResponse<Object> response = new JsonResponse<Object>();
		int imagecount = 0;
		String s = "";
		String x = "";

		String excelName = null;
		try {
			byte[] bytes = (byte[]) session.getAttribute("employeeExcelFile");
			if (bytes != null) {
				long nowTime = new Date().getTime();
				excelName = nowTime + ".xlsx";
			}

			Path path = Paths.get(env.getFileUploadEmployee() + excelName);
			System.out.println("path" + path);
			if (bytes != null) {
				Files.write(path, bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			String fileName = env.getFileUploadEmployee() + excelName;
			System.out.println("fetching path " + fileName);
			List<HrmsDailyAttendanceExcelModel> excelAttendanceList = new ArrayList<HrmsDailyAttendanceExcelModel>();
			try {
				excelAttendanceList = readExcelData.readFile(fileName);
				System.out.println("excelAttendanceList " + excelAttendanceList);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			/* excelAttendanceList.forEach(a -> a.setDate(currDate)); */
			response = restClient.postForObject(env.getEmployeeUrl() + "saveAllExcelAttendanceData",
					excelAttendanceList, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null && response.getMessage() != "") {

			response.setCode(response.getCode());
			response.setMessage(response.getMessage());

		} else {
			response.setMessage("Success");
		}

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Method : saveAllExcelAttendanceData ends");
		return response;
	}

	@GetMapping("/daily-attendance-excel-upload-view")
	public String dailyAttExcelUploadView(Model model, HttpSession session) {

		logger.info("Method : dailyAttExcelUpload starts");

		logger.info("Method : dailyAttExcelUpload ends");

		return "employee/view-daily-att-excel";
	}

	/*
	 * get production details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/daily-attendance-excel-upload-get-data" })
	public @ResponseBody JsonResponse<HrmsDailyAttendanceExcelModel> getDailyData(@RequestParam String date) {
		logger.info("Method : getDailyData starts");

		JsonResponse<HrmsDailyAttendanceExcelModel> res = new JsonResponse<HrmsDailyAttendanceExcelModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "get-daily-data?date=" + date, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getDailyData  ends");
		return res;
	}

	/*
	 * Post Mapping for save daily att data
	 */
	@SuppressWarnings({ "unchecked" })
	@PostMapping("/save-daily-attendance")
	public @ResponseBody JsonResponse<Object> saveAllAttendanceData(
			@RequestBody List<HrmsDailyAttendanceExcelModel> excelAttendanceList, Model model, HttpSession session) {

		logger.info("Method : saveAllAttendanceData Post starts");
		JsonResponse<Object> response = new JsonResponse<Object>();
		try {
			response = restClient.postForObject(env.getEmployeeUrl() + "saveAllAttendanceData", excelAttendanceList,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null && response.getMessage() != "") {

			response.setCode(response.getCode());
			response.setMessage(response.getMessage());

		} else {
			response.setMessage("Success");
		}

		logger.info("Method : saveAllAttendanceData ends");
		return response;
	}

	@GetMapping("/daily-attendance-updated-view")
	public String dailyAttExcelUpdatedView(Model model, HttpSession session) {

		logger.info("Method : dailyAttExcelUpload starts");

		logger.info("Method : dailyAttExcelUpload ends");

		return "employee/view-daily-att-update";
	}
	
	
	/*
	 * get production details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/daily-attendance-updated-view-details" })
	public @ResponseBody JsonResponse<HrmsDailyAttendanceExcelModel> getUpatedData(@RequestParam String date) {
		logger.info("Method : getUpatedData starts");

		JsonResponse<HrmsDailyAttendanceExcelModel> res = new JsonResponse<HrmsDailyAttendanceExcelModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "get-daily-updated-data?date=" + date, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getUpatedData  ends");
		return res;
	}
	
	
}
