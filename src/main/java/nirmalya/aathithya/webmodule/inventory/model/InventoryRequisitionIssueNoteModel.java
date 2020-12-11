/**
 * Class Showing Request Issue Note Entity
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InventoryRequisitionIssueNoteModel {
	private String reqstnIssueNote;
	private String itemRequisition;
	private String iNoteDescription;
	private Boolean pINoteActive;
	private String itemId;
	private String itemCategory;
	private String itemSubCategory;
	private String item;
	private Double iNoteQty;
	private String issueCreate;
	private String requisitionCreate;
	private String itemCode;
	private String serveUnit;
	private Double requestQuantity;
	private Double issueQuantity;
	private String createdBy;
	private String printedBy;
	private String currentDate;
	private String status;
	private String action;
	private String delete;
	private String tStore;
	private String tGodown;
	private String tStoreName;
	private String batchCode;
	private List<String> batchCodeList = new ArrayList<String>();
	private Double reqQty;
	private Double totalIssueQty;
	private Double avlQty;

	public InventoryRequisitionIssueNoteModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getReqstnIssueNote() {
		return reqstnIssueNote;
	}

	public void setReqstnIssueNote(String reqstnIssueNote) {
		this.reqstnIssueNote = reqstnIssueNote;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemRequisition() {
		return itemRequisition;
	}

	public void setItemRequisition(String itemRequisition) {
		this.itemRequisition = itemRequisition;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemSubCategory() {
		return itemSubCategory;
	}

	public void setItemSubCategory(String itemSubCategory) {
		this.itemSubCategory = itemSubCategory;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getiNoteQty() {
		return iNoteQty;
	}

	public void setiNoteQty(Double iNoteQty) {
		this.iNoteQty = iNoteQty;
	}

	public String getiNoteDescription() {
		return iNoteDescription;
	}

	public void setiNoteDescription(String iNoteDescription) {
		this.iNoteDescription = iNoteDescription;
	}

	public Boolean getpINoteActive() {
		return pINoteActive;
	}

	public void setpINoteActive(Boolean pINoteActive) {
		this.pINoteActive = pINoteActive;
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

	public String getIssueCreate() {
		return issueCreate;
	}

	public void setIssueCreate(String issueCreate) {
		this.issueCreate = issueCreate;
	}

	public String getRequisitionCreate() {
		return requisitionCreate;
	}

	public void setRequisitionCreate(String requisitionCreate) {
		this.requisitionCreate = requisitionCreate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getServeUnit() {
		return serveUnit;
	}

	public void setServeUnit(String serveUnit) {
		this.serveUnit = serveUnit;
	}

	public Double getRequestQuantity() {
		return requestQuantity;
	}

	public void setRequestQuantity(Double requestQuantity) {
		this.requestQuantity = requestQuantity;
	}

	public Double getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Double issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public String gettStore() {
		return tStore;
	}

	public void settStore(String tStore) {
		this.tStore = tStore;
	}

	public String gettGodown() {
		return tGodown;
	}

	public void settGodown(String tGodown) {
		this.tGodown = tGodown;
	}

	public String gettStoreName() {
		return tStoreName;
	}

	public void settStoreName(String tStoreName) {
		this.tStoreName = tStoreName;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public List<String> getBatchCodeList() {
		return batchCodeList;
	}

	public void setBatchCodeList(List<String> batchCodeList) {
		this.batchCodeList = batchCodeList;
	}

	public Double getReqQty() {
		return reqQty;
	}

	public void setReqQty(Double reqQty) {
		this.reqQty = reqQty;
	}

	public Double getTotalIssueQty() {
		return totalIssueQty;
	}

	public void setTotalIssueQty(Double totalIssueQty) {
		this.totalIssueQty = totalIssueQty;
	}

	public Double getAvlQty() {
		return avlQty;
	}

	public void setAvlQty(Double avlQty) {
		this.avlQty = avlQty;
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
