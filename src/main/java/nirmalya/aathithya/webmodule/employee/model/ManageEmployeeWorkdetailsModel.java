package nirmalya.aathithya.webmodule.employee.model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class ManageEmployeeWorkdetailsModel {
	
	
	private String employeeworkId;
	private String employeeId;
	private String startDate;
	private String endDate;
	private String jobTitle;
	private String jobType;
	private String department;
	private String employmentStatus;
	private String degination;
	private String band;
	private String manager;
	private String annualCTC;
	
	
	private String createdBy;
	
	
	public ManageEmployeeWorkdetailsModel() {
		super();
		// TODO Auto-generated constructor stub

	}


	public String getEmployeeworkId() {
		return employeeworkId;
	}


	public void setEmployeeworkId(String employeeworkId) {
		this.employeeworkId = employeeworkId;
	}


	public String getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	public String getJobType() {
		return jobType;
	}


	public void setJobType(String jobType) {
		this.jobType = jobType;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getJobTitle() {
		return jobTitle;
	}


	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getEmploymentStatus() {
		return employmentStatus;
	}


	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}


	public String getDegination() {
		return degination;
	}


	public void setDegination(String degination) {
		this.degination = degination;
	}


	public String getBand() {
		return band;
	}


	public void setBand(String band) {
		this.band = band;
	}


	public String getManager() {
		return manager;
	}


	public void setManager(String manager) {
		this.manager = manager;
	}


	public String getAnnualCTC() {
		return annualCTC;
	}


	public void setAnnualCTC(String annualCTC) {
		this.annualCTC = annualCTC;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
