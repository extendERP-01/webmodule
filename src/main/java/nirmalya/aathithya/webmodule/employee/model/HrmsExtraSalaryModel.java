package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsExtraSalaryModel {

	private String employeeId;
	private String employeeName;
	private Double workingDay;
	private Double workDay;
	private Double extraDay;
	private String fromDate;
	private String toDate;

	public HrmsExtraSalaryModel() {
		super(); 
	}

	public HrmsExtraSalaryModel(String employeeId, String employeeName, Double workingDay, Double workDay,
			Double extraDay) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.workingDay = workingDay;
		this.workDay = workDay;
		this.extraDay = extraDay;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Double getWorkingDay() {
		return workingDay;
	}

	public void setWorkingDay(Double workingDay) {
		this.workingDay = workingDay;
	}

	public Double getWorkDay() {
		return workDay;
	}

	public void setWorkDay(Double workDay) {
		this.workDay = workDay;
	}

	public Double getExtraDay() {
		return extraDay;
	}

	public void setExtraDay(Double extraDay) {
		this.extraDay = extraDay;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonStr;
		try {
			jsonStr = mapperObj.writeValueAsString(this);
		} catch (IOException ex) {

			jsonStr = ex.toString();
		}
		return jsonStr;
	}
}
