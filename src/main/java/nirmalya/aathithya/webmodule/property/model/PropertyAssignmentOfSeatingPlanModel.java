/*
 * model for AssignmentOf Seating Plan
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyAssignmentOfSeatingPlanModel {
private String propertyType;
private String seatingPlan;
private Integer pSplanCapacity;
private String sPlanImage;
private String pSplanPrice;
private Boolean pSplanActive;
private String propertyCategory;
private String propertyTypeName;
private String propertyCategoryName;
private String seatingPlanName;
public String action;
private String delete;
private String statusName;
private String id;  		//here id is a imaginary feild for edit in Add page
private String userType;
private String createdBy;
private String userTypeName;
private String currentDate;

public PropertyAssignmentOfSeatingPlanModel() {
	super();
	// TODO Auto-generated constructor stub
}





public PropertyAssignmentOfSeatingPlanModel(String propertyType, String seatingPlan, Integer pSplanCapacity,
		String sPlanImage, String pSplanPrice, Boolean pSplanActive, String propertyCategory, String propertyTypeName,
		String propertyCategoryName, String seatingPlanName, String action, String delete, String statusName, String id,
		String userType, String createdBy, String userTypeName) {
	super();
	this.propertyType = propertyType;
	this.seatingPlan = seatingPlan;
	this.pSplanCapacity = pSplanCapacity;
	this.sPlanImage = sPlanImage;
	this.pSplanPrice = pSplanPrice;
	this.pSplanActive = pSplanActive;
	this.propertyCategory = propertyCategory;
	this.propertyTypeName = propertyTypeName;
	this.propertyCategoryName = propertyCategoryName;
	this.seatingPlanName = seatingPlanName;
	this.action = action;
	this.delete = delete;
	this.statusName = statusName;
	this.id = id;
	this.userType = userType;
	this.createdBy = createdBy;
	this.userTypeName = userTypeName;
}





public String getPropertyType() {
	return propertyType;
}

public void setPropertyType(String propertyType) {
	this.propertyType = propertyType;
}

public String getSeatingPlan() {
	return seatingPlan;
}

public void setSeatingPlan(String seatingPlan) {
	this.seatingPlan = seatingPlan;
}

public Integer getpSplanCapacity() {
	return pSplanCapacity;
}

public void setpSplanCapacity(Integer pSplanCapacity) {
	this.pSplanCapacity = pSplanCapacity;
}

public String getsPlanImage() {
	return sPlanImage;
}

public void setsPlanImage(String sPlanImage) {
	this.sPlanImage = sPlanImage;
}
public String getpSplanPrice() {
	return pSplanPrice;
}

public void setpSplanPrice(String pSplanPrice) {
	this.pSplanPrice = pSplanPrice;
}
public Boolean getpSplanActive() {
	return pSplanActive;
}
public void setpSplanActive(Boolean pSplanActive) {
	this.pSplanActive = pSplanActive;
}
public String getPropertyCategory() {
	return propertyCategory;
}
public void setPropertyCategory(String propertyCategory) {
	this.propertyCategory = propertyCategory;
}
public String getPropertyTypeName() {
	return propertyTypeName;
}
public void setPropertyTypeName(String propertyTypeName) {
	this.propertyTypeName = propertyTypeName;
}
public String getPropertyCategoryName() {
	return propertyCategoryName;
}
public void setPropertyCategoryName(String propertyCategoryName) {
	this.propertyCategoryName = propertyCategoryName;
}
public String getSeatingPlanName() {
	return seatingPlanName;
}
public void setSeatingPlanName(String seatingPlanName) {
	this.seatingPlanName = seatingPlanName;
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
public String getStatusName() {
	return statusName;
}
public void setStatusName(String statusName) {
	this.statusName = statusName;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}

public String getUserType() {
	return userType;
}
public void setUserType(String userType) {
	this.userType = userType;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}

public String getUserTypeName() {
	return userTypeName;
}
public void setUserTypeName(String userTypeName) {
	this.userTypeName = userTypeName;
}
public String getCurrentDate() {
	return currentDate;
}
public void setCurrentDate(String currentDate) {
	this.currentDate = currentDate;
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
