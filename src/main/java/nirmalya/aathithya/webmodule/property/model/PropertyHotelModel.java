/*
 * model for property hotel 
 */
package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyHotelModel {
	private String hotelId;
	private String hotelName;
	private String hotelAdress;
	private String district;
	private String state;
	private String hotelCountry;
	private String hotelPin;
	private String hotelCity;
	private String hotelPhone;
	private String hotelEmail;
	private String hotelWebsite;
	private String hotelTin;
	private String hotelRegdNo;
	private String hotelLogo;
	private String hotelGrade;
	private Boolean hotelStatus;
	private Date hotelCreatedOn;
	private String hotelCreatedBy;
	private Date hotelUpdatedOn;
	private String hotelstatusName;
	private String action;
	private String delete;

	public PropertyHotelModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelAdress() {
		return hotelAdress;
	}

	public void setHotelAdress(String hotelAdress) {
		this.hotelAdress = hotelAdress;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getHotelCountry() {
		return hotelCountry;
	}

	public void setHotelCountry(String hotelCountry) {
		this.hotelCountry = hotelCountry;
	}

	public String getHotelPin() {
		return hotelPin;
	}

	public void setHotelPin(String hotelPin) {
		this.hotelPin = hotelPin;
	}
	

	public String getHotelCity() {
		return hotelCity;
	}

	public void setHotelCity(String hotelCity) {
		this.hotelCity = hotelCity;
	}

	public String getHotelPhone() {
		return hotelPhone;
	}

	public void setHotelPhone(String hotelPhone) {
		this.hotelPhone = hotelPhone;
	}

	public String getHotelEmail() {
		return hotelEmail;
	}

	public void setHotelEmail(String hotelEmail) {
		this.hotelEmail = hotelEmail;
	}

	public String getHotelWebsite() {
		return hotelWebsite;
	}

	public void setHotelWebsite(String hotelWebsite) {
		this.hotelWebsite = hotelWebsite;
	}

	public String getHotelTin() {
		return hotelTin;
	}

	public void setHotelTin(String hotelTin) {
		this.hotelTin = hotelTin;
	}

	public String getHotelRegdNo() {
		return hotelRegdNo;
	}

	public void setHotelRegdNo(String hotelRegdNo) {
		this.hotelRegdNo = hotelRegdNo;
	}

	public String getHotelLogo() {
		return hotelLogo;
	}

	public void setHotelLogo(String hotelLogo) {
		this.hotelLogo = hotelLogo;
	}

	public String getHotelGrade() {
		return hotelGrade;
	}

	public void setHotelGrade(String hotelGrade) {
		this.hotelGrade = hotelGrade;
	}

	public Boolean getHotelStatus() {
		return hotelStatus;
	}

	public void setHotelStatus(Boolean hotelStatus) {
		this.hotelStatus = hotelStatus;
	}

	public Date getHotelCreatedOn() {
		return hotelCreatedOn;
	}

	public void setHotelCreatedOn(Date hotelCreatedOn) {
		this.hotelCreatedOn = hotelCreatedOn;
	}

	public Date getHotelUpdatedOn() {
		return hotelUpdatedOn;
	}

	public void setHotelUpdatedOn(Date hotelUpdatedOn) {
		this.hotelUpdatedOn = hotelUpdatedOn;
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

	public String getHotelstatusName() {
		return hotelstatusName;
	}

	public void setHotelstatusName(String hotelstatusName) {
		this.hotelstatusName = hotelstatusName;
	}
	

	public String getHotelCreatedBy() {
		return hotelCreatedBy;
	}

	public void setHotelCreatedBy(String hotelCreatedBy) {
		this.hotelCreatedBy = hotelCreatedBy;
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
