package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyTypeModel {
	
	private Integer propertyType;
	
	private String propertyCategory;
	
	private String pTypeName;
	
	private String pTypeDescription;
	
	private Boolean pTypeActive;
	
	private Date pTypeCreatedOn;
	
	private Date pTypeUpdatedOn;

	private String activity;
	private String action;
	private String createdBy;
	
	public Integer getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Integer propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getpTypeName() {
		return pTypeName;
	}

	public void setpTypeName(String pTypeName) {
		this.pTypeName = pTypeName;
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

	public Date getpTypeCreatedOn() {
		return pTypeCreatedOn;
	}

	public void setpTypeCreatedOn(Date pTypeCreatedOn) {
		this.pTypeCreatedOn = pTypeCreatedOn;
	}

	public Date getpTypeUpdatedOn() {
		return pTypeUpdatedOn;
	}

	public void setpTypeUpdatedOn(Date pTypeUpdatedOn) {
		this.pTypeUpdatedOn = pTypeUpdatedOn;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
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

	public PropertyTypeModel() {
		super();
		// TODO Auto-generated constructor stub
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
