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
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.apache.poi.ss.usermodel.Font;

import nirmalya.aathithya.webmodule.employee.model.HrmsExtraSalaryModel;

public class ExtraDayExcelReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ExtraDayExcelReport.class);

	double working = 0;
	double work = 0;
	double extra = 0;

	@SuppressWarnings("unchecked")
	@Override

	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<HrmsExtraSalaryModel> accountLedger = (List<HrmsExtraSalaryModel>) model.get("consumption");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Employee Extra Day Report");

			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);

			style.setFont(font);

			realSheet.setDefaultColumnWidth(30);

			HSSFRow row1 = realSheet.createRow(0);
			HSSFCell cell1 = row1.createCell(0);

			cell1 = row1.createCell(2);
			row1.getCell(2).setCellStyle(style);
			cell1.setCellValue("Redimix Employee Extra Day Approval");

			HSSFRow row = realSheet.createRow(1);
			HSSFCell cell = row.createCell(1);

			cell = row.createCell(0);
			row.getCell(0).setCellStyle(style);
			cell.setCellValue("From Date");

			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("To Date");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Employee ID");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Employee Name");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Working Day");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Work Day");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Extra Day");

			int i = 2;
			int j = 1;
			for (HrmsExtraSalaryModel m : accountLedger) {
				row = realSheet.createRow(i++);
				j++;

				cell = row.createCell(0);
				cell.setCellValue(m.getFromDate());

				cell = row.createCell(1);
				cell.setCellValue(m.getToDate());

				cell = row.createCell(2);
				cell.setCellValue(m.getEmployeeId());

				cell = row.createCell(3);
				cell.setCellValue(m.getEmployeeName());

				cell = row.createCell(4);
				cell.setCellValue(m.getWorkingDay());

				cell = row.createCell(5);
				cell.setCellValue(m.getWorkDay());

				cell = row.createCell(6);
				cell.setCellValue(m.getExtraDay());

			}
			row = realSheet.createRow(j + 2);

			for (HrmsExtraSalaryModel m : accountLedger) {

				working = working + m.getWorkingDay();
				work = work + m.getWorkDay();
				extra = extra + m.getExtraDay();

				cell = row.createCell(3);
				cell.setCellValue("Total:");

				cell = row.createCell(4);
				cell.setCellValue(working);

				cell = row.createCell(5);
				cell.setCellValue(work);

				cell = row.createCell(6);
				cell.setCellValue(extra);

			}

			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}