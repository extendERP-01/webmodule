/*
*
 * Class Define ItemCategory Entity
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class InventoryItemCategoryModel {
	private String itmCategory;
	private String itmCatName;
	private String itmCatDescription;
	private Boolean itmCatActive;
	private String createdBy;
	private String status;
	private String action;
	private String delete;

	public InventoryItemCategoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getItmCategory() {
		return itmCategory;
	}

	public void setItmCategory(String itmCategory) {
		this.itmCategory = itmCategory;
	}

	public String getItmCatName() {
		return itmCatName;
	}

	public void setItmCatName(String itmCatName) {
		this.itmCatName = itmCatName;
	}

	public String getItmCatDescription() {
		return itmCatDescription;
	}

	public void setItmCatDescription(String itmCatDescription) {
		this.itmCatDescription = itmCatDescription;
	}

	public Boolean getItmCatActive() {
		return itmCatActive;
	}

	public void setItmCatActive(Boolean itmCatActive) {
		this.itmCatActive = itmCatActive;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
