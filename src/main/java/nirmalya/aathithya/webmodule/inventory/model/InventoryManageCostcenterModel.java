package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryManageCostcenterModel {

	private String costcenterId;
	private String costCenterName;
	private List<String> propertyCategory= new ArrayList<String>();
	private String description;
	private String propertyCategoryName;
	private String action;
	private String delete;
	private String createdBy;
	public InventoryManageCostcenterModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InventoryManageCostcenterModel(String costCenterName, List<String> propertyCategory, String description,
			String propertyCategoryName) {
		super();
		this.costCenterName = costCenterName;
		this.propertyCategory = propertyCategory;
		this.description = description;
		this.propertyCategoryName = propertyCategoryName;
	}
	public String getCostCenterName() {
		return costCenterName;
	}
	public void setCostCenterName(String costCenterName) {
		this.costCenterName = costCenterName;
	}
	public List<String> getPropertyCategory() {
		return propertyCategory;
	}
	public void setPropertyCategory(List<String> propertyCategory) {
		this.propertyCategory = propertyCategory;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPropertyCategoryName() {
		return propertyCategoryName;
	}
	public void setPropertyCategoryName(String propertyCategoryName) {
		this.propertyCategoryName = propertyCategoryName;
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
	public String getCostcenterId() {
		return costcenterId;
	}
	public void setCostcenterId(String costcenterId) {
		this.costcenterId = costcenterId;
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
