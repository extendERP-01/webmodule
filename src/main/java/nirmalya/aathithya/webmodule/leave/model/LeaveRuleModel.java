package nirmalya.aathithya.webmodule.leave.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaveRuleModel {
	
	private String ruleId;//primary key
	
	private String leaveType; 
	
	private String jobTitle; 
	
	private String emplmntStatus; 
	
	private String empl; 
	
	private String department;
	
	private String leavePeriod;  
	
	private Double leavePerPeriod;  
	
	private Boolean isadminAssign;
	
	private Boolean isEmpApply;
	
	private Boolean isEmpAlyMoreThanALeave;
	
	private Boolean isAccrueEnb;
	
	private Boolean isCarriedFwd;
	
	private Double carriedFwdPercnt;
	
	private Double maxCaryFwdAmt; 
	
	private String carryFwdPeriod; 
	
	private Boolean properLeaveJDate;
	
    private String createdOn;
	
	private String updateOn;
	
	private String createdBy;
	
	private String action;
	
	public LeaveRuleModel() {
		super();
	}
	
	public LeaveRuleModel(String ruleId,  String leaveType, String jobTitle,String emplmntStatus,String empl, String department,String leavePeriod,Double leavePerPeriod, Boolean isadminAssign,Boolean isEmpApply,Boolean isEmpAlyMoreThanALeave,Boolean isAccrueEnb,Boolean isCarriedFwd,Double carriedFwdPercnt,Double maxCaryFwdAmt,String carryFwdPeriod,Boolean properLeaveJDate) {
		super();
		this.ruleId = ruleId;
		this.leaveType = leaveType;
		this.jobTitle = jobTitle;
		this.emplmntStatus = emplmntStatus;
		this.empl = empl;
		this.department = department;
		this.leavePeriod = leavePeriod;
		this.leavePerPeriod = leavePerPeriod;
		this.isadminAssign = isadminAssign;
		this.isEmpApply = isEmpApply;
		this.isEmpAlyMoreThanALeave = isEmpAlyMoreThanALeave;
		this.isAccrueEnb = isAccrueEnb;
		this.isCarriedFwd = isCarriedFwd;
		this.carriedFwdPercnt = carriedFwdPercnt;
		this.maxCaryFwdAmt = maxCaryFwdAmt;
		this.carryFwdPeriod = carryFwdPeriod;
		this.properLeaveJDate = properLeaveJDate; 
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getEmplmntStatus() {
		return emplmntStatus;
	}

	public void setEmplmntStatus(String emplmntStatus) {
		this.emplmntStatus = emplmntStatus;
	}

	public String getEmpl() {
		return empl;
	}

	public void setEmpl(String empl) {
		this.empl = empl;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getLeavePeriod() {
		return leavePeriod;
	}

	public void setLeavePeriod(String leavePeriod) {
		this.leavePeriod = leavePeriod;
	}

	public Double getLeavePerPeriod() {
		return leavePerPeriod;
	}

	public void setLeavePerPeriod(Double leavePerPeriod) {
		this.leavePerPeriod = leavePerPeriod;
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

	public Boolean getIsAccrueEnb() {
		return isAccrueEnb;
	}

	public void setIsAccrueEnb(Boolean isAccrueEnb) {
		this.isAccrueEnb = isAccrueEnb;
	}

	public Boolean getIsCarriedFwd() {
		return isCarriedFwd;
	}

	public void setIsCarriedFwd(Boolean isCarriedFwd) {
		this.isCarriedFwd = isCarriedFwd;
	}

	public Double getCarriedFwdPercnt() {
		return carriedFwdPercnt;
	}

	public void setCarriedFwdPercnt(Double carriedFwdPercnt) {
		this.carriedFwdPercnt = carriedFwdPercnt;
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
