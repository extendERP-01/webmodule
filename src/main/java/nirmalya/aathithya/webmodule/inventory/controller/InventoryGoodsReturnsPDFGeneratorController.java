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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.io.IOUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReturnsNoteModel;

@Controller
@RequestMapping(value = "download/")

public class InventoryGoodsReturnsPDFGeneratorController {
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(InventoryGoodsReturnsPDFGeneratorController.class);

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/inventory-goods-returns-pdf" })
	public void generateInventoryGoodsreturnPdf(HttpServletResponse response, Model model,
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
		// Double gtotal = 0.0;
		// Double ggtotal = 0.0;
		JsonResponse<List<InventoryGoodsReturnsNoteModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReturnsNoteModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-all-goodsreturn-pdf", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(env.getInventoryUrl() + "restLogoImage-goodsReturn?logoType="+"background-Logo", DropDownModel[].class);
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
			DropDownModel[] logo = restClient.getForObject(env.getInventoryUrl()+ "restLogoImage-goodsReturn?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryGoodsReturnsNoteModel> goodreturns = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryGoodsReturnsNoteModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String currdate = "";
		String dateFrom = "";
		String dateTo = "";
		if (goodreturns.size() != 0) {

			goodreturns.get(0).setCurDate(curDate);
			currdate = goodreturns.get(0).getCurDate();
			goodreturns.get(0).setDateFrom(param3);
			dateFrom = goodreturns.get(0).getDateFrom();

			goodreturns.get(0).setDateTo(param4);
			dateTo = goodreturns.get(0).getDateTo();

			data.put("currdate", currdate);
			data.put("dateFrom", dateFrom);
			data.put("dateTo", dateTo);
			data.put("image",variable+"image/"+background+"");
	    	data.put("logoImage",variable+"image/"+logo+"");
		} else {

			data.put("currdate", curDate);
			data.put("dateFrom", param3);
			data.put("dateTo", param4);
			data.put("items", "");
			data.put("image",variable+"image/"+background+"");
	    	data.put("logoImage",variable+"image/"+logo+"");
		}

		for (InventoryGoodsReturnsNoteModel m : goodreturns) {
			if (m.getgRtNActive()) {
				s = "active";
			} else {
				s = "inactive";
			}
			m.setActivity(s);

		}
		data.put("IgoodsReturn", goodreturns);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryGoodsReturnNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/goodsreturn_pdf", data);
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

	@SuppressWarnings("unchecked")
	@GetMapping("inventory-view-goods-returns-pdf")
	public void generateOneInventoryGoodsReturnPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<InventoryGoodsReturnsNoteModel> jsonResponse = new JsonResponse<InventoryGoodsReturnsNoteModel>();
		try {

			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "get-goodsreturn-modal-pdf?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(env.getInventoryUrl() + "restLogoImage-goodsReturn?logoType="+"background-Logo", DropDownModel[].class);
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
			DropDownModel[] logo = restClient.getForObject(env.getInventoryUrl()+ "restLogoImage-goodsReturn?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<InventoryGoodsReturnsNoteModel> inventoryGoodsReturnNoteModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryGoodsReturnsNoteModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		if (!inventoryGoodsReturnNoteModel.isEmpty()) {
			String grnNumber = inventoryGoodsReturnNoteModel.get(0).getGoodsReturnNote();
			String purchaseOrder = inventoryGoodsReturnNoteModel.get(0).getPurchaseOrder();
			String invoiceNumber = inventoryGoodsReturnNoteModel.get(0).getgRNInvoiceId();
			Boolean grnActive = inventoryGoodsReturnNoteModel.get(0).getgRtNActive();
			String invNo = inventoryGoodsReturnNoteModel.get(0).getInvNo();
			String gRtN_CreatedOn = inventoryGoodsReturnNoteModel.get(0).getgRtN_CreatedOn();
			String itemId = inventoryGoodsReturnNoteModel.get(0).getItemId();
			String itemName = inventoryGoodsReturnNoteModel.get(0).getItemName();
			Double gRtNQty = inventoryGoodsReturnNoteModel.get(0).getgRtNQty();
			Double quantityReceived = inventoryGoodsReturnNoteModel.get(0).getQuantityReceived();
			Double price = inventoryGoodsReturnNoteModel.get(0).getPrice();
			String serveUnit = inventoryGoodsReturnNoteModel.get(0).getServeUnit();
			String vendorName = inventoryGoodsReturnNoteModel.get(0).getVendorName();
			String vendorAddr = inventoryGoodsReturnNoteModel.get(0).getVendorAddr();

			data.put("goodsReturnNote", grnNumber);
			data.put("purchaseOrder", purchaseOrder);
			data.put("gRNInvoiceId", invoiceNumber);
			data.put("gRtNActive", grnActive);
			data.put("invNo", invNo);
			data.put("gRtN_CreatedOn", gRtN_CreatedOn);
			data.put("itemId", itemId);
			data.put("itemName", itemName);
			data.put("gRtNQty", gRtNQty);
			data.put("quantityReceived", quantityReceived);
			data.put("price", price);
			data.put("serveUnit", serveUnit);
			data.put("vendorName", vendorName);
			data.put("vendorAddr", vendorAddr);
			data.put("image",variable+"image/"+background+"");
	    	data.put("logoImage",variable+"image/"+logo+"");
			
		}
		for (InventoryGoodsReturnsNoteModel m : inventoryGoodsReturnNoteModel) {
			Double total = m.getgRtNQty() * m.getPrice();
			m.setTotal(total);
			if (m.getgRtNActive()) {
				s = "Active";
			} else {
				s = "Inactive";
			}

			m.setActivity(s);
			data.put("gRtNActive", s);
		}
		data.put("OneGoodsreturn", inventoryGoodsReturnNoteModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryGoodsReturns.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/goodsreturn1_pdf", data);
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

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-inventory-goods-returns-download-report" })
	public void generateInventoryGoodsreturnReport(HttpServletResponse response, Model model,
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
		// Double gtotal = 0.0;
		// Double ggtotal = 0.0;
		JsonResponse<List<InventoryGoodsReturnsNoteModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReturnsNoteModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-all-goodsreturn-report", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(env.getInventoryUrl() + "restLogoImage-goodsReturn?logoType="+"background-Logo", DropDownModel[].class);
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
			DropDownModel[] logo = restClient.getForObject(env.getInventoryUrl()+ "restLogoImage-goodsReturn?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryGoodsReturnsNoteModel> goodreturns = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryGoodsReturnsNoteModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String currdate = "";
		String dateFrom = "";
		String dateTo = "";
		if (goodreturns.size() != 0) {

			goodreturns.get(0).setCurDate(curDate);
			currdate = goodreturns.get(0).getCurDate();
			goodreturns.get(0).setDateFrom(param3);
			dateFrom = goodreturns.get(0).getDateFrom();

			goodreturns.get(0).setDateTo(param4);
			dateTo = goodreturns.get(0).getDateTo();

			data.put("currdate", currdate);
			data.put("dateFrom", dateFrom);
			data.put("dateTo", dateTo);
			data.put("image",variable+"image/"+background+"");
	    	data.put("logoImage",variable+"image/"+logo+"");
			
			// data.put("bookTableModels", bookTableModel);
		} else {

			data.put("currdate", curDate);
			data.put("dateFrom", param3);
			data.put("dateTo", param4);
			data.put("items", "");
			data.put("image",variable+"image/"+background+"");
	    	data.put("logoImage",variable+"image/"+logo+"");
			
		}

		for (InventoryGoodsReturnsNoteModel m : goodreturns) {
			if (m.getgRtNActive()) {
				s = "active";
			} else {
				s = "inactive";
			}
			m.setActivity(s);

		}
		data.put("IgoodsReturn", goodreturns);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryGoodsReturnNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/goodsreturn_pdf", data);
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
