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

import nirmalya.aathithya.webmodule.employee.model.EmployeeFoodTrackingModel;

public class FoodDetailsExcelReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(FoodDetailsExcelReport.class);
	int dmeal = 0;
	int nmeal = 0;
	int total = 0;

	@SuppressWarnings("unchecked")
	@Override

	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<EmployeeFoodTrackingModel> accountLedger = (List<EmployeeFoodTrackingModel>) model.get("consumption");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Consumption Report");

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
			cell1.setCellValue("Redimix Foood Approval Excel");

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
			cell.setCellValue("Day Meal");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Night Meal");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Total Meal");

			int i = 2;
			int j = 1;
			for (EmployeeFoodTrackingModel m : accountLedger) {
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
				cell.setCellValue(m.getDayMeal());

				cell = row.createCell(5);
				cell.setCellValue(m.getNightMeal());

				cell = row.createCell(6);
				cell.setCellValue(m.getTotalMeal());
			}
			row = realSheet.createRow(j + 2);

			for (EmployeeFoodTrackingModel m : accountLedger) {

				dmeal = dmeal + m.getDayMeal();
				nmeal = nmeal + m.getNightMeal();
				total = total + m.getTotalMeal();

				cell = row.createCell(3);
				cell.setCellValue("Total:");

				cell = row.createCell(4);
				cell.setCellValue(dmeal);

				cell = row.createCell(5);
				cell.setCellValue(nmeal);

				cell = row.createCell(6);
				cell.setCellValue(total);
			}

			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}