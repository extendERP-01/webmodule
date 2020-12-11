/**
 * web model for property asset code
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyAssetCodeModel {
	private String assetCodeId;
	private String assetCodeName;
	private String item;
	private String dop;
	private String grrnty;
	private String brndNm;
	private String custEmail;
	private String custPhone;
	private String description;
	private Boolean assetactive;
	private Boolean workingStatus;
	private String assignStatus;
	private String action;
	private String delete;
	private String assetStatus;
	private String workingStatusName;
	private String createdBy;
	private String tSerialNo;
	private String store;
	private String model;
	private String chassisNo;
	private String engineNo;
	private String categoryId;
	private String category;
	private String itemId;
	private String barcode;
	private String grn;
	private String className;
	private String classId;

	public PropertyAssetCodeModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PropertyAssetCodeModel(String assetCodeId, String assetCodeName, String item, String dop, String grrnty,
			String brndNm, String custEmail, String custPhone, String description, Boolean assetactive,
			Boolean workingStatus, String assignStatus, String action, String delete, String assetStatus,
			String workingStatusName, String createdBy, String tSerialNo, String store, String model, String chassisNo,
			String engineNo, String categoryId, String category, String itemId) {
		super();
		this.assetCodeId = assetCodeId;
		this.assetCodeName = assetCodeName;
		this.item = item;
		this.dop = dop;
		this.grrnty = grrnty;
		this.brndNm = brndNm;
		this.custEmail = custEmail;
		this.custPhone = custPhone;
		this.description = description;
		this.assetactive = assetactive;
		this.workingStatus = workingStatus;
		this.assignStatus = assignStatus;
		this.action = action;
		this.delete = delete;
		this.assetStatus = assetStatus;
		this.workingStatusName = workingStatusName;
		this.createdBy = createdBy;
		this.tSerialNo = tSerialNo;
		this.store = store;
		this.model = model;
		this.chassisNo = chassisNo;
		this.engineNo = engineNo;
		this.categoryId = categoryId;
		this.category = category;
		this.itemId = itemId;
	}

	public String getAssetCodeId() {
		return assetCodeId;
	}

	public void setAssetCodeId(String assetCodeId) {
		this.assetCodeId = assetCodeId;
	}

	public String getAssetCodeName() {
		return assetCodeName;
	}

	public void setAssetCodeName(String assetCodeName) {
		this.assetCodeName = assetCodeName;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDop() {
		return dop;
	}

	public void setDop(String dop) {
		this.dop = dop;
	}

	public String getGrrnty() {
		return grrnty;
	}

	public void setGrrnty(String grrnty) {
		this.grrnty = grrnty;
	}

	public String getBrndNm() {
		return brndNm;
	}

	public void setBrndNm(String brndNm) {
		this.brndNm = brndNm;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAssetactive() {
		return assetactive;
	}

	public void setAssetactive(Boolean assetactive) {
		this.assetactive = assetactive;
	}

	public Boolean getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(Boolean workingStatus) {
		this.workingStatus = workingStatus;
	}

	public String getAssignStatus() {
		return assignStatus;
	}

	public void setAssignStatus(String assignStatus) {
		this.assignStatus = assignStatus;
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

	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public String getWorkingStatusName() {
		return workingStatusName;
	}

	public void setWorkingStatusName(String workingStatusName) {
		this.workingStatusName = workingStatusName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String gettSerialNo() {
		return tSerialNo;
	}

	public void settSerialNo(String tSerialNo) {
		this.tSerialNo = tSerialNo;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getGrn() {
		return grn;
	}

	public void setGrn(String grn) {
		this.grn = grn;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
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