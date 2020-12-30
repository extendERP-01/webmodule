package nirmalya.aathithya.webmodule.filedownload;

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
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import nirmalya.aathithya.webmodule.account.model.IncomeTaxPayableModel;

public class ExcelIncomeTaxPayableReport extends AbstractXlsView {

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<IncomeTaxPayableModel> incomeTaxPayable = (List<IncomeTaxPayableModel>) model.get("incomeTaxPayable");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Purchase Ledger Report");

			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);

			style.setFont(font);

			realSheet.setDefaultColumnWidth(20);
			HSSFRow row = realSheet.createRow(0);
			HSSFCell cell = row.createCell(0);

			row.getCell(0).setCellStyle(style);
			cell.setCellValue("Sl No.");

			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("Invoice Id");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Date");
			

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Vendor Name");
			

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("TIN No.");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Amount");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("IT Amount");

			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("Net Amt");

			

			int i = 1;
			int j = 1;
			for (IncomeTaxPayableModel m : incomeTaxPayable) {
				row = realSheet.createRow(i++);
				double finalAmount=0.0;
				finalAmount = (m.gettPymntTaxableAmount() - m.gettPymntTDSAmount());
				m.settPymntTotalAmount(finalAmount);
				
				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.gettPymntGrnInvoice());

				cell = row.createCell(2);
				cell.setCellValue(m.gettPymntTransactionDate());

				cell = row.createCell(3);
				cell.setCellValue(m.gettVendor());
		

				cell = row.createCell(4);
				cell.setCellValue(m.gettPaymentMode());

				cell = row.createCell(5);
				cell.setCellValue(m.gettPymntTaxableAmount());

				cell = row.createCell(6);
				cell.setCellValue(m.gettPymntTDSAmount());

				cell = row.createCell(7);
				cell.setCellValue(m.gettPymntTotalAmount());

				

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
