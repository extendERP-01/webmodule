package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryRequisitionModel {

	private String reqId;
	private String desc;
	private String reqType;
	private String reqPrior;
	private String receiveDate;
	private String holdStatus;

	private String sku;
	private String brand;
	private String itemName;
	private String itemId;
	private String model;
	private String itemCategoryId;
	private String itemCategoryName;
	private String uom;
	private String uomId;
	private Double qty;
	private String lastPoDate;
	private Double estimatedPrice;
	private Double estimatedTotalPrice;
	private Double lastPurchaseUnitPrice;
	private Double lastPurchaseTotalPrice;
	private Double quantity;
	private String location;
	private String locationId;
	private String costCenter;
	private String costCenterId;
	private String createdBy;
	private String createdOn;
	private String updatedon;
	private String updatedBy;

	private String moduleId;
	private String componentId;
	private String subComponentId;
	private String approveStatus;
	private String brandName;
	private String modelName;
	private String activeDate;
	private String onHoldDate;
	private String completeDate;
	private String id;

	public InventoryRequisitionModel() {
		super();
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqPrior() {
		return reqPrior;
	}

	public void setReqPrior(String reqPrior) {
		this.reqPrior = reqPrior;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}

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

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getLastPoDate() {
		return lastPoDate;
	}

	public void setLastPoDate(String lastPoDate) {
		this.lastPoDate = lastPoDate;
	}

	public Double getEstimatedPrice() {
		return estimatedPrice;
	}

	public void setEstimatedPrice(Double estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}

	public Double getEstimatedTotalPrice() {
		return estimatedTotalPrice;
	}

	public void setEstimatedTotalPrice(Double estimatedTotalPrice) {
		this.estimatedTotalPrice = estimatedTotalPrice;
	}

	public Double getLastPurchaseUnitPrice() {
		return lastPurchaseUnitPrice;
	}

	public void setLastPurchaseUnitPrice(Double lastPurchaseUnitPrice) {
		this.lastPurchaseUnitPrice = lastPurchaseUnitPrice;
	}

	public Double getLastPurchaseTotalPrice() {
		return lastPurchaseTotalPrice;
	}

	public void setLastPurchaseTotalPrice(Double lastPurchaseTotalPrice) {
		this.lastPurchaseTotalPrice = lastPurchaseTotalPrice;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedon() {
		return updatedon;
	}

	public void setUpdatedon(String updatedon) {
		this.updatedon = updatedon;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUomId() {
		return uomId;
	}

	public void setUomId(String uomId) {
		this.uomId = uomId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getCostCenterId() {
		return costCenterId;
	}

	public void setCostCenterId(String costCenterId) {
		this.costCenterId = costCenterId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getSubComponentId() {
		return subComponentId;
	}

	public void setSubComponentId(String subComponentId) {
		this.subComponentId = subComponentId;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getOnHoldDate() {
		return onHoldDate;
	}

	public void setOnHoldDate(String onHoldDate) {
		this.onHoldDate = onHoldDate;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
