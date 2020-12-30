package nirmalya.aathithya.webmodule.sales.model;

public class CustomerGSTDataModel {
	
	private String key;
	private String name;
	private Boolean gstType;
	public CustomerGSTDataModel() {
		super();
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getGstType() {
		return gstType;
	}
	public void setGstType(Boolean gstType) {
		this.gstType = gstType;
	}

}
