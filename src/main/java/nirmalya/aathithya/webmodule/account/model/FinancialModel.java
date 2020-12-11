package nirmalya.aathithya.webmodule.account.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FinancialModel {
	private String financialYearId;
	private String financialYear;
	private String financialFromDate;
	private String financialTodate;
	private Boolean financialStatus;
	private String createdBy;
	private String action;
	private String statusName;
	public FinancialModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFinancialYearId() {
		return financialYearId;
	}

	public void setFinancialYearId(String financialYearId) {
		this.financialYearId = financialYearId;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getFinancialFromDate() {
		return financialFromDate;
	}

	public void setFinancialFromDate(String financialFromDate) {
		this.financialFromDate = financialFromDate;
	}

	public String getFinancialTodate() {
		return financialTodate;
	}

	public void setFinancialTodate(String financialTodate) {
		this.financialTodate = financialTodate;
	}

	public Boolean getFinancialStatus() {
		return financialStatus;
	}

	public void setFinancialStatus(Boolean financialStatus) {
		this.financialStatus = financialStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
