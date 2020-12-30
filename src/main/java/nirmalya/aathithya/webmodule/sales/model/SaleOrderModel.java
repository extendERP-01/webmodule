package nirmalya.aathithya.webmodule.sales.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SaleOrderModel {
    private String customerId;
	
	private String customerName;
	
	private String qDescription;
	
	private String quotationVDate;
	
	private String qNote;
	
	private Boolean qStatus;
	
	private String itemName;
	
	private String itemCode;
	
	private Double unitPrice;
	
	private Double quantity;
	
	private String qServeType;
	
	private Double lineTotal;
	
	private Boolean taxType;
	
	private Double gstRate;
	
	private Double subTotal;
	
	private Double qIGST;
	
	private Double qCGST;
	
	private Double qSGST;
	
	private Double grandTotal;
	
	private Boolean approveActive;
	
	private String createdBy;
	
	private String action;
	
	private String status;
	
	private String approveStatus;
	
	private String curDate;
	
	private String purchaseOrder;
	
	private String pOrderDate;
	
	private String pOrderImage;
	
	private String salesOrder;
	
	private String sOrderDate;
	
	private String hotelName;
	
	private String hotelAddress;
	
	private String hotelCity;
	
	private String hotelPhn;
	
	private String hotelGSTIn;
	
	private String custName;
	
	private String custId;
	
	private String custAddress;
	
	private String custCountry;
	
	private String custGmail;
	
	private String custPhn;
	
	private String custGSTIn;
	
	private Boolean invoiceType;
	
	private Boolean salesOrderActive;
	
	private Double discount;
	
	private Double advAmount;
	
	private String payMode;
	
	private String transId;
	
	private String checkId;
	
	private String payRefNo;
	
	private String voucherId;
	
	private String posNumber;
	
	private Double itemGST;
	
	private Double itemCGST;
	
	private Double itemSGST;
	
	private Double itemIGST;
	
	private String printedBy;
	
	private String dateFrom;
	
	private String dateTo;

	private String storeId;
	
	private String quationId;
	
	private String orderReceiveDate;
	
	private String orderReceiveTime;
	
	private Double itemCess;
	
	private Double totalCess;
	
	public SaleOrderModel() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getqDescription() {
		return qDescription;
	}

	public void setqDescription(String qDescription) {
		this.qDescription = qDescription;
	}

	public String getQuotationVDate() {
		return quotationVDate;
	}

	public void setQuotationVDate(String quotationVDate) {
		this.quotationVDate = quotationVDate;
	}

	public String getqNote() {
		return qNote;
	}

	public void setqNote(String qNote) {
		this.qNote = qNote;
	}

	public Boolean getqStatus() {
		return qStatus;
	}

	public void setqStatus(Boolean qStatus) {
		this.qStatus = qStatus;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getqServeType() {
		return qServeType;
	}

	public void setqServeType(String qServeType) {
		this.qServeType = qServeType;
	}

	public Double getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
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

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Boolean getApproveActive() {
		return approveActive;
	}

	public void setApproveActive(Boolean approveActive) {
		this.approveActive = approveActive;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getpOrderDate() {
		return pOrderDate;
	}

	public void setpOrderDate(String pOrderDate) {
		this.pOrderDate = pOrderDate;
	}

	public String getpOrderImage() {
		return pOrderImage;
	}

	public void setpOrderImage(String pOrderImage) {
		this.pOrderImage = pOrderImage;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getsOrderDate() {
		return sOrderDate;
	}

	public void setsOrderDate(String sOrderDate) {
		this.sOrderDate = sOrderDate;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelAddress() {
		return hotelAddress;
	}

	public void setHotelAddress(String hotelAddress) {
		this.hotelAddress = hotelAddress;
	}

	public String getHotelCity() {
		return hotelCity;
	}

	public void setHotelCity(String hotelCity) {
		this.hotelCity = hotelCity;
	}

	public String getHotelPhn() {
		return hotelPhn;
	}

	public void setHotelPhn(String hotelPhn) {
		this.hotelPhn = hotelPhn;
	}

	public String getHotelGSTIn() {
		return hotelGSTIn;
	}

	public void setHotelGSTIn(String hotelGSTIn) {
		this.hotelGSTIn = hotelGSTIn;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	public String getCustCountry() {
		return custCountry;
	}

	public void setCustCountry(String custCountry) {
		this.custCountry = custCountry;
	}

	public String getCustGmail() {
		return custGmail;
	}

	public void setCustGmail(String custGmail) {
		this.custGmail = custGmail;
	}

	public String getCustPhn() {
		return custPhn;
	}

	public void setCustPhn(String custPhn) {
		this.custPhn = custPhn;
	}

	public String getCustGSTIn() {
		return custGSTIn;
	}

	public void setCustGSTIn(String custGSTIn) {
		this.custGSTIn = custGSTIn;
	}

	public Boolean getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Boolean invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Boolean getSalesOrderActive() {
		return salesOrderActive;
	}

	public void setSalesOrderActive(Boolean salesOrderActive) {
		this.salesOrderActive = salesOrderActive;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getAdvAmount() {
		return advAmount;
	}

	public void setAdvAmount(Double advAmount) {
		this.advAmount = advAmount;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public String getPayRefNo() {
		return payRefNo;
	}

	public void setPayRefNo(String payRefNo) {
		this.payRefNo = payRefNo;
	}

	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public String getPosNumber() {
		return posNumber;
	}

	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}

	public Double getItemGST() {
		return itemGST;
	}

	public void setItemGST(Double itemGST) {
		this.itemGST = itemGST;
	}

	public Double getItemCGST() {
		return itemCGST;
	}

	public void setItemCGST(Double itemCGST) {
		this.itemCGST = itemCGST;
	}

	public Double getItemSGST() {
		return itemSGST;
	}

	public void setItemSGST(Double itemSGST) {
		this.itemSGST = itemSGST;
	}

	public Double getItemIGST() {
		return itemIGST;
	}

	public void setItemIGST(Double itemIGST) {
		this.itemIGST = itemIGST;
	}

	public String getPrintedBy() {
		return printedBy;
	}

	public void setPrintedBy(String printedBy) {
		this.printedBy = printedBy;
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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getQuationId() {
		return quationId;
	}

	public void setQuationId(String quationId) {
		this.quationId = quationId;
	}

	public String getOrderReceiveDate() {
		return orderReceiveDate;
	}

	public void setOrderReceiveDate(String orderReceiveDate) {
		this.orderReceiveDate = orderReceiveDate;
	}

	public String getOrderReceiveTime() {
		return orderReceiveTime;
	}

	public void setOrderReceiveTime(String orderReceiveTime) {
		this.orderReceiveTime = orderReceiveTime;
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
