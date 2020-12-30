package nirmalya.aathithya.webmodule.inventory.controller;

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

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.InventoryStockReportModel;



/*
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class InventoryStockReportController {
	Logger logger = LoggerFactory.getLogger(InventoryStockReportController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	/*
	 * 
	 * 
	 * Default Generate Report Page 
	 * 
	 * 
	 */
	@GetMapping("/view-inventory-stock-generate-report")
	public String generateInventoryStockReport(Model model, HttpSession session) {

		logger.info("Method : generateInventoryStockReport starts");
		/*
		 * dropdown data for store name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getInventoryUrl() + "report-restGet-store",
					DropDownModel[].class);
			List<DropDownModel> storeName = Arrays.asList(dropDownModel);
			model.addAttribute("storeName", storeName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/*
		 * dropdown for godown name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getInventoryUrl() + "report-restGet-godown",
					DropDownModel[].class);
			List<DropDownModel> godownName = Arrays.asList(dropDownModel);
			model.addAttribute("godownName", godownName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/*
		 * dropdown for item Name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getInventoryUrl() + "report-restGet-item",
					DropDownModel[].class);
			List<DropDownModel>itemName = Arrays.asList(dropDownModel);
			model.addAttribute("itemName", itemName);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateInventoryStockReport ends");
		return "inventory/inventoryStockReport";
		
	}

	/*
	 * View Preview page
	 *
	 */

	@GetMapping(value = { "preview-inventory-stock-report" })
	public String previewInventoryStockReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3) {
		logger.info("Method : previewInventoryStockReport starts");

		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);
		model.addAttribute("param3", param3);
	

		logger.info("Method : previewInventoryStockReport ends");
		return "inventory/inventoryStockReportPreview";
	}

	/*
	 * View Preview page through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/preview-inventory-stock-report-throughAjax")
	public @ResponseBody DataTableResponse previewInventoryStockReportThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedPraram3) {
		logger.info("Method : previewInventoryStockReportThroughAjax starts");
		//byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
	//	byte[] encodeByte3 = Base64.getDecoder().decode(encodedPraram3.getBytes());
	

		//String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		//String param3 = (new String(encodeByte3));
	
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam1(encodedPraram1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(encodedPraram3);
		
		JsonResponse<List<InventoryStockReportModel>> jsonResponse = new JsonResponse<List<InventoryStockReportModel>>();
		try {
			jsonResponse = restTemplate.postForObject(env.getInventoryUrl() + "previewInventoryStockReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryStockReportModel> stockReport = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryStockReportModel>>() {
					});
			/*for (InventoryStockReportModel m : stockReport) {
			Double total = m.getCreditAmount() - m.getDebitAmount();
			m.settTotalAmount(total);
	
			}*/


			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(stockReport);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : previewInventoryStockReportThroughAjax ends");
		return response;
	}

	/**
	 * download inventory stock report
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/download-inventory-stock-report")
	public void generatInventoryStockReportPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {
		logger.info("Method : generatInventoryStockReportPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		
	

		JsonResponse<List<InventoryStockReportModel>> jsonResponse = new JsonResponse<List<InventoryStockReportModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			

			jsonResponse = restTemplate.postForObject(env.getInventoryUrl() + "generatInventoryStockReportPdf", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		/**
		 * get Hotel Logo
		 *
		 *//*
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restTemplate.getForObject(
					env.getInventoryUrl() + "restLogoImage-stockReport?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		*//**
		 * get Hotel Logo Background
		 *
		 *//*
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restTemplate.getForObject(
					env.getInventoryUrl() + "restLogoImage-stockReport?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}*/
		List<InventoryStockReportModel> stockReport = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryStockReportModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		String curDate = "";
		String store = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String variable = env.getBaseUrlPath();
		/*String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();*/
System.out.println("stockReport"+stockReport);
		String currdate = "";
		if (stockReport.size() != 0) {

			stockReport.get(0).setCurDate(curDate);
			currdate = stockReport.get(0).getCurDate();
			store=stockReport.get(0).getStore();

			

			/*data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");*/
			data.put("currdate", currdate);
		
			data.put("store", store);
			
			data.put("stockReport", stockReport);
		} else {

			/*data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");*/
			data.put("store", store);
		
			data.put("currdate", curDate);
		}
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryStockReport.pdf");
		File file;
		byte[] fileData = null;
		try {

			file = pdfGeneratorUtil.createPdf("inventory/inventoryStockReportPdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			// response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : generatInventoryStockReportPdf ends");
	}

}
