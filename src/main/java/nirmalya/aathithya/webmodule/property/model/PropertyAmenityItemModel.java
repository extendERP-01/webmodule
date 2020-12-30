/*
 * web model for amenity item
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyAmenityItemModel {
	private String propertyType;
	private String amenities;
	private String item;
	private Float amenityItemQty;
	private Boolean amntyItemActive;
	private String action;
	private String delete;
	private String propertyCategory;
	private String itemCategory;
	private String ItemSubCategory;
	private String statusName;
	private String propertyName;
	private String amntsName;
	private String itemName;
	private String propertyCategoryName;
	private String createdBy;

	
	public PropertyAmenityItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PropertyAmenityItemModel(String propertyType, String amenities, String item, Float amenityItemQty,
			Boolean amntyItemActive, String action, String delete,String propertyName,String amntsName,String itemName, String propertyCategory,String ItemSubCategory,String itemCategory, String propertyCategoryName) {
		super();
		this.propertyType = propertyType;
		this.amenities = amenities;
		this.item = item;
		this.amenityItemQty = amenityItemQty;
		this.amntyItemActive = amntyItemActive;
		this.action = action;
		this.delete = delete;
		this.propertyName = propertyName;
		this.amntsName = amntsName;
		this.itemName = itemName;
		this.propertyCategory=propertyCategory;
		this.ItemSubCategory=ItemSubCategory;
		this. itemCategory=itemCategory;
		this.propertyCategoryName=propertyCategoryName;
		
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getAmenities() {
		return amenities;
	}
	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Float getAmenityItemQty() {
		return amenityItemQty;
	}
	public void setAmenityItemQty(Float amenityItemQty) {
		this.amenityItemQty = amenityItemQty;
	}
	public Boolean getAmntyItemActive() {
		return amntyItemActive;
	}
	public void setAmntyItemActive(Boolean amntyItemActive) {
		this.amntyItemActive = amntyItemActive;
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
	public String getPropertyCategory() {
		return propertyCategory;
	}
	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}
	public String getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	
	
	public String getItemSubCategory() {
		return ItemSubCategory;
	}
	public void setItemSubCategory(String itemSubCategory) {
		ItemSubCategory = itemSubCategory;
	}
	
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getAmntsName() {
		return amntsName;
	}
	public void setAmntsName(String amntsName) {
		this.amntsName = amntsName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getPropertyCategoryName() {
		return propertyCategoryName;
	}
	public void setPropertyCategoryName(String propertyCategoryName) {
		this.propertyCategoryName = propertyCategoryName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Override
	public String toString() {
		ObjectMapper  mapperObj=new ObjectMapper();
		String jsonStr;
		try{
			jsonStr=mapperObj.writeValueAsString(this);
		}catch(IOException ex){
			
			jsonStr=ex.toString();
		}
		return jsonStr;
	}
	
}
