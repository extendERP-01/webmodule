package nirmalya.aathithya.webmodule.account.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.AccountTrialBalanceModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;

@Controller
@RequestMapping(value = "account")
public class AccountTrialBalanceReportController {

	Logger logger = LoggerFactory.getLogger(AccountTrialBalanceReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * get mapping for view trial balance
	 */
	@GetMapping("/view-trial-balance-report")
	public String viewaBalanceSheet(Model model, HttpSession session) {

		logger.info("Method : viewaBalanceSheet starts");
		// for get cost center list
		try {
			String costCenter = (String)session.getAttribute("costcenter");
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);
			
			if(costCenter!=null) {
				model.addAttribute("costCenter",costCenter);
			}else {
				model.addAttribute("costCenter",null);
			}
			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewaBalanceSheet ends");
		return "account/trial-balance-search";
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trial-balance-report-ajax")
	public @ResponseBody DataTableResponse viewaBalanceSheetAjax(HttpSession session,HttpServletRequest request,@RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		session.setAttribute("costcenter", param3);
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "all-trail-balance-report-view", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {});
		Integer i=1;
		String s="";
		System.out.println("accountTrialBalanceModel: "+accountTrialBalanceModel);
		for(AccountTrialBalanceModel m:accountTrialBalanceModel) {
			m.setSlNo(i);
			byte[] encodeId = Base64.getEncoder().encode(m.getDesc().getBytes()); //desc contains accountgroup id
			s= "<a href='javascript:void(0)' onclick='accountGroupRequest(\""+new String(encodeId)+"\")' title='Drilldown'>"+m.getCostCenter()+"</a>";
			m.setAction(s);
			s="";
			i++;
		}
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(accountTrialBalanceModel);
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trial-balance-report-account-group")
	public String viewaAccountGroupDrilldown(Model model, HttpSession session,@RequestParam("accountGroup")String param1,
			@RequestParam("fromdate")String param2,@RequestParam("fromdate")String param3,
			@RequestParam("costcenter")String param4) {
		
		logger.info("Method : viewaBalanceSheet starts");
		String accountGroup= new String(Base64.getDecoder().decode(param1.getBytes()));
		String fromDate = new String(Base64.getDecoder().decode(param2.getBytes()));
		String toDate = new String(Base64.getDecoder().decode(param3.getBytes()));
		String costcenter = new String(Base64.getDecoder().decode(param4.getBytes()));
		try {
			
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);
			model.addAttribute("costCenterList", costCenterList);
			
			DropDownModel[] subGrp = restClient.getForObject(env.getAccountUrl() + "get-account-subgroups",
					DropDownModel[].class);
			List<DropDownModel> subGroupListList = Arrays.asList(subGrp);
			model.addAttribute("SubgroupList", subGroupListList);
			
			model.addAttribute("drillup","/account/view-trial-balance-report");
			model.addAttribute("fromDate",fromDate);
			model.addAttribute("toDate",toDate);
			model.addAttribute("costcenter",costcenter );
			session.setAttribute("accountGroupS", accountGroup);
		}catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewaBalanceSheet ends");
		return "account/trialbalanceaccountgroup";
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trial-balance-report-account-group-ajax")
	public @ResponseBody DataTableResponse viewaBalanceSheetSubgroup(HttpSession session,HttpServletRequest request,@RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String accountGroup = (String)session.getAttribute("accountGroupS");
		
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(accountGroup);
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "all-trail-balance-report-accountgroup", tableRequest,JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {});
		Integer i=1;
		String s="";
		for(AccountTrialBalanceModel m:accountTrialBalanceModel) {
			m.setSlNo(i);
			System.out.println("here aa gya ");
			byte[] encodeId = Base64.getEncoder().encode(m.getCostCenter().getBytes());
			s= "<a href='/account/view-trial-balance-report-account-group?accountGroup="+new String(encodeId)+"' title='Drilldown'>"+m.getCostCenter()+"</a>";
			m.setAction(s);
			s="";
			i++;
		}
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(accountTrialBalanceModel);
		
		return response;
	}
	
	
	/*
	 * Generate Pdf For trial balance
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-trial-balance-download-report" })
	public void generateAssignedAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);
		double totalDebit = 0;
		double totalCredit = 0;
		for (AccountTrialBalanceModel a : accountTrialBalanceModel) {
			double creditBal = 0;
			double debitBal = 0;

			if (a.getCreditVal() != null) {
				creditBal = a.getCreditVal();
			}
			if (a.getDebitBal() != null) {
				debitBal = a.getDebitBal();
			}

			double total = creditBal - debitBal;
			totalDebit = totalDebit + debitBal;
			totalCredit = totalCredit + creditBal;
			if (total > 0) {
				a.setCreditVal(total);
				a.setDebitBal(null);
			} else if (total < 0) {
				a.setCreditVal(null);
				a.setDebitBal(Math.abs(total));
			}
		}
		data.put("accountTrialBalanceModel", accountTrialBalanceModel);

		data.put("totalDebit", totalDebit);

		data.put("totalCredit", totalCredit);
		data.put("fromDate", param1);
		data.put("toDate", param2);
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "background-Logo",
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
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "header-Logo",
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
		response.setHeader("Content-disposition", "inline; filename=TrialBalance.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("account/trial-balance-report", data);
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
	 * get mapping for view trial balance total
	 */
	@GetMapping("/view-trial-balance-total-report")
	public String viewTrialBalanceTotal(Model model, HttpSession session) {

		logger.info("Method : viewTrialBalanceTotal starts");
		// for get cost center list
		try {
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewTrialBalanceTotal ends");
		return "account/trial-balance-total-search";
	}

	/*
	 * Generate Pdf For total balance sheet
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-trial-balance-total-download-report" })
	public void generateTrialBalanceTotalPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {
		logger.info("Method : generateTrialBalanceTotalPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);
		double totalDebit = 0;
		double totalCredit = 0;
		for (AccountTrialBalanceModel a : accountTrialBalanceModel) {
			double creditBal = 0;
			double debitBal = 0;

			if (a.getCreditVal() != null) {
				creditBal = a.getCreditVal();
			}
			if (a.getDebitBal() != null) {
				debitBal = a.getDebitBal();
			}

			totalCredit = totalCredit + creditBal;
			totalDebit = totalDebit + debitBal;
		}
		data.put("accountTrialBalanceModel", accountTrialBalanceModel);

		data.put("totalDebit", totalDebit);

		data.put("totalCredit", totalCredit);
		data.put("fromDate", param1);
		data.put("toDate", param2);
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "background-Logo",
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
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "header-Logo",
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
		response.setHeader("Content-disposition", "inline; filename=TrialBalanceTotal.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("account/trial-balance-total-report", data);
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
		logger.info("Method : generateTrialBalanceTotalPdf  ends");
	}

	/*
	 * Get Mapping for excel view in Trial balance totals
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download-excel-trial-balance-total")
	public void downloadExcelTrailBalanceTotal(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedParam3) {

		logger.info("Method : downloadExcelTrailBalanceTotal start");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<AccountTrialBalanceModel> trialBalance = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountTrialBalanceModel>>() {
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
			cell.setCellValue("ACCOUNT Name");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("DEBIT");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("CREDIT");
			double totalDebit = 0;
			double totalCredit = 0;
			int i = 1;
			int j = 1;
			int count = 2;
			for (AccountTrialBalanceModel m : trialBalance) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getAccountHeadId());

				cell = row.createCell(2);
				if (m.getDebitBal() != null)
					cell.setCellValue(m.getDebitBal());

				cell = row.createCell(3);
				if (m.getCreditVal() != null)
					cell.setCellValue(m.getCreditVal());

				count = count + 1;
				double creditBal = 0;
				double debitBal = 0;

				if (m.getCreditVal() != null) {
					creditBal = m.getCreditVal();
				}
				if (m.getDebitBal() != null) {
					debitBal = m.getDebitBal();
				}

				totalCredit = totalCredit + creditBal;
				totalDebit = totalDebit + debitBal;
			}

			row = realSheet.createRow(count);
			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("Total");
			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue(totalDebit);
			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
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
	
	/*
	 * Get Mapping for excel view in Trial balance 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download-excel-trial-balance")
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

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<AccountTrialBalanceModel> trialBalance = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountTrialBalanceModel>>() {
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
			cell.setCellValue("ACCOUNT Name");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("DEBIT");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("CREDIT");
			double totalDebit = 0;
			double totalCredit = 0;
			int i = 1;
			int j = 1;
			int count = 2;
			for (AccountTrialBalanceModel m : trialBalance) {
				
				double creditBal = 0;
				double debitBal = 0;

				if (m.getCreditVal() != null) {
					creditBal = m.getCreditVal();
				}
				if (m.getDebitBal() != null) {
					debitBal = m.getDebitBal();
				}

				double total = creditBal - debitBal;
				totalDebit = totalDebit + debitBal;
				totalCredit = totalCredit + creditBal;
				if (total > 0) {
					m.setCreditVal(total);
					m.setDebitBal(null);
				} else if (total < 0) {
					m.setCreditVal(m.getCreditVal());
					m.setDebitBal(m.getDebitBal());
				}else {
					m.setCreditVal(m.getCreditVal());
					m.setCreditVal(total);
				}
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getAccountHeadId());

				cell = row.createCell(2);
				if (total<0)
					cell.setCellValue(m.getDebitBal());

				cell = row.createCell(3);
				if (total>0)
					cell.setCellValue(m.getCreditVal());

				count = count + 1;
				
			}

			row = realSheet.createRow(count);
			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("Total");
			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue(totalDebit);
			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
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
