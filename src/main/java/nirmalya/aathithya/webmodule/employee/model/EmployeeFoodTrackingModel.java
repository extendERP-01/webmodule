package nirmalya.aathithya.webmodule.employee.model;

public class EmployeeFoodTrackingModel {

	private String employeeId;
	private String employeeName;
	private String date;
	private Integer dayMeal;
	private Integer nightMeal;
	private String isEdit;
	private String createdBy;
	private Integer totalMeal;
	private String fromDate;
	private String toDate;

	public EmployeeFoodTrackingModel() {
		super();
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getDayMeal() {
		return dayMeal;
	}

	public void setDayMeal(Integer dayMeal) {
		this.dayMeal = dayMeal;
	}

	public Integer getNightMeal() {
		return nightMeal;
	}

	public void setNightMeal(Integer nightMeal) {
		this.nightMeal = nightMeal;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getTotalMeal() {
		return totalMeal;
	}

	public void setTotalMeal(Integer totalMeal) {
		this.totalMeal = totalMeal;
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
}
