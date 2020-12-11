package nirmalya.aathithya.webmodule.account.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class AccountTrialBalanceModel {

	private String costCenter;
	private String accountHeadId;
	private Double debitBal;
	private Double creditVal;
	private Double remainingBal;
	private Double totalDebit;
	private Double totalCredit;
	private String desc;
	private String action;
	private Integer slNo;
	private DropDownModel parent;
	private String parentSeq;
	private List<String> parentSeqList = new ArrayList<String>();;
	
	private String openBal;
	
	public String getOpenBal() {
		return openBal;
	}

	public void setOpenBal(String openBal) {
		this.openBal = openBal;
	}

	public List<String> getParentSeqList() {
		return parentSeqList;
	}

	public void setParentSeqList(List<String> parentSeqList) {
		this.parentSeqList = parentSeqList;
	}
	public DropDownModel getParent() {
		return parent;
	}

	public void setParent(DropDownModel parent) {
		this.parent = parent;
	}

	public String getParentSeq() {
		return parentSeq;
	}

	public void setParentSeq(String parentSeq) {
		this.parentSeq = parentSeq;
	}
	public AccountTrialBalanceModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAccountHeadId() {
		return accountHeadId;
	}

	public void setAccountHeadId(String accountHeadId) {
		this.accountHeadId = accountHeadId;
	}

	public Double getDebitBal() {
		return debitBal;
	}

	public void setDebitBal(Double debitBal) {
		this.debitBal = debitBal;
	}

	public Double getCreditVal() {
		return creditVal;
	}

	public void setCreditVal(Double creditVal) {
		this.creditVal = creditVal;
	}

	public Double getRemainingBal() {
		return remainingBal;
	}

	public void setRemainingBal(Double remainingBal) {
		this.remainingBal = remainingBal;
	}

	public Double getTotalDebit() {
		return totalDebit;
	}

	public void setTotalDebit(Double totalDebit) {
		this.totalDebit = totalDebit;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(Double totalCredit) {
		this.totalCredit = totalCredit;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
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
