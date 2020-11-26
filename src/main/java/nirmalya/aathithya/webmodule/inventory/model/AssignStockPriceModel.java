package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AssignStockPriceModel {

	private String storeName;
	private String storeId;
	private String itemName;
	private String itemId;
	private Double itemSalePrice;
	private Double itemSpecialPrice;
	private Double priceAll;
	private String selectType;
	private String createdBy;
  
	public AssignStockPriceModel() {
		super(); 
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Double getItemSalePrice() {
		return itemSalePrice;
	}

	public void setItemSalePrice(Double itemSalePrice) {
		this.itemSalePrice = itemSalePrice;
	}

	public Double getItemSpecialPrice() {
		return itemSpecialPrice;
	}

	public void setItemSpecialPrice(Double itemSpecialPrice) {
		this.itemSpecialPrice = itemSpecialPrice;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Double getPriceAll() {
		return priceAll;
	}

	public void setPriceAll(Double priceAll) {
		this.priceAll = priceAll;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
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
