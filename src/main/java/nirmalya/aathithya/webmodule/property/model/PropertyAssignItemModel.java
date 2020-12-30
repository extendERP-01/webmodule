/*

 * Web model for Property Asssign Item 
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyAssignItemModel {
	private String propertyCategory;
	private String property;
	private String itemCategory;
	private String itemSubCategory;
	private String item;
	private Boolean availableQty;
	private String assignQty;
	private String createdBy;

	
	public PropertyAssignItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PropertyAssignItemModel(String propertyCategory, String property, String itemCategory,
			String itemSubCategory, String item, Boolean availableQty, String assignQty) {
		super();
		this.propertyCategory = propertyCategory;
		this.property = property;
		this.itemCategory = itemCategory;
		this.itemSubCategory = itemSubCategory;
		this.item = item;
		this.availableQty = availableQty;
		this.assignQty = assignQty;
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
	public Boolean getAvailableQty() {
		return availableQty;
	}
	public void setAvailableQty(Boolean availableQty) {
		this.availableQty = availableQty;
	}
	public String getAssignQty() {
		return assignQty;
	}
	public void setAssignQty(String assignQty) {
		this.assignQty = assignQty;
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
