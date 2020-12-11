/**
 * 
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author USER
 *
 */
public class PropertyAssignConsumedItemModel {

	private String propertyCategory;
	private String propertyNameId;
	private String amenity;
	private String itemCategory;
	private String itemSubCategory;
	private String itemNameId;
	private Float assignQuantity;
	private Boolean assignActive;
	private String createdOn;
	private Date updatedOn;
	private String propertyName;
	private String itemName;
	private String action;
	private String status;
	private String createdBy;
	private String curDate;
	private String dateFrom;
	private String dateTo;

	public PropertyAssignConsumedItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PropertyAssignConsumedItemModel(String propertyCategory, String propertyNameId, String amenity,
			String itemCategory, String itemSubCategory, String itemNameId, Float assignQuantity, Boolean assignActive,
			String createdOn, Date updatedOn, String propertyName, String itemName, String action, String status,
			String createdBy) {
		super();
		this.propertyCategory = propertyCategory;
		this.propertyNameId = propertyNameId;
		this.amenity = amenity;
		this.itemCategory = itemCategory;
		this.itemSubCategory = itemSubCategory;
		this.itemNameId = itemNameId;
		this.assignQuantity = assignQuantity;
		this.assignActive = assignActive;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.propertyName = propertyName;
		this.itemName = itemName;
		this.action = action;
		this.status = status;
		this.createdBy = createdBy;
	}

	public String getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getPropertyNameId() {
		return propertyNameId;
	}

	public void setPropertyNameId(String propertyNameId) {
		this.propertyNameId = propertyNameId;
	}

	public String getAmenity() {
		return amenity;
	}

	public void setAmenity(String amenity) {
		this.amenity = amenity;
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

	public String getItemNameId() {
		return itemNameId;
	}

	public void setItemNameId(String itemNameId) {
		this.itemNameId = itemNameId;
	}

	public Float getAssignQuantity() {
		return assignQuantity;
	}

	public void setAssignQuantity(Float assignQuantity) {
		this.assignQuantity = assignQuantity;
	}

	public Boolean getAssignActive() {
		return assignActive;
	}

	public void setAssignActive(Boolean assignActive) {
		this.assignActive = assignActive;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
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
