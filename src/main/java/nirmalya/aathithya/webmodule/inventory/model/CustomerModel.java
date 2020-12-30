package nirmalya.aathithya.webmodule.inventory.model;

public class CustomerModel {
	
	private String customerName;
	private String customerId;
	
	
	
	public CustomerModel(String customerName, String customerId) {
		super();
		this.customerName = customerName;
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Override
	public String toString() {
		return "CustomerModel [customerName=" + customerName + ", customerId=" + customerId + "]";
	}
	
}
