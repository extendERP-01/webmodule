package nirmalya.aathithya.webmodule.account.model;

import java.util.List;

public class VendorModel {
	private String vendor;
	
	private List<InvoiceModel>invModel;

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public List<InvoiceModel> getInvModel() {
		return invModel;
	}

	public void setInvModel(List<InvoiceModel> invModel) {
		this.invModel = invModel;
	}
	
}
