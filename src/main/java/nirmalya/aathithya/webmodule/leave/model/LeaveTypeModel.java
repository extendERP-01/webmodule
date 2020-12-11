package nirmalya.aathithya.webmodule.leave.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaveTypeModel {

	private String lTypeId;//primary key
	
	private String lTypeName; 
	
	private Double lTypePeriod; 
	
	private Boolean isadminAssign;
	
	private Boolean isEmpApply;
	
	private Boolean isEmpAlyMoreThanALeave;
	
	private Boolean lAccrueEnb;
	
	private Boolean lCarriedFwd;
	
	private Double lCarriedFwdPercnt;
	
	private Double maxCaryFwdAmt; 
	
	private String carryFwdPeriod; 
	
	private Boolean properLeaveJDate;
	
	private Boolean sentEmailNotify;
	
	private String lTypeShowActive;
	
	private String lTypeShowAccrue;

	private String lTypeShowCarried;
	
	private Boolean lTypeStatus;
	
    private String lTypeCreatedOn;
	
	private String lTypeUpdateOn;
	
	private String createdBy;
	
	private String action;
	
	public LeaveTypeModel() {
		super();
	}
	
		
	public LeaveTypeModel(String lTypeId,  String lTypeName, Double lTypePeriod, Boolean isadminAssign,Boolean isEmpApply,Boolean isEmpAlyMoreThanALeave,Boolean lAccrueEnb,Boolean lCarriedFwd,Double lCarriedFwdPercnt,Double maxCaryFwdAmt,String carryFwdPeriod,Boolean properLeaveJDate,Boolean sentEmailNotify,Boolean lTypeStatus) {
		super();
		this.lTypeId = lTypeId;
		this.lTypeName = lTypeName;
		this.lTypePeriod = lTypePeriod;
		this.isadminAssign = isadminAssign;
		this.isEmpApply = isEmpApply;
		this.isEmpAlyMoreThanALeave = isEmpAlyMoreThanALeave;
		this.lAccrueEnb = lAccrueEnb;
		this.lCarriedFwd = lCarriedFwd;
		this.lCarriedFwdPercnt = lCarriedFwdPercnt;
		this.maxCaryFwdAmt = maxCaryFwdAmt;
		this.carryFwdPeriod = carryFwdPeriod;
		this.properLeaveJDate = properLeaveJDate;
		this.sentEmailNotify = sentEmailNotify;
		this.lTypeStatus = lTypeStatus;
	}

	
	
	
	public String getlTypeId() {
		return lTypeId;
	}



	public void setlTypeId(String lTypeId) {
		this.lTypeId = lTypeId;
	}



	public String getlTypeName() {
		return lTypeName;
	}



	public void setlTypeName(String lTypeName) {
		this.lTypeName = lTypeName;
	}



	public Double getlTypePeriod() {
		return lTypePeriod;
	}



	public void setlTypePeriod(Double lTypePeriod) {
		this.lTypePeriod = lTypePeriod;
	}



	public Boolean getIsadminAssign() {
		return isadminAssign;
	}



	public void setIsadminAssign(Boolean isadminAssign) {
		this.isadminAssign = isadminAssign;
	}



	public Boolean getIsEmpApply() {
		return isEmpApply;
	}



	public void setIsEmpApply(Boolean isEmpApply) {
		this.isEmpApply = isEmpApply;
	}



	public Boolean getIsEmpAlyMoreThanALeave() {
		return isEmpAlyMoreThanALeave;
	}



	public void setIsEmpAlyMoreThanALeave(Boolean isEmpAlyMoreThanALeave) {
		this.isEmpAlyMoreThanALeave = isEmpAlyMoreThanALeave;
	}

	public String getlTypeShowAccrue() {
		return lTypeShowAccrue;
	}


	public void setlTypeShowAccrue(String lTypeShowAccrue) {
		this.lTypeShowAccrue = lTypeShowAccrue;
	}

	
	public String getlTypeShowCarried() {
		return lTypeShowCarried;
	}


	public void setlTypeShowCarried(String lTypeShowCarried) {
		this.lTypeShowCarried = lTypeShowCarried;
	}


	public Boolean getlAccrueEnb() {
		return lAccrueEnb;
	}



	public void setlAccrueEnb(Boolean lAccrueEnb) {
		this.lAccrueEnb = lAccrueEnb;
	}



	public Boolean getlCarriedFwd() {
		return lCarriedFwd;
	}



	public void setlCarriedFwd(Boolean lCarriedFwd) {
		this.lCarriedFwd = lCarriedFwd;
	}



	public Double getlCarriedFwdPercnt() {
		return lCarriedFwdPercnt;
	}



	public void setlCarriedFwdPercnt(Double lCarriedFwdPercnt) {
		this.lCarriedFwdPercnt = lCarriedFwdPercnt;
	}



	public Double getMaxCaryFwdAmt() {
		return maxCaryFwdAmt;
	}



	public void setMaxCaryFwdAmt(Double maxCaryFwdAmt) {
		this.maxCaryFwdAmt = maxCaryFwdAmt;
	}

	
	public String getCarryFwdPeriod() {
		return carryFwdPeriod;
	}

	public void setCarryFwdPeriod(String carryFwdPeriod) {
		this.carryFwdPeriod = carryFwdPeriod;
	}


	public Boolean getProperLeaveJDate() {
		return properLeaveJDate;
	}



	public void setProperLeaveJDate(Boolean properLeaveJDate) {
		this.properLeaveJDate = properLeaveJDate;
	}



	public Boolean getSentEmailNotify() {
		return sentEmailNotify;
	}



	public void setSentEmailNotify(Boolean sentEmailNotify) {
		this.sentEmailNotify = sentEmailNotify;
	}



	public String getlTypeShowActive() {
		return lTypeShowActive;
	}



	public void setlTypeShowActive(String lTypeShowActive) {
		this.lTypeShowActive = lTypeShowActive;
	}



	public Boolean getlTypeStatus() {
		return lTypeStatus;
	}



	public void setlTypeStatus(Boolean lTypeStatus) {
		this.lTypeStatus = lTypeStatus;
	}



	public String getlTypeCreatedOn() {
		return lTypeCreatedOn;
	}



	public void setlTypeCreatedOn(String lTypeCreatedOn) {
		this.lTypeCreatedOn = lTypeCreatedOn;
	}



	public String getlTypeUpdateOn() {
		return lTypeUpdateOn;
	}



	public void setlTypeUpdateOn(String lTypeUpdateOn) {
		this.lTypeUpdateOn = lTypeUpdateOn;
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
