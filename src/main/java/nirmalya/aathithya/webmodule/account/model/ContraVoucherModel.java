package nirmalya.aathithya.webmodule.account.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class ContraVoucherModel {

	private String contraVoucherId;
	
	private String contraVoucherType;
	
	private String costCenter;
	
	private String fromBank;
	
	private String fromBranch;
	
	private String fromAccount;
	
	private String fromCash;
	
	private String toBank;
	
	private String toBranch;
	
	private String toAccount;
	
	private String toCash;
	
	private String cvDescription;
	
	private String cvDate;
	
	private Double cvAmount;
	
	private String createdBy;
	
	private String action;
	
	private String curDate;
	
	private String printedBy;
	
	private String dateFrom;
	
	private String dateTo;
	private String amount;

	public ContraVoucherModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getContraVoucherId() {
		return contraVoucherId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setContraVoucherId(String contraVoucherId) {
		this.contraVoucherId = contraVoucherId;
	}

	public String getContraVoucherType() {
		return contraVoucherType;
	}

	public void setContraVoucherType(String contraVoucherType) {
		this.contraVoucherType = contraVoucherType;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getFromBank() {
		return fromBank;
	}

	public void setFromBank(String fromBank) {
		this.fromBank = fromBank;
	}

	public String getFromBranch() {
		return fromBranch;
	}

	public void setFromBranch(String fromBranch) {
		this.fromBranch = fromBranch;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getFromCash() {
		return fromCash;
	}

	public void setFromCash(String fromCash) {
		this.fromCash = fromCash;
	}

	public String getToBank() {
		return toBank;
	}

	public void setToBank(String toBank) {
		this.toBank = toBank;
	}

	public String getToBranch() {
		return toBranch;
	}

	public void setToBranch(String toBranch) {
		this.toBranch = toBranch;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getToCash() {
		return toCash;
	}

	public void setToCash(String toCash) {
		this.toCash = toCash;
	}

	public String getCvDescription() {
		return cvDescription;
	}

	public void setCvDescription(String cvDescription) {
		this.cvDescription = cvDescription;
	}

	public String getCvDate() {
		return cvDate;
	}

	public void setCvDate(String cvDate) {
		this.cvDate = cvDate;
	}

	public Double getCvAmount() {
		return cvAmount;
	}

	public void setCvAmount(Double cvAmount) {
		this.cvAmount = cvAmount;
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
