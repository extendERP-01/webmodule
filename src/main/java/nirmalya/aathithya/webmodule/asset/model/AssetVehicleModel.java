package nirmalya.aathithya.webmodule.asset.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetVehicleModel {

	private String vehicleAssetId;
	
	private String vehicleNo;
	
	private String vehicleCategory;
	
	private String assetId;
	
	private String serialNo;
	
	private String category;
	
	private Double assignKM;
	
	private Double removeKM;
	
	private String assignDate;
	
	private String removeDate;
	
	private Boolean assignType;
	
	private Boolean assignStatus;
	
	private String createdBy;
	
	private String comment;
	
	private String store;
	
	private String action;
	
	private String status;
	
	private String type;

	public AssetVehicleModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getVehicleAssetId() {
		return vehicleAssetId;
	}

	public void setVehicleAssetId(String vehicleAssetId) {
		this.vehicleAssetId = vehicleAssetId;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getAssignKM() {
		return assignKM;
	}

	public void setAssignKM(Double assignKM) {
		this.assignKM = assignKM;
	}

	public Double getRemoveKM() {
		return removeKM;
	}

	public void setRemoveKM(Double removeKM) {
		this.removeKM = removeKM;
	}

	public String getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}

	public String getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}

	public Boolean getAssignType() {
		return assignType;
	}

	public void setAssignType(Boolean assignType) {
		this.assignType = assignType;
	}

	public Boolean getAssignStatus() {
		return assignStatus;
	}

	public void setAssignStatus(Boolean assignStatus) {
		this.assignStatus = assignStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		ObjectMapper  mapperObj=new ObjectMapper();
		String jsonStr;
		try{
			jsonStr=mapperObj.writeValueAsString(this);
		}catch(IOException ex){
			
			jsonStr=ex.toString();
		}
		return jsonStr;
	}
}
