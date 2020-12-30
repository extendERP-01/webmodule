package nirmalya.aathithya.webmodule.leave.model;

import java.io.IOException; 

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeavePeriodModel {


	private String lPeriodId;//primary key
	
	private String lPeriodName; 

	private String lPeriodStartDate;
	
	private String lPeriodEndDate;
	
	private Boolean lPeriodStatus;
	
	private String lPeriodShowActive;
	
    private String lPeriodCreatedOn;
	
	private String lPeriodUpdateOn;
	
	private String createdBy;
	
	private String action;
	
	
	public LeavePeriodModel() {
		super();
	}
	
	public LeavePeriodModel(String lPeriodId,  String lPeriodName, String lPeriodStartDate, String lPeriodEndDate,
			Boolean lPeriodStatus,  String action) {
		super();
		this.lPeriodId = lPeriodId;
		this.lPeriodName = lPeriodName;
		this.lPeriodStartDate = lPeriodStartDate;
		this.lPeriodEndDate = lPeriodEndDate;
		this.lPeriodStatus = lPeriodStatus;
		this.action = action;
	}
	
	
	public String getlPeriodId() {
		return lPeriodId;
	}

	public void setlPeriodId(String lPeriodId) {
		this.lPeriodId = lPeriodId;
	}

	public String getlPeriodName() {
		return lPeriodName;
	}

	public void setlPeriodName(String lPeriodName) {
		this.lPeriodName = lPeriodName;
	}

	public String getlPeriodStartDate() {
		return lPeriodStartDate;
	}

	public void setlPeriodStartDate(String lPeriodStartDate) {
		this.lPeriodStartDate = lPeriodStartDate;
	}

	public String getlPeriodEndDate() {
		return lPeriodEndDate;
	}

	public void setlPeriodEndDate(String lPeriodEndDate) {
		this.lPeriodEndDate = lPeriodEndDate;
	}

	public Boolean getlPeriodStatus() {
		return lPeriodStatus;
	}

	public void setlPeriodStatus(Boolean lPeriodStatus) {
		this.lPeriodStatus = lPeriodStatus;
	}

	public String getlPeriodShowActive() {
		return lPeriodShowActive;
	}

	public void setlPeriodShowActive(String lPeriodShowActive) {
		this.lPeriodShowActive = lPeriodShowActive;
	}

	public String getlPeriodCreatedOn() {
		return lPeriodCreatedOn;
	}

	public void setlPeriodCreatedOn(String lPeriodCreatedOn) {
		this.lPeriodCreatedOn = lPeriodCreatedOn;
	}

	public String getlPeriodUpdateOn() {
		return lPeriodUpdateOn;
	}

	public void setlPeriodUpdateOn(String lPeriodUpdateOn) {
		this.lPeriodUpdateOn = lPeriodUpdateOn;
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

