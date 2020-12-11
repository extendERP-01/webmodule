package nirmalya.aathithya.webmodule.property.model;

/**
 * @author Nirmalya Labs
 *
 */
public class AssignAssetToStaffModel {

	private String costCenter;
	
	private String staff;
	
	private String category;
	
	private String subcategory;
	
	private String item;
	
	private String asset;
	
	private String assignDate;
	
	private Boolean active;
	
	private String costId;
	
	private String staffId;
	
	private String categoryID;
	
	private String subcategoryId;
	
	private String itemId;
	
	private String assetId;
	
	private String action;
	
	private String status;
	
	private String createdBy;
	
	public AssignAssetToStaffModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssignAssetToStaffModel(String costCenter, String staff, String category,
			String subcategory, String item, String asset, String assignDate, Boolean active, String costId, String staffId,
			String categoryID, String subcategoryId, String itemId, String assetId, String action, String status) {
		super();
		this.costCenter = costCenter;
		this.staff = staff;
		this.category = category;
		this.subcategory = subcategory;
		this.item = item;
		this.asset = asset;
		this.assignDate = assignDate;
		this.active = active;
		this.costId = costId;
		this.staffId = staffId;
		this.categoryID = categoryID;
		this.subcategoryId = subcategoryId;
		this.itemId = itemId;
		this.assetId = assetId;
		this.action = action;
		this.status = status;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCostId() {
		return costId;
	}

	public void setCostId(String costId) {
		this.costId = costId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(String subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
