package nirmalya.aathithya.webmodule.account.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import nirmalya.aathithya.webmodule.account.model.AccountCashBookCreditModel;
import nirmalya.aathithya.webmodule.account.model.AccountCashBookDebitModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;

@Controller
@RequestMapping(value = "account")
public class AccountCashBookController {

	Logger logger = LoggerFactory.getLogger(AccountCashBookController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * get mapping for view payment voucher
	 */
	@GetMapping("/view-cash-book-report")
	public String viewCashBook(Model model, HttpSession session) {

		logger.info("Method : viewCashBook starts");
		// for get cost center list
		try {
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenter",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewCashBook ends");
		return "account/cash-book-search";
	}

	/*
	 * Generate Pdf For Assigned Asset
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-cash-book-download-report" })
	public void generateAssignedAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<List<AccountCashBookCreditModel>> jsonResponse = new JsonResponse<List<AccountCashBookCreditModel>>();
		JsonResponse<List<AccountCashBookDebitModel>> jsonResponse1 = new JsonResponse<List<AccountCashBookDebitModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllcashBookCreditReport", tableRequest,
					JsonResponse.class);
			jsonResponse1 = restClient.postForObject(env.getAccountUrl() + "getAllcashBookDebitReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AccountCashBookCreditModel> accountCashBookCreditModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountCashBookCreditModel>>() {
				});
		List<AccountCashBookDebitModel> accountCashBookDebitModel = mapper.convertValue(jsonResponse1.getBody(),
				new TypeReference<List<AccountCashBookDebitModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		double totalDebit = 0;
		double totalCredit = 0;
		int count = accountCashBookDebitModel.size() - accountCashBookCreditModel.size();
		
		for (AccountCashBookDebitModel a : accountCashBookDebitModel) {
			totalDebit = totalDebit + a.getAmount();
		}
		for (AccountCashBookCreditModel a : accountCashBookCreditModel) {
			totalCredit = totalCredit + a.getAmount();
		}
		
		data.put("totalCredit", totalCredit);
		data.put("totalDebit", totalDebit);
		data.put("accountCashBookCreditModel", accountCashBookCreditModel);
		data.put("accountCashBookDebitModel", accountCashBookDebitModel);
		 
        data.put("debitcount",new int[Math.abs(count)]);
        data.put("creditCount" , new int[Math.abs(count)]);
        data.put("count",count);
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-cashBook?logoType=" + "background-Logo",
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
					env.getAccountUrl() + "restLogoImage-cashBook?logoType=" + "header-Logo",
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
		response.setHeader("Content-disposition", "inline; filename=CashBook.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("account/cash-book-report", data);
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
	 * Get Mapping for excel view in Trial balance 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download-excel-cash-book")
	public void downloadExcelTrailBalance(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedParam3) {

		logger.info("Method : downloadExcelTrailBalanceTotal start");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		DataTableRequest tableRequest = new DataTableRequest();
		JsonResponse<List<AccountCashBookCreditModel>> jsonResponse = new JsonResponse<List<AccountCashBookCreditModel>>();
		JsonResponse<List<AccountCashBookDebitModel>> jsonResponse1 = new JsonResponse<List<AccountCashBookDebitModel>>();
	

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllcashBookCreditReport", tableRequest,
					JsonResponse.class);
			jsonResponse1 = restClient.postForObject(env.getAccountUrl() + "getAllcashBookDebitReport", tableRequest,
					JsonResponse.class);

		
		ObjectMapper mapper = new ObjectMapper();

		List<AccountCashBookCreditModel> accountCashBookCreditModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountCashBookCreditModel>>() {
				});
		List<AccountCashBookDebitModel> accountCashBookDebitModel = mapper.convertValue(jsonResponse1.getBody(),
				new TypeReference<List<AccountCashBookDebitModel>>() {
				});
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet realSheet = workbook.createSheet("trial");
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);
			style.setFont(font);

			realSheet.setDefaultColumnWidth(12);
			XSSFRow row = realSheet.createRow(0);
			XSSFCell cell = row.createCell(0);

			row.getCell(0).setCellStyle(style);
			cell.setCellValue("Sl No.");

			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("DATE");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("DESCRIPTION");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("VOUCHER NUMBER");
			
			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("DR./CR.");
			
			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("AMOUNT");

			
			double totalDebit = 0;
			double totalCredit = 0;
			int i = 1;
			int j = 1;
		

	for (AccountCashBookDebitModel m : accountCashBookDebitModel) {
				
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getDate());

				cell = row.createCell(2);
				cell.setCellValue(m.getDesc());
				
				cell = row.createCell(3);
				cell.setCellValue(m.getVoucherNO());
				
				cell = row.createCell(4);
				cell.setCellValue("Debited");
				
				cell = row.createCell(5);
				cell.setCellValue(m.getAmount());
				
				
				totalDebit = totalDebit + m.getAmount();
				
				
			}
	
			for (AccountCashBookCreditModel m : accountCashBookCreditModel) {
				
				
				row = realSheet.createRow(i++);
				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getDate());

				cell = row.createCell(2);
				cell.setCellValue(m.getDesc());
				
				cell = row.createCell(3);
				cell.setCellValue(m.getVoucherNO());
				
				cell = row.createCell(4);
				cell.setCellValue("Credited");
				
				cell = row.createCell(5);
				cell.setCellValue(m.getAmount());
				
				totalCredit  =   totalCredit + m.getAmount();
				
				
			}
			row = realSheet.createRow(j+2);
			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Debit Total");
			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue(totalDebit);
			
			row = realSheet.createRow(j+4);
			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Credit Total");
			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue(totalCredit);
			
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
			workbook.write((OutputStream) servResponse.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("downloadExcelTrailBalanceTotal -> downloadExcel GET", e);
		}
		logger.info("Method : downloadExcelTrailBalanceTotal ends");

	}
	

}
