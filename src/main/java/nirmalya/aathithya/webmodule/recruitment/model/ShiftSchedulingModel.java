package nirmalya.aathithya.webmodule.recruitment.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ShiftSchedulingModel {
	private String fromDate;
	private String tSchedule;
	private String tScheduleId;
	private String tSection;
	private String tSectionId;
	private String toDate;
	private String tDepartment;
	private String tDepartmentId;
	private String tShift;
	private String tShiftId;
	private String tEmp;
	private String tEmpId;
	private String tRemark;
	private String editId;
    private String status;
	private String action;
	private String createdBy;
	private String sShiftId;
	public ShiftSchedulingModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String gettSchedule() {
		return tSchedule;
	}
	public void settSchedule(String tSchedule) {
		this.tSchedule = tSchedule;
	}
	public String gettSection() {
		return tSection;
	}
	public void settSection(String tSection) {
		this.tSection = tSection;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String gettDepartment() {
		return tDepartment;
	}
	public void settDepartment(String tDepartment) {
		this.tDepartment = tDepartment;
	}
	public String gettShift() {
		return tShift;
	}
	public void settShift(String tShift) {
		this.tShift = tShift;
	}
	public String gettEmp() {
		return tEmp;
	}
	public void settEmp(String tEmp) {
		this.tEmp = tEmp;
	}
	public String gettRemark() {
		return tRemark;
	}
	public void settRemark(String tRemark) {
		this.tRemark = tRemark;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String gettDepartmentId() {
		return tDepartmentId;
	}
	public void settDepartmentId(String tDepartmentId) {
		this.tDepartmentId = tDepartmentId;
	}
	public String gettShiftId() {
		return tShiftId;
	}
	public void settShiftId(String tShiftId) {
		this.tShiftId = tShiftId;
	}
	public String gettEmpId() {
		return tEmpId;
	}
	public void settEmpId(String tEmpId) {
		this.tEmpId = tEmpId;
	}
	
	
	
	public String gettScheduleId() {
		return tScheduleId;
	}
	public void settScheduleId(String tScheduleId) {
		this.tScheduleId = tScheduleId;
	}
	public String gettSectionId() {
		return tSectionId;
	}
	public void settSectionId(String tSectionId) {
		this.tSectionId = tSectionId;
	}
	public String getEditId() {
		return editId;
	}
	public void setEditId(String editId) {
		this.editId = editId;
	}
	
	public String getsShiftId() {
		return sShiftId;
	}
	public void setsShiftId(String sShiftId) {
		this.sShiftId = sShiftId;
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
