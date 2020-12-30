package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InventoryStockTransferModel {

	private String tStockTransferId;
	private String tFromStore;
	private String tToStore;
	private String tTransferDate;
	private String tSerialNo;
	private String tTransferNo;
	private String tDescription;
	private Boolean tTransferStatus;
	private String tItem;
	private String tItemDescription;
	private String tItemUnit;
	private Double tItemQuantity;
	private String createdBy;
	private String taxRate;
	private Double price;
	private Double total;
	private Double subTotal;
	private String status;
	private String action;
	private String item;
	private String desc;
	private String unit;
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public InventoryStockTransferModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettStockTransferId() {
		return tStockTransferId;
	}

	public void settStockTransferId(String tStockTransferId) {
		this.tStockTransferId = tStockTransferId;
	}

	public String gettFromStore() {
		return tFromStore;
	}

	
	public String getItem() {
		return item;
	}

	public String getDesc() {
		return desc;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Double getPrice() {
		return price;
	}

	public Double getTotal() {
		return total;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void settFromStore(String tFromStore) {
		this.tFromStore = tFromStore;
	}

	public String gettToStore() {
		return tToStore;
	}

	public void settToStore(String tToStore) {
		this.tToStore = tToStore;
	}

	public String gettTransferDate() {
		return tTransferDate;
	}

	public void settTransferDate(String tTransferDate) {
		this.tTransferDate = tTransferDate;
	}

	public String gettSerialNo() {
		return tSerialNo;
	}

	public void settSerialNo(String tSerialNo) {
		this.tSerialNo = tSerialNo;
	}

	public String gettTransferNo() {
		return tTransferNo;
	}

	public void settTransferNo(String tTransferNo) {
		this.tTransferNo = tTransferNo;
	}

	public String gettDescription() {
		return tDescription;
	}

	public void settDescription(String tDescription) {
		this.tDescription = tDescription;
	}

	

	public String gettItem() {
		return tItem;
	}

	public void settItem(String tItem) {
		this.tItem = tItem;
	}

	
	public String gettItemUnit() {
		return tItemUnit;
	}

	public void settItemUnit(String tItemUnit) {
		this.tItemUnit = tItemUnit;
	}

	

	public Boolean gettTransferStatus() {
		return tTransferStatus;
	}

	public void settTransferStatus(Boolean tTransferStatus) {
		this.tTransferStatus = tTransferStatus;
	}

	public String gettItemDescription() {
		return tItemDescription;
	}

	public void settItemDescription(String tItemDescription) {
		this.tItemDescription = tItemDescription;
	}

	public Double gettItemQuantity() {
		return tItemQuantity;
	}

	public void settItemQuantity(Double tItemQuantity) {
		this.tItemQuantity = tItemQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
