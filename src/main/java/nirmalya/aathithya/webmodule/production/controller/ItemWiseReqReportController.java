package nirmalya.aathithya.webmodule.production.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.common.utils.StringConstands;
import nirmalya.aathithya.webmodule.production.excel.ItemWiseRequisitionExcelReport;
import nirmalya.aathithya.webmodule.production.excel.ItemWiseSaleOrderExcelReport;
import nirmalya.aathithya.webmodule.production.model.ItemWiseReqModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "production/")
public class ItemWiseReqReportController {

	Logger logger = LoggerFactory.getLogger(ItemWiseReqReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * Get Mapping for add employee dependent
	 */

	@GetMapping("item-req-report")
	public String itemReqReport(Model model, HttpSession session) {

		logger.info("Method : itemReqReport starts");
		/**
		 * Get DropDown Value Store List
		 *
		 */
		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : itemReqReport ends");
		return "production/req-store-wise-report";
	}

	/*
	 * Generate Pdf For mother coil slit
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/item-req-report-download" })
	public void viewMotherCoilSlitReport(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		String storeId = (new String(encodeByte1));
		String date = (new String(encodeByte2));

		JsonResponse<List<ItemWiseReqModel>> jsonResponse = new JsonResponse<List<ItemWiseReqModel>>();

		try {

			jsonResponse = restClient.getForObject(
					env.getProduction() + "get-req-item-wise?storeId=" + storeId + "&date=" + date, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<ItemWiseReqModel> packagingList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<ItemWiseReqModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();

		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getProduction() + "restLogoImage?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);

			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();
		String logo = logoList.get(0).getName();
		data.put("packagingList", packagingList);
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=PipepackagingReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("production/pipe-packaging-generate-report-pdf", data);
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
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("item-req-report-excel")
	public ModelAndView downloadExcelForItemRequisition(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {
		logger.info("Method : downloadExcelForItemRequisition starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<ItemWiseReqModel>> jsonResponse = new JsonResponse<List<ItemWiseReqModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		try {

			jsonResponse = restClient.postForObject(env.getProduction() + "get-requisition-items-reports", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ItemWiseReqModel> itemReqiList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ItemWiseReqModel>>() {
					});
			if (!itemReqiList.isEmpty() && itemReqiList != null) {
				for (ItemWiseReqModel s : itemReqiList) {
					s.setSum(s.getStoreAngul() + s.getStoreBargarh() + s.getStoreBeheramal() + s.getStoreBudharaja()
							+ s.getStoreCityCenter() + s.getStoreCuttack() + s.getStoreGoleBazar() + s.getStorePatia()
							+ s.getStoreSarabahal() + s.getStoreSnagar());
				}
			}

			map.put("itemReqiList", itemReqiList);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition",
					"attachment; filename=" + "Reqi_Item" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("downloadExcelForItemRequisition -> ItemWiseReqReport GET", e);
		}
		logger.info("Method : downloadExcelForItemRequisition ends");
		return new ModelAndView(new ItemWiseRequisitionExcelReport(), map);
	}

	/*
	 * Get Mapping for add employee dependent
	 */

	@GetMapping("item-sale-order-report")
	public String itemSaleOrderReport(Model model, HttpSession session) {

		logger.info("Method : itemSaleOrderReport starts");
		/**
		 * Get DropDown Value Store List
		 *
		 */
		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : itemSaleOrderReport ends");
		return "production/sale-order-item-wise";
	}

	/*
	 * Excel Download
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("item-sale-order-report-excel")
	public ModelAndView downloadExcelForItemSaleOrder(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2) {
		logger.info("Method : downloadExcelForItemSaleOrder starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));

		JsonResponse<List<ItemWiseReqModel>> jsonResponse = new JsonResponse<List<ItemWiseReqModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		try {

			jsonResponse = restClient.postForObject(env.getProduction() + "get-sale-order-items-reports", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ItemWiseReqModel> itemReqiList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ItemWiseReqModel>>() {
					});
			  
			map.put("itemReqiList", itemReqiList);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition",
					"attachment; filename=" + "Reqi_Item" + new Date().getTime() + ".xls");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("downloadExcelForItemRequisition -> downloadExcelForItemSaleOrder GET", e);
		}
		logger.info("Method : downloadExcelForItemSaleOrder ends");
		return new ModelAndView(new ItemWiseSaleOrderExcelReport(), map);
	}

}
