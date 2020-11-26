package nirmalya.aathithya.webmodule.account.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountHeadTypeModel {

	private String accountHeadTypeId;
	private String accountHeadType;
	private String desc;
	private Boolean status;
	private String createdBy;
	private  String statusName;
	private String delete;
	private String action;
	public AccountHeadTypeModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AccountHeadTypeModel(String accountHeadType, String desc, Boolean status) {
		super();
		this.accountHeadType = accountHeadType;
		this.desc = desc;
		this.status = status;
	}
	public String getAccountHeadType() {
		return accountHeadType;
	}
	public void setAccountHeadType(String accountHeadType) {
		this.accountHeadType = accountHeadType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getDelete() {
		return delete;
	}
	public void setDelete(String delete) {
		this.delete = delete;
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
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getAccountHeadTypeId() {
		return accountHeadTypeId;
	}
	public void setAccountHeadTypeId(String accountHeadTypeId) {
		this.accountHeadTypeId = accountHeadTypeId;
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
