/*

 * model for category 
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;



import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyCategoryModel {
	private String propertyCatId;
	private String categoryName;
	private String categoryDescription;
	private Boolean categoryActive;
	private String action;
	private String delete;
	private String statusName;
	
	
	public PropertyCategoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public PropertyCategoryModel(String propertyCatId, String categoryName, String categoryDescription,
			Boolean categoryActive,  String action, String delete,String statusName) {
		super();
		this.propertyCatId = propertyCatId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
		this.categoryActive = categoryActive;
		this.action = action;
		this.delete = delete;
		this.statusName= statusName;
	}




	public String getPropertyCatId() {
		return propertyCatId;
	}




	public void setPropertyCatId(String propertyCatId) {
		this.propertyCatId = propertyCatId;
	}




	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getCategoryDescription() {
		return categoryDescription;
	}


	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}


	public Boolean getCategoryActive() {
		return categoryActive;
	}


	public void setCategoryActive(Boolean categoryActive) {
		this.categoryActive = categoryActive;
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
