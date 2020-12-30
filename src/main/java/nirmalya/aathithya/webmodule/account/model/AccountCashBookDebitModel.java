package nirmalya.aathithya.webmodule.account.model;

public class AccountCashBookDebitModel {

	private String date;
	private String voucherNO;
	private String desc;
	private double amount;
	private double totalDebit;
	public AccountCashBookDebitModel() {
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
	public double getTotalDebit() {
		return totalDebit;
	}
	public void setTotalDebit(double totalDebit) {
		this.totalDebit = totalDebit;
	}
	
	
	
}
