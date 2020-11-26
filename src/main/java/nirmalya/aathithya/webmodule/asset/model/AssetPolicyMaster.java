package nirmalya.aathithya.webmodule.asset.model;

import java.util.ArrayList;
import java.util.List;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class AssetPolicyMaster {

	private Integer policyId;
	private String itemId;
	private String itemName;
	private String serviceName;
	private String freqId;
	private String freqName;
	private String taskPerform;
	private List<DropDownModel> option = new ArrayList<DropDownModel>();
	private String createdBy;
	private String action;
	private String editId;

	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getFreqId() {
		return freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public String getFreqName() {
		return freqName;
	}

	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

	public String getTaskPerform() {
		return taskPerform;
	}

	public List<DropDownModel> getOption() {
		return option;
	}

	public void setOption(List<DropDownModel> option) {
		this.option = option;
	}

	public void setTaskPerform(String taskPerform) {
		this.taskPerform = taskPerform;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

}
