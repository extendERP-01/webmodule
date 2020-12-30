package nirmalya.aathithya.webmodule.sales.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SalesDeliveryChallanModel {

	private String delChallanId;

	private String saleOrderNo;

	private String customerId;

	private String customerName;

	private String toPlace;

	private String fromPlace;

	private String delChallanDate;

	private String transName;

	private String transMoNo;

	private String transVehNo;

	private String qNote;

	private String itemId;

	private String itemName;

	private Boolean taxType;

	private Double gstRate;

	private Double advanceAmnt;

	private Double remainAmnt;

	private Double subTotal;

	private Double unitPrice;

	private Double qIGST;

	private Double qCGST;

	private Double qSGST;

	private Double quantity;

	private Double lineTotal;

	private Double grandTotal;

	private String createdBy;

	private Byte approveStatus;

	private String showStatus;

	private String logoName;

	private String action;

	private String subCat;

	private String category;

	private String imageName;

	private String approveShowStatus;

	private String invoiceNo;

	private String purchaseOrderNo;

	private Double totalDelItem;

	private String itemCat;

	private String itemSubCat;

	private String itemCatName;

	private String itemSubCatName;

	private Double itemWt;

	private Double bundles;

	private Double itemDiscount;

	private Double totalDiscount;

	private Double totalSgst;

	private Double totalIgst;

	private Double totalCgst;
	
	private String storeId;
	
	private Double itemCess;
	private Double totalCess;

	public SalesDeliveryChallanModel() {
		super();
	}

	public SalesDeliveryChallanModel(String delChallanId, String saleOrderNo, String customerId, String customerName,
			String toPlace, String fromPlace, String delChallanDate, String transName, String transMoNo,
			String transVehNo, String itemId, String itemName, Double unitPrice, Double quantity, Double lineTotal,
			Double subTotal, Double qIGST, Double qCGST, Double qSGST, Double grandTotal, Byte approveStatus,
			String qNote, Boolean taxType, Double gstRate, String subCat, String category, String imageName,
			Double advanceAmnt, Double remainAmnt, String invoiceNo, String purchaseOrderNo, Double totalDelItem,
			String action) {
		super();

		this.delChallanId = delChallanId;
		this.saleOrderNo = saleOrderNo;
		this.customerId = customerId;
		this.customerName = customerName;
		this.toPlace = toPlace;
		this.fromPlace = fromPlace;
		this.delChallanDate = delChallanDate;
		this.transName = transName;
		this.transMoNo = transMoNo;
		this.transVehNo = transVehNo;
		this.itemId = itemId;
		this.itemName = itemName;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.lineTotal = lineTotal;
		this.subTotal = subTotal;
		this.qIGST = qIGST;
		this.qCGST = qCGST;
		this.qSGST = qSGST;
		this.grandTotal = grandTotal;
		this.approveStatus = approveStatus;
		this.qNote = qNote;
		this.taxType = taxType;
		this.gstRate = gstRate;
		this.subCat = subCat;
		this.category = category;
		this.imageName = imageName;
		this.advanceAmnt = advanceAmnt;
		this.remainAmnt = remainAmnt;
		this.invoiceNo = invoiceNo;
		this.purchaseOrderNo = purchaseOrderNo;
		this.totalDelItem = totalDelItem;
		this.action = action;
	}

	public Double getTotalDelItem() {
		return totalDelItem;
	}

	public void setTotalDelItem(Double totalDelItem) {
		this.totalDelItem = totalDelItem;
	}

	public String getPurchaseOrderNo() {
		return purchaseOrderNo;
	}

	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getRemainAmnt() {
		return remainAmnt;
	}

	public void setRemainAmnt(Double remainAmnt) {
		this.remainAmnt = remainAmnt;
	}

	public Double getAdvanceAmnt() {
		return advanceAmnt;
	}

	public void setAdvanceAmnt(Double advanceAmnt) {
		this.advanceAmnt = advanceAmnt;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getApproveShowStatus() {
		return approveShowStatus;
	}

	public void setApproveShowStatus(String approveShowStatus) {
		this.approveShowStatus = approveShowStatus;
	}

	public String getDelChallanId() {
		return delChallanId;
	}

	public void setDelChallanId(String delChallanId) {
		this.delChallanId = delChallanId;
	}

	public String getSaleOrderNo() {
		return saleOrderNo;
	}

	public void setSaleOrderNo(String saleOrderNo) {
		this.saleOrderNo = saleOrderNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getToPlace() {
		return toPlace;
	}

	public void setToPlace(String toPlace) {
		this.toPlace = toPlace;
	}

	public String getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public String getDelChallanDate() {
		return delChallanDate;
	}

	public void setDelChallanDate(String delChallanDate) {
		this.delChallanDate = delChallanDate;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getTransMoNo() {
		return transMoNo;
	}

	public void setTransMoNo(String transMoNo) {
		this.transMoNo = transMoNo;
	}

	public String getTransVehNo() {
		return transVehNo;
	}

	public void setTransVehNo(String transVehNo) {
		this.transVehNo = transVehNo;
	}

	public String getqNote() {
		return qNote;
	}

	public void setqNote(String qNote) {
		this.qNote = qNote;
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

	public Boolean getTaxType() {
		return taxType;
	}

	public void setTaxType(Boolean taxType) {
		this.taxType = taxType;
	}

	public Double getGstRate() {
		return gstRate;
	}

	public void setGstRate(Double gstRate) {
		this.gstRate = gstRate;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getqIGST() {
		return qIGST;
	}

	public void setqIGST(Double qIGST) {
		this.qIGST = qIGST;
	}

	public Double getqCGST() {
		return qCGST;
	}

	public void setqCGST(Double qCGST) {
		this.qCGST = qCGST;
	}

	public Double getqSGST() {
		return qSGST;
	}

	public void setqSGST(Double qSGST) {
		this.qSGST = qSGST;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Byte getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Byte approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSubCat() {
		return subCat;
	}

	public void setSubCat(String subCat) {
		this.subCat = subCat;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getItemCat() {
		return itemCat;
	}

	public String getItemSubCat() {
		return itemSubCat;
	}

	public void setItemCat(String itemCat) {
		this.itemCat = itemCat;
	}

	public void setItemSubCat(String itemSubCat) {
		this.itemSubCat = itemSubCat;
	}

	public String getItemCatName() {
		return itemCatName;
	}

	public String getItemSubCatName() {
		return itemSubCatName;
	}

	public void setItemCatName(String itemCatName) {
		this.itemCatName = itemCatName;
	}

	public void setItemSubCatName(String itemSubCatName) {
		this.itemSubCatName = itemSubCatName;
	}

	public Double getItemWt() {
		return itemWt;
	}

	public void setItemWt(Double itemWt) {
		this.itemWt = itemWt;
	}

	public Double getBundles() {
		return bundles;
	}

	public void setBundles(Double bundles) {
		this.bundles = bundles;
	}

	public Double getItemDiscount() {
		return itemDiscount;
	}

	public void setItemDiscount(Double itemDiscount) {
		this.itemDiscount = itemDiscount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalSgst() {
		return totalSgst;
	}

	public void setTotalSgst(Double totalSgst) {
		this.totalSgst = totalSgst;
	}

	public Double getTotalIgst() {
		return totalIgst;
	}

	public void setTotalIgst(Double totalIgst) {
		this.totalIgst = totalIgst;
	}

	public Double getTotalCgst() {
		return totalCgst;
	}

	public void setTotalCgst(Double totalCgst) {
		this.totalCgst = totalCgst;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Double getItemCess() {
		return itemCess;
	}

	public void setItemCess(Double itemCess) {
		this.itemCess = itemCess;
	}

	public Double getTotalCess() {
		return totalCess;
	}

	public void setTotalCess(Double totalCess) {
		this.totalCess = totalCess;
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
