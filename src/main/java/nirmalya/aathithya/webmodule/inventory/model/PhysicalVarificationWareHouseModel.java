package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PhysicalVarificationWareHouseModel {
	private String storeId;
	private String subInventoryId;
	private String wareHouse;
	private String rack;
	private String date;
	private String item;
	private String itemName;
	private String description;
	private String barcode;
	private Boolean pvStatus;
	private String createdBy;
	private Integer totalStatusCount;
	private BigInteger totalBarCodeCount;
	private List<PhysicalVerificationBarCodeModel> physicalVerificationBarCodeModel = new ArrayList<PhysicalVerificationBarCodeModel>();
	public PhysicalVarificationWareHouseModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getSubInventoryId() {
		return subInventoryId;
	}
	public void setSubInventoryId(String subInventoryId) {
		this.subInventoryId = subInventoryId;
	}
	public String getWareHouse() {
		return wareHouse;
	}
	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}
	public String getRack() {
		return rack;
	}
	public void setRack(String rack) {
		this.rack = rack;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Boolean getPvStatus() {
		return pvStatus;
	}
	public void setPvStatus(Boolean pvStatus) {
		this.pvStatus = pvStatus;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public List<PhysicalVerificationBarCodeModel> getPhysicalVerificationBarCodeModel() {
		return physicalVerificationBarCodeModel;
	}
	public void setPhysicalVerificationBarCodeModel(
			List<PhysicalVerificationBarCodeModel> physicalVerificationBarCodeModel) {
		this.physicalVerificationBarCodeModel = physicalVerificationBarCodeModel;
	}
	
	public Integer getTotalStatusCount() {
		return totalStatusCount;
	}
	public void setTotalStatusCount(Integer totalStatusCount) {
		this.totalStatusCount = totalStatusCount;
	}
	public BigInteger getTotalBarCodeCount() {
		return totalBarCodeCount;
	}
	public void setTotalBarCodeCount(BigInteger totalBarCodeCount) {
		this.totalBarCodeCount = totalBarCodeCount;
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
