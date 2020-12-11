package nirmalya.aathithya.webmodule.reimbursement.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsTravelingRequisituionModel {

	private String placeName;
	private String fromDate;
	private String toDate;
	private String purpose;
	private Boolean advanceNeed;
	private Double advanceAmount;
	private String reqId;
	private String delete;
	private String action;
	private String createdBy;
	private String companyId;
	private byte reqStatus;
	private String approveStatus ;
	private Integer  currentStageNo;
	private Integer approversStageNo;
	private String returnDesc;
	private String rejectionType;

	public HrmsTravelingRequisituionModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Double getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	public Boolean getAdvanceNeed() {
		return advanceNeed;
	}

	public void setAdvanceNeed(Boolean advanceNeed) {
		this.advanceNeed = advanceNeed;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public byte getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(byte reqStatus) {
		this.reqStatus = reqStatus;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Integer getCurrentStageNo() {
		return currentStageNo;
	}

	public void setCurrentStageNo(Integer currentStageNo) {
		this.currentStageNo = currentStageNo;
	}

	public Integer getApproversStageNo() {
		return approversStageNo;
	}

	public void setApproversStageNo(Integer approversStageNo) {
		this.approversStageNo = approversStageNo;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public String getRejectionType() {
		return rejectionType;
	}

	public void setRejectionType(String rejectionType) {
		this.rejectionType = rejectionType;
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
