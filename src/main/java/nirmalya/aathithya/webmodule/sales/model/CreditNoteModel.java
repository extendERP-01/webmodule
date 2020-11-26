package nirmalya.aathithya.webmodule.sales.model;

public class CreditNoteModel {

	private String creditNote;
	
	private String invoiceNo;
	
	private String saleInvReturn;
	
	private String costCenter;
	
	private String organization;
	
	private String customer;
	
	private String description;
	
	private String creditDate;
	
	private Double grandTotal;
	
	private String action;

	public CreditNoteModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(String creditNote) {
		this.creditNote = creditNote;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getSaleInvReturn() {
		return saleInvReturn;
	}

	public void setSaleInvReturn(String saleInvReturn) {
		this.saleInvReturn = saleInvReturn;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
