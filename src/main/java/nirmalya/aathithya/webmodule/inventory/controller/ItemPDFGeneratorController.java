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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemModel;

@Controller
public class ItemPDFGeneratorController {

	@Autowired
	RestTemplate restTemplate; 
	
	@Autowired
	EnvironmentVaribles environmentVaribles;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(ItemPDFGeneratorController.class);

	/*
	 * 
	 * 
	 * Method to view report
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@GetMapping("download/view-items-download-pdf")
	public void generateInventoryItemPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<InventoryItemModel>> jsonResponse = new JsonResponse<List<InventoryItemModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-ItemsPdf",
					tableRequest, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "restLogoImage-item?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "restLogoImage-item?logoType="+"background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		List<InventoryItemModel> inventoryItemModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryItemModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String printBy = "p.k Ghosh";
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String variable = environmentVaribles.getBaseUrlPath();
		String logo = logoList.get(0).getName();
		String background = logoBgList.get(0).getName();
		for (InventoryItemModel m : inventoryItemModel) {
			m.setPrintedBy(printBy);
			if (m.getItemActive()) {
				m.setStatus("Active");
			} else {
				m.setStatus("Inactive");
			}
		}

		if (inventoryItemModel.size() != 0) {
			inventoryItemModel.get(0).setCurrentDate(curDate);
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("image",variable+"image/"+background+"");
		} else {
			
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("image",variable+"image/"+background+"");
		}

		data.put("item", inventoryItemModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=ItemReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/item", data);
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

	/*
	 * 
	 * Method to generate report
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("inventory/view-items-download-report")
	public void generateInventoryItemReport(HttpServletResponse response, Model model, HttpSession session,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3 ,@RequestParam("param4") String encodedParam4) {
		
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));

		JsonResponse<List<InventoryItemModel>> jsonResponse = new JsonResponse<List<InventoryItemModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			System.out.println(tableRequest);
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-ItemsPdf",
					tableRequest, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restTemplate.getForObject(environmentVaribles.getInventoryUrl() + "restLogoImage-item?logoType="+"background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "restLogoImage-item?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryItemModel> inventoryItemModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryItemModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch(Exception e) {
			e.printStackTrace();
		}
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String variable = environmentVaribles.getBaseUrlPath();
		String logo = logoList.get(0).getName();
		String background = logoBgList.get(0).getName();
		for (InventoryItemModel m : inventoryItemModel) {
			
			if(m.getItemActive()!=null) {
				m.setPrintedBy(userName);
			if (m.getItemActive()) {
				m.setStatus("Active");
			} else {
				m.setStatus("Inactive");
			}
			}
			if(m.getTsalesSubGroup()=="" || m.getTsalesSubGroup()==null) {
				m.setTsalesSubGroup("N/A");
			}
			if(m.gettPurchaseSubGroup()=="" || m.gettPurchaseSubGroup()==null) {
				m.settPurchaseSubGroup("N/A");
			}
		} 
		if (inventoryItemModel.size() != 0) {
			//inventoryItemModel.get(0).setCurrentDate(curDate);
			data.put("logoImage", variable + "document/hotel/" + logo + "");
			data.put("image",variable+"document/hotel/"+background+"");
			data.put("printedBy", userName);
			data.put("item", inventoryItemModel);
			data.put("curDate", curDate);
		} else {
			data.put("logoImage", variable + "document/hotel/" + logo + "");
			data.put("image",variable+"document/hotel/"+background+"");
			data.put("printedBy", "");
			data.put("item", "");
			data.put("curDate", "");
		}
	
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=ItemReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/item", data);
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