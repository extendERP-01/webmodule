package nirmalya.aathithya.webmodule.account.model;

public class DataModel {
	String vendor;
	String invoice;
	Double crdAmt;
	Double dbtAmt;
	Byte ledgerType;
	Double totalamt;

	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public Double getCrdAmt() {
		return crdAmt;
	}
	public void setCrdAmt(Double crdAmt) {
		this.crdAmt = crdAmt;
	}
	public Double getDbtAmt() {
		return dbtAmt;
	}
	public void setDbtAmt(Double dbtAmt) {
		this.dbtAmt = dbtAmt;
	}
	public Byte getLedgerType() {
		return ledgerType;
	}
	public void setLedgerType(Byte ledgerType) {
		this.ledgerType = ledgerType;
	}
	public Double getTotalamt() {
		return totalamt;
	}
	public void setTotalamt(Double totalamt) {
		this.totalamt = totalamt;
	}
	
	
}
