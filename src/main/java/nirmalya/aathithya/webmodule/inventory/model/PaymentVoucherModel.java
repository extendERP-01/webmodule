package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentVoucherModel {
	private String vendorId;
	private String vendorName;
	private String fromDate;
	private String toDate;
	private String poNumber;
	private String grnNo;
	private String invNo;
	private Double subTotal;
	private Double discount;
	private Double total;
	private String paidTo;
	private String paidFrom;
	private String paymentMode;
	private String action;
	private Double taxableAmount;
	private Double sgst;
	private Double cgst;
	private Double igst;
	private Boolean taxType;
	private Double tdsRate;
	private Double tdsAmount;
	private String goodsReturnNote;
	private Double returnQuanity;
	private Double returnPrice;
	private String resturnDesc;
	private String gstRate;
	private Double returnCgst;
	private Double returnSgst;
	private Double returnIgst;
	private Double returnDiscount;
	private Double returnTotal;
	private String bankName;
	private String branchName;
	private String accNo;
	private String chequeNo;
	private String transNo;
	private String transactionDate;
	private String createdBy;
	private String refno;
	private String paymentVoucher;
	private String verndorAddrs;
	private String venderGstNo;
	private String vendorMobile;
	private String vendorEmail;
	private String hotelName;
	private String hotelAddr;
	private String hotelEmail;
	private String hotelMob;
	private String gstNo;
	private Double grnSubtotal;
	private Double grnTotal;
	private Double taxRate;
	private String scheduleDate;
	private String dueDate;
	private Boolean aproveStaus;
	private String action1;
	private Boolean activeStatus;
	private Double avilBalance;
	private Double outstandingAmount;
	private Boolean paymentType;
	private Double partialAmt;
	private Boolean paymentStatus;
	private Double cessAmt;

	public PaymentVoucherModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getPaidTo() {
		return paidTo;
	}

	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}

	public String getPaidFrom() {
		return paidFrom;
	}

	public void setPaidFrom(String paidFrom) {
		this.paidFrom = paidFrom;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public Double getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public Boolean getTaxType() {
		return taxType;
	}

	public void setTaxType(Boolean taxType) {
		this.taxType = taxType;
	}

	public Double getTdsRate() {
		return tdsRate;
	}

	public void setTdsRate(Double tdsRate) {
		this.tdsRate = tdsRate;
	}

	public Double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public String getGoodsReturnNote() {
		return goodsReturnNote;
	}

	public void setGoodsReturnNote(String goodsReturnNote) {
		this.goodsReturnNote = goodsReturnNote;
	}

	public Double getReturnQuanity() {
		return returnQuanity;
	}

	public void setReturnQuanity(Double returnQuanity) {
		this.returnQuanity = returnQuanity;
	}

	public Double getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(Double returnPrice) {
		this.returnPrice = returnPrice;
	}

	public String getResturnDesc() {
		return resturnDesc;
	}

	public void setResturnDesc(String resturnDesc) {
		this.resturnDesc = resturnDesc;
	}

	public String getGstRate() {
		return gstRate;
	}

	public void setGstRate(String gstRate) {
		this.gstRate = gstRate;
	}

	public Double getReturnCgst() {
		return returnCgst;
	}

	public void setReturnCgst(Double returnCgst) {
		this.returnCgst = returnCgst;
	}

	public Double getReturnSgst() {
		return returnSgst;
	}

	public void setReturnSgst(Double returnSgst) {
		this.returnSgst = returnSgst;
	}

	public Double getReturnIgst() {
		return returnIgst;
	}

	public void setReturnIgst(Double returnIgst) {
		this.returnIgst = returnIgst;
	}

	public Double getReturnDiscount() {
		return returnDiscount;
	}

	public void setReturnDiscount(Double returnDiscount) {
		this.returnDiscount = returnDiscount;
	}

	public Double getReturnTotal() {
		return returnTotal;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public void setReturnTotal(Double returnTotal) {
		this.returnTotal = returnTotal;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getPaymentVoucher() {
		return paymentVoucher;
	}

	public void setPaymentVoucher(String paymentVoucher) {
		this.paymentVoucher = paymentVoucher;
	}

	public String getVerndorAddrs() {
		return verndorAddrs;
	}

	public void setVerndorAddrs(String verndorAddrs) {
		this.verndorAddrs = verndorAddrs;
	}

	public String getVenderGstNo() {
		return venderGstNo;
	}

	public void setVenderGstNo(String venderGstNo) {
		this.venderGstNo = venderGstNo;
	}

	public String getVendorMobile() {
		return vendorMobile;
	}

	public void setVendorMobile(String vendorMobile) {
		this.vendorMobile = vendorMobile;
	}

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelAddr() {
		return hotelAddr;
	}

	public void setHotelAddr(String hotelAddr) {
		this.hotelAddr = hotelAddr;
	}

	public String getHotelEmail() {
		return hotelEmail;
	}

	public void setHotelEmail(String hotelEmail) {
		this.hotelEmail = hotelEmail;
	}

	public String getHotelMob() {
		return hotelMob;
	}

	public void setHotelMob(String hotelMob) {
		this.hotelMob = hotelMob;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public Double getGrnSubtotal() {
		return grnSubtotal;
	}

	public void setGrnSubtotal(Double grnSubtotal) {
		this.grnSubtotal = grnSubtotal;
	}

	public Double getGrnTotal() {
		return grnTotal;
	}

	public void setGrnTotal(Double grnTotal) {
		this.grnTotal = grnTotal;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Boolean getAproveStaus() {
		return aproveStaus;
	}

	public void setAproveStaus(Boolean aproveStaus) {
		this.aproveStaus = aproveStaus;
	}

	public String getAction1() {
		return action1;
	}

	public void setAction1(String action1) {
		this.action1 = action1;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Double getAvilBalance() {
		return avilBalance;
	}

	public void setAvilBalance(Double avilBalance) {
		this.avilBalance = avilBalance;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public Boolean getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Boolean paymentType) {
		this.paymentType = paymentType;
	}

	public Double getPartialAmt() {
		return partialAmt;
	}

	public void setPartialAmt(Double partialAmt) {
		this.partialAmt = partialAmt;
	}

	public Boolean getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Double getCessAmt() {
		return cessAmt;
	}

	public void setCessAmt(Double cessAmt) {
		this.cessAmt = cessAmt;
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
