package nirmalya.aathithya.webmodule.employee.model;

public class HrmsEmployeeLeaveModel {
	private String leaveId;
	private String employeeId;
	private String employeeName;
	private String fromDate;
	private String toDate;
	private String leaveDays;
	private String leaveGrantBy;
	private String action;
	private String createdBy;

	public HrmsEmployeeLeaveModel() {
		super();
	}

	public String getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(String leaveDays) {
		this.leaveDays = leaveDays;
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

	public String getLeaveGrantBy() {
		return leaveGrantBy;
	}

	public void setLeaveGrantBy(String leaveGrantBy) {
		this.leaveGrantBy = leaveGrantBy;
	}

}
