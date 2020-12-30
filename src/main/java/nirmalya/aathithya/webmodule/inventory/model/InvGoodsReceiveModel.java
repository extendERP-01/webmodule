/**
 * class showing inventory related enity
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InvGoodsReceiveModel {

	private String gRNInvoiceId;
	private String gRNPurchaseOrderId;
	private String gRNItmId;
	private String gRnInvoiceNumber;
	private Double gRnInvoiceQuantity;
	private String gRnInvoiceDescription;
	private Boolean gRnInvoiceActive;
	private String itmCategory;
	private String itmSubCategory;
	private String gRnInvoiceItmName;
	private String action;
	private String status;
	private Double gRnPrice;
	private Double total;
	private Double gTotal;
	private Double gDiscount;
	private Double subTotal;
	private Double tax;
	private String createdOn;
	private String updatedOn;
	private String createdBy;
	private String curDate;
	private String dateFrom;
	private String dateTo;
	private String porder;
	private Boolean gstType;
	private Double gstRate;
	private Double gsubTotal;
	private Double discount;
	private Double sgst;
	private Double cgst;
	private Double igst;
	private Double grandTotal;
	private Double lineTotal;
	private String vendorName;
	private String invDate;
	private String dueDate;
	private String invImg;
	private String printedBy;
	private String porderDate;
	private String itemName;
	
	
	public InvGoodsReceiveModel() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	
	

	public InvGoodsReceiveModel(String gRNInvoiceId, String gRNPurchaseOrderId, String gRNItmId,
			String gRnInvoiceNumber, Double gRnInvoiceQuantity, String gRnInvoiceDescription, Boolean gRnInvoiceActive,
			String itmCategory, String itmSubCategory, String gRnInvoiceItmName, String action, String status,
			Double gRnPrice, Double total, Double gTotal, Double gDiscount, Double subTotal, Double tax,
			String createdOn, String updatedOn,String createdBy,String porder,Boolean gstType,Double gstRate,Double gsubTotal,Double discount,Double sgst,Double cgst,Double igst,Double grandTotal,Double lineTotal,String vendorName,String invDate,String dueDate,String invImg) {
		super();
		this.gRNInvoiceId = gRNInvoiceId;
		this.gRNPurchaseOrderId = gRNPurchaseOrderId;
		this.gRNItmId = gRNItmId;
		this.gRnInvoiceNumber = gRnInvoiceNumber;
		this.gRnInvoiceQuantity = gRnInvoiceQuantity;
		this.gRnInvoiceDescription = gRnInvoiceDescription;
		this.gRnInvoiceActive = gRnInvoiceActive;
		this.itmCategory = itmCategory;
		this.itmSubCategory = itmSubCategory;
		this.gRnInvoiceItmName = gRnInvoiceItmName;
		this.action = action;
		this.status = status;
		this.gRnPrice = gRnPrice;
		this.total = total;
		this.gTotal = gTotal;
		this.gDiscount = gDiscount;
		this.subTotal = subTotal;
		this.tax = tax;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.porder = porder;
		this.gstType = gstType;
		this.gstRate = gstRate;
		this.gsubTotal = gsubTotal;
		this.discount = discount;
		this.sgst = sgst;
		this.cgst = cgst;
		this.igst = igst;
		this.grandTotal = grandTotal;
		this.lineTotal = lineTotal;
		this.vendorName  = vendorName;
		this.invDate = invDate;
		this.dueDate = dueDate;
		this.invImg = invImg;
	}


	public Double getLineTotal() {
		return lineTotal;
	}




	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
	}




	public String getPorderDate() {
		return porderDate;
	}




	public void setPorderDate(String porderDate) {
		this.porderDate = porderDate;
	}




	public String getgRNInvoiceId() {
		return gRNInvoiceId;
	}




	public String getPrintedBy() {
		return printedBy;
	}




	public void setPrintedBy(String printedBy) {
		this.printedBy = printedBy;
	}




	public void setgRNInvoiceId(String gRNInvoiceId) {
		this.gRNInvoiceId = gRNInvoiceId;
	}




	public String getgRNPurchaseOrderId() {
		return gRNPurchaseOrderId;
	}




	public void setgRNPurchaseOrderId(String gRNPurchaseOrderId) {
		this.gRNPurchaseOrderId = gRNPurchaseOrderId;
	}




	public String getgRNItmId() {
		return gRNItmId;
	}




	public void setgRNItmId(String gRNItmId) {
		this.gRNItmId = gRNItmId;
	}




	public String getgRnInvoiceNumber() {
		return gRnInvoiceNumber;
	}




	public void setgRnInvoiceNumber(String gRnInvoiceNumber) {
		this.gRnInvoiceNumber = gRnInvoiceNumber;
	}




	public Double getgRnInvoiceQuantity() {
		return gRnInvoiceQuantity;
	}




	public void setgRnInvoiceQuantity(Double gRnInvoiceQuantity) {
		this.gRnInvoiceQuantity = gRnInvoiceQuantity;
	}




	public String getgRnInvoiceDescription() {
		return gRnInvoiceDescription;
	}




	public void setgRnInvoiceDescription(String gRnInvoiceDescription) {
		this.gRnInvoiceDescription = gRnInvoiceDescription;
	}




	public Boolean getgRnInvoiceActive() {
		return gRnInvoiceActive;
	}




	public void setgRnInvoiceActive(Boolean gRnInvoiceActive) {
		this.gRnInvoiceActive = gRnInvoiceActive;
	}




	public String getItmCategory() {
		return itmCategory;
	}




	public void setItmCategory(String itmCategory) {
		this.itmCategory = itmCategory;
	}




	public String getItmSubCategory() {
		return itmSubCategory;
	}




	public void setItmSubCategory(String itmSubCategory) {
		this.itmSubCategory = itmSubCategory;
	}




	public String getgRnInvoiceItmName() {
		return gRnInvoiceItmName;
	}




	public void setgRnInvoiceItmName(String gRnInvoiceItmName) {
		this.gRnInvoiceItmName = gRnInvoiceItmName;
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




	public Double getgRnPrice() {
		return gRnPrice;
	}




	public void setgRnPrice(Double gRnPrice) {
		this.gRnPrice = gRnPrice;
	}




	public Double getTotal() {
		return total;
	}




	public void setTotal(Double total) {
		this.total = total;
	}




	public Double getgTotal() {
		return gTotal;
	}




	public void setgTotal(Double gTotal) {
		this.gTotal = gTotal;
	}




	public Double getgDiscount() {
		return gDiscount;
	}




	public void setgDiscount(Double gDiscount) {
		this.gDiscount = gDiscount;
	}




	public Double getSubTotal() {
		return subTotal;
	}




	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}




	public Double getTax() {
		return tax;
	}




	public void setTax(Double tax) {
		this.tax = tax;
	}




	public String getCreatedOn() {
		return createdOn;
	}




	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}




	public String getUpdatedOn() {
		return updatedOn;
	}




	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}




	public String getCreatedBy() {
		return createdBy;
	}




	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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




	public Boolean getGstType() {
		return gstType;
	}




	public void setGstType(Boolean gstType) {
		this.gstType = gstType;
	}




	public Double getGstRate() {
		return gstRate;
	}




	public void setGstRate(Double gstRate) {
		this.gstRate = gstRate;
	}




	public Double getGsubTotal() {
		return gsubTotal;
	}




	public void setGsubTotal(Double gsubTotal) {
		this.gsubTotal = gsubTotal;
	}




	public Double getDiscount() {
		return discount;
	}




	public void setDiscount(Double discount) {
		this.discount = discount;
	}




	public Double getSgst() {
		return sgst;
	}




	public void setSgst(Double sgst) {
		this.sgst = sgst;
	}




	public Double getCgst() {
		return cgst;
	}




	public void setCgst(Double cgst) {
		this.cgst = cgst;
	}




	public Double getIgst() {
		return igst;
	}




	public void setIgst(Double igst) {
		this.igst = igst;
	}




	public Double getGrandTotal() {
		return grandTotal;
	}




	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}




	public String getVendorName() {
		return vendorName;
	}




	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}




	public String getInvDate() {
		return invDate;
	}




	public void setInvDate(String invDate) {
		this.invDate = invDate;
	}




	public String getDueDate() {
		return dueDate;
	}




	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}




	public String getInvImg() {
		return invImg;
	}




	public void setInvImg(String invImg) {
		this.invImg = invImg;
	}




	public String getItemName() {
		return itemName;
	}




	public void setItemName(String itemName) {
		this.itemName = itemName;
	}




	@Override
	public String toString()
	{
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonStr;
		try 
		{
			jsonStr = mapperObj.writeValueAsString(this);
		} catch (IOException ex)
		{

			jsonStr = ex.toString();
		}
		return jsonStr;

	}
}
