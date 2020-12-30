/**
 * 
 */
package nirmalya.aathithya.webmodule.inventory.controller;

import java.io.File;
import java.io.FileInputStream; 
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReceiveModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryStoreDetailsModel;

/**
 * @author USER
 *
 */
@Controller
//@RequestMapping(value = "download/")
public class InvGoodsReceivePdfController {

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(InvGoodsReceivePdfController.class);

	/*
	 * 
	 * method to print pdf
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download/download-goods-receive-note")
	public void generateGoodsReceiveNotePdf(HttpServletResponse response, Model model,
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
		JsonResponse<List<InventoryGoodsReceiveModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReceiveModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "gets-alls-goods-receives-pdfs",
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
			DropDownModel[] logoBg = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-goodsReceive?logoType=" + "background-Logo",
					DropDownModel[].class);
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
			DropDownModel[] logo = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-goodsReceive?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryGoodsReceiveModel> invGoodsReceiveModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryGoodsReceiveModel>>() {
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

		if (!invGoodsReceiveModel.isEmpty()) {

			invGoodsReceiveModel.get(0).setCurDate(curDate);
			currdate = invGoodsReceiveModel.get(0).getCurDate();

			invGoodsReceiveModel.get(0).setDateFrom(param3);
			dateFrom = invGoodsReceiveModel.get(0).getDateFrom();

			invGoodsReceiveModel.get(0).setDateTo(param4);
			dateTo = invGoodsReceiveModel.get(0).getDateTo();
			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");

			data.put("currdate", currdate);
			data.put("dateFrom", dateFrom);
			data.put("dateTo", dateTo);

		} else {
			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
			data.put("currdate", curDate);
			data.put("dateFrom", param3);
			data.put("dateTo", param4);
			data.put("items", "");
		}

		for (InventoryGoodsReceiveModel m : invGoodsReceiveModel) {

			if (Boolean.TRUE.equals(m.getgRnInvoiceActive())) {

				s = "Active";
			} else {
				s = "Inactive";
			}

			m.setStatus(s);
		}

		data.put("items", invGoodsReceiveModel);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryGoodsReceiveNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/InventoryGoodsReceiveNote_pdf", data);
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
	 * method to print report
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download/view-inventory-goods-receives-pdfs")
	public void generateOneInventoryGoodsReceivePdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<InventoryGoodsReceiveModel> jsonResponse = new JsonResponse<InventoryGoodsReceiveModel>();
		try {

			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "gets-goods-receives-news-pdfs?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-goodsReceive?logoType=" + "background-Logo",
					DropDownModel[].class);
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
			DropDownModel[] logo = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-goodsReceive?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryGoodsReceiveModel> invGoodsReceiveModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryGoodsReceiveModel>>() {
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
		invGoodsReceiveModel.get(0).setCurDate(curDate);

		if (!invGoodsReceiveModel.isEmpty()) {
			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");

			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");

			data.put("items", invGoodsReceiveModel);
		}

		for (InventoryGoodsReceiveModel m : invGoodsReceiveModel) {

			// Boolean status = m.getgRnInvoiceActive();
			if (Boolean.TRUE.equals(m.getgRnInvoiceActive())) {
				s = "Active";
			} else {
				s = "Inactive";
			}

			m.setStatus(s);
			data.put("gRnInvoiceActive", s);
		}

		if (!invGoodsReceiveModel.isEmpty()) {
			try {
				InventoryStoreDetailsModel[] storeDetails = restClient.getForObject(
						env.getInventoryUrl() + "getStoresDetails?id=" + invGoodsReceiveModel.get(0).getGodown(),
						InventoryStoreDetailsModel[].class);
				List<InventoryStoreDetailsModel> hotelList = Arrays.asList(storeDetails);

				data.put("hotelList", hotelList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
		}

		data.put("OneGoodsReceive", invGoodsReceiveModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InvGoodsReceiveNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/goodsreceive_pdf", data);
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
	 * method to print one pdf
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("inventory/view-goods-receive-note-download-report")
	public void generateGoodsReceiveNoteReport(HttpServletResponse response, Model model,
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

		JsonResponse<List<InventoryGoodsReceiveModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReceiveModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {

			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "gets-alls-goods-receives-report",
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
			DropDownModel[] logoBg = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-goodsReceive?logoType=" + "background-Logo",
					DropDownModel[].class);
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
			DropDownModel[] logo = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-goodsReceive?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		ObjectMapper mapper = new ObjectMapper();

		List<InventoryGoodsReceiveModel> invGoodsReceiveModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<InventoryGoodsReceiveModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		String currdate = "";
		String dateFrom = "";
		String dateTo = "";

		if (!invGoodsReceiveModel.isEmpty()) {

			invGoodsReceiveModel.get(0).setCurDate(curDate);
			currdate = invGoodsReceiveModel.get(0).getCurDate();

			invGoodsReceiveModel.get(0).setDateFrom(param3);
			dateFrom = invGoodsReceiveModel.get(0).getDateFrom();

			invGoodsReceiveModel.get(0).setDateTo(param4);
			dateTo = invGoodsReceiveModel.get(0).getDateTo();

			data.put("currdate", currdate);
			data.put("dateFrom", dateFrom);
			data.put("dateTo", dateTo);
			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");

		} else {

			data.put("currdate", curDate);
			data.put("dateFrom", param3);
			data.put("dateTo", param4);
			data.put("items", "");
			data.put("image", variable + "image/" + background + "");
			data.put("logoImage", variable + "image/" + logo + "");
		}

		for (InventoryGoodsReceiveModel m : invGoodsReceiveModel) {

			if (Boolean.TRUE.equals(m.getgRnInvoiceActive())) {

				s = "Active";
			} else {
				s = "Inactive";
			}

			m.setStatus(s);
		}

		data.put("items", invGoodsReceiveModel);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryGoodsReceiveNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/InventoryGoodsReceiveNote_pdf", data);
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
