package nirmalya.aathithya.webmodule.inventory.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryPurchaseOrderModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryStoreDetailsModel;

@Controller
//@RequestMapping(value = "inventory/")
public class PurchaseOrderPDFGeneratorController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles environmentVaribles;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(PurchaseOrderPDFGeneratorController.class);

	@SuppressWarnings("unchecked")
	@GetMapping("download/inventory-purchase-order")
	public void generateInventoryPurchaseOrderPdf(HttpServletResponse response, Model model,
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
		JsonResponse<List<InventoryPurchaseOrderModel>> jsonResponse = new JsonResponse<List<InventoryPurchaseOrderModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-all-purchase-orderPdf", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryPurchaseOrderModel> inventoryPurchaseOrderModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryPurchaseOrderModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		for (InventoryPurchaseOrderModel m : inventoryPurchaseOrderModel) {
			if (m.getpOStatus()) {
				m.setStatus("Active");
			} else {
				m.setStatus("Inactive");
			}

		}
		data.put("Porder", inventoryPurchaseOrderModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryPurchaseOrder.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/purchase_order", data);
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
	@GetMapping("inventory/view-purchase-order-download-report")
	public void generatePurchaseOrderReport(HttpSession session,HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4,
			@RequestParam("param5") String encodedParam5, @RequestParam("param6") String encodedParam6) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		byte[] encodeByte6 = Base64.getDecoder().decode(encodedParam6.getBytes());
		String userName = "";
		try {
			userName = (String)session.getAttribute("USER_NAME");
		}catch(Exception e) {
			
		}
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		String param6 = (new String(encodeByte6));

		JsonResponse<List<InventoryPurchaseOrderModel>> jsonResponse = new JsonResponse<List<InventoryPurchaseOrderModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			tableRequest.setParam6(param6);
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getInventoryUrl() + "get-purchase-order-report-Pdf", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryPurchaseOrderModel> InventoryPurchaseOrderModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryPurchaseOrderModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		String pattern = "dd-MM-yyyy HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		data.put("printDate", date);

		/*
		 * String curDate=""; String printDate=""; DateFormat dateFormat = new
		 * SimpleDateFormat("dd-MM-yyyy"); Date cal = new Date(); curDate=
		 * dateFormat.format(cal);
		 * InventoryPurchaseOrderModel.get(0).setCurrentDate(curDate);; printDate =
		 * InventoryPurchaseOrderModel.get(0).getCurrentDate();
		 * data.put("printDate",printDate);
		 */

		for (InventoryPurchaseOrderModel m : InventoryPurchaseOrderModel) {
			m.setPrintedBy(userName);
			if (m.getpOStatus()) {
				s = "Active";
			} else {
				s = "Inactive";
			}
			m.setStatus(s);
		}

		data.put("printedBy", userName);
		String backgroundImg = "http://localhost:8051/assets/images/invoicebg.png";
		data.put("backgroundImage", backgroundImg);
		// data.put("status", s);
		data.put("item", InventoryPurchaseOrderModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryItem.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/view_purchase_order_details_report", data);
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
	// @GetMapping("/download/view-one-purchase-order-pdf")
	@GetMapping("download/view-purchase-order-one-pdf")
	public void generateOneInventoryPurchaseOrderPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {
	 
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<InventoryPurchaseOrderModel> jsonResponse = new JsonResponse<InventoryPurchaseOrderModel>();
		try {

			jsonResponse = restTemplate.getForObject(
					environmentVaribles.getInventoryUrl() + "get-purchaseOrder-forModel?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<InventoryPurchaseOrderModel> inventoryPurchaseOrderModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryPurchaseOrderModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		String purchaseOrder = inventoryPurchaseOrderModel.get(0).getPurchaseOrder();
		String vendor = inventoryPurchaseOrderModel.get(0).getVendor();
		String porderDate = inventoryPurchaseOrderModel.get(0).getPorderDate();
		String validity = inventoryPurchaseOrderModel.get(0).getDeliveryPeriod();
		data.put("purchaseOrder", purchaseOrder);
		data.put("porderDate", porderDate);
		data.put("delivery", validity);
		data.put("vendor", vendor);

		
		if(!inventoryPurchaseOrderModel.isEmpty()) {
			try {
				InventoryStoreDetailsModel[] storeDetails = restTemplate.getForObject(
						environmentVaribles.getInventoryUrl() + "getStoresDetails?id=" + inventoryPurchaseOrderModel.get(0).getStore(),
						InventoryStoreDetailsModel[].class);
				List<InventoryStoreDetailsModel> hotelList = Arrays.asList(storeDetails);

				data.put("hotelList", hotelList); 
			} catch (RestClientException e) {
				e.printStackTrace();
			}
		}
		//String backgroundImg = "http://localhost:8051/assets/images/invoicebg.png";
		//data.put("backgroundImage", backgroundImg);

		Double subtotal = 0.0;
		Double grandtotal = 0.0;
		for (InventoryPurchaseOrderModel m : inventoryPurchaseOrderModel) {

			Double total = m.getPrice() ;;
			subtotal = subtotal + total;
			grandtotal = subtotal;
			m.setTotal(total);
			if (m.getpOStatus()) {
				s = "Active";
			} else {
				s = "Inactive";
			}
			m.setStatus(s);

		}

		String pattern = "dd-MM-yyyy HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		data.put("currdate", date);

		data.put("subTotal", grandtotal);
		data.put("status", s);
		data.put("OnePorder", inventoryPurchaseOrderModel);
		// data.put("currdate",date);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryOnePurchaseOrder.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/list_purchase_order", data);
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