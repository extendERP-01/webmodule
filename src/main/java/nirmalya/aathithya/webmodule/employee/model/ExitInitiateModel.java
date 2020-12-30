package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExitInitiateModel {
	private String intiateId;
	private String empId;
	private String empName;
	private String manager;
	private String empDepartment;
	private String department;
	private String person;
	private Boolean clearanceStatus;
	private String comment;
	private String createdBy;
	private String empDepName;
	private String action;
	public ExitInitiateModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getIntiateId() {
		return intiateId;
	}

	public void setIntiateId(String intiateId) {
		this.intiateId = intiateId;
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
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getEmpDepartment() {
		return empDepartment;
	}
	public void setEmpDepartment(String empDepartment) {
		this.empDepartment = empDepartment;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public Boolean getClearanceStatus() {
		return clearanceStatus;
	}
	public void setClearanceStatus(Boolean clearanceStatus) {
		this.clearanceStatus = clearanceStatus;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getEmpDepName() {
		return empDepName;
	}
	public void setEmpDepName(String empDepName) {
		this.empDepName = empDepName;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
