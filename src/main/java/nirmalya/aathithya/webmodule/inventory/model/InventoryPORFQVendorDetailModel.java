package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryPORFQVendorDetailModel {
private String venQuotId;
	
	private String rfqNo;
	
	private String vendor;
	
	private String vendorName;

	private String qNote;
	
	private String itemId;
	
	private String itemName;
	
	private Boolean taxType;
	
	private Double gstRate;
	
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
    
	private String quotName;
	
	private String quotValidity;
	
	private String rfqName;
	
	private String logoName;
	
	private String action;
	
	private String subCat;
	
	private String category;

	public InventoryPORFQVendorDetailModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	  
	public InventoryPORFQVendorDetailModel(String venQuotId, String rfqNo, String vendor,String qNote, String itemId,
			Boolean taxType, Double gstRate,Double subTotal,Double unitPrice,Double qIGST,Double qCGST, Double qSGST,
			Double quantity,Double lineTotal,Double grandTotal,String itemName, String vendorName,Byte approveStatus,String rfqName,
			String quotName,String quotValidity,String logoName,String subCat,String category,  String action) {
		super();
		this.venQuotId = venQuotId;
		this.rfqNo = rfqNo;
		this.vendor = vendor;
		this.qNote = qNote;
		this.itemId = itemId;
		this.taxType = taxType;
		this.gstRate = gstRate;
		this.subTotal = subTotal;
		this.unitPrice = unitPrice;
		this.qIGST = qIGST;
		this.qCGST = qCGST;
		this.qSGST = qSGST;
		this.quantity = quantity;
		this.lineTotal = lineTotal;
		this.quantity = quantity;
		this.grandTotal = grandTotal;
		this.itemName = itemName;
		this.vendorName = vendorName;
		this.approveStatus = approveStatus;
		this.rfqName=rfqName;
		this.quotName=quotName;
		this.quotValidity=quotValidity;
		this.logoName=logoName;
		this.subCat=subCat;
		this.category=category;
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

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getQuotName() {
		return quotName;
	}

	public void setQuotName(String quotName) {
		this.quotName = quotName;
	}

	public String getQuotValidity() {
		return quotValidity;
	}

	public void setQuotValidity(String quotValidity) {
		this.quotValidity = quotValidity;
	}

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public String getRfqName() {
		return rfqName;
	}

	public void setRfqName(String rfqName) {
		this.rfqName = rfqName;
	}

	public Byte getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Byte approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getVenQuotId() {
		return venQuotId;
	}

	public void setVenQuotId(String venQuotId) {
		this.venQuotId = venQuotId;
	}

	public String getRfqNo() {
		return rfqNo;
	}

	public void setRfqNo(String rfqNo) {
		this.rfqNo = rfqNo;
	}
	
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}


	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
