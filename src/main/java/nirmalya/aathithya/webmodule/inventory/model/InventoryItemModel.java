/**
 *Class showing model for Item Entity 
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InventoryItemModel {
	private String item;
	private String tAccountGroupType;
	private String tPurchaseSubGroup;
	private String tsalesSubGroup;
	private String itemCategory;
	private String itemSubCategory;
	private String serveType;
	private String itemName;
	private Double itemMinStock;
	private Double itemMaxStock;
	private Boolean itemActive;
	private String serviceType;
	private String sacCode;
	private String createdBy;
	private String printedBy;
	private String currentDate;
	private String status;
	private String action;
	private String delete;
	private String itemPipeSize;
	private String itemGrade;
	private String itemThickness;
	private String productionType;
	private String productionTypeId;
	private String itemImg;
	private Double itemQty;
	private Double price;
	private Double salesPrice;
	private String hsnCode;
	private Double purchaseTAX;
	private Double purchaseCess;
	private Double saleTAX;
	private Double saleCess;
	private String purchaseName;
	private String saleName;

	public InventoryItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemSubCategory() {
		return itemSubCategory;
	}

	public void setItemSubCategory(String itemSubCategory) {
		this.itemSubCategory = itemSubCategory;
	}

	public String getServeType() {
		return serveType;
	}

	public void setServeType(String serveType) {
		this.serveType = serveType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getItemMinStock() {
		return itemMinStock;
	}

	public void setItemMinStock(Double itemMinStock) {
		this.itemMinStock = itemMinStock;
	}

	public Double getItemMaxStock() {
		return itemMaxStock;
	}

	public void setItemMaxStock(Double itemMaxStock) {
		this.itemMaxStock = itemMaxStock;
	}

	public Boolean getItemActive() {
		return itemActive;
	}

	public void setItemActive(Boolean itemActive) {
		this.itemActive = itemActive;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSacCode() {
		return sacCode;
	}

	public void setSacCode(String sacCode) {
		this.sacCode = sacCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getPrintedBy() {
		return printedBy;
	}

	public void setPrintedBy(String printedBy) {
		this.printedBy = printedBy;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String gettAccountGroupType() {
		return tAccountGroupType;
	}

	public void settAccountGroupType(String tAccountGroupType) {
		this.tAccountGroupType = tAccountGroupType;
	}

	public String gettPurchaseSubGroup() {
		return tPurchaseSubGroup;
	}

	public void settPurchaseSubGroup(String tPurchaseSubGroup) {
		this.tPurchaseSubGroup = tPurchaseSubGroup;
	}

	public String getTsalesSubGroup() {
		return tsalesSubGroup;
	}

	public void setTsalesSubGroup(String tsalesSubGroup) {
		this.tsalesSubGroup = tsalesSubGroup;
	}

	public String getItemPipeSize() {
		return itemPipeSize;
	}

	public void setItemPipeSize(String itemPipeSize) {
		this.itemPipeSize = itemPipeSize;
	}

	public String getItemGrade() {
		return itemGrade;
	}

	public String getItemThickness() {
		return itemThickness;
	}

	public void setItemGrade(String itemGrade) {
		this.itemGrade = itemGrade;
	}

	public void setItemThickness(String itemThickness) {
		this.itemThickness = itemThickness;
	}

	public String getProductionType() {
		return productionType;
	}

	public void setProductionType(String productionType) {
		this.productionType = productionType;
	}

	public String getProductionTypeId() {
		return productionTypeId;
	}

	public void setProductionTypeId(String productionTypeId) {
		this.productionTypeId = productionTypeId;
	}

	public String getItemImg() {
		return itemImg;
	}

	public Double getItemQty() {
		return itemQty;
	}

	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Double getPurchaseTAX() {
		return purchaseTAX;
	}

	public void setPurchaseTAX(Double purchaseTAX) {
		this.purchaseTAX = purchaseTAX;
	}

	public Double getPurchaseCess() {
		return purchaseCess;
	}

	public void setPurchaseCess(Double purchaseCess) {
		this.purchaseCess = purchaseCess;
	}

	public Double getSaleTAX() {
		return saleTAX;
	}

	public void setSaleTAX(Double saleTAX) {
		this.saleTAX = saleTAX;
	}

	public Double getSaleCess() {
		return saleCess;
	}

	public void setSaleCess(Double saleCess) {
		this.saleCess = saleCess;
	}

	public String getPurchaseName() {
		return purchaseName;
	}

	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
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
