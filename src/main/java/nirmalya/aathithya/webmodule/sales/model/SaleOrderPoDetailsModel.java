package nirmalya.aathithya.webmodule.sales.model;

import java.math.BigInteger;

public class SaleOrderPoDetailsModel {

	private String poId;
	private String customerId;
	private String customerName;
	private String tin;
	private String rmcGrade;
	private String rmcGradeId;
	private String pumpService;
	private String pumpServiceId;
	private BigInteger noOfTrips;
	private Double noOfRmcGrade;
	private Double availCement;

	public SaleOrderPoDetailsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
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

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getRmcGrade() {
		return rmcGrade;
	}

	public void setRmcGrade(String rmcGrade) {
		this.rmcGrade = rmcGrade;
	}

	public String getPumpService() {
		return pumpService;
	}

	public void setPumpService(String pumpService) {
		this.pumpService = pumpService;
	}

	public String getRmcGradeId() {
		return rmcGradeId;
	}

	public void setRmcGradeId(String rmcGradeId) {
		this.rmcGradeId = rmcGradeId;
	}

	public String getPumpServiceId() {
		return pumpServiceId;
	}

	public void setPumpServiceId(String pumpServiceId) {
		this.pumpServiceId = pumpServiceId;
	}

	public BigInteger getNoOfTrips() {
		return noOfTrips;
	}

	public void setNoOfTrips(BigInteger noOfTrips) {
		this.noOfTrips = noOfTrips;
	}

	public Double getNoOfRmcGrade() {
		return noOfRmcGrade;
	}

	public void setNoOfRmcGrade(Double noOfRmcGrade) {
		this.noOfRmcGrade = noOfRmcGrade;
	}

	public Double getAvailCement() {
		return availCement;
	}

	public void setAvailCement(Double availCement) {
		this.availCement = availCement;
	}

}
