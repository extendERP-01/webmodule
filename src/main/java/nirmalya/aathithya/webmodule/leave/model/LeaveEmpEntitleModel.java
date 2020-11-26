package nirmalya.aathithya.webmodule.leave.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaveEmpEntitleModel {

	private String empl;

	private String leaveTypeName;

	private String leavePeriodName;

	private Double totalLeaveDays;

	private Double totalPending;

	private Double totalApproved;

	private Double totalRejected;

	private Double totalCancelled;

	private String totalApplicableLeaves;

	private boolean canApplyMoreLeave;

	private String action;

	public LeaveEmpEntitleModel() {
		super();
	}

	public LeaveEmpEntitleModel(String empl, String leaveTypeName, String leavePeriodName, Double totalLeaveDays,
			Double totalPending, Double totalApproved, Double totalRejected, Double totalCancelled) {
		super();
		this.empl = empl;
		this.leaveTypeName = leaveTypeName;
		this.leavePeriodName = leavePeriodName;
		this.totalLeaveDays = totalLeaveDays;
		this.totalPending = totalPending;
		this.totalApproved = totalApproved;
		this.totalRejected = totalRejected;
		this.totalCancelled = totalCancelled;
	}

	public String getEmpl() {
		return empl;
	}

	public void setEmpl(String empl) {
		this.empl = empl;
	}

	public String getLeaveTypeName() {
		return leaveTypeName;
	}

	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}

	public String getLeavePeriodName() {
		return leavePeriodName;
	}

	public void setLeavePeriodName(String leavePeriodName) {
		this.leavePeriodName = leavePeriodName;
	}

	public Double getTotalLeaveDays() {
		return totalLeaveDays;
	}

	public void setTotalLeaveDays(Double totalLeaveDays) {
		this.totalLeaveDays = totalLeaveDays;
	}

	public Double getTotalPending() {
		return totalPending;
	}

	public void setTotalPending(Double totalPending) {
		this.totalPending = totalPending;
	}

	public Double getTotalApproved() {
		return totalApproved;
	}

	public void setTotalApproved(Double totalApproved) {
		this.totalApproved = totalApproved;
	}

	public Double getTotalRejected() {
		return totalRejected;
	}

	public void setTotalRejected(Double totalRejected) {
		this.totalRejected = totalRejected;
	}

	public Double getTotalCancelled() {
		return totalCancelled;
	}

	public void setTotalCancelled(Double totalCancelled) {
		this.totalCancelled = totalCancelled;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTotalApplicableLeaves() {
		return totalApplicableLeaves;
	}

	public void setTotalApplicableLeaves(String totalApplicableLeaves) {
		this.totalApplicableLeaves = totalApplicableLeaves;
	}

	public boolean isCanApplyMoreLeave() {
		return canApplyMoreLeave;
	}

	public void setCanApplyMoreLeave(boolean canApplyMoreLeave) {
		this.canApplyMoreLeave = canApplyMoreLeave;
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
