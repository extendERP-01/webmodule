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

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.InventoryGrnPaymentDetails;
import nirmalya.aathithya.webmodule.inventory.model.PaymentVoucherModel;

@Controller
@RequestMapping(value = "download")
public class InventoryPdfController {
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(InventoryPdfController.class);
	/*
	 * pdf for other service invoice
	 */

	@GetMapping(value = { "/inventory-view-payment-voucher-view-pdf" })
	public void pdfPaymentVoucher(HttpServletResponse response, @RequestParam("id") String encodeId, Model model) {
		logger.info("Method in web:download-view-payment-voucher-view-pdf starts");
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		Map<String, Object> data = new HashMap<String, Object>();
		try {

			PaymentVoucherModel[] paymentVoucher = restClient
					.getForObject(env.getInventoryUrl() + "getReturnData?id=" + id, PaymentVoucherModel[].class);

			List<PaymentVoucherModel> PaymentVoucherModel = Arrays.asList(paymentVoucher);

			data.put("PaymentVoucherModel", PaymentVoucherModel);
			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String pdfCurrentDate = sdf.format(date);
			data.put("pdfCurrentDate", pdfCurrentDate);
			/**
			 * for vendor details
			 */
			if (!PaymentVoucherModel.isEmpty()) {

//				double totalSgst = 0;
//				double totalIgst = 0;
//				double total = 0;
//				if (PaymentVoucherModel.get(0).getGoodsReturnNote() != null) {
//					totalSgst = PaymentVoucherModel.get(0).getSgst() - PaymentVoucherModel.get(0).getReturnSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst() - PaymentVoucherModel.get(0).getReturnIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal() - PaymentVoucherModel.get(0).getReturnTotal();
//				} else {
//					totalSgst = PaymentVoucherModel.get(0).getSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal();
//				}
//
//				double tdsAmount = (PaymentVoucherModel.get(0).getSubTotal() * 10) / 100;
//				PaymentVoucherModel.get(0).setTdsAmount(tdsAmount);
//				PaymentVoucherModel.get(0).setSgst(totalSgst);
//				PaymentVoucherModel.get(0).setCgst(totalSgst);
//				PaymentVoucherModel.get(0).setIgst(totalIgst);
//				total = PaymentVoucherModel.get(0).getSubTotal() + tdsAmount - totalIgst - (2 * totalSgst);
//				PaymentVoucherModel.get(0).setTotal(total);

				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getVenderDetails?id=" + PaymentVoucherModel.get(0).getVendorId(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> vendorList = Arrays.asList(UserDetails);

					data.put("vendorList", vendorList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (PaymentVoucherModel.size() != 0) {
					data.put("paymentVoucher", PaymentVoucherModel.get(0).getPaymentVoucher());
					data.put("transactionDate", PaymentVoucherModel.get(0).getTransactionDate());
					data.put("poNumber", PaymentVoucherModel.get(0).getPoNumber());
				} else {
					data.put("paymentVoucher", "--");
					data.put("transactionDate", "--");
					data.put("poNumber", "--");
				}
			}
			data.put("PaymentVoucherModel", PaymentVoucherModel);
			/**
			 * for hotel details
			 */
			if (!PaymentVoucherModel.isEmpty()) {
				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getHotelDetails?id=" + PaymentVoucherModel.get(0).getGrnNo(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> hotelList = Arrays.asList(UserDetails);

					data.put("hotelList", hotelList);
					System.out.println("hotelList " + hotelList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}

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
			file = pdfGeneratorUtil.createPdf("inventory/inventory-payment-voucher-pfd", data);
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
		logger.info("Method in web:download-view-payment-voucher-view-pdf ends");
	}

	/*
	 * pdf for other service invoice
	 */

	@GetMapping(value = { "/view-payment-voucher-individual-payment-pdf" })
	public void pdfPaymentDetails(HttpServletResponse response, @RequestParam("id") String encodeId, Model model) {
		logger.info("Method in web:pdfPaymentDetails starts");
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		Map<String, Object> data = new HashMap<String, Object>();
		try {

			PaymentVoucherModel[] paymentVoucher = restClient
					.getForObject(env.getInventoryUrl() + "getPaymentDataPdf?id=" + id, PaymentVoucherModel[].class);

			List<PaymentVoucherModel> PaymentVoucherModel = Arrays.asList(paymentVoucher);

			data.put("PaymentVoucherModel", PaymentVoucherModel);

			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String pdfCurrentDate = sdf.format(date);
			data.put("pdfCurrentDate", pdfCurrentDate);
			/**
			 * for vendor details
			 */
			if (!PaymentVoucherModel.isEmpty()) {

//				double totalSgst = 0;
//				double totalIgst = 0;
//				double total = 0;
//				if (PaymentVoucherModel.get(0).getGoodsReturnNote() != null) {
//					totalSgst = PaymentVoucherModel.get(0).getSgst() - PaymentVoucherModel.get(0).getReturnSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst() - PaymentVoucherModel.get(0).getReturnIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal() - PaymentVoucherModel.get(0).getReturnTotal();
//				} else {
//					totalSgst = PaymentVoucherModel.get(0).getSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal();
//				}
//				double tdsAmount = (PaymentVoucherModel.get(0).getSubTotal() * 10) / 100;
//				PaymentVoucherModel.get(0).setTdsAmount(tdsAmount);
//				PaymentVoucherModel.get(0).setSgst(totalSgst);
//				PaymentVoucherModel.get(0).setCgst(totalSgst);
//				PaymentVoucherModel.get(0).setIgst(totalIgst);
//				total = PaymentVoucherModel.get(0).getSubTotal() + tdsAmount - totalIgst - (2 * totalSgst);
//				PaymentVoucherModel.get(0).setTotal(total);

				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getVenderDetails?id=" + PaymentVoucherModel.get(0).getVendorId(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> vendorList = Arrays.asList(UserDetails);

					data.put("vendorList", vendorList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (PaymentVoucherModel.size() != 0) {
					data.put("paymentVoucher", PaymentVoucherModel.get(0).getPaymentVoucher());
					data.put("transactionDate", PaymentVoucherModel.get(0).getTransactionDate());
					data.put("poNumber", PaymentVoucherModel.get(0).getPoNumber());
				} else {
					data.put("paymentVoucher", "--");
					data.put("transactionDate", "--");
					data.put("poNumber", "--");
				}
			}
			data.put("PaymentVoucherModel", PaymentVoucherModel);
			/**
			 * for hotel details
			 */
			if (!PaymentVoucherModel.isEmpty()) {
				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getHotelDetails?id=" + PaymentVoucherModel.get(0).getAccNo(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> hotelList = Arrays.asList(UserDetails);

					data.put("hotelList", hotelList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}

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
			file = pdfGeneratorUtil.createPdf("inventory/inventory-payment-voucher-pfd", data);
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
		logger.info("Method in web:pdfPaymentDetails ends");
	}

	/*
	 * pdf for All payment details
	 */
	@GetMapping(value = { "/inventory-view-payment-voucher-all-payments" })
	public void pdfAllPaymentDetails(HttpServletResponse response, @RequestParam("id") String encodeId, Model model) {
		logger.info("Method in web: pdfAllPaymentDetails starts");
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		Map<String, Object> data = new HashMap<String, Object>();
		try {

			PaymentVoucherModel[] paymentVoucher = restClient
					.getForObject(env.getInventoryUrl() + "getReturnData?id=" + id, PaymentVoucherModel[].class);

			List<PaymentVoucherModel> PaymentVoucherModel = Arrays.asList(paymentVoucher);

			data.put("PaymentVoucherModel", PaymentVoucherModel);

			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String pdfCurrentDate = sdf.format(date);
			data.put("pdfCurrentDate", pdfCurrentDate);
			/**
			 * for vendor details
			 */
			if (!PaymentVoucherModel.isEmpty()) {
//				double totalSgst = 0;
//				double totalIgst = 0;
//				double total = 0;
//				if (PaymentVoucherModel.get(0).getGoodsReturnNote() != null) {
//					totalSgst = PaymentVoucherModel.get(0).getSgst() - PaymentVoucherModel.get(0).getReturnSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst() - PaymentVoucherModel.get(0).getReturnIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal() - PaymentVoucherModel.get(0).getReturnTotal();
//				} else {
//					totalSgst = PaymentVoucherModel.get(0).getSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal();
//				}
//				double tdsAmount = (PaymentVoucherModel.get(0).getSubTotal() * 10) / 100;
//				PaymentVoucherModel.get(0).setTdsAmount(tdsAmount);
//				PaymentVoucherModel.get(0).setSgst(totalSgst);
//				PaymentVoucherModel.get(0).setCgst(totalSgst);
//				PaymentVoucherModel.get(0).setIgst(totalIgst);
//				total = PaymentVoucherModel.get(0).getSubTotal() + tdsAmount - totalIgst - (2 * totalSgst);
//				PaymentVoucherModel.get(0).setTotal(total);

				try {
					InventoryGrnPaymentDetails[] inventoryGrnPaymentDetails = restClient
							.getForObject(
									env.getInventoryUrl() + "getCreditDetailsForPdf?id="
											+ PaymentVoucherModel.get(0).getGrnNo(),
									InventoryGrnPaymentDetails[].class);
					List<InventoryGrnPaymentDetails> inventoryGrnPaymentDetails1 = Arrays
							.asList(inventoryGrnPaymentDetails);
					data.put("inventoryGrnPaymentDetails", inventoryGrnPaymentDetails1);

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getVenderDetails?id=" + PaymentVoucherModel.get(0).getVendorId(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> vendorList = Arrays.asList(UserDetails);

					data.put("vendorList", vendorList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (PaymentVoucherModel.size() != 0) {
					data.put("paymentVoucher", PaymentVoucherModel.get(0).getPaymentVoucher());
					data.put("transactionDate", PaymentVoucherModel.get(0).getTransactionDate());
					data.put("poNumber", PaymentVoucherModel.get(0).getPoNumber());
				} else {
					data.put("paymentVoucher", "--");
					data.put("transactionDate", "--");
					data.put("poNumber", "--");
				}
			}
			data.put("PaymentVoucherModel", PaymentVoucherModel);
			/**
			 * for hotel details
			 */
			if (!PaymentVoucherModel.isEmpty()) {
				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getHotelDetails?id=" + PaymentVoucherModel.get(0).getGrnNo(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> hotelList = Arrays.asList(UserDetails);

					data.put("hotelList", hotelList);
					System.out.println("hotelList " + hotelList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}

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
			file = pdfGeneratorUtil.createPdf("inventory/inventory-payment-voucher-total-pdf", data);
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
		logger.info("Method in web:  pdfAllPaymentDetails ends");
	}

}
