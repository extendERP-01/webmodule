package nirmalya.aathithya.webmodule.reimbursement.controller;

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
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsEmployeeCompanyDetailsModel;
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsReimbursementPaymentModal;

@Controller
@RequestMapping(value = "download")
public class ReimbursementPdfController {

	
	Logger logger = LoggerFactory.getLogger(ReimbursementPdfController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	/*
	 * pdf for other service invoice
	 */

	@GetMapping(value = { "/reimbursement-view-payment-pdf" })
	public void pdfPaymentVoucher(HttpServletResponse response, @RequestParam("id") String encodeId, Model model) {
		logger.info("Method in web:download-view-payment-voucher-view-pdf starts");
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		Map<String, Object> data = new HashMap<String, Object>();
		try {

			HrmsReimbursementPaymentModal[] paymentVoucher = restClient
					.getForObject(env.getReimbursementUrl() + "getReimbPayData?id=" + id, HrmsReimbursementPaymentModal[].class);

			List<HrmsReimbursementPaymentModal> HrmsReimbursementPaymentModal = Arrays.asList(paymentVoucher);
			System.out.println(HrmsReimbursementPaymentModal);
			data.put("HrmsReimbursementPaymentModal", HrmsReimbursementPaymentModal);
			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String pdfCurrentDate = sdf.format(date);
			data.put("pdfCurrentDate", pdfCurrentDate);
			/**
			 * for vendor details
			 */
			if (!HrmsReimbursementPaymentModal.isEmpty()) {

				
				try {
					HrmsEmployeeCompanyDetailsModel[] UserDetails = restClient.getForObject(
							env.getReimbursementUrl() + "getVenderDetails?id=" + HrmsReimbursementPaymentModal.get(0).getEmployeeId(),
							HrmsEmployeeCompanyDetailsModel[].class);
					List<HrmsEmployeeCompanyDetailsModel> vendorList = Arrays.asList(UserDetails);

					data.put("vendorList", vendorList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (HrmsReimbursementPaymentModal.size() != 0) {
					data.put("paymentVoucher", HrmsReimbursementPaymentModal.get(0).getVoucherNo());
					data.put("transactionDate", HrmsReimbursementPaymentModal.get(0).getTransactionDate());
					data.put("poNumber", HrmsReimbursementPaymentModal.get(0).getReimbNo());
				} else {
					data.put("paymentVoucher", "--");
					data.put("transactionDate", "--");
					data.put("poNumber", "--");
				}
			}
			data.put("HrmsReimbursementPaymentModal", HrmsReimbursementPaymentModal);
			/**
			 * for hotel details
			 */
			if (!HrmsReimbursementPaymentModal.isEmpty()) {
				try {
					HrmsEmployeeCompanyDetailsModel[] UserDetails = restClient.getForObject(
							env.getReimbursementUrl() + "getHotelDetails?id=" + HrmsReimbursementPaymentModal.get(0).getAccNo(),
							HrmsEmployeeCompanyDetailsModel[].class);
					List<HrmsEmployeeCompanyDetailsModel> hotelList = Arrays.asList(UserDetails);

					data.put("hotelList", hotelList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}

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
			DropDownModel[] logoBg = restClient.getForObject(
					env.getReimbursementUrl() + "restLogoImage-PaymentVoucher?logoType=" + "background-Logo",
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
					env.getReimbursementUrl() + "restLogoImage-PaymentVoucher?logoType=" + "header-Logo",
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
		response.setHeader("Content-disposition", "inline; filename=Reimbursement_paymentVoucher.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("reimbursement/reimbursement-payment-voucher-pdf", data);
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

	@GetMapping(value = { "/view-payment-voucherR-individual-payment-pdf" })
	public void pdfPaymentDetails(HttpServletResponse response, @RequestParam("id") String encodeId, Model model) {
		logger.info("Method in web:pdfPaymentDetails starts");
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		Map<String, Object> data = new HashMap<String, Object>();
		try {

			HrmsReimbursementPaymentModal[] paymentVoucher = restClient
					.getForObject(env.getReimbursementUrl() + "getPaymentDataPdf?id=" + id, HrmsReimbursementPaymentModal[].class);

			List<HrmsReimbursementPaymentModal> HrmsReimbursementPaymentModal = Arrays.asList(paymentVoucher);

			data.put("HrmsReimbursementPaymentModal", HrmsReimbursementPaymentModal);

			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String pdfCurrentDate = sdf.format(date);
			data.put("pdfCurrentDate", pdfCurrentDate);
			/**
			 * for vendor details
			 */
			if (!HrmsReimbursementPaymentModal.isEmpty()) {

				
				try {
					HrmsEmployeeCompanyDetailsModel[] UserDetails = restClient.getForObject(
							env.getReimbursementUrl()+ "getVenderDetails?id=" + HrmsReimbursementPaymentModal.get(0).getEmployeeId(),
							HrmsEmployeeCompanyDetailsModel[].class);
					List<HrmsEmployeeCompanyDetailsModel> vendorList = Arrays.asList(UserDetails);

					data.put("vendorList", vendorList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (HrmsReimbursementPaymentModal.size() != 0) {
					data.put("paymentVoucher", HrmsReimbursementPaymentModal.get(0).getVoucherNo());
					data.put("transactionDate", HrmsReimbursementPaymentModal.get(0).getTransactionDate());
					data.put("poNumber", HrmsReimbursementPaymentModal.get(0).getReimbNo());
				} else {
					data.put("paymentVoucher", "--");
					data.put("transactionDate", "--");
					data.put("poNumber", "--");
				}
			}
			data.put("HrmsReimbursementPaymentModal", HrmsReimbursementPaymentModal);
			/**
			 * for hotel details
			 */
			if (!HrmsReimbursementPaymentModal.isEmpty()) {
				try {
					HrmsEmployeeCompanyDetailsModel[] UserDetails = restClient.getForObject(
							env.getReimbursementUrl() + "getHotelDetails?id=" + HrmsReimbursementPaymentModal.get(0).getAccNo(),
							HrmsEmployeeCompanyDetailsModel[].class);
					List<HrmsEmployeeCompanyDetailsModel> hotelList = Arrays.asList(UserDetails);

					data.put("hotelList", hotelList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}

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
			DropDownModel[] logoBg = restClient.getForObject(
					env.getReimbursementUrl() + "restLogoImage-PaymentVoucher?logoType=" + "background-Logo",
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
					env.getReimbursementUrl() + "restLogoImage-PaymentVoucher?logoType=" + "header-Logo",
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

	
}
