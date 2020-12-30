package nirmalya.aathithya.webmodule.asset.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AssignVehicleToDriverModel {

	private String driverId;
	
	private String driverName;
	
	private String vAssetId;
	
	private String vehicleNo;
	
	private String category;
	
	private String assignDate;
	
	private Boolean assignStatus;
	
	private String createdBy;
	
	private String removeDate;
	
	private String comment;
	
	private String createdOnDate;
	
	private String createdOnTime;
	
	private String action;
	
	private String status;
	
	private Double assignedWt;
	
	private Double removedWt;

	public AssignVehicleToDriverModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getvAssetId() {
		return vAssetId;
	}

	public void setvAssetId(String vAssetId) {
		this.vAssetId = vAssetId;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
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

	public String getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedOnDate() {
		return createdOnDate;
	}

	public void setCreatedOnDate(String createdOnDate) {
		this.createdOnDate = createdOnDate;
	}

	public String getCreatedOnTime() {
		return createdOnTime;
	}

	public void setCreatedOnTime(String createdOnTime) {
		this.createdOnTime = createdOnTime;
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

	public Double getAssignedWt() {
		return assignedWt;
	}

	public void setAssignedWt(Double assignedWt) {
		this.assignedWt = assignedWt;
	}

	public Double getRemovedWt() {
		return removedWt;
	}

	public void setRemovedWt(Double removedWt) {
		this.removedWt = removedWt;
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
