package nirmalya.aathithya.webmodule.gatepass.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GatePassEntryModel {

	private String gatePass;
	private Byte weightType;
	private String entryDate;
	private String entryTime;
	private String refGatePassNo;
	private String store;
	private Byte passType;
	private String vendor;
	private String challanNo;
	private String vehicleNo;
	private String rstNo;
	private String clientRSTNo;
	private String driverName;
	private Double gross;
	private Double tare;
	private String exitTime;
	private String remarks;
	private String itemCode;
	private String itemName;
	private Double clientNetQty;
	private Double actualNetQty;
	private String serveTypeId;
	private String serveType;
	private String createdBy;
	private String itemCategory;
	private String subCategory;
	private String vendorId;
	private String pOrder;
	private Boolean grnStatus;
	private String customerId;
	private String customer;
	private Double weight;
	private String action;
	private String passTypeName;
	
	public GatePassEntryModel() {
		super();
	}

	public String getGatePass() {
		return gatePass;
	}

	public void setGatePass(String gatePass) {
		this.gatePass = gatePass;
	}

	public Byte getWeightType() {
		return weightType;
	}

	public void setWeightType(Byte weightType) {
		this.weightType = weightType;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getRefGatePassNo() {
		return refGatePassNo;
	}

	public void setRefGatePassNo(String refGatePassNo) {
		this.refGatePassNo = refGatePassNo;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Byte getPassType() {
		return passType;
	}

	public void setPassType(Byte passType) {
		this.passType = passType;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getChallanNo() {
		return challanNo;
	}

	public void setChallanNo(String challanNo) {
		this.challanNo = challanNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getRstNo() {
		return rstNo;
	}

	public void setRstNo(String rstNo) {
		this.rstNo = rstNo;
	}

	public String getClientRSTNo() {
		return clientRSTNo;
	}

	public void setClientRSTNo(String clientRSTNo) {
		this.clientRSTNo = clientRSTNo;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Double getGross() {
		return gross;
	}

	public void setGross(Double gross) {
		this.gross = gross;
	}

	public Double getTare() {
		return tare;
	}

	public void setTare(Double tare) {
		this.tare = tare;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getClientNetQty() {
		return clientNetQty;
	}

	public void setClientNetQty(Double clientNetQty) {
		this.clientNetQty = clientNetQty;
	}

	public Double getActualNetQty() {
		return actualNetQty;
	}

	public void setActualNetQty(Double actualNetQty) {
		this.actualNetQty = actualNetQty;
	}

	public String getServeTypeId() {
		return serveTypeId;
	}

	public void setServeTypeId(String serveTypeId) {
		this.serveTypeId = serveTypeId;
	}

	public String getServeType() {
		return serveType;
	}

	public void setServeType(String serveType) {
		this.serveType = serveType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getpOrder() {
		return pOrder;
	}

	public void setpOrder(String pOrder) {
		this.pOrder = pOrder;
	}

	public Boolean getGrnStatus() {
		return grnStatus;
	}

	public void setGrnStatus(Boolean grnStatus) {
		this.grnStatus = grnStatus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPassTypeName() {
		return passTypeName;
	}

	public void setPassTypeName(String passTypeName) {
		this.passTypeName = passTypeName;
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
