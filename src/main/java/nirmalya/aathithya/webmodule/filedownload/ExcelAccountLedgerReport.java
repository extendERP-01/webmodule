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

import nirmalya.aathithya.webmodule.account.model.AccountLedgerModel;

public class ExcelAccountLedgerReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ExcelAccountLedgerReport.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<AccountLedgerModel> accountLedger = (List<AccountLedgerModel>) model.get("accountLedger");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Account Ledger Report");

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
			cell.setCellValue("Ledger Id");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Cost Center");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Invoice");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Account Head");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Registration Id");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Registration Type");

			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("Group");

			cell = row.createCell(8);
			row.getCell(8).setCellStyle(style);
			cell.setCellValue("Ledger Type");

			cell = row.createCell(9);
			row.getCell(9).setCellStyle(style);
			cell.setCellValue("Notes");

			cell = row.createCell(10);
			row.getCell(10).setCellStyle(style);
			cell.setCellValue("Total Amt");

			cell = row.createCell(11);
			row.getCell(11).setCellStyle(style);
			cell.setCellValue("Voucher No.");

			cell = row.createCell(12);
			row.getCell(12).setCellStyle(style);
			cell.setCellValue("Bank");

			cell = row.createCell(13);
			row.getCell(13).setCellStyle(style);
			cell.setCellValue("Pay Mode");

			cell = row.createCell(14);
			row.getCell(14).setCellStyle(style);
			cell.setCellValue("Ref No.");

			cell = row.createCell(15);
			row.getCell(15).setCellStyle(style);
			cell.setCellValue("Transac Id");

			cell = row.createCell(16);
			row.getCell(16).setCellStyle(style);
			cell.setCellValue("Transc date");

			cell = row.createCell(17);
			row.getCell(17).setCellStyle(style);
			cell.setCellValue("Payment Status");

			cell = row.createCell(18);
			row.getCell(18).setCellStyle(style);
			cell.setCellValue("Created By");

			cell = row.createCell(19);
			row.getCell(19).setCellStyle(style);
			cell.setCellValue("Created On");

			int i = 1;
			int j = 1;
			for (AccountLedgerModel m : accountLedger) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getLedgerId());

				cell = row.createCell(2);
				cell.setCellValue(m.getCostCenter());

				cell = row.createCell(3);
				cell.setCellValue(m.getInvoice());

				cell = row.createCell(4);
				cell.setCellValue(m.getAccountHead());

				cell = row.createCell(5);
				cell.setCellValue(m.getRegestrationId());

				cell = row.createCell(6);
				cell.setCellValue(m.getRegistrationType());

				cell = row.createCell(7);
				cell.setCellValue(m.getGroup());

				cell = row.createCell(8);
				cell.setCellValue(m.getLedgerType());

				cell = row.createCell(9);
				cell.setCellValue(m.getNotes());

				cell = row.createCell(10);
				cell.setCellValue(m.getTotalAmount());

				cell = row.createCell(11);
				cell.setCellValue(m.getVoucherNumber());

				cell = row.createCell(12);
				cell.setCellValue(m.getBank());

				cell = row.createCell(13);
				cell.setCellValue(m.getPaymentMode());

				cell = row.createCell(14);
				cell.setCellValue(m.getReferenceNumber());

				cell = row.createCell(15);
				cell.setCellValue(m.getTransactionId());

				cell = row.createCell(16);
				cell.setCellValue(m.getTransactionDate());

				cell = row.createCell(17);
				cell.setCellValue(m.getPaymentStatus());

				cell = row.createCell(18);
				cell.setCellValue(m.getCreatedBy());

				cell = row.createCell(19);
				cell.setCellValue(m.getCreatedOn());

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
