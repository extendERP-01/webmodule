/**
 * Download Excel ViewFucntion
 */
package nirmalya.aathithya.webmodule.inventory.filedownload;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import nirmalya.aathithya.webmodule.inventory.model.InventoryRequisitionIssueNoteModel;
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

/**
 * @author NirmalyaLabs
 *
 */

public class InventoryIssueNoteExcelModel extends AbstractXlsView {
	Logger logger = LoggerFactory.getLogger(InventoryIssueNoteExcelModel.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument function starts");
		try {
			
			List<InventoryRequisitionIssueNoteModel> inventoryRequisitionIssueNoteModel = (List<InventoryRequisitionIssueNoteModel>) model.get("inventoryRequisitionIssueNoteModel");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Inventory IssueNote Sheet");

			
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
			cell.setCellValue("Issue Number");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("Requisition Number");


			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("Status");

			cell = row.createCell(4);
			row.getCell(4).setCellStyle(style);
			cell.setCellValue("Item Category");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue("Item Sub Category");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Item Name");

			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("Quantity");
			int i = 1;
			int j = 1;
			

			for (InventoryRequisitionIssueNoteModel m : inventoryRequisitionIssueNoteModel) {
				
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);
				
				cell = row.createCell(1);
				cell.setCellValue(m.getReqstnIssueNote());


				cell = row.createCell(2);
				cell.setCellValue(m.getItemRequisition());

				cell = row.createCell(3);
				if (m.getpINoteActive()) {
					cell.setCellValue("Active");
				} else {
					cell.setCellValue("Inactive");
				}

				cell = row.createCell(4);
				cell.setCellValue(m.getItemCategory());
				
				cell = row.createCell(5);
				cell.setCellValue(m.getItemSubCategory());

				cell = row.createCell(6);
				cell.setCellValue(m.getItem());
				
				cell = row.createCell(7);
				cell.setCellValue(m.getiNoteQty());
				
			}
			logger.info("Method : buildExcelDocument function starts");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

