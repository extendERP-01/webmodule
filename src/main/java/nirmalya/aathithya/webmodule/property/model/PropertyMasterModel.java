/**
 * web model for property master 
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyMasterModel {
	private String propertyId;
	private String propertyCategory;
	private String propertyType;
	private String propertyFloor;
	private String propertyName;
	private Boolean cleanStatus;
	private String propertyDescription;
	private Boolean propertyActive;
	private String propertyImage;
	private String action;
	private String delete;
	private String propertyStatus;
	private String cleanStatusName;
	private String propertyCreatedBy;
	private Boolean pBookingStatus;
	private String bookingStatusName;
	private Boolean reservationStatus;
	private String reservationStatusName;

	public PropertyMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PropertyMasterModel(String propertyId, String propertyCategory, String propertyType, String propertyFloor,
			String propertyName, Boolean cleanStatus, String propertyDescription, Boolean propertyActive,
			String propertyImage, String action, String delete, String propertyStatus, String cleanStatusName,
			String propertyCreatedBy, Boolean pBookingStatus, String bookingStatusName, Boolean reservationStatus,
			String reservationStatusName) {
		super();
		this.propertyId = propertyId;
		this.propertyCategory = propertyCategory;
		this.propertyType = propertyType;
		this.propertyFloor = propertyFloor;
		this.propertyName = propertyName;
		this.cleanStatus = cleanStatus;
		this.propertyDescription = propertyDescription;
		this.propertyActive = propertyActive;
		this.propertyImage = propertyImage;
		this.action = action;
		this.delete = delete;
		this.propertyStatus = propertyStatus;
		this.cleanStatusName = cleanStatusName;
		this.propertyCreatedBy = propertyCreatedBy;
		this.pBookingStatus = pBookingStatus;
		this.bookingStatusName = bookingStatusName;
		this.reservationStatus = reservationStatus;
		this.reservationStatusName = reservationStatusName;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyFloor() {
		return propertyFloor;
	}

	public void setPropertyFloor(String propertyFloor) {
		this.propertyFloor = propertyFloor;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Boolean getCleanStatus() {
		return cleanStatus;
	}

	public void setCleanStatus(Boolean cleanStatus) {
		this.cleanStatus = cleanStatus;
	}

	public String getPropertyDescription() {
		return propertyDescription;
	}

	public void setPropertyDescription(String propertyDescription) {
		this.propertyDescription = propertyDescription;
	}

	public Boolean getPropertyActive() {
		return propertyActive;
	}

	public void setPropertyActive(Boolean propertyActive) {
		this.propertyActive = propertyActive;
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

	public String getPropertyStatus() {
		return propertyStatus;
	}

	public void setPropertyStatus(String propertyStatus) {
		this.propertyStatus = propertyStatus;
	}

	public String getCleanStatusName() {
		return cleanStatusName;
	}

	public void setCleanStatusName(String cleanStatusName) {
		this.cleanStatusName = cleanStatusName;
	}

	public String getPropertyImage() {
		return propertyImage;
	}

	public void setPropertyImage(String propertyImage) {
		this.propertyImage = propertyImage;
	}

	public String getPropertyCreatedBy() {
		return propertyCreatedBy;
	}

	public void setPropertyCreatedBy(String propertyCreatedBy) {
		this.propertyCreatedBy = propertyCreatedBy;
	}

	public Boolean getpBookingStatus() {
		return pBookingStatus;
	}

	public void setpBookingStatus(Boolean pBookingStatus) {
		this.pBookingStatus = pBookingStatus;
	}

	public String getBookingStatusName() {
		return bookingStatusName;
	}

	public void setBookingStatusName(String bookingStatusName) {
		this.bookingStatusName = bookingStatusName;
	}

	public Boolean getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(Boolean reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public String getReservationStatusName() {
		return reservationStatusName;
	}

	public void setReservationStatusName(String reservationStatusName) {
		this.reservationStatusName = reservationStatusName;
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
