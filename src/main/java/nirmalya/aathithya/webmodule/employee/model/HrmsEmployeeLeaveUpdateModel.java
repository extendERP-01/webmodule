package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsEmployeeLeaveUpdateModel {

	private Integer empLeaveId;
	private String empId;
	private String empName;
	private String date;
	private Integer avlLeave;
	private String createdBy;
	private String prevDate;
	private Byte apvStatus;
	private String action;

	public HrmsEmployeeLeaveUpdateModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getEmpLeaveId() {
		return empLeaveId;
	}

	public void setEmpLeaveId(Integer empLeaveId) {
		this.empLeaveId = empLeaveId;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getAvlLeave() {
		return avlLeave;
	}

	public void setAvlLeave(Integer avlLeave) {
		this.avlLeave = avlLeave;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getPrevDate() {
		return prevDate;
	}

	public void setPrevDate(String prevDate) {
		this.prevDate = prevDate;
	}

	public Byte getApvStatus() {
		return apvStatus;
	}

	public void setApvStatus(Byte apvStatus) {
		this.apvStatus = apvStatus;
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
