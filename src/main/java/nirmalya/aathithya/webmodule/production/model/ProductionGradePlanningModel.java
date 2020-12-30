package nirmalya.aathithya.webmodule.production.model;

public class ProductionGradePlanningModel {

	private String ppId;
	private String fromDate;
	private String toDate;
	private Integer sizeId;
	private String size;

	private Double slit;
	private Double mcoil;
	private Double plan;
	private Double sales;
	private Double wip;
	private Double fp;
	private String action;
	private Double ratio;
	private Double sockPercentage;
	private Double total;

	public ProductionGradePlanningModel() {

	}

	public String getPpId() {
		return ppId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public Integer getSizeId() {
		return sizeId;
	}

	public String getSize() {
		return size;
	}

	public Double getSlit() {
		return slit;
	}

	public Double getMcoil() {
		return mcoil;
	}

	public Double getPlan() {
		return plan;
	}

	public Double getSales() {
		return sales;
	}

	public Double getWip() {
		return wip;
	}

	public Double getFp() {
		return fp;
	}

	public String getAction() {
		return action;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setSlit(Double slit) {
		this.slit = slit;
	}

	public void setMcoil(Double mcoil) {
		this.mcoil = mcoil;
	}

	public void setPlan(Double plan) {
		this.plan = plan;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public void setWip(Double wip) {
		this.wip = wip;
	}

	public void setFp(Double fp) {
		this.fp = fp;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public Double getSockPercentage() {
		return sockPercentage;
	}

	public Double getTotal() {
		return total;
	}

	public void setSockPercentage(Double sockPercentage) {
		this.sockPercentage = sockPercentage;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
