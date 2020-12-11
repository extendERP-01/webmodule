/*
 * model for floor 
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyFloorModel {
	private String floorId;
	private String floorName;
	private String floorDesc;
	private Boolean floorActive;
	private String action;
	private String Delete;
	private String statusName;
	private String pFloorCreatedBy;

	public PropertyFloorModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PropertyFloorModel(String floorId, String floorName, String floorDesc, Boolean floorActive, String action,
			String delete, String statusName) {
		super();
		this.floorId = floorId;
		this.floorName = floorName;
		this.floorDesc = floorDesc;
		this.floorActive = floorActive;
		this.action = action;
		Delete = delete;
		this.statusName = statusName;
	}

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getFloorDesc() {
		return floorDesc;
	}

	public void setFloorDesc(String floorDesc) {
		this.floorDesc = floorDesc;
	}

	public Boolean getFloorActive() {
		return floorActive;
	}

	public void setFloorActive(Boolean floorActive) {
		this.floorActive = floorActive;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDelete() {
		return Delete;
	}

	public void setDelete(String delete) {
		Delete = delete;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getpFloorCreatedBy() {
		return pFloorCreatedBy;
	}

	public void setpFloorCreatedBy(String pFloorCreatedBy) {
		this.pFloorCreatedBy = pFloorCreatedBy;
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
