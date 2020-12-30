package nirmalya.aathithya.webmodule.recruitment.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AddTrainingPlanning {

	private String trainningId;
	private String tranningName;
	private String tranningType;
	private String startDate;
	private String endDate;
	private Boolean status;
	private String createdBy;
	private String editId;
	private String statusName;

	private String Action;

	public AddTrainingPlanning() {
		super();
		// TODO Auto-generated constructor stub

	}

	public String getTrainningId() {
		return trainningId;
	}

	public void setTrainningId(String trainningId) {
		this.trainningId = trainningId;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getTranningName() {
		return tranningName;
	}

	public void setTranningName(String tranningName) {
		this.tranningName = tranningName;
	}

	public String getTranningType() {
		return tranningType;
	}

	public void setTranningType(String tranningType) {
		this.tranningType = tranningType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		Action = action;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
