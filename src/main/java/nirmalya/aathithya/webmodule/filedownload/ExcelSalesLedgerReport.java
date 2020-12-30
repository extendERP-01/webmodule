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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import nirmalya.aathithya.webmodule.account.model.SalesAccountLedgerModel;

public class ExcelSalesLedgerReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ExcelSalesLedgerReport.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<SalesAccountLedgerModel> salesLedger = (List<SalesAccountLedgerModel>) model.get("salesLedger");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Sales Ledger Report");

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
			cell.setCellValue("Customer Name");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Address");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("GSTIN No.");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("CGST");

			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("SGST");

			cell = row.createCell(8);
			row.getCell(8).setCellStyle(style);
			cell.setCellValue("IGST");

			cell = row.createCell(9);
			row.getCell(9).setCellStyle(style);
			cell.setCellValue("Amount");

			cell = row.createCell(10);
			row.getCell(10).setCellStyle(style);
			cell.setCellValue("Total Amount");

			int i = 1;
			int j = 1;
			for (SalesAccountLedgerModel m : salesLedger) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.gettRVcInvoiceId());

				cell = row.createCell(2);
				cell.setCellValue(m.getCreatedOn());

				cell = row.createCell(3);
				cell.setCellValue(m.gettPaidFromCustomer());

				cell = row.createCell(4);
				cell.setCellValue(m.gettRVcPaymentFor());

				cell = row.createCell(5);
				cell.setCellValue(m.gettRVcOrderNo());

				cell = row.createCell(6);
				cell.setCellValue(m.gettRVcCGST());

				cell = row.createCell(7);
				cell.setCellValue(m.gettRVcSGST());

				cell = row.createCell(8);
				cell.setCellValue(m.gettRVcIGST());

				cell = row.createCell(9);
				cell.setCellValue(m.gettRVcTaxableAmount());

				cell = row.createCell(10);
				cell.setCellValue(m.gettRVcTotalAmount());

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
