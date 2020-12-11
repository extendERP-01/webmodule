package nirmalya.aathithya.webmodule.reimbursement.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsReimbursementPaymentModal {

	private String reqNo;
	private String reimbNo;
	private String fromDate;
	private String toDate;
	private String placeName;
	private String paymentMode;
	private String bankName;
	private String branchName;
	private String accNo;
	private String accHolderName;
	private String employeeId;
	private String action;
	private Double totalAmonut;
	private Double advanceTaken;
	private Double amountTobePaid;
	private Boolean payStatus;
	private String transactionDate;
	private String chequeNo;
	private String transactionNo;
	private String createdBy;
	private String referenceNo;
	private String voucherNo;

	public HrmsReimbursementPaymentModal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getReimbNo() {
		return reimbNo;
	}

	public void setReimbNo(String reimbNo) {
		this.reimbNo = reimbNo;
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

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccHolderName() {
		return accHolderName;
	}

	public void setAccHolderName(String accHolderName) {
		this.accHolderName = accHolderName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double getTotalAmonut() {
		return totalAmonut;
	}

	public void setTotalAmonut(Double totalAmonut) {
		this.totalAmonut = totalAmonut;
	}

	public Double getAdvanceTaken() {
		return advanceTaken;
	}

	public void setAdvanceTaken(Double advanceTaken) {
		this.advanceTaken = advanceTaken;
	}

	public Double getAmountTobePaid() {
		return amountTobePaid;
	}

	public void setAmountTobePaid(Double amountTobePaid) {
		this.amountTobePaid = amountTobePaid;
	}

	public Boolean getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Boolean payStatus) {
		this.payStatus = payStatus;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
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
