package nirmalya.aathithya.webmodule.leave.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaveApplyAdminModel {
	
	private String applyId;//primary key
	
	private String ltypeName; 
	
	private String empName; 
	
	private Double totalLeave; 
	
	private String lApplyStartDate;
	
	private String lApplyEndDate;
	
	private String lReason;
	
	private String lApplyStatus;
	
	private String lApplyShowActive; 
	
	private String createdBy; 
	
	private String action;
	
	public LeaveApplyAdminModel() {
		super();
	}
	
	
	public LeaveApplyAdminModel(String applyId,  String ltypeName,String empName,Double totalLeave, String lApplyStartDate, String lApplyEndDate,String lReason,
			String lApplyStatus,  String action) {
		super();
		this.applyId = applyId;
		this.ltypeName = ltypeName;
		this.empName = empName;
		this.totalLeave = totalLeave;
		this.lApplyStartDate = lApplyStartDate;
		this.lApplyEndDate = lApplyEndDate;
		this.lReason=lReason;
		this.lApplyStatus = lApplyStatus;
		this.action = action;
	}


	public String getApplyId() {
		return applyId;
	}


	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}


	public String getLtypeName() {
		return ltypeName;
	}


	public void setLtypeName(String ltypeName) {
		this.ltypeName = ltypeName;
	}


	public String getEmpName() {
		return empName;
	}


	public void setEmpName(String empName) {
		this.empName = empName;
	}


	public Double getTotalLeave() {
		return totalLeave;
	}


	public void setTotalLeave(Double totalLeave) {
		this.totalLeave = totalLeave;
	}


	public String getlApplyStartDate() {
		return lApplyStartDate;
	}


	public void setlApplyStartDate(String lApplyStartDate) {
		this.lApplyStartDate = lApplyStartDate;
	}


	public String getlApplyEndDate() {
		return lApplyEndDate;
	}


	public void setlApplyEndDate(String lApplyEndDate) {
		this.lApplyEndDate = lApplyEndDate;
	}


	public String getlReason() {
		return lReason;
	}


	public void setlReason(String lReason) {
		this.lReason = lReason;
	}

	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getlApplyStatus() {
		return lApplyStatus;
	}


	public void setlApplyStatus(String lApplyStatus) {
		this.lApplyStatus = lApplyStatus;
	}


	public String getlApplyShowActive() {
		return lApplyShowActive;
	}


	public void setlApplyShowActive(String lApplyShowActive) {
		this.lApplyShowActive = lApplyShowActive;
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
