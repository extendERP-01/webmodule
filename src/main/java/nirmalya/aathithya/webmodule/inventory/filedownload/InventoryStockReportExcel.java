package nirmalya.aathithya.webmodule.inventory.filedownload;

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
import org.springframework.web.servlet.view.document.AbstractXlsView;

import nirmalya.aathithya.webmodule.inventory.model.InventoryStockDailyReportFinalModel;

public class InventoryStockReportExcel extends AbstractXlsView {
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		InventoryStockDailyReportFinalModel stockReportData = (InventoryStockDailyReportFinalModel) model
				.get("excelData");
		HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Stock Report Report");
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();

		font.setBold(true);
		font.setColor(HSSFColor.RED.index);

		style.setFont(font);
		HSSFRow row = realSheet.createRow(0);
		HSSFCell cell = null;

		for (int i = 0; i < stockReportData.getDates().size(); i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(stockReportData.getDates().get(i));
		}

		for (int i = 0; i < stockReportData.getDataList().size(); i++) {
			row = realSheet.createRow(i+1);

			for (int j = 0; j < stockReportData.getDates().size() ; j++) {
				cell = row.createCell(j);
				if (j == 0) {
					cell.setCellValue(stockReportData.getDataList().get(i).getName());
				} else {
					cell.setCellValue(stockReportData.getDataList().get(i).getData().get(j-1));
				}
			}

		}

	}
}
