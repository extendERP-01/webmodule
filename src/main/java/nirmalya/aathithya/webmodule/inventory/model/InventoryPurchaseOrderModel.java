/**
 * Class Showing Purchase Order Entity
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InventoryPurchaseOrderModel {
	private String purchaseOrder;
	private String vendor;
	private String pODescription;
	private Boolean pOStatus;
	private String itemCategory;
	private String itemSubCategory;
	private String itemName;
	private Double pOQty;
	private String status;
	private String delete;
	private String action;
	private String checkedItem;
	private String checkedPoStatus;
	private Double price;
	private Double total;
	private Double subTotal;
	private Double minStock;
	private Double totalReq;
	private Double availQnt;
	private String createdBy;
	private String currentDate;
	private String printedBy;
	private String subGroup;
	private String porderDate;
	private String deliveryPeriod;
	private String termsAndConditions;
	private Integer currentStageNo;
	private Integer approverStageNo;
	private Integer currentLevelNo;
	private Integer approverLevelNo;
	private Byte approveStatus;
	private String approveStatusName;
	private String store;
	private String venderName;

	public Integer getCurrentStageNo() {
		return currentStageNo;
	}

	public void setCurrentStageNo(Integer currentStageNo) {
		this.currentStageNo = currentStageNo;
	}

	public Integer getApproverStageNo() {
		return approverStageNo;
	}

	public void setApproverStageNo(Integer approverStageNo) {
		this.approverStageNo = approverStageNo;
	}

	public Integer getCurrentLevelNo() {
		return currentLevelNo;
	}

	public void setCurrentLevelNo(Integer currentLevelNo) {
		this.currentLevelNo = currentLevelNo;
	}

	public Integer getApproverLevelNo() {
		return approverLevelNo;
	}

	public void setApproverLevelNo(Integer approverLevelNo) {
		this.approverLevelNo = approverLevelNo;
	}

	public Byte getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Byte approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getApproveStatusName() {
		return approveStatusName;
	}

	public void setApproveStatusName(String approveStatusName) {
		this.approveStatusName = approveStatusName;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public String getVendor() {
		return vendor;
	}

	public String getpODescription() {
		return pODescription;
	}

	public Boolean getpOStatus() {
		return pOStatus;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public String getItemSubCategory() {
		return itemSubCategory;
	}

	public String getItemName() {
		return itemName;
	}

	public Double getpOQty() {
		return pOQty;
	}

	public String getStatus() {
		return status;
	}

	public String getDelete() {
		return delete;
	}

	public String getAction() {
		return action;
	}

	public String getCheckedItem() {
		return checkedItem;
	}

	public String getCheckedPoStatus() {
		return checkedPoStatus;
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

	public Double getMinStock() {
		return minStock;
	}

	public Double getTotalReq() {
		return totalReq;
	}

	public Double getAvailQnt() {
		return availQnt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public String getPrintedBy() {
		return printedBy;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public String getPorderDate() {
		return porderDate;
	}

	public String getDeliveryPeriod() {
		return deliveryPeriod;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public void setpODescription(String pODescription) {
		this.pODescription = pODescription;
	}

	public void setpOStatus(Boolean pOStatus) {
		this.pOStatus = pOStatus;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public void setItemSubCategory(String itemSubCategory) {
		this.itemSubCategory = itemSubCategory;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setpOQty(Double pOQty) {
		this.pOQty = pOQty;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setCheckedItem(String checkedItem) {
		this.checkedItem = checkedItem;
	}

	public void setCheckedPoStatus(String checkedPoStatus) {
		this.checkedPoStatus = checkedPoStatus;
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

	public void setMinStock(Double minStock) {
		this.minStock = minStock;
	}

	public void setTotalReq(Double totalReq) {
		this.totalReq = totalReq;
	}

	public void setAvailQnt(Double availQnt) {
		this.availQnt = availQnt;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public void setPrintedBy(String printedBy) {
		this.printedBy = printedBy;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public void setPorderDate(String porderDate) {
		this.porderDate = porderDate;
	}

	public void setDeliveryPeriod(String deliveryPeriod) {
		this.deliveryPeriod = deliveryPeriod;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getVenderName() {
		return venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
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
