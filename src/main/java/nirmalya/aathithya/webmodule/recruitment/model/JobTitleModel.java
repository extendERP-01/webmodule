package nirmalya.aathithya.webmodule.recruitment.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JobTitleModel {
	private String jobId;
	private String jObtitle;
	private String department;
	private String intPragraph;
	private String responsibility;
	private String jobType;
	private String workHourBenifit;
	private String skill;
	private String education;
	private String action;
	private Boolean status;
	private String createdBy;
	private String activeStatus;
	
	public JobTitleModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getjObtitle() {
		return jObtitle;
	}
	public void setjObtitle(String jObtitle) {
		this.jObtitle = jObtitle;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getIntPragraph() {
		return intPragraph;
	}
	public void setIntPragraph(String intPragraph) {
		this.intPragraph = intPragraph;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getWorkHourBenifit() {
		return workHourBenifit;
	}
	public void setWorkHourBenifit(String workHourBenifit) {
		this.workHourBenifit = workHourBenifit;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

