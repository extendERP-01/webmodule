package nirmalya.aathithya.webmodule.account.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class SalesAccountLedgerModel {

	private String tReceiptVoucher;

	private String tPaidFromCustomer;

	private String tRVcPaymentFor;

	private String tRVcOrderNo;

	private String tRVcInvoiceId;

	private Boolean tRVcGSTType;

	private Double tRVcGSTRate;

	private Double tRVcTaxableAmount;

	private Double tRVcIGST;

	private Double tRVcCGST;

	private Double tRVcSGST;

	private String tPaymentMode;

	private String tBank;

	private String tBankBranch;

	private String tBankAccountNo;

	private String tPaymentTransactionNo;

	private String tPaymentReferenceNo;

	private Double tRVcTotalAmount;

	private Boolean tRVcActive;

	private String createdOn;

	private String createdBy;

	private String curDate;

	private String status;

	private String action;

	private String fromDate;

	private String toDate;

	public SalesAccountLedgerModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettReceiptVoucher() {
		return tReceiptVoucher;
	}

	public void settReceiptVoucher(String tReceiptVoucher) {
		this.tReceiptVoucher = tReceiptVoucher;
	}

	public String gettPaidFromCustomer() {
		return tPaidFromCustomer;
	}

	public void settPaidFromCustomer(String tPaidFromCustomer) {
		this.tPaidFromCustomer = tPaidFromCustomer;
	}

	public String gettRVcPaymentFor() {
		return tRVcPaymentFor;
	}

	public void settRVcPaymentFor(String tRVcPaymentFor) {
		this.tRVcPaymentFor = tRVcPaymentFor;
	}

	public String gettRVcOrderNo() {
		return tRVcOrderNo;
	}

	public void settRVcOrderNo(String tRVcOrderNo) {
		this.tRVcOrderNo = tRVcOrderNo;
	}

	public String gettRVcInvoiceId() {
		return tRVcInvoiceId;
	}

	public void settRVcInvoiceId(String tRVcInvoiceId) {
		this.tRVcInvoiceId = tRVcInvoiceId;
	}

	public Boolean gettRVcGSTType() {
		return tRVcGSTType;
	}

	public void settRVcGSTType(Boolean tRVcGSTType) {
		this.tRVcGSTType = tRVcGSTType;
	}

	public Double gettRVcGSTRate() {
		return tRVcGSTRate;
	}

	public void settRVcGSTRate(Double tRVcGSTRate) {
		this.tRVcGSTRate = tRVcGSTRate;
	}

	public Double gettRVcTaxableAmount() {
		return tRVcTaxableAmount;
	}

	public void settRVcTaxableAmount(Double tRVcTaxableAmount) {
		this.tRVcTaxableAmount = tRVcTaxableAmount;
	}

	public Double gettRVcIGST() {
		return tRVcIGST;
	}

	public void settRVcIGST(Double tRVcIGST) {
		this.tRVcIGST = tRVcIGST;
	}

	public Double gettRVcCGST() {
		return tRVcCGST;
	}

	public void settRVcCGST(Double tRVcCGST) {
		this.tRVcCGST = tRVcCGST;
	}

	public Double gettRVcSGST() {
		return tRVcSGST;
	}

	public void settRVcSGST(Double tRVcSGST) {
		this.tRVcSGST = tRVcSGST;
	}

	public String gettPaymentMode() {
		return tPaymentMode;
	}

	public void settPaymentMode(String tPaymentMode) {
		this.tPaymentMode = tPaymentMode;
	}

	public String gettBank() {
		return tBank;
	}

	public void settBank(String tBank) {
		this.tBank = tBank;
	}

	public String gettBankBranch() {
		return tBankBranch;
	}

	public void settBankBranch(String tBankBranch) {
		this.tBankBranch = tBankBranch;
	}

	public String gettBankAccountNo() {
		return tBankAccountNo;
	}

	public void settBankAccountNo(String tBankAccountNo) {
		this.tBankAccountNo = tBankAccountNo;
	}

	public String gettPaymentTransactionNo() {
		return tPaymentTransactionNo;
	}

	public void settPaymentTransactionNo(String tPaymentTransactionNo) {
		this.tPaymentTransactionNo = tPaymentTransactionNo;
	}

	public String gettPaymentReferenceNo() {
		return tPaymentReferenceNo;
	}

	public void settPaymentReferenceNo(String tPaymentReferenceNo) {
		this.tPaymentReferenceNo = tPaymentReferenceNo;
	}

	public Double gettRVcTotalAmount() {
		return tRVcTotalAmount;
	}

	public void settRVcTotalAmount(Double tRVcTotalAmount) {
		this.tRVcTotalAmount = tRVcTotalAmount;
	}

	public Boolean gettRVcActive() {
		return tRVcActive;
	}

	public void settRVcActive(Boolean tRVcActive) {
		this.tRVcActive = tRVcActive;
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

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
