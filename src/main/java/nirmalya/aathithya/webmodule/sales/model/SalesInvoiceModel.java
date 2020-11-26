package nirmalya.aathithya.webmodule.sales.model;

/**
 * @author Nirmalya Labs
 *
 */
public class SalesInvoiceModel {

	private String salesInvoice;

	private String salesOrderId;

	private String salesOrder;

	private String purchaseOrder;

	private String quotationId;

	private String saleInvNote;

	private Boolean gstType;

	private Double gstRate;

	private Double subTotal;

	private Double sIGST;

	private Double sCGST;

	private Double sSGST;

	private Double grandTotal;

	private String saleItem;

	private String saleItemCode;

	private Double salePrice;

	private Double saleQuantity;

	private String sServeType;

	private Double saleAmount;

	private String createdBy;

	private String payMode;

	private String payRefNo;

	private String transactionId;

	private String transactionDate;

	private Boolean payStatus;

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

	private String invoiceDate;

	private String voucherId;

	private Boolean saleInvoiceType;

	private Double saleDiscount;

	private Double payableAmt;

	private Double advanceAmt;

	private String checkNumber;

	private String contraReceipt;

	private String posNumber;

	private Boolean paymentType;

	private Double outstndngAmt;

	private String action;

	private String curDate;

	private String printedBy;

	private String dateFrom;

	private String dateTo;

	private Double itemWt;
	private String payMode1;
	private String payMode2;
	private Double cardAmount;
	private Double cashAmount;
	private String storeId;
	
	private Double itemCess;
	private Double totalCess;

	public Double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public Double getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(Double cardAmount) {
		this.cardAmount = cardAmount;
	}

	public String getPayMode1() {
		return payMode1;
	}

	public void setPayMode1(String payMode1) {
		this.payMode1 = payMode1;
	}

	public String getPayMode2() {
		return payMode2;
	}

	public void setPayMode2(String payMode2) {
		this.payMode2 = payMode2;
	}

	public SalesInvoiceModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSalesInvoice() {
		return salesInvoice;
	}

	public void setSalesInvoice(String salesInvoice) {
		this.salesInvoice = salesInvoice;
	}

	public String getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(String salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}

	public String getSaleInvNote() {
		return saleInvNote;
	}

	public void setSaleInvNote(String saleInvNote) {
		this.saleInvNote = saleInvNote;
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

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getsIGST() {
		return sIGST;
	}

	public void setsIGST(Double sIGST) {
		this.sIGST = sIGST;
	}

	public Double getsCGST() {
		return sCGST;
	}

	public void setsCGST(Double sCGST) {
		this.sCGST = sCGST;
	}

	public Double getsSGST() {
		return sSGST;
	}

	public void setsSGST(Double sSGST) {
		this.sSGST = sSGST;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getSaleItem() {
		return saleItem;
	}

	public void setSaleItem(String saleItem) {
		this.saleItem = saleItem;
	}

	public String getSaleItemCode() {
		return saleItemCode;
	}

	public void setSaleItemCode(String saleItemCode) {
		this.saleItemCode = saleItemCode;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getSaleQuantity() {
		return saleQuantity;
	}

	public void setSaleQuantity(Double saleQuantity) {
		this.saleQuantity = saleQuantity;
	}

	public String getsServeType() {
		return sServeType;
	}

	public void setsServeType(String sServeType) {
		this.sServeType = sServeType;
	}

	public Double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(Double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayRefNo() {
		return payRefNo;
	}

	public void setPayRefNo(String payRefNo) {
		this.payRefNo = payRefNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Boolean getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Boolean payStatus) {
		this.payStatus = payStatus;
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

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public Boolean getSaleInvoiceType() {
		return saleInvoiceType;
	}

	public void setSaleInvoiceType(Boolean saleInvoiceType) {
		this.saleInvoiceType = saleInvoiceType;
	}

	public Double getSaleDiscount() {
		return saleDiscount;
	}

	public void setSaleDiscount(Double saleDiscount) {
		this.saleDiscount = saleDiscount;
	}

	public Double getPayableAmt() {
		return payableAmt;
	}

	public void setPayableAmt(Double payableAmt) {
		this.payableAmt = payableAmt;
	}

	public Double getAdvanceAmt() {
		return advanceAmt;
	}

	public void setAdvanceAmt(Double advanceAmt) {
		this.advanceAmt = advanceAmt;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getContraReceipt() {
		return contraReceipt;
	}

	public void setContraReceipt(String contraReceipt) {
		this.contraReceipt = contraReceipt;
	}

	public String getPosNumber() {
		return posNumber;
	}

	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}

	public Boolean getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Boolean paymentType) {
		this.paymentType = paymentType;
	}

	public Double getOutstndngAmt() {
		return outstndngAmt;
	}

	public void setOutstndngAmt(Double outstndngAmt) {
		this.outstndngAmt = outstndngAmt;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
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

	public Double getItemWt() {
		return itemWt;
	}

	public void setItemWt(Double itemWt) {
		this.itemWt = itemWt;
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

}
