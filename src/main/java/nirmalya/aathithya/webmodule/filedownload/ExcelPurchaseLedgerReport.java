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

import nirmalya.aathithya.webmodule.account.model.PurchaseAccountLedgerModel;


public class ExcelPurchaseLedgerReport extends AbstractXlsView {

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<PurchaseAccountLedgerModel> purchaseLedger = (List<PurchaseAccountLedgerModel>) model.get("purchaseLedger");
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
			cell.setCellValue("Payment Voucher No.");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Vendor Name");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Invoice No.");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Date");

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
			cell.setCellValue("Tax Amnt");

			cell = row.createCell(10);
			row.getCell(10).setCellStyle(style);
			cell.setCellValue("Total Amt");

			int i = 1;
			int j = 1;
			for (PurchaseAccountLedgerModel m : purchaseLedger) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.gettPaymentVoucher());

				cell = row.createCell(2);
				cell.setCellValue(m.gettVendor());

				cell = row.createCell(3);
				cell.setCellValue(m.gettPymntGrnInvoice());
		

				cell = row.createCell(4);
				cell.setCellValue(m.gettPymntTransactionDate());

				cell = row.createCell(5);
				cell.setCellValue(m.gettPaymentMode());


				cell = row.createCell(6);
				cell.setCellValue(m.gettPymntCGST());

				cell = row.createCell(7);
				cell.setCellValue(m.gettPymntSGST());

				cell = row.createCell(8);
				cell.setCellValue(m.gettPymntIGST());

				cell = row.createCell(9);
				cell.setCellValue(m.gettPymntTaxableAmount());

				cell = row.createCell(10);
				cell.setCellValue(m.gettPymntTotalAmount());

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

