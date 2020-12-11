package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */

public class InventoryGoodsReturnsNoteModel {
	private String goodsReturnNote;
	private String gRNInvoiceId;
	private String purchaseOrder;
	private String gRtNDescription;
	private Boolean gRtNActive;
	private String itemCategory;
	private String itemSubCategory;
	private String itemId;
	private Double gRtNQty;
	private String itemName;
	private String action;
	private String activity;
	private Double price;
	private String invNo;
	private String gRtN_CreatedOn;
	private String serveUnit;
	private Double quantityReceived;
	private Double total;
	private String vendorName;
	private String vendorAddr;
	private String gRDtlsCreatedBy;
	private String curDate;
	private String dateFrom;
	private String dateTo;
	private String porder;
	private String invNumber;
	private Double gsubTotal;
	private Double lineTotal;
	private Double discount;
	private Double rcvSubTotal;
	private Boolean gstType;
	private Double gstRate;
	private Double sgst;
	private Double cgst;
	private Double igst;
	private Double grandTotal;
	private Double cessAmount;
	private Double totalCess;

	public Double getGrandTotal() {
		return grandTotal;
	}
	public Double getCessAmount() {
		return cessAmount;
	}
	public Double getTotalCess() {
		return totalCess;
	}
	public void setTotalCess(Double totalCess) {
		this.totalCess = totalCess;
	}
	public void setCessAmount(Double cessAmount) {
		this.cessAmount = cessAmount;
	}
	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}
	public String getGoodsReturnNote() {
		return goodsReturnNote;
	}
	public void setGoodsReturnNote(String goodsReturnNote) {
		this.goodsReturnNote = goodsReturnNote;
	}
	
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public String getgRtNDescription() {
		return gRtNDescription;
	}
	public void setgRtNDescription(String gRtNDescription) {
		this.gRtNDescription = gRtNDescription;
	}
	public Boolean getgRtNActive() {
		return gRtNActive;
	}
	public void setgRtNActive(Boolean gRtNActive) {
		this.gRtNActive = gRtNActive;
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Double getgRtNQty() {
		return gRtNQty;
	}
	public void setgRtNQty(Double gRtNQty) {
		this.gRtNQty = gRtNQty;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getgRNInvoiceId() {
		return gRNInvoiceId;
	}
	public void setgRNInvoiceId(String gRNInvoiceId) {
		this.gRNInvoiceId = gRNInvoiceId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getInvNo() {
		return invNo;
	}
	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}
	public String getgRtN_CreatedOn() {
		return gRtN_CreatedOn;
	}
	public void setgRtN_CreatedOn(String gRtN_CreatedOn) {
		this.gRtN_CreatedOn = gRtN_CreatedOn;
	}
	public String getServeUnit() {
		return serveUnit;
	}
	public void setServeUnit(String serveUnit) {
		this.serveUnit = serveUnit;
	}
	
	public Double getQuantityReceived() {
		return quantityReceived;
	}
	public void setQuantityReceived(Double quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorAddr() {
		return vendorAddr;
	}
	public void setVendorAddr(String vendorAddr) {
		this.vendorAddr = vendorAddr;
	}
	public String getgRDtlsCreatedBy() {
		return gRDtlsCreatedBy;
	}
	public void setgRDtlsCreatedBy(String gRDtlsCreatedBy) {
		this.gRDtlsCreatedBy = gRDtlsCreatedBy;
	}
	public String getCurDate() {
		return curDate;
	}
	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getPorder() {
		return porder;
	}
	public void setPorder(String porder) {
		this.porder = porder;
	}
	public String getInvNumber() {
		return invNumber;
	}
	public void setInvNumber(String invNumber) {
		this.invNumber = invNumber;
	}
	public Double getGsubTotal() {
		return gsubTotal;
	}
	public void setGsubTotal(Double gsubTotal) {
		this.gsubTotal = gsubTotal;
	}
	public Double getLineTotal() {
		return lineTotal;
	}
	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getRcvSubTotal() {
		return rcvSubTotal;
	}
	public void setRcvSubTotal(Double rcvSubTotal) {
		this.rcvSubTotal = rcvSubTotal;
	}
	public Boolean getGstType() {
		return gstType;
	}
	public Double getGstRate() {
		return gstRate;
	}
	public void setGstType(Boolean gstType) {
		this.gstType = gstType;
	}
	public Double getSgst() {
		return sgst;
	}
	public Double getCgst() {
		return cgst;
	}
	public Double getIgst() {
		return igst;
	}
	public void setSgst(Double sgst) {
		this.sgst = sgst;
	}
	public void setCgst(Double cgst) {
		this.cgst = cgst;
	}
	public void setIgst(Double igst) {
		this.igst = igst;
	}
	public void setGstRate(Double gstRate) {
		this.gstRate = gstRate;
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
