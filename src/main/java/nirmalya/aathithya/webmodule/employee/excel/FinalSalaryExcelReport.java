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

import nirmalya.aathithya.webmodule.employee.model.HrmsSalaryModel;

public class FinalSalaryExcelReport extends AbstractXlsView {
	Logger logger = LoggerFactory.getLogger(FinalSalaryExcelReport.class);

	double Total_Day = 0;
	double Work_Day = 0;
	double Leave_Day = 0;
	double Extra_Day = 0;
	double Paid_Day = 0;
	double Monthly_Gross = 0;
	double BasicPay = 0;
	double HRA = 0;
	double Other = 0;
	double Total_Trip = 0;
	double Total_Trip_Bonous = 0;
	double Food_Taken = 0;
	double Food_Avail = 0;
	double Food_Allow = 0;
	double Food_Amount = 0;
	double Extra_Salary = 0;
	double Empl_Epf = 0;
	double Empr_Epf = 0;
	double Empl_Esic = 0;
	double Empr_Esic = 0;
	double Advance_Amount = 0;
	double Net_Salary = 0;

	@SuppressWarnings("unchecked")
	@Override

	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("Method : buildExcelDocument starts");
		try {

			List<HrmsSalaryModel> accountLedger = (List<HrmsSalaryModel>) model.get("consumption");
			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet("Final Salary Report");

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
			cell1.setCellValue("Redimix Final Salary Details");

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
			cell.setCellValue("Total Day");

			cell = row.createCell(5);
			row.getCell(5).setCellStyle(style);
			cell.setCellValue(" Emp Work Day ");

			cell = row.createCell(6);
			row.getCell(6).setCellStyle(style);
			cell.setCellValue("Leave Day");

			cell = row.createCell(7);
			row.getCell(7).setCellStyle(style);
			cell.setCellValue("Extra Day");

			cell = row.createCell(8);
			row.getCell(8).setCellStyle(style);
			cell.setCellValue("Paid Day");

			cell = row.createCell(9);
			row.getCell(9).setCellStyle(style);
			cell.setCellValue("Monthly Gross");

			cell = row.createCell(10);
			row.getCell(10).setCellStyle(style);
			cell.setCellValue("Basic Pay");

			cell = row.createCell(11);
			row.getCell(11).setCellStyle(style);
			cell.setCellValue("HRA");

			cell = row.createCell(12);
			row.getCell(12).setCellStyle(style);
			cell.setCellValue("Other");

			cell = row.createCell(13);
			row.getCell(13).setCellStyle(style);
			cell.setCellValue("Total Trip");

			cell = row.createCell(14);
			row.getCell(14).setCellStyle(style);
			cell.setCellValue("Total Trip Bonous");

			cell = row.createCell(15);
			row.getCell(15).setCellStyle(style);
			cell.setCellValue("Food Taken");

			cell = row.createCell(16);
			row.getCell(16).setCellStyle(style);
			cell.setCellValue("Food Avail");

			cell = row.createCell(17);
			row.getCell(17).setCellStyle(style);
			cell.setCellValue("Food Allow");

			cell = row.createCell(18);
			row.getCell(18).setCellStyle(style);
			cell.setCellValue("Food Amount");

			cell = row.createCell(19);
			row.getCell(19).setCellStyle(style);
			cell.setCellValue("Extra Salary");

			cell = row.createCell(20);
			row.getCell(20).setCellStyle(style);
			cell.setCellValue("Empl Epf");

			cell = row.createCell(21);
			row.getCell(21).setCellStyle(style);
			cell.setCellValue("Emr Epf");

			cell = row.createCell(22);
			row.getCell(22).setCellStyle(style);
			cell.setCellValue("Empl Esic");

			cell = row.createCell(23);
			row.getCell(23).setCellStyle(style);
			cell.setCellValue("Empr Esic");

			cell = row.createCell(24);
			row.getCell(24).setCellStyle(style);
			cell.setCellValue("Advance Amount");

			cell = row.createCell(25);
			row.getCell(25).setCellStyle(style);
			cell.setCellValue("Net Salary");

			int i = 2;
			int j = 1;
			for (HrmsSalaryModel m : accountLedger) {
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
				cell.setCellValue(m.getWorkingDays());

				cell = row.createCell(5);
				cell.setCellValue(m.getEmpWorkDays());

				cell = row.createCell(6);
				cell.setCellValue(m.getLeavDays());

				cell = row.createCell(7);
				cell.setCellValue(m.getExtraWorkDays());

				cell = row.createCell(8);
				cell.setCellValue(m.getPaidDays());

				cell = row.createCell(9);
				cell.setCellValue(m.getMonthlyGross());

				cell = row.createCell(10);
				cell.setCellValue(m.getBasic());

				cell = row.createCell(11);
				cell.setCellValue(m.getHra());

				cell = row.createCell(12);
				cell.setCellValue(m.getOther());

				cell = row.createCell(13);
				cell.setCellValue(m.getTotalTrip());

				cell = row.createCell(14);
				cell.setCellValue(m.getTotalTripAmount());

				cell = row.createCell(15);
				cell.setCellValue(m.getFoodConsumed());

				cell = row.createCell(16);
				cell.setCellValue(m.getFoodAvail());

				cell = row.createCell(17);
				cell.setCellValue(m.getFoodAllowAmount());

				cell = row.createCell(18);
				cell.setCellValue(m.getFoodAmount());

				cell = row.createCell(19);
				cell.setCellValue(m.getExtraSalary());

				cell = row.createCell(20);
				cell.setCellValue(m.getEmpEpf());

				cell = row.createCell(21);
				cell.setCellValue(m.getEmployerEpf());

				cell = row.createCell(22);
				cell.setCellValue(m.getEmpEsic());

				cell = row.createCell(23);
				cell.setCellValue(m.getEmployerEsic());

				cell = row.createCell(24);
				cell.setCellValue(m.getAdvanceAmount());

				cell = row.createCell(25);
				cell.setCellValue(m.getNetSalary());

			}
			row = realSheet.createRow(j + 2);

			for (HrmsSalaryModel m : accountLedger) {

				Total_Day = Total_Day + m.getWorkingDays();
				Work_Day = Work_Day + m.getEmpWorkDays();
				Leave_Day = Leave_Day + m.getLeavDays();
				Extra_Day = Extra_Day + m.getExtraWorkDays();
				Paid_Day = Paid_Day + m.getPaidDays();
				Monthly_Gross = Monthly_Gross + m.getMonthlyGross();
				BasicPay = BasicPay + m.getBasic();
				HRA = HRA + m.getHra();
				Other = Other + m.getOther();
				Total_Trip = Total_Trip + m.getTotalTrip();
				Total_Trip_Bonous = Total_Trip_Bonous + m.getTotalTripAmount();
				Food_Taken = Food_Taken + m.getFoodConsumed();
				Food_Avail = Food_Avail + m.getFoodAvail();
				Food_Allow = Food_Allow + m.getFoodAllowAmount();
				Food_Amount = Food_Amount + m.getFoodAmount();
				Extra_Salary = Extra_Salary + m.getExtraSalary();
				Empl_Epf = Empl_Epf + m.getEmpEpf();
				Empr_Epf = Empr_Epf + m.getEmployerEpf();
				Empl_Esic = Empl_Esic + m.getEmpEsic();
				Empr_Esic = Empr_Esic + m.getEmployerEsic();
				Advance_Amount = Advance_Amount + m.getAdvanceAmount();
				Net_Salary = Net_Salary + m.getNetSalary();

				cell = row.createCell(3);
				row.getCell(3).setCellStyle(style);
				cell.setCellValue("Total:");

				cell = row.createCell(4);
				cell.setCellValue(Total_Day);

				cell = row.createCell(5);
				cell.setCellValue(Work_Day);

				cell = row.createCell(6);
				cell.setCellValue(Leave_Day);

				cell = row.createCell(7);
				cell.setCellValue(Extra_Day);

				cell = row.createCell(8);
				cell.setCellValue(Paid_Day);

				cell = row.createCell(9);
				cell.setCellValue(Monthly_Gross);

				cell = row.createCell(10);
				cell.setCellValue(BasicPay);

				cell = row.createCell(11);
				cell.setCellValue(HRA);

				cell = row.createCell(12);
				cell.setCellValue(Other);

				cell = row.createCell(13);
				cell.setCellValue(Total_Trip);

				cell = row.createCell(14);
				cell.setCellValue(Total_Trip_Bonous);

				cell = row.createCell(15);
				cell.setCellValue(Food_Taken);

				cell = row.createCell(16);
				cell.setCellValue(Food_Avail);

				cell = row.createCell(17);
				cell.setCellValue(Food_Allow);

				cell = row.createCell(18);
				cell.setCellValue(Food_Amount);

				cell = row.createCell(19);
				cell.setCellValue(Extra_Salary);

				cell = row.createCell(20);
				cell.setCellValue(Empl_Epf);

				cell = row.createCell(21);
				cell.setCellValue(Empr_Epf);

				cell = row.createCell(22);
				cell.setCellValue(Empl_Esic);

				cell = row.createCell(23);
				cell.setCellValue(Empr_Esic);

				cell = row.createCell(24);
				cell.setCellValue(Advance_Amount);

				cell = row.createCell(25);
				cell.setCellValue(Net_Salary);

			}

			logger.info("Method : buildExcelDocument ends");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}