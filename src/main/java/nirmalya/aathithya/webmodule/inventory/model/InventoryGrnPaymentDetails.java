package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryGrnPaymentDetails {

	private String poNumber;
	private String invoiceNo;
	private String paymentMode;
	private String referenceNo;
	private String voucherNo;
	private Double outSatandingAmount;
	private String transactionDate;
	private String creditDate;
	private String creditNote;
	private Double creditAmount;

	public InventoryGrnPaymentDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
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

	public Double getOutSatandingAmount() {
		return outSatandingAmount;
	}

	public void setOutSatandingAmount(Double outSatandingAmount) {
		this.outSatandingAmount = outSatandingAmount;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getcreditDate() {
		return creditDate;
	}

	public void setcreditDate(String creditDate) {
		this.creditDate = creditDate;
	}

	public String getcreditNote() {
		return creditNote;
	}

	public void setcreditNote(String creditNote) {
		this.creditNote = creditNote;
	}

	public Double getcreditAmount() {
		return creditAmount;
	}

	public void setcreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
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
