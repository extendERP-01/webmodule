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


import nirmalya.aathithya.webmodule.production.model.ProductionPipeNonpolishModel;

public class ExcelNonPolishingReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ExcelNonPolishingReport.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<ProductionPipeNonpolishModel> productionPipeNonpolishModel = (List<ProductionPipeNonpolishModel>) model.get("productionPipeNonpolishModel");
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
			cell.setCellValue("MotorCoilThickNess");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("MotorCoilGrade");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Slit Batch");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Slit Width");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Slit Qty");

			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("Slit SubGroup");

			cell = row.createCell(8);
			row.getCell(8).setCellStyle(style);
			cell.setCellValue("Pipe Size");

			cell = row.createCell(9);
			row.getCell(9).setCellStyle(style);
			cell.setCellValue("NonPolishing Qty");

			cell = row.createCell(10);
			row.getCell(10).setCellStyle(style);
			cell.setCellValue("NonPolishing Wt");

			int i = 1;
			int j = 1;
			for (ProductionPipeNonpolishModel m : productionPipeNonpolishModel) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.gettMotherCoilBatch());

				cell = row.createCell(2);
				cell.setCellValue(m.gettMotherCoilThickness());

				cell = row.createCell(3);
				cell.setCellValue(m.gettMotherCoilGrade());

				cell = row.createCell(4);
				cell.setCellValue(m.gettPipeSlitBatch());

				cell = row.createCell(5);
				cell.setCellValue(m.gettPipeSlitWidth());

				cell = row.createCell(6);
				cell.setCellValue(m.gettPipeSlitQty());

				cell = row.createCell(7);
				cell.setCellValue(m.getSlitSubGroup());

				cell = row.createCell(8);
				cell.setCellValue(m.gettPipeSize());

				cell = row.createCell(9);
				cell.setCellValue(m.getNonPolishingQty());

				cell = row.createCell(10);
				cell.setCellValue(m.getNonPolishingWt());

				

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
