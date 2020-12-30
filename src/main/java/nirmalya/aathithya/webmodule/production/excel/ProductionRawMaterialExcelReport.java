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

import nirmalya.aathithya.webmodule.production.model.ProductionSummaryDetailsModel;

public class ProductionRawMaterialExcelReport extends AbstractXlsView {

	Logger logger = LoggerFactory.getLogger(ProductionRawMaterialExcelReport.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<ProductionSummaryDetailsModel> productionSummaryDetailsModeliList = (List<ProductionSummaryDetailsModel>) model
					.get("itemReqiList");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Production Excel Report");

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
			cell.setCellValue("Store");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Planning");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Item Name");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Plan Quantity");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Production Quantity");
			
			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("Return Quantity");
 

			int i = 1;
			int j = 1;
			for (ProductionSummaryDetailsModel m : productionSummaryDetailsModeliList) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getDate());

				cell = row.createCell(2);
				cell.setCellValue(m.getStoreId());

				cell = row.createCell(3);
				cell.setCellValue(m.getPlanId());

				cell = row.createCell(4);
				cell.setCellValue(m.getItemName());

				cell = row.createCell(5);
				cell.setCellValue(m.getPlanQty());
 

				cell = row.createCell(6);
				cell.setCellValue(m.getPlanQty() - m.getScrapQty());
				
				
				cell = row.createCell(7);
				cell.setCellValue(m.getScrapQty());

			}
			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
