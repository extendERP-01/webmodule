/**
 * Define ItemSubCategoryEntity
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class InventoryItemSubCategoryModel {
	
	private String itmSubCategoryId;
	private String itmSubCategoryName;
	private String itmCategory;
	private String itmSubDescription;
	private Boolean itmSubActive;
	private String action;
	private String status;
	private Date itmSubCreatedOn;
	private Date itmSubUpdatedOn;
	private String itmCategoryName;
	private String createdBy;
	
	public InventoryItemSubCategoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InventoryItemSubCategoryModel(String itmSubCategoryId, String itmSubCategoryName, String itmCategory,
			String itmSubDescription, Boolean itmSubActive, String action, String status, Date itmSubCreatedOn,
			Date itmSubUpdatedOn, Object itmCategoryName,String createdBy) {
		super();
		this.itmSubCategoryId = itmSubCategoryId;
		this.itmSubCategoryName = itmSubCategoryName;
		this.itmCategory = itmCategory;
		this.itmSubDescription = itmSubDescription;
		this.itmSubActive = itmSubActive;
		this.action = action;
		this.status = status;
		this.itmSubCreatedOn = itmSubCreatedOn;
		this.itmSubUpdatedOn = itmSubUpdatedOn;
		this.itmCategoryName = (String) itmCategoryName;
		this.createdBy = createdBy;
	}

	public String getItmSubCategoryId() {
		return itmSubCategoryId;
	}

	public void setItmSubCategoryId(String itmSubCategoryId) {
		this.itmSubCategoryId = itmSubCategoryId;
	}

	public String getItmSubCategoryName() {
		return itmSubCategoryName;
	}

	public void setItmSubCategoryName(String itmSubCategoryName) {
		this.itmSubCategoryName = itmSubCategoryName;
	}

	public String getItmCategory() {
		return itmCategory;
	}

	public void setItmCategory(String itmCategory) {
		this.itmCategory = itmCategory;
	}

	public String getItmSubDescription() {
		return itmSubDescription;
	}

	public void setItmSubDescription(String itmSubDescription) {
		this.itmSubDescription = itmSubDescription;
	}

	public Boolean getItmSubActive() {
		return itmSubActive;
	}

	public void setItmSubActive(Boolean itmSubActive) {
		this.itmSubActive = itmSubActive;
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

	public Date getItmSubCreatedOn() {
		return itmSubCreatedOn;
	}

	public void setItmSubCreatedOn(Date itmSubCreatedOn) {
		this.itmSubCreatedOn = itmSubCreatedOn;
	}

	public Date getItmSubUpdatedOn() {
		return itmSubUpdatedOn;
	}

	public void setItmSubUpdatedOn(Date itmSubUpdatedOn) {
		this.itmSubUpdatedOn = itmSubUpdatedOn;
	}
	
	public String getItmCategoryName() {
		return itmCategoryName;
	}

	public void setItmCategoryName(String itmCategoryName) {
		this.itmCategoryName = itmCategoryName;
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
