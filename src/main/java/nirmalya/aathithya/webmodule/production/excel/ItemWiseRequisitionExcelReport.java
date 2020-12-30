package nirmalya.aathithya.webmodule.production.excel;

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

import nirmalya.aathithya.webmodule.production.model.ItemWiseReqModel; 

public class ItemWiseRequisitionExcelReport  extends AbstractXlsView{
	Logger logger = LoggerFactory.getLogger(ItemWiseRequisitionExcelReport.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<ItemWiseReqModel> itemReqiList = (List<ItemWiseReqModel>) model.get("itemReqiList");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Item Requisition Report");

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
			cell.setCellValue("Date");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Item Name");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Patia");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Saheed Nagar");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("BudhaRaja");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("GoleBazar");

			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("Angul");

			cell = row.createCell(8);
			row.getCell(8).setCellStyle(style);
			cell.setCellValue("Cuttack");
			
			cell = row.createCell(9);
			row.getCell(9).setCellStyle(style);
			cell.setCellValue("City Center");
			
			cell = row.createCell(10);
			row.getCell(10).setCellStyle(style);
			cell.setCellValue("BeheraMal");
			
			cell = row.createCell(11);
			row.getCell(11).setCellStyle(style);
			cell.setCellValue("SaraBahal");
			
			
			cell = row.createCell(12);
			row.getCell(12).setCellStyle(style);
			cell.setCellValue("Bargarh");
			
			cell = row.createCell(13);
			row.getCell(13).setCellStyle(style);
			cell.setCellValue("Total");


			int i = 1;
			int j = 1;
			for (ItemWiseReqModel m : itemReqiList) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getDate());

				cell = row.createCell(2);
				cell.setCellValue(m.getItemName());

				cell = row.createCell(3);
				cell.setCellValue(m.getStorePatia());

				cell = row.createCell(4);
				cell.setCellValue(m.getStoreSnagar());

				cell = row.createCell(5);
				cell.setCellValue(m.getStoreBudharaja());

				cell = row.createCell(6);
				cell.setCellValue(m.getStoreGoleBazar());

				cell = row.createCell(7);
				cell.setCellValue(m.getStoreAngul());

				cell = row.createCell(8);
				cell.setCellValue(m.getStoreCuttack());
				
				cell = row.createCell(9);
				cell.setCellValue(m.getStoreCityCenter());
				
				cell = row.createCell(10);
				cell.setCellValue(m.getStoreBeheramal());
				
				cell = row.createCell(11);
				cell.setCellValue(m.getStoreSarabahal());
				
				cell = row.createCell(12);
				cell.setCellValue(m.getStoreBargarh());
				
				cell = row.createCell(13);
				cell.setCellValue(m.getSum());


			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
