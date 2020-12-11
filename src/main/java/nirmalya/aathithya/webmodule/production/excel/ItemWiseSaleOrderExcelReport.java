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

public class ItemWiseSaleOrderExcelReport extends AbstractXlsView{
	Logger logger = LoggerFactory.getLogger(ItemWiseSaleOrderExcelReport.class);

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
			cell.setCellValue("Store Name");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Item Name");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Quantity");

			 


			int i = 1;
			int j = 1;
			for (ItemWiseReqModel m : itemReqiList) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getDate());

				cell = row.createCell(2);
				cell.setCellValue(m.getStoreName());

				cell = row.createCell(3);
				cell.setCellValue(m.getItemName());

				cell = row.createCell(4);
				cell.setCellValue(m.getStorePatia());

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
