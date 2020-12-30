package nirmalya.aathithya.webmodule.sales.model;

import java.math.BigInteger;

public class SalesDashboardModel {

	private BigInteger totalSales;
	
	private Double totalSaleAmount;
	
	private Double totalPaidAmount;
	
	private Double totalDueAmount;

	public SalesDashboardModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BigInteger getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(BigInteger totalSales) {
		this.totalSales = totalSales;
	}

	public Double getTotalSaleAmount() {
		return totalSaleAmount;
	}

	public void setTotalSaleAmount(Double totalSaleAmount) {
		this.totalSaleAmount = totalSaleAmount;
	}

	public Double getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public void setTotalPaidAmount(Double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public Double getTotalDueAmount() {
		return totalDueAmount;
	}

	public void setTotalDueAmount(Double totalDueAmount) {
		this.totalDueAmount = totalDueAmount;
	}
	
}
