package nirmalya.aathithya.webmodule.production.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductionGoCoolModel {

	private String planId;

	private String batchId;

	private Double batchQty;

	private String prodItemId;

	private String prodItemName;

	private Double prodQty;

	private String rawMaterialItem;

	private String rawMaterialItemName;

	private Double quantity;

	private Double availQty;

	private String storeId;

	private String prodId;
	private String mixStartDate;
	private String mixEndtDate;
	private Double srcapWt;
	private Double scrapQty;
	private String action;
	private String createdBy;
	private Double mixQty;
	private Double mixWt;
	private String serveType;
	
	public ProductionGoCoolModel() {
		super();
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Double getBatchQty() {
		return batchQty;
	}

	public void setBatchQty(Double batchQty) {
		this.batchQty = batchQty;
	}

	public String getProdItemId() {
		return prodItemId;
	}

	public void setProdItemId(String prodItemId) {
		this.prodItemId = prodItemId;
	}

	public String getProdItemName() {
		return prodItemName;
	}

	public void setProdItemName(String prodItemName) {
		this.prodItemName = prodItemName;
	}

	public Double getProdQty() {
		return prodQty;
	}

	public void setProdQty(Double prodQty) {
		this.prodQty = prodQty;
	}

	public String getRawMaterialItem() {
		return rawMaterialItem;
	}

	public void setRawMaterialItem(String rawMaterialItem) {
		this.rawMaterialItem = rawMaterialItem;
	}

	public String getRawMaterialItemName() {
		return rawMaterialItemName;
	}

	public void setRawMaterialItemName(String rawMaterialItemName) {
		this.rawMaterialItemName = rawMaterialItemName;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getAvailQty() {
		return availQty;
	}

	public void setAvailQty(Double availQty) {
		this.availQty = availQty;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getMixStartDate() {
		return mixStartDate;
	}

	public void setMixStartDate(String mixStartDate) {
		this.mixStartDate = mixStartDate;
	}

	public String getMixEndtDate() {
		return mixEndtDate;
	}

	public void setMixEndtDate(String mixEndtDate) {
		this.mixEndtDate = mixEndtDate;
	}

	public Double getSrcapWt() {
		return srcapWt;
	}

	public void setSrcapWt(Double srcapWt) {
		this.srcapWt = srcapWt;
	}

	public Double getScrapQty() {
		return scrapQty;
	}

	public void setScrapQty(Double scrapQty) {
		this.scrapQty = scrapQty;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Double getMixQty() {
		return mixQty;
	}

	public void setMixQty(Double mixQty) {
		this.mixQty = mixQty;
	}

	public Double getMixWt() {
		return mixWt;
	}

	public void setMixWt(Double mixWt) {
		this.mixWt = mixWt;
	}

	public String getServeType() {
		return serveType;
	}

	public void setServeType(String serveType) {
		this.serveType = serveType;
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
