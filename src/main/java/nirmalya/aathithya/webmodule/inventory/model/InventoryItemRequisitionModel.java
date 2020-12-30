/*
 * 
 * Class Showing Item Requisition Entity
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InventoryItemRequisitionModel {
	private String itemRequisition;
	private String costCenter;
	private Double iRQty;
	private String iRDescription;
	private String iRExpectedDate;
	private String iRType;
	private Boolean iRStatus;
	private String dlItemRequisition;
	private String dlItemCategory;
	private String dlItemSubCategory;
	private String dlItem; 
	private String dlItemId; 
	private Double dlQty;
	private Boolean dlActive; 
	private Boolean isEdit;
	private String createdDate;
	private String updatedDate;
	private String createdBy;
	private String serveUnit;
	private String printedBy;
	private Double minStock;
	private String crntInvStore;
	private String currentDate;
    private String status;
	private String action;
    private String delete;
	private String checkbox;
	private Double total;
	private Integer currentStageNo;
	private Integer approverStageNo;
	private Integer currentLevelNo;
	private Integer approverLevelNo;
	private Byte approveStatus;
	private String approveStatusName;
	private String tStore;
	
	
	public String getCheckbox() {
		return checkbox;
	}



	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}



	public InventoryItemRequisitionModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	public String getItemRequisition() {
		return itemRequisition;
	}



	public void setItemRequisition(String itemRequisition) {
		this.itemRequisition = itemRequisition;
	}



	public String getCostCenter() {
		return costCenter;
	}



	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}



	public Double getiRQty() {
		return iRQty;
	}



	public void setiRQty(Double iRQty) {
		this.iRQty = iRQty;
	}



	public String getiRDescription() {
		return iRDescription;
	}



	public void setiRDescription(String iRDescription) {
		this.iRDescription = iRDescription;
	}



	public String getiRExpectedDate() {
		return iRExpectedDate;
	}



	public void setiRExpectedDate(String iRExpectedDate) {
		this.iRExpectedDate = iRExpectedDate;
	}



	public String getiRType() {
		return iRType;
	}



	public void setiRType(String iRType) {
		this.iRType = iRType;
	}



	public Boolean getiRStatus() {
		return iRStatus;
	}



	public void setiRStatus(Boolean iRStatus) {
		this.iRStatus = iRStatus;
	}



	public String getDlItemRequisition() {
		return dlItemRequisition;
	}



	public void setDlItemRequisition(String dlItemRequisition) {
		this.dlItemRequisition = dlItemRequisition;
	}



	public String getDlItemCategory() {
		return dlItemCategory;
	}



	public void setDlItemCategory(String dlItemCategory) {
		this.dlItemCategory = dlItemCategory;
	}



	public String getDlItemSubCategory() {
		return dlItemSubCategory;
	}



	public void setDlItemSubCategory(String dlItemSubCategory) {
		this.dlItemSubCategory = dlItemSubCategory;
	}



	public String getDlItem() {
		return dlItem;
	}



	public void setDlItem(String dlItem) {
		this.dlItem = dlItem;
	}



	public String getDlItemId() {
		return dlItemId;
	}



	public void setDlItemId(String dlItemId) {
		this.dlItemId = dlItemId;
	}



	public Double getDlQty() {
		return dlQty;
	}



	public void setDlQty(Double dlQty) {
		this.dlQty = dlQty;
	}



	public Boolean getDlActive() {
		return dlActive;
	}



	public void setDlActive(Boolean dlActive) {
		this.dlActive = dlActive;
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



	public String getDelete() {
		return delete;
	}



	public void setDelete(String delete) {
		this.delete = delete;
	}



		public Boolean getIsEdit() {
		return isEdit;
	}



	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}



		public Double getTotal() {
		return total;
	}



	public void setTotal(Double total) {
		this.total = total;
	}



		public String getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}



	public String getUpdatedDate() {
		return updatedDate;
	}



	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}



		public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}





	public String getServeUnit() {
		return serveUnit;
	}



	public void setServeUnit(String serveUnit) {
		this.serveUnit = serveUnit;
	}



	public String getPrintedBy() {
		return printedBy;
	}



	public void setPrintedBy(String printedBy) {
		this.printedBy = printedBy;
	}



	public String getCurrentDate() {
		return currentDate;
	}



	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}



	public Double getMinStock() {
		return minStock;
	}



	public void setMinStock(Double minStock) {
		this.minStock = minStock;
	}



	public String getCrntInvStore() {
		return crntInvStore;
	}



	public void setCrntInvStore(String crntInvStore) {
		this.crntInvStore = crntInvStore;
	}

	
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



	public String gettStore() {
		return tStore;
	}



	public void settStore(String tStore) {
		this.tStore = tStore;
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
