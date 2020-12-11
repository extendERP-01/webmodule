/*
 * model for assign assets
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyAssignAssetModel {

	private String propertyCategory;
	private String property;
	private String itemCategory;
	private String itemSubCategory;
	private String item;
	private Float pAsstQty;
	private Boolean pAsstActive;
	private String action;
	private String delete;
	private List<String> assetsList = new ArrayList<String>();
	private String assetsName;
	private String propertyName;
	private String itemName;
	private String itemCategoryName;
	private String itemSubCategoryName;
	private String propertyTypeName;
	private String amenity;
	private String pdfCurrentDate;
	private String createdBy;


	public PropertyAssignAssetModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PropertyAssignAssetModel(String propertyCategory, String property, String itemCategory,
			String itemSubCategory, String item, Float pAsstQty, Boolean pAsstActive, String action, String delete,
			List<String> assetsList) {
		super();
		this.propertyCategory = propertyCategory;
		this.property = property;
		this.itemCategory = itemCategory;
		this.itemSubCategory = itemSubCategory;
		this.item = item;

		this.pAsstQty = pAsstQty;
		this.pAsstActive = pAsstActive;
		this.action = action;
		this.delete = delete;
		this.assetsList = assetsList;
	}

	public String getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
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

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Float getpAsstQty() {
		return pAsstQty;
	}

	public void setpAsstQty(Float pAsstQty) {
		this.pAsstQty = pAsstQty;
	}

	public Boolean getpAsstActive() {
		return pAsstActive;
	}

	public void setpAsstActive(Boolean pAsstActive) {
		this.pAsstActive = pAsstActive;
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

	public List<String> getAssetsList() {
		return assetsList;
	}

	public void setAssetsList(List<String> assetsList) {
		this.assetsList = assetsList;
	}

	public String getAssetsName() {
		return assetsName;
	}

	public void setAssetsName(String assetsName) {
		this.assetsName = assetsName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCategoryName() {
		return itemCategoryName;
	}

	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}

	public String getItemSubCategoryName() {
		return itemSubCategoryName;
	}

	public void setItemSubCategoryName(String itemSubCategoryName) {
		this.itemSubCategoryName = itemSubCategoryName;
	}

	public String getPropertyTypeName() {
		return propertyTypeName;
	}

	public void setPropertyTypeName(String propertyTypeName) {
		this.propertyTypeName = propertyTypeName;
	}

	public String getAmenity() {
		return amenity;
	}

	public void setAmenity(String amenity) {
		this.amenity = amenity;
	}

	public String getPdfCurrentDate() {
		return pdfCurrentDate;
	}

	public void setPdfCurrentDate(String pdfCurrentDate) {
		this.pdfCurrentDate = pdfCurrentDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
