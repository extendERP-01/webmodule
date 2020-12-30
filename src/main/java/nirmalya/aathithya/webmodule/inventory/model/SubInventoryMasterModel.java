package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SubInventoryMasterModel {

	private String subInventoryId;
	private String subInventoryName;
	private String store;
	private String subInvDesc;
	private String subInvActive;
	private String createdBy;
	private String action;
	
	public SubInventoryMasterModel() {
		super();
	}

	public String getSubInventoryId() {
		return subInventoryId;
	}

	public void setSubInventoryId(String subInventoryId) {
		this.subInventoryId = subInventoryId;
	}

	public String getSubInventoryName() {
		return subInventoryName;
	}

	public void setSubInventoryName(String subInventoryName) {
		this.subInventoryName = subInventoryName;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getSubInvDesc() {
		return subInvDesc;
	}

	public void setSubInvDesc(String subInvDesc) {
		this.subInvDesc = subInvDesc;
	}

	public String getSubInvActive() {
		return subInvActive;
	}

	public void setSubInvActive(String subInvActive) {
		this.subInvActive = subInvActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
