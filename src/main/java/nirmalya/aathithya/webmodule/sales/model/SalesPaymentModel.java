package nirmalya.aathithya.webmodule.sales.model;

public class SalesPaymentModel {

	private Double grandTotal;
	
	private String payMode;
	
	private String payRefNo;
	
	private String createdDate;

	public SalesPaymentModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayRefNo() {
		return payRefNo;
	}

	public void setPayRefNo(String payRefNo) {
		this.payRefNo = payRefNo;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
