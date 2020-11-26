package nirmalya.aathithya.webmodule.account.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class IncomeTaxPayableModel {

	private String tPaymentVoucher;
	private String tVendor;
	private String tPaymentMode;
	private String tPymntTransactionNo;
	private String tPymntTransactionDate;
	private String tPymntCheckNo;
	private String tBank;
	private String tBankBranch;
	private String tBankAccountNo;
	private String tPymntGrnInvoice;

	private String tPymntReferenceNo;
	private Boolean tPymntGSTType;
	private Double tPymntGSTRate;
	private Double tPymntTDSRate;
	private Double tPymntTaxableAmount;
	private Double tPymntTDSAmount;
	private Double tPymntIGST;
	private Double tPymntCGST;
	private Double tPymntSGST;
	private Double tPymntTotalAmount;
	private Boolean tPymntActive;
	private String createdOn;
	private String createdBy;
	private String curDate;

	private String fromDate;
	private String toDate;
	private String status;
	private String action;

	public IncomeTaxPayableModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettPaymentVoucher() {
		return tPaymentVoucher;
	}

	public void settPaymentVoucher(String tPaymentVoucher) {
		this.tPaymentVoucher = tPaymentVoucher;
	}

	public String gettVendor() {
		return tVendor;
	}

	public void settVendor(String tVendor) {
		this.tVendor = tVendor;
	}

	public String gettPaymentMode() {
		return tPaymentMode;
	}

	public void settPaymentMode(String tPaymentMode) {
		this.tPaymentMode = tPaymentMode;
	}

	public String gettPymntTransactionNo() {
		return tPymntTransactionNo;
	}

	public void settPymntTransactionNo(String tPymntTransactionNo) {
		this.tPymntTransactionNo = tPymntTransactionNo;
	}

	public String gettPymntTransactionDate() {
		return tPymntTransactionDate;
	}

	public void settPymntTransactionDate(String tPymntTransactionDate) {
		this.tPymntTransactionDate = tPymntTransactionDate;
	}

	public String gettPymntCheckNo() {
		return tPymntCheckNo;
	}

	public void settPymntCheckNo(String tPymntCheckNo) {
		this.tPymntCheckNo = tPymntCheckNo;
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

	public String gettPymntGrnInvoice() {
		return tPymntGrnInvoice;
	}

	public void settPymntGrnInvoice(String tPymntGrnInvoice) {
		this.tPymntGrnInvoice = tPymntGrnInvoice;
	}

	public String gettPymntReferenceNo() {
		return tPymntReferenceNo;
	}

	public void settPymntReferenceNo(String tPymntReferenceNo) {
		this.tPymntReferenceNo = tPymntReferenceNo;
	}

	public Boolean gettPymntGSTType() {
		return tPymntGSTType;
	}

	public void settPymntGSTType(Boolean tPymntGSTType) {
		this.tPymntGSTType = tPymntGSTType;
	}

	public Double gettPymntGSTRate() {
		return tPymntGSTRate;
	}

	public void settPymntGSTRate(Double tPymntGSTRate) {
		this.tPymntGSTRate = tPymntGSTRate;
	}

	public Double gettPymntTDSRate() {
		return tPymntTDSRate;
	}

	public void settPymntTDSRate(Double tPymntTDSRate) {
		this.tPymntTDSRate = tPymntTDSRate;
	}

	public Double gettPymntTaxableAmount() {
		return tPymntTaxableAmount;
	}

	public void settPymntTaxableAmount(Double tPymntTaxableAmount) {
		this.tPymntTaxableAmount = tPymntTaxableAmount;
	}

	public Double gettPymntTDSAmount() {
		return tPymntTDSAmount;
	}

	public void settPymntTDSAmount(Double tPymntTDSAmount) {
		this.tPymntTDSAmount = tPymntTDSAmount;
	}

	public Double gettPymntIGST() {
		return tPymntIGST;
	}

	public void settPymntIGST(Double tPymntIGST) {
		this.tPymntIGST = tPymntIGST;
	}

	public Double gettPymntCGST() {
		return tPymntCGST;
	}

	public void settPymntCGST(Double tPymntCGST) {
		this.tPymntCGST = tPymntCGST;
	}

	public Double gettPymntSGST() {
		return tPymntSGST;
	}

	public void settPymntSGST(Double tPymntSGST) {
		this.tPymntSGST = tPymntSGST;
	}

	public Double gettPymntTotalAmount() {
		return tPymntTotalAmount;
	}

	public void settPymntTotalAmount(Double tPymntTotalAmount) {
		this.tPymntTotalAmount = tPymntTotalAmount;
	}

	public Boolean gettPymntActive() {
		return tPymntActive;
	}

	public void settPymntActive(Boolean tPymntActive) {
		this.tPymntActive = tPymntActive;
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
