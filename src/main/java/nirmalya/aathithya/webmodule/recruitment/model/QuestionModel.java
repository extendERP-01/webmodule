package nirmalya.aathithya.webmodule.recruitment.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QuestionModel {
	private String questionId;
    private String questionName;
    private String questionDesc;
    private Boolean questionActive;
    private String questionCreatedBy;
    private String action;
    public QuestionModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getQuestionDesc() {
		return questionDesc;
	}
	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}
	public Boolean getQuestionActive() {
		return questionActive;
	}
	public void setQuestionActive(Boolean questionActive) {
		this.questionActive = questionActive;
	}
	public String getQuestionCreatedBy() {
		return questionCreatedBy;
	}
	public void setQuestionCreatedBy(String questionCreatedBy) {
		this.questionCreatedBy = questionCreatedBy;
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
