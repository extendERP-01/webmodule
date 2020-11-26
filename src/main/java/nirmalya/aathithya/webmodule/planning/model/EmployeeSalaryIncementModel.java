package nirmalya.aathithya.webmodule.planning.model;

public class EmployeeSalaryIncementModel {

	private String empId;
	private String componentId;
	private String componentName;
	private Double ctc;
	private Double monthlyAmount;
	private Double annualAmount;
	private String calculationType;
	private Double newMonthlyAmount;
	private Double newAnnualAmount;
	private String calcTypeName;
	private Double amount;
	private Double currentCtc;
	private Double currentGross;
	private String createdBy;
	private String action;
	private String effectiveDate;

	public EmployeeSalaryIncementModel() {
		super();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public Double getCtc() {
		return ctc;
	}

	public void setCtc(Double ctc) {
		this.ctc = ctc;
	}

	public Double getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(Double monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public Double getAnnualAmount() {
		return annualAmount;
	}

	public void setAnnualAmount(Double annualAmount) {
		this.annualAmount = annualAmount;
	}

	public String getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}

	public Double getNewMonthlyAmount() {
		return newMonthlyAmount;
	}

	public void setNewMonthlyAmount(Double newMonthlyAmount) {
		this.newMonthlyAmount = newMonthlyAmount;
	}

	public Double getNewAnnualAmount() {
		return newAnnualAmount;
	}

	public void setNewAnnualAmount(Double newAnnualAmount) {
		this.newAnnualAmount = newAnnualAmount;
	}

	public String getCalcTypeName() {
		return calcTypeName;
	}

	public void setCalcTypeName(String calcTypeName) {
		this.calcTypeName = calcTypeName;
	}

	public Double getCurrentCtc() {
		return currentCtc;
	}

	public void setCurrentCtc(Double currentCtc) {
		this.currentCtc = currentCtc;
	}

	public Double getCurrentGross() {
		return currentGross;
	}

	public void setCurrentGross(Double currentGross) {
		this.currentGross = currentGross;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
 

}
