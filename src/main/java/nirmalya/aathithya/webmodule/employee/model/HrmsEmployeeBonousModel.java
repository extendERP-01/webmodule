package nirmalya.aathithya.webmodule.employee.model;

import java.math.BigInteger;

public class HrmsEmployeeBonousModel {

	private String bonousId;
	private String fromDate;
	private String toDate;
	private String employeeId;
	private String empName;
	private Double tripBonous;
	private Double totalBonous;
	private String action;
	private String createdBy;
	private BigInteger attendance;
	private BigInteger totalTrip;

	public String getBonousId() {
		return bonousId;
	}

	public void setBonousId(String bonousId) {
		this.bonousId = bonousId;
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

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Double getTripBonous() {
		return tripBonous;
	}

	public void setTripBonous(Double tripBonous) {
		this.tripBonous = tripBonous;
	}

	public Double getTotalBonous() {
		return totalBonous;
	}

	public void setTotalBonous(Double totalBonous) {
		this.totalBonous = totalBonous;
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

	public BigInteger getAttendance() {
		return attendance;
	}

	public void setAttendance(BigInteger attendance) {
		this.attendance = attendance;
	}

	public BigInteger getTotalTrip() {
		return totalTrip;
	}

	public void setTotalTrip(BigInteger totalTrip) {
		this.totalTrip = totalTrip;
	}

}
