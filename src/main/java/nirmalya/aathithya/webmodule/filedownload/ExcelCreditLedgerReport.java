package nirmalya.aathithya.webmodule.filedownload;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import nirmalya.aathithya.webmodule.account.model.DebitCreditLedgerModel;

public class ExcelCreditLedgerReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ExcelCreditLedgerReport.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date d =new Date();
		
		try {

			List<DebitCreditLedgerModel> creditLedger = (List<DebitCreditLedgerModel>) model.get("creditLedger");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Credit Ledger Report");

			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);

			style.setFont(font);

			CellStyle styleTotal = workbook.createCellStyle();
            Font fontTotal = workbook.createFont();

            fontTotal.setBold(true);
            fontTotal.setColor(HSSFColor.BLACK.index);

            styleTotal.setFont(fontTotal);
			
			realSheet.setDefaultColumnWidth(20);
			
			CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            short height=280;//14px
            headerFont.setBold(false);
            headerFont.setFontHeight(height);
            //headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
            headerFont.setColor(HSSFColor.BLUE.index);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
           
			HSSFRow headerRow = realSheet.createRow(0);
			HSSFCell headerCell = headerRow.createCell(0);
			
			realSheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
			headerCell.setCellStyle(headerStyle);
			
			headerCell.setCellValue("Nirmalya Labs Pvt Ltd,Bhubaneswar");
			
			headerCell = headerRow.createCell(6);
			headerCell.setCellValue("Date : "+sdf.format(d));
			
			HSSFRow row = realSheet.createRow(1);
			
			HSSFCell cell = row.createCell(0);
			row.getCell(0).setCellStyle(style);
			cell.setCellValue("Sl No.");
			
			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("Vendor Name");
			
			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Invoice Id");
			
			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Invoice Date");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Invoice Amount");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Payment Amount");

			int i = 2;
			int j = 1;
		
			String curr_vendor="";
			String curr_invoice="";
			Double credit_total=0.0;
			Double debit_total=0.0;
			Double invCrd=0.0;
			Double invDbt=0.0;
			
			for (DebitCreditLedgerModel m : creditLedger) {
				row = realSheet.createRow(i);
				
				
				if(curr_vendor.equals("") || !m.gettRegistrationType().equalsIgnoreCase(curr_vendor)) {					
					if(!curr_vendor.equals("")) {
						System.out.println("first vendor :"+curr_vendor);
						row = realSheet.createRow(++i);
						cell = row.createCell(1);
						cell.setCellValue(curr_vendor +": Total");
						cell.setCellStyle(styleTotal);
						
						
						cell = row.createCell(2);
						cell.setCellValue("");
						
						cell = row.createCell(3);
						cell.setCellValue("");
						
						cell = row.createCell(4);
						cell.setCellValue(credit_total);
						cell.setCellStyle(styleTotal);
						
						cell = row.createCell(5);
						cell.setCellValue(debit_total);
						cell.setCellStyle(styleTotal);
						i++;
						invCrd = 0.0;
						invDbt = 0.0;
						credit_total = 0.0;
						debit_total = 0.0;
						row = realSheet.createRow(i);
					}
					
					curr_vendor=m.gettRegistrationType();
					cell = row.createCell(0);
					cell.setCellValue(j);
					
					cell = row.createCell(1);
					cell.setCellValue(m.gettRegistrationType());
					
					curr_invoice = m.gettInvoice();
					cell = row.createCell(2);
					cell.setCellValue(m.gettInvoice());
					
					cell = row.createCell(3);
					cell.setCellValue(m.gettTransactionDate());
					
					cell = row.createCell(4);
					cell.setCellValue(m.getCreditAmount());
					
					cell = row.createCell(5);
					cell.setCellValue(m.getDebitAmount());
					
					credit_total = credit_total+m.getCreditAmount();
					debit_total = debit_total+m.getDebitAmount();
					
					invCrd = invCrd+m.getCreditAmount();
					invDbt = invDbt+m.getDebitAmount();
					j++;
				} else {
					cell = row.createCell(1);
					cell.setCellValue("");
					
					if(!m.gettInvoice().equalsIgnoreCase(curr_invoice)) {
						
						cell = row.createCell(2);
						cell.setCellValue(curr_invoice+": Total");
						cell.setCellStyle(styleTotal);
						cell = row.createCell(3);
						cell.setCellValue("");
						
						cell = row.createCell(4);
						cell.setCellValue(invCrd);
						cell.setCellStyle(styleTotal);
						
						cell = row.createCell(5);
						cell.setCellValue(invDbt);
						cell.setCellStyle(styleTotal);
						invCrd = 0.0;
						invDbt = 0.0;
						
						i++;
						row = realSheet.createRow(i);
						cell = row.createCell(1);
						cell.setCellValue("");
						
						curr_invoice = m.gettInvoice();
						cell = row.createCell(2);
						cell.setCellValue(m.gettInvoice());
						
						cell = row.createCell(3);
						cell.setCellValue(m.gettTransactionDate());
						
						cell = row.createCell(4);
						cell.setCellValue(m.getCreditAmount());
						
						cell = row.createCell(5);
						cell.setCellValue(m.getDebitAmount());
						
						invCrd = invCrd+m.getCreditAmount();
						invDbt = invDbt+m.getDebitAmount();
						
					} else {
						cell = row.createCell(2);
						cell.setCellValue("");
						
						cell = row.createCell(3);
						cell.setCellValue(m.gettTransactionDate());
						
						cell = row.createCell(4);
						cell.setCellValue(m.getCreditAmount());
						
						cell = row.createCell(5);
						cell.setCellValue(m.getDebitAmount());
						
						invCrd = invCrd+m.getCreditAmount();
						invDbt = invDbt+m.getDebitAmount();
					}
					credit_total = credit_total+m.getCreditAmount();
					debit_total = debit_total+m.getDebitAmount();
				}
				++i;
			}
			
			row = realSheet.createRow(i);
			cell = row.createCell(1);
			cell.setCellValue("");
			
			row = realSheet.createRow(i);
			cell = row.createCell(2);
			cell.setCellValue(curr_invoice+": Total");
			cell.setCellStyle(styleTotal);
			cell = row.createCell(3);
			cell.setCellValue("");
			
			cell = row.createCell(4);
			cell.setCellValue(invCrd);
			cell.setCellStyle(styleTotal);
			
			cell = row.createCell(5);
			cell.setCellValue(invDbt);
			cell.setCellStyle(styleTotal);
			invCrd = 0.0;
			invDbt = 0.0;
			i++;
			
			row = realSheet.createRow(i);
			
			cell = row.createCell(1);
			cell.setCellValue(curr_vendor +": Total");
			cell.setCellStyle(styleTotal);
			
			cell = row.createCell(2);
			cell.setCellValue("");
			
			cell = row.createCell(3);
			cell.setCellValue("");
			
			cell = row.createCell(4);
			cell.setCellValue(credit_total);
			cell.setCellStyle(styleTotal);
			
			cell = row.createCell(5);
			cell.setCellValue(debit_total);
			cell.setCellStyle(styleTotal);
			
			credit_total = 0.0;
			debit_total = 0.0;
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
