package nirmalya.aathithya.webmodule.sales.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.sales.model.DeliveryChalanModel;
import nirmalya.aathithya.webmodule.sales.model.DeliveryChallanInvoiceModel;
import nirmalya.aathithya.webmodule.sales.model.SalesInvoiceModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "download/")
public class DeliveryChallanPdfController {
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles environmentVaribles;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(DeliveryChallanPdfController.class);

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/delivery-challan-pdf" })
	public void generateDeliveryChallanPdf(HttpServletResponse response, @RequestParam("id") String encodedParam1) {
		logger.info("generateDeliveryChallanPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		String id = (new String(encodeByte1));
		JsonResponse<DeliveryChalanModel> jsonresponse = new JsonResponse<DeliveryChalanModel>();
		try {
			jsonresponse = restTemplate.getForObject(
					environmentVaribles.getSalesUrl() + "getdeliveryChalanModalById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		DeliveryChalanModel deliveryChalanModelMaster = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<DeliveryChalanModel>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("deliveryChalanModel", deliveryChalanModelMaster);

		if (deliveryChalanModelMaster != null) {
			JsonResponse<List<SalesInvoiceModel>> jsonResponse = new JsonResponse<List<SalesInvoiceModel>>();
			try {

				jsonResponse = restTemplate.getForObject(environmentVaribles.getSalesUrl() + "SaleInvTotalPdf?id="
						+ deliveryChalanModelMaster.getInvoiceNumber(), JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			ObjectMapper mapper1 = new ObjectMapper();

			List<SalesInvoiceModel> salesInvoice = mapper1.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesInvoiceModel>>() {
					});

			for (SalesInvoiceModel m : salesInvoice) {

				if (m.getsIGST() != 0.0) {
					m.setPayableAmt(m.getSubTotal() + m.getsIGST());
				} else {
					m.setPayableAmt(m.getSubTotal() + m.getsCGST() + m.getsSGST());
				}
			} 
			data.put("salesInvoice", salesInvoice);
		}

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=DeliveryChallan.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/devivery-challan-pdf", data);
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
		logger.info("generateDeliveryChallanPdf ends");
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/delivery-challan-invoice-pdf" })
	public void generateDeliveryChallanInvoicePdf(HttpServletResponse response,
			@RequestParam("id") String encodedParam1) {
		logger.info("generateDeliveryChallanInvoicePdf starts in pdf controller ");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		String id = (new String(encodeByte1));

		JsonResponse<List<DeliveryChallanInvoiceModel>> jsonresponse = new JsonResponse<List<DeliveryChallanInvoiceModel>>();
		try {
			jsonresponse = restTemplate.getForObject(
					environmentVaribles.getSalesUrl() + "getInvoiceDataPreview?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<DeliveryChallanInvoiceModel> deliveryChallanList = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<DeliveryChallanInvoiceModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		for (DeliveryChallanInvoiceModel a : deliveryChallanList) {
			a.setTotalPrice(a.getQuantity() * a.getPrice());
		}

		data.put("deliveryChalanModel", deliveryChallanList);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);

		data.put("currentDate", currentDate);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=DeliveryChallanInvoiceConfirm.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/delivery-challan-invoice-pdf", data);
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

		logger.info("generateDeliveryChallanInvoicePdf ends in pdf controller ");
	}

}
