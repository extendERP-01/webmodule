package nirmalya.aathithya.webmodule.recruitment.model;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
public class AddRequistionModel {
	private String requistionId;
	private String jobCode;
	private String jobTitle;
	private String Department;
	
	private String hiringManager;
	private String onboardBy;
	private Double Budget;
	
	private String createdBy;
	private String Action;
	
	
	public AddRequistionModel() {
		super();
		// TODO Auto-generated constructor stub
	
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}
	public String getHiringManager() {
		return hiringManager;
	}
	public void setHiringManager(String hiringManager) {
		this.hiringManager = hiringManager;
	}
	public String getOnboardBy() {
		return onboardBy;
	}
	public void setOnboardBy(String onboardBy) {
		this.onboardBy = onboardBy;
	}
	public Double getBudget() {
		return Budget;
	}
	public void setBudget(Double budget) {
		Budget = budget;
	}
	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
	

	
	
	public String getRequistionId() {
		return requistionId;
	}
	public void setRequistionId(String requistionId) {
		this.requistionId = requistionId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
