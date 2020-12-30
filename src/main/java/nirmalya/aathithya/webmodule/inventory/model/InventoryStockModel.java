package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryStockModel {

	private String sku;
	private String brand;
	private String itemName;
	private String itemId;
	private String model;
	private String itemCategoryId;
	private String itemCategoryName;
	private String uom;
	private Double minStock;
	private Double stockInHand;
	private Double stockInTransit;
	private Double totalStock;
	private Double lastPoQty;
	private Double lastReceiveQty;
	private String lastPoDate;
	private String lastReceiceDate;
	private String action;
	private String mode;
  
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(String itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public String getItemCategoryName() {
		return itemCategoryName;
	}

	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Double getMinStock() {
		return minStock;
	}

	public void setMinStock(Double minStock) {
		this.minStock = minStock;
	}

	public Double getStockInHand() {
		return stockInHand;
	}

	public void setStockInHand(Double stockInHand) {
		this.stockInHand = stockInHand;
	}

	public Double getStockInTransit() {
		return stockInTransit;
	}

	public void setStockInTransit(Double stockInTransit) {
		this.stockInTransit = stockInTransit;
	}

	public Double getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Double totalStock) {
		this.totalStock = totalStock;
	}

	public Double getLastPoQty() {
		return lastPoQty;
	}

	public void setLastPoQty(Double lastPoQty) {
		this.lastPoQty = lastPoQty;
	}

	public Double getLastReceiveQty() {
		return lastReceiveQty;
	}

	public void setLastReceiveQty(Double lastReceiveQty) {
		this.lastReceiveQty = lastReceiveQty;
	}

	public String getLastPoDate() {
		return lastPoDate;
	}

	public void setLastPoDate(String lastPoDate) {
		this.lastPoDate = lastPoDate;
	}

	public String getLastReceiceDate() {
		return lastReceiceDate;
	}

	public void setLastReceiceDate(String lastReceiceDate) {
		this.lastReceiceDate = lastReceiceDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public InventoryStockModel() {
		super();
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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
