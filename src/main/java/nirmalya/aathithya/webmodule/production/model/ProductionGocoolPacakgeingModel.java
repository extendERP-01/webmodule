package nirmalya.aathithya.webmodule.production.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductionGocoolPacakgeingModel {

	private String prodId;

	private String planId;

	private String batchId;

	private Double batchQty;

	private String prodItemId;

	private String prodItemName;

	private Double prodQty;

	private String storeId;

	private String packageingStartDate;

	private String packageingEndtDate;

	private Double srcapWt;

	private Double scrapQty;

	private String action;

	private String createdBy;

	private Double prodoductionQty;

	private Double prodoductionWt;

	private Byte productionStage;

	private Double productionQty;

	private Double prodWt;

	private String barcodeImageName;

	private String barcodeImageNumber;

	private Double packagingTotal;

	private Double noOfBundles;

	private Double packagingQty;
	private Double packagingWt;

	public ProductionGocoolPacakgeingModel() {
		super();
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getPackageingStartDate() {
		return packageingStartDate;
	}

	public void setPackageingStartDate(String packageingStartDate) {
		this.packageingStartDate = packageingStartDate;
	}

	public String getPackageingEndtDate() {
		return packageingEndtDate;
	}

	public void setPackageingEndtDate(String packageingEndtDate) {
		this.packageingEndtDate = packageingEndtDate;
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

	public Double getProdoductionQty() {
		return prodoductionQty;
	}

	public void setProdoductionQty(Double prodoductionQty) {
		this.prodoductionQty = prodoductionQty;
	}

	public Double getProdoductionWt() {
		return prodoductionWt;
	}

	public void setProdoductionWt(Double prodoductionWt) {
		this.prodoductionWt = prodoductionWt;
	}

	public Byte getProductionStage() {
		return productionStage;
	}

	public void setProductionStage(Byte productionStage) {
		this.productionStage = productionStage;
	}

	public Double getProductionQty() {
		return productionQty;
	}

	public void setProductionQty(Double productionQty) {
		this.productionQty = productionQty;
	}

	public Double getProdWt() {
		return prodWt;
	}

	public void setProdWt(Double prodWt) {
		this.prodWt = prodWt;
	}

	public String getBarcodeImageName() {
		return barcodeImageName;
	}

	public void setBarcodeImageName(String barcodeImageName) {
		this.barcodeImageName = barcodeImageName;
	}

	public String getBarcodeImageNumber() {
		return barcodeImageNumber;
	}

	public void setBarcodeImageNumber(String barcodeImageNumber) {
		this.barcodeImageNumber = barcodeImageNumber;
	}

	public Double getPackagingTotal() {
		return packagingTotal;
	}

	public void setPackagingTotal(Double packagingTotal) {
		this.packagingTotal = packagingTotal;
	}

	public Double getNoOfBundles() {
		return noOfBundles;
	}

	public void setNoOfBundles(Double noOfBundles) {
		this.noOfBundles = noOfBundles;
	}

	public Double getPackagingQty() {
		return packagingQty;
	}

	public void setPackagingQty(Double packagingQty) {
		this.packagingQty = packagingQty;
	}

	public Double getPackagingWt() {
		return packagingWt;
	}

	public void setPackagingWt(Double packagingWt) {
		this.packagingWt = packagingWt;
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
