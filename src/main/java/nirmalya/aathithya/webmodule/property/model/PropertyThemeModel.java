/*
 * Model for property theme
 */
package nirmalya.aathithya.webmodule.property.model;


import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyThemeModel {

	private String theme;
	private String propertyCategory;
	private String thmName;
	private String themeCategoryName;
	private String thmDescription;
	private Boolean thmActive;
	private String statusName;
	private String action;
	private String delete;
	private String createdBy;

	
	
	public PropertyThemeModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public PropertyThemeModel(String theme, String propertyCategory, String thmName, String themeCategoryName,
			String thmDescription, Boolean thmActive,String statusName,
			String action, String delete) {
		super();
		this.theme = theme;
		this.propertyCategory = propertyCategory;
		this.thmName = thmName;
		this.themeCategoryName = themeCategoryName;
		this.thmDescription = thmDescription;
		this.thmActive = thmActive;
		this.statusName = statusName;
		this.action = action;
		this.delete = delete;
	}
	
	
	

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getThmName() {
		return thmName;
	}

	public void setThmName(String thmName) {
		this.thmName = thmName;
	}

	public String getThemeCategoryName() {
		return themeCategoryName;
	}

	public void setThemeCategoryName(String themeCategoryName) {
		this.themeCategoryName = themeCategoryName;
	}

	public String getThmDescription() {
		return thmDescription;
	}

	public void setThmDescription(String thmDescription) {
		this.thmDescription = thmDescription;
	}

	public Boolean getThmActive() {
		return thmActive;
	}

	public void setThmActive(Boolean thmActive) {
		this.thmActive = thmActive;
	}


	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

