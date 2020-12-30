package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsEmployeeSalaryStructureModel {

	private String employeeId;
	private Double annualCtc;
	private String salaryComponent;
	private String caltype;
	private Double monthlyAmount;
	private Double annualAmount;
	private Double variableAmount;
	private String salaryComponentName;
	private String caltypeName;
	private String effectiveDate;
	private String empFirstName;
	private String empLastName;
	private String payGradeId;
	private String payGradeName;
	private String jobTitleId;
	private String jobTitleName;

	public HrmsEmployeeSalaryStructureModel() {
		super();
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Double getAnnualCtc() {
		return annualCtc;
	}

	public void setAnnualCtc(Double annualCtc) {
		this.annualCtc = annualCtc;
	}

	public String getSalaryComponent() {
		return salaryComponent;
	}

	public void setSalaryComponent(String salaryComponent) {
		this.salaryComponent = salaryComponent;
	}

	public String getCaltype() {
		return caltype;
	}

	public void setCaltype(String caltype) {
		this.caltype = caltype;
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

	public Double getVariableAmount() {
		return variableAmount;
	}

	public void setVariableAmount(Double variableAmount) {
		this.variableAmount = variableAmount;
	}

	public String getSalaryComponentName() {
		return salaryComponentName;
	}

	public void setSalaryComponentName(String salaryComponentName) {
		this.salaryComponentName = salaryComponentName;
	}

	public String getCaltypeName() {
		return caltypeName;
	}

	public void setCaltypeName(String caltypeName) {
		this.caltypeName = caltypeName;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getEmpFirstName() {
		return empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	public String getEmpLastName() {
		return empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	public String getPayGradeId() {
		return payGradeId;
	}

	public void setPayGradeId(String payGradeId) {
		this.payGradeId = payGradeId;
	}

	public String getPayGradeName() {
		return payGradeName;
	}

	public void setPayGradeName(String payGradeName) {
		this.payGradeName = payGradeName;
	}

	public String getJobTitleId() {
		return jobTitleId;
	}

	public void setJobTitleId(String jobTitleId) {
		this.jobTitleId = jobTitleId;
	}

	public String getJobTitleName() {
		return jobTitleName;
	}

	public void setJobTitleName(String jobTitleName) {
		this.jobTitleName = jobTitleName;
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
