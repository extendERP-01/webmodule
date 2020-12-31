package nirmalya.aathithya.webmodule.master.controller;

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

import nirmalya.aathithya.webmodule.master.model.LocationMasterModel;

public class ExcelLocationReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ExcelLocationReport.class);
	@SuppressWarnings("unchecked")
	@Override

	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		
		try {
			List<LocationMasterModel> accountLedger = (List<LocationMasterModel>) model.get("location");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Location Report");
		
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);

			style.setFont(font);

			realSheet.setDefaultColumnWidth(30);
			
			HSSFRow row = realSheet.createRow(0);
			HSSFCell cell = row.createCell(0);
			
			cell = row.createCell(0);
			row.getCell(0).setCellStyle(style);
			cell.setCellValue("ID");

			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("Code");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Name");
			
			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Type");
			
			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Virtual");
			
			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Address");
			
			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("City");
			
			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("State");
			
			cell = row.createCell(8);
			row.getCell(8).setCellStyle(style);
			cell.setCellValue("Country");
			
			cell = row.createCell(9);
			row.getCell(9).setCellStyle(style);
			cell.setCellValue("Status");
			
			cell = row.createCell(10);
			row.getCell(10).setCellStyle(style);
			cell.setCellValue("Create Date");
			
			int i = 2;
			int j = 1;
			
			for (LocationMasterModel m : accountLedger) {
				row = realSheet.createRow(i++);
					j++;
					
					if(m.getLocVirtual().contentEquals("1")) {
						m.setLocVirtual("Yes");
					} else {
						m.setLocVirtual("No");
					}
					
					if(m.getLocStatus().contentEquals("1")) {
						m.setLocStatus("Active");
					} else {
						m.setLocStatus("Inactive");
					}
					
					cell = row.createCell(0);
					cell.setCellValue(m.getLocationId());
					
					cell = row.createCell(1);
					cell.setCellValue(m.getLocationCode());
					
					cell = row.createCell(2);
					cell.setCellValue(m.getLocationName());
					
					cell = row.createCell(3);
					cell.setCellValue(m.getLocationType());
					
					cell = row.createCell(4);
					cell.setCellValue(m.getLocVirtual());
					
					cell = row.createCell(5);
					cell.setCellValue(m.getLocStreet());
					
					cell = row.createCell(6);
					cell.setCellValue(m.getLocCity());
					
					cell = row.createCell(7);
					cell.setCellValue(m.getLocState());
					
					cell = row.createCell(8);
					cell.setCellValue(m.getLocCountry());
					
					cell = row.createCell(9);
					cell.setCellValue(m.getLocStatus());
					
					cell = row.createCell(10);
					cell.setCellValue(m.getCreatedDate());
			}
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : buildExcelDocument ends");
	}
}
