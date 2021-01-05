package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrMasterModel {
	
	private String jobTypeId;
	private String jobTypeOrder;
	private String jobTypeName;
	private String jobTypeStatus;
	private String createdOn;
	private String createdBy;
	private String updatedOn;
	
	
	public HrMasterModel() {
		super();
	}


	public String getJobTypeId() {
		return jobTypeId;
	}


	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
	}


	public String getJobTypeOrder() {
		return jobTypeOrder;
	}


	public void setJobTypeOrder(String jobTypeOrder) {
		this.jobTypeOrder = jobTypeOrder;
	}


	public String getJobTypeName() {
		return jobTypeName;
	}


	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}


	public String getJobTypeStatus() {
		return jobTypeStatus;
	}


	public void setJobTypeStatus(String jobTypeStatus) {
		this.jobTypeStatus = jobTypeStatus;
	}


	public String getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
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
