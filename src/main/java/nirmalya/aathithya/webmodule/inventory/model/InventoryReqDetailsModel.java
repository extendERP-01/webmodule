package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class InventoryReqDetailsModel {

	private String reqQuotId;// primary key

	private String rfqName;

	private String rfqValidDate;

	private String rfqBackground;

	private String rfqCreatedOn;

	private String rfqUpdateOn;

	private String rfqDetails;

	private String createdBy;

	private Byte approvedStatus;

	private String rfqVendor;

	private String quotationNo;

	private String checkedRFQId;

	private String ifApproved;

	private List<String> rfqVendors = new ArrayList<String>();

	private String purOrderNo;

	private String purchaseOrderS;

	private String action;

	private String itemCategory;
	
	private List<DropDownModel> vendorList = new ArrayList<DropDownModel>();

	public InventoryReqDetailsModel() {
		super();
	}

	public InventoryReqDetailsModel(String reqQuotId, String rfqName, String rfqValidDate, String rfqBackground,
			String createdBy, String rfqDetails, Byte approvedStatus, String quotationNo, String checkedRFQId,
			String purOrderNo, String action) {
		super();
		this.reqQuotId = reqQuotId;
		this.rfqName = rfqName;
		this.rfqValidDate = rfqValidDate;
		this.rfqBackground = rfqBackground;
		this.rfqDetails = rfqDetails;
		this.createdBy = createdBy;
		this.approvedStatus = approvedStatus;
		this.quotationNo = quotationNo;
		this.checkedRFQId = checkedRFQId;
		this.purOrderNo = purOrderNo;
		this.action = action;
	}

	public String getPurchaseOrderS() {
		return purchaseOrderS;
	}

	public void setPurchaseOrderS(String purchaseOrderS) {
		this.purchaseOrderS = purchaseOrderS;
	}

	public String getPurOrderNo() {
		return purOrderNo;
	}

	public void setPurOrderNo(String purOrderNo) {
		this.purOrderNo = purOrderNo;
	}

	public String getIfApproved() {
		return ifApproved;
	}

	public void setIfApproved(String ifApproved) {
		this.ifApproved = ifApproved;
	}

	public String getCheckedRFQId() {
		return checkedRFQId;
	}

	public void setCheckedRFQId(String checkedRFQId) {
		this.checkedRFQId = checkedRFQId;
	}

	public String getQuotationNo() {
		return quotationNo;
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}

	public String getRfqDetails() {
		return rfqDetails;
	}

	public void setRfqDetails(String rfqDetails) {
		this.rfqDetails = rfqDetails;
	}

	public String getReqQuotId() {
		return reqQuotId;
	}

	public void setReqQuotId(String reqQuotId) {
		this.reqQuotId = reqQuotId;
	}

	public String getRfqName() {
		return rfqName;
	}

	public void setRfqName(String rfqName) {
		this.rfqName = rfqName;
	}

	public String getRfqValidDate() {
		return rfqValidDate;
	}

	public void setRfqValidDate(String rfqValidDate) {
		this.rfqValidDate = rfqValidDate;
	}

	public List<String> getRfqVendors() {
		return rfqVendors;
	}

	public void setRfqVendors(List<String> rfqVendors) {
		this.rfqVendors = rfqVendors;
	}

	public String getRfqVendor() {
		return rfqVendor;
	}

	public void setRfqVendor(String rfqVendor) {
		this.rfqVendor = rfqVendor;
	}

	public String getRfqBackground() {
		return rfqBackground;
	}

	public void setRfqBackground(String rfqBackground) {
		this.rfqBackground = rfqBackground;
	}

	public Byte getApprovedStatus() {
		return approvedStatus;
	}

	public void setApprovedStatus(Byte approvedStatus) {
		this.approvedStatus = approvedStatus;
	}

	public String getRfqCreatedOn() {
		return rfqCreatedOn;
	}

	public void setRfqCreatedOn(String rfqCreatedOn) {
		this.rfqCreatedOn = rfqCreatedOn;
	}

	public String getRfqUpdateOn() {
		return rfqUpdateOn;
	}

	public void setRfqUpdateOn(String rfqUpdateOn) {
		this.rfqUpdateOn = rfqUpdateOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public List<DropDownModel> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<DropDownModel> vendorList) {
		this.vendorList = vendorList;
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
