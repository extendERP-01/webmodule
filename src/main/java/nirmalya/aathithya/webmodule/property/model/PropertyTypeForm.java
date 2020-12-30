package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyTypeForm {
  
	private String pTypeName;
	
	private String propertyType;

	private String propertyCategory;

	private String pCategoryName;
	
	private List<String> amenitiesList= new ArrayList<String>();
	
	private String amenities;
	
	private String amntsName;

	private String pTypeDescription;

	private Boolean pTypeActive;

	private String action;

	private String activityStatus;
	
	private String createdBy;

	public PropertyTypeForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public String getpTypeName() {
		return pTypeName;
	}



	public void setpTypeName(String pTypeName) {
		this.pTypeName = pTypeName;
	}



	public String getPropertyType() {
		return propertyType;
	}



	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}



	public String getPropertyCategory() {
		return propertyCategory;
	}



	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}



	public String getpCategoryName() {
		return pCategoryName;
	}



	public void setpCategoryName(String pCategoryName) {
		this.pCategoryName = pCategoryName;
	}



	public List<String> getAmenitiesList() {
		return amenitiesList;
	}



	public void setAmenitiesList(List<String> amenitiesList) {
		this.amenitiesList = amenitiesList;
	}



	public String getAmenities() {
		return amenities;
	}



	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}



	public String getAmntsName() {
		return amntsName;
	}



	public void setAmntsName(String amntsName) {
		this.amntsName = amntsName;
	}



	public String getpTypeDescription() {
		return pTypeDescription;
	}



	public void setpTypeDescription(String pTypeDescription) {
		this.pTypeDescription = pTypeDescription;
	}



	public Boolean getpTypeActive() {
		return pTypeActive;
	}



	public void setpTypeActive(Boolean pTypeActive) {
		this.pTypeActive = pTypeActive;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}



	public String getActivityStatus() {
		return activityStatus;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
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
