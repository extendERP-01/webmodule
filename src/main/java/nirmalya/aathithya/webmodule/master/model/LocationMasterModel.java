package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class LocationMasterModel {

	private String locationId;
	private String locationName;
	private String locationCode;
	private String locationType;
	private String locCountry;
	private String locState;
	private String locCity;
	private String locStreet;
	private String locVirtual;
	private String locStatus;
	private String createdBy;
	private String fileLocation;
	private String createdDate;
	private List<DropDownModel> stateList = new ArrayList<DropDownModel>();
	private List<DropDownModel> cityList = new ArrayList<DropDownModel>();
	private String floorId;
	private Integer floorSlNo;
	private List<LocationSectionModel> sectionList = new ArrayList<LocationSectionModel>();
	private BigInteger locCount;
	private BigInteger floorCount;
	
	public LocationMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocCountry() {
		return locCountry;
	}

	public void setLocCountry(String locCountry) {
		this.locCountry = locCountry;
	}

	public String getLocState() {
		return locState;
	}

	public void setLocState(String locState) {
		this.locState = locState;
	}

	public String getLocCity() {
		return locCity;
	}

	public void setLocCity(String locCity) {
		this.locCity = locCity;
	}

	public String getLocStreet() {
		return locStreet;
	}

	public void setLocStreet(String locStreet) {
		this.locStreet = locStreet;
	}

	public String getLocVirtual() {
		return locVirtual;
	}

	public void setLocVirtual(String locVirtual) {
		this.locVirtual = locVirtual;
	}

	public String getLocStatus() {
		return locStatus;
	}

	public void setLocStatus(String locStatus) {
		this.locStatus = locStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public List<DropDownModel> getStateList() {
		return stateList;
	}

	public void setStateList(List<DropDownModel> stateList) {
		this.stateList = stateList;
	}

	public List<DropDownModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<DropDownModel> cityList) {
		this.cityList = cityList;
	}

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public Integer getFloorSlNo() {
		return floorSlNo;
	}

	public void setFloorSlNo(Integer floorSlNo) {
		this.floorSlNo = floorSlNo;
	}

	public List<LocationSectionModel> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<LocationSectionModel> sectionList) {
		this.sectionList = sectionList;
	}

	public BigInteger getLocCount() {
		return locCount;
	}

	public void setLocCount(BigInteger locCount) {
		this.locCount = locCount;
	}

	public BigInteger getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(BigInteger floorCount) {
		this.floorCount = floorCount;
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
