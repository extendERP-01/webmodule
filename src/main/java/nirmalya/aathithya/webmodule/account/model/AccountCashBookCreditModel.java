package nirmalya.aathithya.webmodule.account.model;

public class AccountCashBookCreditModel {

	private String date;
	private String voucherNO;
	private String desc;
	private double amount;
	private double totalCredit;
	public AccountCashBookCreditModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getVoucherNO() {
		return voucherNO;
	}
	public void setVoucherNO(String voucherNO) {
		this.voucherNO = voucherNO;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(double totalCredit) {
		this.totalCredit = totalCredit;
	}
}
