/**
 * 
 */
package nirmalya.aathithya.webmodule.sales.model;

/**
 * @author ashis
 *
 */
public class SalesDashboardoneModel {

	public Integer customerCount;

	public Integer quotationCompleted;

	public Integer saleInvCompleted;

	public Integer quotationPending;

	public Integer saleInvPending;

	public Double totalSale;

	public String itemName;
	
	public String encodedInv;
	
	public String getEncodedInv() {
		return encodedInv;
	}

	public void setEncodedInv(String encodedInv) {
		this.encodedInv = encodedInv;
	}

	public SalesDashboardoneModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCustomerCount() {
		return customerCount;
	}

	public void setCustomerCount(Integer customerCount) {
		this.customerCount = customerCount;
	}

	public Integer getQuotationCompleted() {
		return quotationCompleted;
	}

	public void setQuotationCompleted(Integer quotationCompleted) {
		this.quotationCompleted = quotationCompleted;
	}

	public Integer getSaleInvCompleted() {
		return saleInvCompleted;
	}

	public void setSaleInvCompleted(Integer saleInvCompleted) {
		this.saleInvCompleted = saleInvCompleted;
	}

	public Integer getQuotationPending() {
		return quotationPending;
	}

	public void setQuotationPending(Integer quotationPending) {
		this.quotationPending = quotationPending;
	}

	public Integer getSaleInvPending() {
		return saleInvPending;
	}

	public void setSaleInvPending(Integer saleInvPending) {
		this.saleInvPending = saleInvPending;
	}

	public Double getTotalSale() {
		return totalSale;
	}

	public void setTotalSale(Double totalSale) {
		this.totalSale = totalSale;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
