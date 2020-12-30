package nirmalya.aathithya.webmodule.employee.model;

import java.math.BigInteger;

public class HrmsSalaryApproveCountModel {

	private String fromDate;
	private String toDate;
	private BigInteger attVal;
	private BigInteger foodVal;
	private BigInteger tripVal;
	private BigInteger extraVal;
	private BigInteger advanceVal;
	private BigInteger incomeVal;
	private BigInteger finalVal;
	private BigInteger leaveVal;

	public HrmsSalaryApproveCountModel() {
		super();
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

	public BigInteger getAttVal() {
		return attVal;
	}

	public void setAttVal(BigInteger attVal) {
		this.attVal = attVal;
	}

	public BigInteger getFoodVal() {
		return foodVal;
	}

	public void setFoodVal(BigInteger foodVal) {
		this.foodVal = foodVal;
	}

	public BigInteger getTripVal() {
		return tripVal;
	}

	public void setTripVal(BigInteger tripVal) {
		this.tripVal = tripVal;
	}

	public BigInteger getExtraVal() {
		return extraVal;
	}

	public void setExtraVal(BigInteger extraVal) {
		this.extraVal = extraVal;
	}

	public BigInteger getAdvanceVal() {
		return advanceVal;
	}

	public void setAdvanceVal(BigInteger advanceVal) {
		this.advanceVal = advanceVal;
	}

	public BigInteger getIncomeVal() {
		return incomeVal;
	}

	public void setIncomeVal(BigInteger incomeVal) {
		this.incomeVal = incomeVal;
	}

	public BigInteger getFinalVal() {
		return finalVal;
	}

	public void setFinalVal(BigInteger finalVal) {
		this.finalVal = finalVal;
	}

	public BigInteger getLeaveVal() {
		return leaveVal;
	}

	public void setLeaveVal(BigInteger leaveVal) {
		this.leaveVal = leaveVal;
	}

}
