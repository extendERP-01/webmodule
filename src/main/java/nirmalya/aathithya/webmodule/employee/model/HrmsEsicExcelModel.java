package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsEsicExcelModel {
	private String fromDate;
	private String toDate;
	private String empId;
	private String empName;
	private Double empEsci;
	private Double empeerEsci;

	public Double getEmpEsci() {
		return empEsci;
	}

	public void setEmpEsci(Double empEsci) {
		this.empEsci = empEsci;
	}

	public Double getEmpeerEsci() {
		return empeerEsci;
	}

	public void setEmpeerEsci(Double empeerEsci) {
		this.empeerEsci = empeerEsci;
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

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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
