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
import org.springframework.web.bind.annotation.RequestMapping;
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
import nirmalya.aathithya.webmodule.inventory.model.PaymentVoucherModel;

@Controller
@RequestMapping(value = "report")
public class InventoryReportController {

	Logger logger = LoggerFactory.getLogger(InventoryPaymentVoucherController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * get mapping for view payment voucher
	 */
	@GetMapping("/view-payment-voucher-generate-report")
	public String viewPaymentVoucher(Model model, HttpSession session) {

		logger.info("Method : viewPaymentVoucher starts");

		logger.info("Method : viewPaymentVoucher ends");
		return "inventory/inventory-payment-voucher-report";
	}

	/*
	 * Generate Pdf For Assigned Asset
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-payment-voucher-download-report" })
	public void generateAssignedAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param3") String encodedParam3,
			@RequestParam("param4") String encodedParam4) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));

		JsonResponse<List<PaymentVoucherModel>> jsonResponse = new JsonResponse<List<PaymentVoucherModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		try {

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllPaymentVoucherReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<PaymentVoucherModel> PaymentVoucherModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<PaymentVoucherModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);
		double baseAmount = 0;
		double grandAmount = 0;
		double totalCgst = 0;
		double totaIgst = 0;
		if (PaymentVoucherModel.size() != 0) {
			for (PaymentVoucherModel a : PaymentVoucherModel) {
				baseAmount = baseAmount + a.getSubTotal();
				grandAmount = grandAmount + a.getTotal();
				if (a.getGoodsReturnNote() != null) {
					if (!a.getTaxType()) {
						totalCgst = totalCgst + a.getCgst() - a.getReturnCgst();
					} else {
						totaIgst = totaIgst + a.getIgst() - a.getReturnIgst();
					}
				}
			}
			PaymentVoucherModel.get(0).setSubTotal(baseAmount);
			PaymentVoucherModel.get(0).setTotal(grandAmount);
			data.put("totalSgst",totalCgst);
			data.put("totalCgst",totalCgst);
			data.put("totalIgst",totaIgst);
		}
		data.put("PaymentVoucherModel", PaymentVoucherModel);
		if (PaymentVoucherModel.size() != 0) {
			data.put("paymentVoucher", PaymentVoucherModel.get(0).getPaymentVoucher());
			data.put("transactionDate", PaymentVoucherModel.get(0).getTransactionDate());
			data.put("poNumber", PaymentVoucherModel.get(0).getPoNumber());
		} else {
			data.put("paymentVoucher", "--");
			data.put("transactionDate", "--");
			data.put("poNumber", "--");
		}
		/**
		 * for vendor details
		 */
		if (!PaymentVoucherModel.isEmpty()) {
			try {
				PaymentVoucherModel[] UserDetails = restClient.getForObject(
						env.getInventoryUrl() + "getVenderDetails?id=" + PaymentVoucherModel.get(0).getVendorId(),
						PaymentVoucherModel[].class);
				List<PaymentVoucherModel> vendorList = Arrays.asList(UserDetails);

				data.put("vendorList", vendorList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
		}

		/**
		 * for hotel details
		 */
		if (!PaymentVoucherModel.isEmpty()) {
			try {
				PaymentVoucherModel[] UserDetails = restClient.getForObject(
						env.getInventoryUrl() + "getHotelDetails?id=" + PaymentVoucherModel.get(0).getAccNo(),
						PaymentVoucherModel[].class);
				List<PaymentVoucherModel> hotelList = Arrays.asList(UserDetails);

				model.addAttribute("hotelList", hotelList);
				data.put("hotelList", hotelList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-PaymentVoucher?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);

			data.put("logoBgList", logoBgList);

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
					env.getInventoryUrl() + "restLogoImage-PaymentVoucher?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);

			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=paymentVoucher.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/inventory-payment-voucher-reportPdf", data);
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
