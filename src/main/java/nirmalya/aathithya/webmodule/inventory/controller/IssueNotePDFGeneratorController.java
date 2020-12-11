package nirmalya.aathithya.webmodule.inventory.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryRequisitionIssueNoteModel;

@Controller
// @RequestMapping(value = "download/")
public class IssueNotePDFGeneratorController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles environmentVaribles;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(IssueNotePDFGeneratorController.class);

	/*
	 * 
	 * 
	 * Method to view report
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("download/view-issue-note-download-pdf")
	public void generateInventoryIssueNotePdf(HttpSession session, HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));

		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception e) {

		}
		JsonResponse<List<InventoryRequisitionIssueNoteModel>> jsonResponse = new JsonResponse<List<InventoryRequisitionIssueNoteModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-issue-note-Pdf",
					tableRequest, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryRequisitionIssueNoteModel> inventoryItemRequisitionIssueNoteModel = mapper
				.convertValue(jsonResponse.getBody(), new TypeReference<List<InventoryRequisitionIssueNoteModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String curDate = "";
		String printDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		for (InventoryRequisitionIssueNoteModel m : inventoryItemRequisitionIssueNoteModel) {
			m.setPrintedBy(userName);
			if (m.getpINoteActive()) {
				m.setStatus("Active");
			} else {
				m.setStatus("Inactive");
			}
		}

		if (inventoryItemRequisitionIssueNoteModel.size() != 0) {
			inventoryItemRequisitionIssueNoteModel.get(0).setCurrentDate(curDate);
			printDate = inventoryItemRequisitionIssueNoteModel.get(0).getCurrentDate();

			data.put("printedBy", userName);
			data.put("printDate", printDate);
		} else {
			data.put("printDate", printDate);
			data.put("printedBy", userName);
		}

		data.put("issueNote", inventoryItemRequisitionIssueNoteModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryIssueNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/issue_note", data);
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
	@GetMapping("inventory/view-issue-note-download-report")
	public void generateInventoryIssueNoteReport(HttpSession session, HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String userName = "";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception e) {

		}
		JsonResponse<List<InventoryRequisitionIssueNoteModel>> jsonResponse = new JsonResponse<List<InventoryRequisitionIssueNoteModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-all-issue-note-Report", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryRequisitionIssueNoteModel> inventoryItemRequisitionIssueNoteModel = mapper
				.convertValue(jsonResponse.getBody(), new TypeReference<List<InventoryRequisitionIssueNoteModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String curDate = "";
		String printDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		for (InventoryRequisitionIssueNoteModel m : inventoryItemRequisitionIssueNoteModel) {
			m.setPrintedBy(userName);
			if (m.getpINoteActive()) {
				m.setStatus("Active");
			} else {
				m.setStatus("Inactive");
			}
		}
		if (!inventoryItemRequisitionIssueNoteModel.isEmpty()) {
			inventoryItemRequisitionIssueNoteModel.get(0).setCurrentDate(curDate);

			printDate = inventoryItemRequisitionIssueNoteModel.get(0).getCurrentDate();
			data.put("printedBy", userName);
			data.put("printDate", curDate);
		} else {
			data.put("printedBy", userName);
			data.put("printDate", curDate);
		}

		data.put("issueNote", inventoryItemRequisitionIssueNoteModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryIssueNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/issue_note", data);
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
	 * Method to view individual report
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("download/inventory-view-one-issue-note-pdf")
	public void generateOneInventoryPurchaseOrderPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String reqstnIssueNote = "";
		String itemRequisition = "";
		String description = "";
		String issueCreate = "";
		JsonResponse<InventoryRequisitionIssueNoteModel> jsonResponse = new JsonResponse<InventoryRequisitionIssueNoteModel>();
		try {

			jsonResponse = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-issueNote-pdf?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<InventoryRequisitionIssueNoteModel> inventoryRequisitionIssueNoteModel = mapper
				.convertValue(jsonResponse.getBody(), new TypeReference<List<InventoryRequisitionIssueNoteModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		if (!inventoryRequisitionIssueNoteModel.isEmpty()) {
			reqstnIssueNote = inventoryRequisitionIssueNoteModel.get(0).getReqstnIssueNote();
			itemRequisition = inventoryRequisitionIssueNoteModel.get(0).getItemRequisition();
			description = inventoryRequisitionIssueNoteModel.get(0).getiNoteDescription();
			issueCreate = inventoryRequisitionIssueNoteModel.get(0).getIssueCreate();
			data.put("reqstnIssueNote", reqstnIssueNote);
			data.put("itemRequisition", itemRequisition);
			data.put("issueCreate", issueCreate);
			data.put("description", description);
		} else {
			data.put("reqstnIssueNote", reqstnIssueNote);
			data.put("itemRequisition", itemRequisition);
			data.put("issueCreate", issueCreate);
			data.put("description", description);
		}

		data.put("OneIssue", inventoryRequisitionIssueNoteModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryOneIssueNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/issue_noteDetail", data);
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