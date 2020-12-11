package nirmalya.aathithya.webmodule.employee.excel;

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

import nirmalya.aathithya.webmodule.employee.model.EmployeeIncomeTaxDetails;

public class IncomeTaxDetailsExcel extends AbstractXlsView{

	Logger logger = LoggerFactory.getLogger(AttendanceExcelReport.class);

	@SuppressWarnings("unchecked")
	@Override

	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<EmployeeIncomeTaxDetails> accountLedger = (List<EmployeeIncomeTaxDetails>) model
					.get("consumption");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Employee Income Tax Report");

			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);

			style.setFont(font);

			realSheet.setDefaultColumnWidth(30);

			/*
			 * HSSFRow row1 = realSheet.createRow(0); HSSFCell cell1 = row1.createCell(0);
			 * 
			 * cell1 = row1.createCell(2); row1.getCell(2).setCellStyle(style);
			 * cell1.setCellValue("Redimix Employee Income Tax Details");
			 */

			HSSFRow row = realSheet.createRow(0);
			HSSFCell cell = row.createCell(0);

			cell = row.createCell(0);
			row.getCell(0).setCellStyle(style);
			cell.setCellValue("From Date");

			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("To Date");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Employee Id");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Employee Name");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Tax Amount");


			int i = 1;
			int j = 1;
			for (EmployeeIncomeTaxDetails m : accountLedger) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(m.getFromDate());

				cell = row.createCell(1);
				cell.setCellValue(m.getToDate());

				cell = row.createCell(2);
				cell.setCellValue(m.getEmployeeId());

				cell = row.createCell(3);
				cell.setCellValue(m.getEmployeeName());

				cell = row.createCell(4);
				cell.setCellValue(m.getTaxAmount());

			}

			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
