package nirmalya.aathithya.webmodule.account.model;

import java.util.List;

public class InvoiceModel {
	private String invoice;
	private List<DataModel>datamodel;
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public List<DataModel> getDatamodel() {
		return datamodel;
	}
	public void setDatamodel(List<DataModel> datamodel) {
		this.datamodel = datamodel;
	}
	
}
