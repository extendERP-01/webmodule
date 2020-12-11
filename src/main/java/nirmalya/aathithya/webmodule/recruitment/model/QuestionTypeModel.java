package nirmalya.aathithya.webmodule.recruitment.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QuestionTypeModel {
	private String speTypeId;
	 private String specificName;
    private String questionType;
    private String CreatedBy;
    private String editId;
    private String action;
   
	public QuestionTypeModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getSpeTypeId() {
		return speTypeId;
	}


	public void setSpeTypeId(String speTypeId) {
		this.speTypeId = speTypeId;
	}


	
	  public String getSpecificName() { return specificName; } public void
	 setSpecificName(String specificName) { this.specificName = specificName; }
	 
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	
	
	public String getEditId() {
		return editId;
	}


	public void setEditId(String editId) {
		this.editId = editId;
	}


	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
