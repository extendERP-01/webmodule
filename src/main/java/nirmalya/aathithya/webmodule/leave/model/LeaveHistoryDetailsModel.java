package nirmalya.aathithya.webmodule.leave.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaveHistoryDetailsModel {

	private String date;
	private String action;
	private String comment;
	private String actionTakenFrom;
	private String reqId;
	private String actionTakenTo;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getActionTakenFrom() {
		return actionTakenFrom;
	}
	public void setActionTakenFrom(String actionTakenFrom) {
		this.actionTakenFrom = actionTakenFrom;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getActionTakenTo() {
		return actionTakenTo;
	}
	public void setActionTakenTo(String actionTakenTo) {
		this.actionTakenTo = actionTakenTo;
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
