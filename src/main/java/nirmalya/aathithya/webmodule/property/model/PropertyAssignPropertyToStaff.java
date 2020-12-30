package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyAssignPropertyToStaff {
	
	private String costCenter;
	private String userRole;
	private String user;
	private List<String> propertyList= new ArrayList<String>();
	private String propertyListNames;
	private Boolean satffActive;
	private String action;
	private String delete;
	private String costCenterId;
	private String userRoleId;
	private String userId;
	private String editId;
	private String statusName;
	private String propertyNameId;
	private String createdBy;
	private String currentDate;

	public PropertyAssignPropertyToStaff() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PropertyAssignPropertyToStaff(String costCenter, String userRole, String user, List<String> propertyList,
			String propertyListNames, Boolean satffActive) {
		super();
		this.costCenter = costCenter;
		this.userRole = userRole;
		this.user = user;
		this.propertyList = propertyList;
		this.propertyListNames = propertyListNames;
		this.satffActive = satffActive;
	}
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public List<String> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<String> propertyList) {
		this.propertyList = propertyList;
	}
	public String getPropertyListNames() {
		return propertyListNames;
	}
	public void setPropertyListNames(String propertyListNames) {
		this.propertyListNames = propertyListNames;
	}
	public Boolean getSatffActive() {
		return satffActive;
	}
	public void setSatffActive(Boolean satffActive) {
		this.satffActive = satffActive;
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
	
	public String getCostCenterId() {
		return costCenterId;
	}
	public void setCostCenterId(String costCenterId) {
		this.costCenterId = costCenterId;
	}
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEditId() {
		return editId;
	}
	public void setEditId(String editId) {
		this.editId = editId;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getPropertyNameId() {
		return propertyNameId;
	}
	public void setPropertyNameId(String propertyNameId) {
		this.propertyNameId = propertyNameId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
