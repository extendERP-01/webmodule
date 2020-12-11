package nirmalya.aathithya.webmodule.gatepass.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GatePassOutModel {

	private String gatePassOut;
	private String delChallanId;
	private String delChallan;
	private String store;
	private Byte passType;
	private Byte weightType;
	private String outDate;
	private String outTime;
	private String refGatePassNo;
	private String rstNo;
	private String vehicleNo;
	private String driverName;
	private String customerId;
	private String customer;
	private Double gross;
	private Double tare;
	private String itemCategoryId;
	private String itemCategory;
	private String subCatId;
	private String subCat;
	private String itemCode;
	private String itemName;
	private Double actualNetQty;
	private String serveTypeId;
	private String serveType;
	private String remarks;
	private String createdBy;
	private Double weight;
	private String fileUpload;
	private String action;
	private String passTypeName;
	
	public GatePassOutModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getGatePassOut() {
		return gatePassOut;
	}

	public void setGatePassOut(String gatePassOut) {
		this.gatePassOut = gatePassOut;
	}

	public String getDelChallanId() {
		return delChallanId;
	}

	public void setDelChallanId(String delChallanId) {
		this.delChallanId = delChallanId;
	}

	public String getDelChallan() {
		return delChallan;
	}

	public void setDelChallan(String delChallan) {
		this.delChallan = delChallan;
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

	public Byte getWeightType() {
		return weightType;
	}

	public void setWeightType(Byte weightType) {
		this.weightType = weightType;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getRefGatePassNo() {
		return refGatePassNo;
	}

	public void setRefGatePassNo(String refGatePassNo) {
		this.refGatePassNo = refGatePassNo;
	}

	public String getRstNo() {
		return rstNo;
	}

	public void setRstNo(String rstNo) {
		this.rstNo = rstNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
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

	public String getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(String itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(String subCatId) {
		this.subCatId = subCatId;
	}

	public String getSubCat() {
		return subCat;
	}

	public void setSubCat(String subCat) {
		this.subCat = subCat;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
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
