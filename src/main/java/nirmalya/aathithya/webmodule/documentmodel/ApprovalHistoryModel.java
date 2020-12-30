/**
 * 
 */
package nirmalya.aathithya.webmodule.documentmodel;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class ApprovalHistoryModel {
	private Integer approvalHistory;
	private String process;
	private String processItem;
	private Integer approvalFromStage;
	private Integer approvalToStage;
	private String approvalComment;
	private Byte approvalFromStatus;
	private Byte approvalToStatus;
	private String approvalAction;
	private String approvalCreatedOn;
	private String approvalBy;
	private String approvalUpdatedOn;
	public Integer getApprovalHistory() {
		return approvalHistory;
	}
	public void setApprovalHistory(Integer approvalHistory) {
		this.approvalHistory = approvalHistory;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getProcessItem() {
		return processItem;
	}
	public void setProcessItem(String processItem) {
		this.processItem = processItem;
	}
	public Integer getApprovalFromStage() {
		return approvalFromStage;
	}
	public void setApprovalFromStage(Integer approvalFromStage) {
		this.approvalFromStage = approvalFromStage;
	}
	public Integer getApprovalToStage() {
		return approvalToStage;
	}
	public void setApprovalToStage(Integer approvalToStage) {
		this.approvalToStage = approvalToStage;
	}
	public String getApprovalComment() {
		return approvalComment;
	}
	public void setApprovalComment(String approvalComment) {
		this.approvalComment = approvalComment;
	}
	public Byte getApprovalFromStatus() {
		return approvalFromStatus;
	}
	public void setApprovalFromStatus(Byte approvalFromStatus) {
		this.approvalFromStatus = approvalFromStatus;
	}
	public Byte getApprovalToStatus() {
		return approvalToStatus;
	}
	public void setApprovalToStatus(Byte approvalToStatus) {
		this.approvalToStatus = approvalToStatus;
	}
	public String getApprovalAction() {
		return approvalAction;
	}
	public void setApprovalAction(String approvalAction) {
		this.approvalAction = approvalAction;
	}
	public String getApprovalCreatedOn() {
		return approvalCreatedOn;
	}
	public void setApprovalCreatedOn(String approvalCreatedOn) {
		this.approvalCreatedOn = approvalCreatedOn;
	}
	public String getApprovalBy() {
		return approvalBy;
	}
	public void setApprovalBy(String approvalBy) {
		this.approvalBy = approvalBy;
	}
	public String getApprovalUpdatedOn() {
		return approvalUpdatedOn;
	}
	public void setApprovalUpdatedOn(String approvalUpdatedOn) {
		this.approvalUpdatedOn = approvalUpdatedOn;
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
