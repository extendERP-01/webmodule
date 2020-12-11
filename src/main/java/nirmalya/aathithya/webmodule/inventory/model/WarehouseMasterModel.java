package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WarehouseMasterModel {

	private String warehouseId;
	private String warehouseName;
	private String store;
	private String subInventory;
	private String whDesc;
	private String whActive;
	private String createdBy;
	private String action;
	
	public WarehouseMasterModel() {
		super();
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getSubInventory() {
		return subInventory;
	}

	public void setSubInventory(String subInventory) {
		this.subInventory = subInventory;
	}

	public String getWhDesc() {
		return whDesc;
	}

	public void setWhDesc(String whDesc) {
		this.whDesc = whDesc;
	}

	public String getWhActive() {
		return whActive;
	}

	public void setWhActive(String whActive) {
		this.whActive = whActive;
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
