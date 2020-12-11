package nirmalya.aathithya.webmodule.production.controller;

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

import nirmalya.aathithya.webmodule.production.model.MotherCoilSlitReportModel; 

public class ExcelMotherCoilSlitBatchReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ExcelMotherCoilSlitBatchReport.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<MotherCoilSlitReportModel> motherCoilSlitReportModel = (List<MotherCoilSlitReportModel>) model
					.get("motherCoilSlitReportModel");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Nonpolishing Report");

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
			cell.setCellValue("MotorCoilBatch");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Grn");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("MotorCoilGrade");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("MotorCoilThickNess");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Pipe Size");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Batch Code");

			int i = 1;
			int j = 1;
			for (MotherCoilSlitReportModel m : motherCoilSlitReportModel) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getMotherCoilBatch());

				cell = row.createCell(2);
				cell.setCellValue(m.getGrn());

				cell = row.createCell(3);
				cell.setCellValue(m.getItemCateGory());

				cell = row.createCell(4);
				cell.setCellValue(m.getItemSubCateGory());

				cell = row.createCell(5);
				cell.setCellValue(m.getItem());

				cell = row.createCell(6);
				cell.setCellValue(m.getBatchCode());

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
