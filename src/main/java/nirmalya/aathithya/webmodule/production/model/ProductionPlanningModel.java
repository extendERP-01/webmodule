package nirmalya.aathithya.webmodule.production.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductionPlanningModel {

	private String ppId;
	private String fromDate;
	private String toDate;
	private String itemId;
	private String itemName;
	private Double reqQuantity;
	private Double prodQuantity;

	private Double total;
	private Boolean productionStatus;
	private Byte approveStatus;
	private String createdBy;
	private Byte currentStageNo;
	private Integer approverStageNo;
	private String returnType;
	private String returnDesc;
	private String action;
	private Double count;
	private String productionStatusName;
	private String approveStatusName;

	private String batchCode;

	private Double batchQty;

	private String rawMaterialId;

	private String rawMaterialName;

	private Double rawMaterialQty;

	private String serveType;

	private String storeId;

	private Boolean totalCompleteStatus;

	private ProductionRawMaterialDetailsModel rawMaterialDetails;

	public ProductionPlanningModel() {
		super();
	}

	public String getPpId() {
		return ppId;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getReqQuantity() {
		return reqQuantity;
	}

	public void setReqQuantity(Double reqQuantity) {
		this.reqQuantity = reqQuantity;
	}

	public Double getProdQuantity() {
		return prodQuantity;
	}

	public void setProdQuantity(Double prodQuantity) {
		this.prodQuantity = prodQuantity;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Boolean getProductionStatus() {
		return productionStatus;
	}

	public void setProductionStatus(Boolean productionStatus) {
		this.productionStatus = productionStatus;
	}

	public Byte getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Byte approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Byte getCurrentStageNo() {
		return currentStageNo;
	}

	public void setCurrentStageNo(Byte currentStageNo) {
		this.currentStageNo = currentStageNo;
	}

	public Integer getApproverStageNo() {
		return approverStageNo;
	}

	public void setApproverStageNo(Integer approverStageNo) {
		this.approverStageNo = approverStageNo;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public String getProductionStatusName() {
		return productionStatusName;
	}

	public void setProductionStatusName(String productionStatusName) {
		this.productionStatusName = productionStatusName;
	}

	public String getApproveStatusName() {
		return approveStatusName;
	}

	public void setApproveStatusName(String approveStatusName) {
		this.approveStatusName = approveStatusName;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Double getBatchQty() {
		return batchQty;
	}

	public void setBatchQty(Double batchQty) {
		this.batchQty = batchQty;
	}

	public ProductionRawMaterialDetailsModel getRawMaterialDetails() {
		return rawMaterialDetails;
	}

	public void setRawMaterialDetails(ProductionRawMaterialDetailsModel rawMaterialDetails) {
		this.rawMaterialDetails = rawMaterialDetails;
	}

	public String getRawMaterialId() {
		return rawMaterialId;
	}

	public void setRawMaterialId(String rawMaterialId) {
		this.rawMaterialId = rawMaterialId;
	}

	public String getRawMaterialName() {
		return rawMaterialName;
	}

	public void setRawMaterialName(String rawMaterialName) {
		this.rawMaterialName = rawMaterialName;
	}

	public Double getRawMaterialQty() {
		return rawMaterialQty;
	}

	public void setRawMaterialQty(Double rawMaterialQty) {
		this.rawMaterialQty = rawMaterialQty;
	}

	public String getServeType() {
		return serveType;
	}

	public void setServeType(String serveType) {
		this.serveType = serveType;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Boolean getTotalCompleteStatus() {
		return totalCompleteStatus;
	}

	public void setTotalCompleteStatus(Boolean totalCompleteStatus) {
		this.totalCompleteStatus = totalCompleteStatus;
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
