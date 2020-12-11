package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExitManagementModel {
	private String exitManagementId;
	private String empId;
	private String empDesignation;
	private String resignationDate;
	private Double salary;
	private Double bonus;
	private Double recovery;
	private String noticeperiod;
	private String releaseDate;
	private String resignation;
	private String createdBy;
	private String action;
	private String empName;
	private String empDesName;
	private String empDepartment;
	private String empDepartmentName;
	private Byte exitStatus;
	public ExitManagementModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getExitManagementId() {
		return exitManagementId;
	}
	public void setExitManagementId(String exitManagementId) {
		this.exitManagementId = exitManagementId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpDesignation() {
		return empDesignation;
	}
	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}
	public String getResignationDate() {
		return resignationDate;
	}
	public void setResignationDate(String resignationDate) {
		this.resignationDate = resignationDate;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public Double getRecovery() {
		return recovery;
	}
	public void setRecovery(Double recovery) {
		this.recovery = recovery;
	}
	public String getNoticeperiod() {
		return noticeperiod;
	}
	public void setNoticeperiod(String noticeperiod) {
		this.noticeperiod = noticeperiod;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getResignation() {
		return resignation;
	}
	public void setResignation(String resignation) {
		this.resignation = resignation;
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
	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpDesName() {
		return empDesName;
	}
	public void setEmpDesName(String empDesName) {
		this.empDesName = empDesName;
	}
	
	public String getEmpDepartment() {
		return empDepartment;
	}
	public void setEmpDepartment(String empDepartment) {
		this.empDepartment = empDepartment;
	}
	public Byte getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(Byte exitStatus) {
		this.exitStatus = exitStatus;
	}
	
	public String getEmpDepartmentName() {
		return empDepartmentName;
	}
	public void setEmpDepartmentName(String empDepartmentName) {
		this.empDepartmentName = empDepartmentName;
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
