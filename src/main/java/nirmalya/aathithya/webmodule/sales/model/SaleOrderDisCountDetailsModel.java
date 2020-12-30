package nirmalya.aathithya.webmodule.sales.model;

public class SaleOrderDisCountDetailsModel {

	private Double storeDiscount;
	private Double userDiscount;
	private Double totalDiscount;
	private Double canDiscount;

	public SaleOrderDisCountDetailsModel() {
		super();

	}

	public Double getStoreDiscount() {
		return storeDiscount;
	}

	public void setStoreDiscount(Double storeDiscount) {
		this.storeDiscount = storeDiscount;
	}

	public Double getUserDiscount() {
		return userDiscount;
	}

	public void setUserDiscount(Double userDiscount) {
		this.userDiscount = userDiscount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getCanDiscount() {
		return canDiscount;
	}

	public void setCanDiscount(Double canDiscount) {
		this.canDiscount = canDiscount;
	}

}
