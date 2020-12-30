package nirmalya.aathithya.webmodule.leave.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeavePaidTimeOffModel {
	
	private String paidId;//primary key
	
	private String leaveType;  
	
	private String empl; 
	
	private String leavePeriod;  
	
	private Double leavePeriodAmt; 
	
	private String note;
	
    private String createdOn;
	
	private String updateOn;
	
	private String createdBy;
	
	private String action;
	
	public LeavePaidTimeOffModel() {
		super();
	}
	
	public LeavePaidTimeOffModel(String paidId,  String leaveType,String empl, String leavePeriod,Double leavePeriodAmt,String note) {
		super();
		this.paidId = paidId;
		this.leaveType = leaveType;
		this.empl = empl;
		this.leavePeriod = leavePeriod;
		this.leavePeriodAmt = leavePeriodAmt;
		this.note = note; 
	}
	
	
	public String getPaidId() {
		return paidId;
	}

	public void setPaidId(String paidId) {
		this.paidId = paidId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getEmpl() {
		return empl;
	}

	public void setEmpl(String empl) {
		this.empl = empl;
	}

	public String getLeavePeriod() {
		return leavePeriod;
	}

	public void setLeavePeriod(String leavePeriod) {
		this.leavePeriod = leavePeriod;
	}

	public Double getLeavePeriodAmt() {
		return leavePeriodAmt;
	}

	public void setLeavePeriodAmt(Double leavePeriodAmt) {
		this.leavePeriodAmt = leavePeriodAmt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(String updateOn) {
		this.updateOn = updateOn;
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
