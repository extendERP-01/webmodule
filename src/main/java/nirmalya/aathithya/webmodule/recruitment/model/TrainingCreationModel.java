package nirmalya.aathithya.webmodule.recruitment.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TrainingCreationModel {
	private String trainingId;
	private String trainingName;
	private String trainingType;
	private String trainingCriteria;
	private String trainingDesc;
	private String action;
	private Boolean status;
	private String createdBy;
	private String activeStatus;

	public TrainingCreationModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}

	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	public String getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}

	public String getTrainingCriteria() {
		return trainingCriteria;
	}

	public void setTrainingCriteria(String trainingCriteria) {
		this.trainingCriteria = trainingCriteria;
	}

	public String getTrainingDesc() {
		return trainingDesc;
	}

	public void setTrainingDesc(String trainingDesc) {
		this.trainingDesc = trainingDesc;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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
