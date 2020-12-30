package nirmalya.aathithya.webmodule.inventory.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemRequisitionModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryStoreDetailsModel;

@Controller
// @RequestMapping(value = "download/")
public class ItemRequisitionPDFGeneratorController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles environmentVaribles;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(ItemRequisitionPDFGeneratorController.class);

	/*
	 * 
	 * 
	 * Method to view report
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download/view-item-requisitions-pdf")
	public void InventoryItemRequisitionPdf(HttpSession session, HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4,
			@RequestParam("param5") String encodedParam5) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception e) {

		}
		JsonResponse<List<InventoryItemRequisitionModel>> jsonResponse = new JsonResponse<List<InventoryItemRequisitionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);

			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "generate-all-Item-requsitionsPdf", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryItemRequisitionModel> inventoryItemRequisitionModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryItemRequisitionModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String curDate = "";
		String printDate = "";

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		for (InventoryItemRequisitionModel m : inventoryItemRequisitionModel) {
			m.setPrintedBy(userName);
			if (m.getiRStatus()) {
				m.setStatus("Active");
			} else {
				m.setStatus("Inactive");
			}
		}

		if (inventoryItemRequisitionModel.size() != 0) {
			inventoryItemRequisitionModel.get(0).setCurrentDate(curDate);
			printDate = inventoryItemRequisitionModel.get(0).getCurrentDate();
			data.put("printedBy", userName);
			data.put("printDate", printDate);
		} else {
			data.put("printDate", printDate);
			data.put("printedBy", userName);
		}

		data.put("itemRequisition", inventoryItemRequisitionModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryItemRequisition.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/item_requisition", data);
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
	@GetMapping("inventory/view-item-requisitions-download-report")
	public void generateInventoryItemRequisitionPdf(HttpSession session, HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4,
			@RequestParam("param5") String encodedParam5) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception e) {

		}
		JsonResponse<List<InventoryItemRequisitionModel>> jsonResponse = new JsonResponse<List<InventoryItemRequisitionModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-all-Item-requsitionsPdf", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryItemRequisitionModel> inventoryItemRequisitionModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryItemRequisitionModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String curDate = "";
		String printDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		for (InventoryItemRequisitionModel m : inventoryItemRequisitionModel) {
			m.setPrintedBy(userName);
			if (m.getiRStatus()) {
				m.setStatus("Active");
			} else {
				m.setStatus("Inactive");
			}
		}

		if (inventoryItemRequisitionModel.size() != 0) {
			inventoryItemRequisitionModel.get(0).setCurrentDate(curDate);
			printDate = inventoryItemRequisitionModel.get(0).getCurrentDate();

			data.put("printedBy", userName);
			data.put("printDate", printDate);
		} else {
			data.put("printDate", printDate);
			data.put("printedBy", userName);
		}

		data.put("itemRequisition", inventoryItemRequisitionModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryItemRequisition.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/item_requisition", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	/*
	 * 
	 * Method to view individual report
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("download/inventory-view-one-item-requisitions-pdf")
	public void generateOneInventoryItemRequisitionPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		Double gtotal = 0.0;
		Double grandtotal = 0.0;
		String itemRequisition = "";
		String costCenter = "";
		String expectedDate = "";
		String requisitionType = "";
		String description = "";
		String createdDate = "";
		String requisitionBy = "";
		JsonResponse<InventoryItemRequisitionModel> jsonResponse = new JsonResponse<InventoryItemRequisitionModel>();
		try {

			jsonResponse = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "getRequisitionPdf?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<InventoryItemRequisitionModel> inventoryItemRequisitionModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryItemRequisitionModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		if (!inventoryItemRequisitionModel.isEmpty()) {
			itemRequisition = inventoryItemRequisitionModel.get(0).getItemRequisition();
			costCenter = inventoryItemRequisitionModel.get(0).getCostCenter();
			expectedDate = inventoryItemRequisitionModel.get(0).getiRExpectedDate();
			requisitionType = inventoryItemRequisitionModel.get(0).getiRType();
			description = inventoryItemRequisitionModel.get(0).getiRDescription();
			createdDate = inventoryItemRequisitionModel.get(0).getCreatedDate();
			requisitionBy = inventoryItemRequisitionModel.get(0).getCreatedBy();
			grandtotal = inventoryItemRequisitionModel.get(0).getTotal();

			data.put("itemRequisition", itemRequisition);
			data.put("costCenter", costCenter);
			data.put("expectedDate", expectedDate);
			data.put("grandtotal", grandtotal);
			data.put("requisitionType", requisitionType);
			data.put("description", description);
			data.put("createdDate", createdDate);
			data.put("requisitionBy", requisitionBy);
		} else {
			data.put("itemRequisition", itemRequisition);
			data.put("costCenter", costCenter);
			data.put("expectedDate", expectedDate);
			data.put("requisitionType", requisitionType);
			data.put("description", description);
			data.put("grandtotal", grandtotal);
			data.put("createdDate", createdDate);
			data.put("requisitionBy", requisitionBy);
			data.put("OneItemRequisition", "");
			data.put("status", "");
		}
		for (InventoryItemRequisitionModel m : inventoryItemRequisitionModel) {
			if (Boolean.TRUE.equals(m.getiRStatus())) {
				s = "Active";
			} else {
				s = "Inactive";
			}
			m.setStatus(s);
		}

		for (InventoryItemRequisitionModel m : inventoryItemRequisitionModel) {
			Double allTotal = m.getDlQty();
			gtotal = gtotal + allTotal;

			m.setTotal(gtotal);

		}
		data.put("gtotal", gtotal);
		if (!inventoryItemRequisitionModel.isEmpty()) {
			try {
				InventoryStoreDetailsModel[] storeDetails = restTemplate
						.getForObject(
								environmentVaribles.getInventoryUrl() + "getStoresDetails?id="
										+ inventoryItemRequisitionModel.get(0).gettStore(),
								InventoryStoreDetailsModel[].class);
				List<InventoryStoreDetailsModel> hotelList = Arrays.asList(storeDetails);

				data.put("hotelList", hotelList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
		}
		data.put("status", s);
		data.put("OneItemRequisition", inventoryItemRequisitionModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryOneItemRequisition.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/item_requisitionDetail", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}