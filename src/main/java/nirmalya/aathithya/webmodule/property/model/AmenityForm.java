package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AmenityForm {
  
    private String amenitiesId;
	
	private String propertyCategoryId;
	
	private String propertyCategoryName;
	
	private String amntsName;
	
	private String amntsDescription;
	
	private Boolean amntsActive;
	
	private String action;
	
	private String activity_status;
	
	private String createdBy;

	public AmenityForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAmenitiesId() {
		return amenitiesId;
	}

	public void setAmenitiesId(String amenitiesId) {
		this.amenitiesId = amenitiesId;
	}

	public String getPropertyCategoryId() {
		return propertyCategoryId;
	}

	public void setPropertyCategoryId(String propertyCategoryId) {
		this.propertyCategoryId = propertyCategoryId;
	}

	public String getPropertyCategoryName() {
		return propertyCategoryName;
	}

	public void setPropertyCategoryName(String propertyCategoryName) {
		this.propertyCategoryName = propertyCategoryName;
	}

	public String getAmntsName() {
		return amntsName;
	}

	public void setAmntsName(String amntsName) {
		this.amntsName = amntsName;
	}

	public String getAmntsDescription() {
		return amntsDescription;
	}

	public void setAmntsDescription(String amntsDescription) {
		this.amntsDescription = amntsDescription;
	}

	public Boolean getAmntsActive() {
		return amntsActive;
	}

	public void setAmntsActive(Boolean amntsActive) {
		this.amntsActive = amntsActive;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActivity_status() {
		return activity_status;
	}

	public void setActivity_status(String activity_status) {
		this.activity_status = activity_status;
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
