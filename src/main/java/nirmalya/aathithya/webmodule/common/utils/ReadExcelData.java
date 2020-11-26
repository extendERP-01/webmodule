package nirmalya.aathithya.webmodule.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nirmalya.aathithya.webmodule.employee.model.HrmsDailyAttendanceExcelModel;

public class ReadExcelData {

	Logger logger = LoggerFactory.getLogger(ReadExcelData.class);

	XSSFRow row;
	XSSFRow row1;
	XSSFCell cell;

	@SuppressWarnings({ "resource", "deprecation" })
	public List<HrmsDailyAttendanceExcelModel> readFile(String fileName)
			throws FileNotFoundException, IllegalStateException, IOException {
		logger.info("read file Start");
		FileInputStream fis;
		List<HrmsDailyAttendanceExcelModel> attendanceList = new ArrayList<HrmsDailyAttendanceExcelModel>();
		try {
			System.out.println(
					"-------------------------------READING THE excelData -------------------------------------");
			fis = new FileInputStream(fileName);
			XSSFWorkbook workbookRead = new XSSFWorkbook(fis);
			XSSFSheet spreadsheetRead = workbookRead.getSheetAt(0);

			Iterator<Row> rowIterator = spreadsheetRead.iterator();
			/*
			 * Iterator rows = spreadsheetRead.rowIterator(); while (rows.hasNext()) { row1
			 * = (XSSFRow) rows.next();
			 * 
			 * Iterator cells = row1.cellIterator(); while (cells.hasNext()) {
			 * cell=(XSSFCell) cells.next(); if (cell.getCellType() ==
			 * XSSFCell.CELL_TYPE_STRING) { System.out.print(cell.getStringCellValue()+",");
			 * } else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			 * System.out.print(cell.getNumericCellValue()+","); } }
			 * 
			 * }
			 */
			
			int rowNumber = 0;
			while (rowIterator.hasNext()) {
				HrmsDailyAttendanceExcelModel moodels = new HrmsDailyAttendanceExcelModel();
				row = (XSSFRow) rowIterator.next();

				System.out.println("row number " + row.getRowNum());
				if (row.getRowNum() == 0) {
					continue; // just skip the rows if row number is 0 or 1
				}

				SimpleDateFormat formatDAte = new SimpleDateFormat("yyyy-MM-dd");
				Date currDate = row.getCell(0).getDateCellValue();
				String dbDate = formatDAte.format(currDate);
				String name = row.getCell(2).getStringCellValue();
				String bioId = "";

				if (row.getCell(1).getCellType() == XSSFCell.CELL_TYPE_STRING) {
					bioId = row.getCell(1).getStringCellValue();
				} else if (row.getCell(1).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					Double empId = row.getCell(1).getNumericCellValue();
					bioId = empId.toString();
				}

				if (row.getCell(3).getCellType() == XSSFCell.CELL_TYPE_STRING) {
					moodels.setInTime("A");
				}
				if (row.getCell(3).getCellType() == Cell.CELL_TYPE_NUMERIC
						&& DateUtil.isCellDateFormatted(row.getCell(3))) {

					Date date = row.getCell(3).getDateCellValue();
					SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss aa");
					moodels.setInTime(formatTime.format(date));
				}

				if (row.getCell(4).getCellType() == XSSFCell.CELL_TYPE_STRING) {
					moodels.setOutTime("A");
				}
				if (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC
						&& DateUtil.isCellDateFormatted(row.getCell(4))) {

					Date date = row.getCell(4).getDateCellValue();
					SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss aa");
					moodels.setOutTime(formatTime.format(date));

				}
				System.out.println("bioId" + bioId);
				System.out.println("name" + name);
				if(bioId.contains(".")) {
					String [] a = bioId.split("\\.");
					System.out.println(a);
					moodels.setBioMetricId(a[0]);
				}else {
					moodels.setBioMetricId(bioId);
				}
				
				moodels.setDate(dbDate);
				moodels.setEmployeeName(name);
				attendanceList.add(moodels);
			}
			System.out.println(attendanceList);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return attendanceList;
	}

}
