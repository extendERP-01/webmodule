package nirmalya.aathithya.webmodule.recruitment.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SpecificTypeModel {
	private String specificId;
	private String specificName;
	private String specificDesc;
	private Boolean specificActive;
	private String specificCreatedBy;
	private String action;
	private String statusName;

	public SpecificTypeModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSpecificId() {
		return specificId;
	}

	public void setSpecificId(String specificId) {
		this.specificId = specificId;
	}

	public String getSpecificName() {
		return specificName;
	}

	public void setSpecificName(String specificName) {
		this.specificName = specificName;
	}

	public String getSpecificDesc() {
		return specificDesc;
	}

	public void setSpecificDesc(String specificDesc) {
		this.specificDesc = specificDesc;
	}

	public Boolean getSpecificActive() {
		return specificActive;
	}

	public void setSpecificActive(Boolean specificActive) {
		this.specificActive = specificActive;
	}

	public String getSpecificCreatedBy() {
		return specificCreatedBy;
	}

	public void setSpecificCreatedBy(String specificCreatedBy) {
		this.specificCreatedBy = specificCreatedBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
