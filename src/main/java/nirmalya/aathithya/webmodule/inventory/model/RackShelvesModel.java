package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class RackShelvesModel {

	private String rackId;
	private String store;
	private String subInventory;
	private String warehouse;
	private String rackName;
	private Double shelvesCapacity;
	private String shelf;
	private String itemId;
	private Double itemCapacity;
	private String createdBy;
	private String slNo;
	private String action;
	private List<DropDownModel> rackList;
	private List<DropDownModel> shelvesList;
	private List<DropDownModel> itemList;

	public RackShelvesModel() {
		super();
	}

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
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

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getRackName() {
		return rackName;
	}

	public void setRackName(String rackName) {
		this.rackName = rackName;
	}

	public Double getShelvesCapacity() {
		return shelvesCapacity;
	}

	public void setShelvesCapacity(Double shelvesCapacity) {
		this.shelvesCapacity = shelvesCapacity;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Double getItemCapacity() {
		return itemCapacity;
	}

	public void setItemCapacity(Double itemCapacity) {
		this.itemCapacity = itemCapacity;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSlNo() {
		return slNo;
	}

	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<DropDownModel> getRackList() {
		return rackList;
	}

	public void setRackList(List<DropDownModel> rackList) {
		this.rackList = rackList;
	}

	public List<DropDownModel> getShelvesList() {
		return shelvesList;
	}

	public void setShelvesList(List<DropDownModel> shelvesList) {
		this.shelvesList = shelvesList;
	}

	public List<DropDownModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<DropDownModel> itemList) {
		this.itemList = itemList;
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
