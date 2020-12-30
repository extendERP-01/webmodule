package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LocationRoomModel {

	private String locationId;
	private String floorId;
	private String sectionId;
	private String roomId;
	private String locationCode;
	private String floorCode;
	private String sectionCode;
	private String roomCode;
	private String floorName;
	private String sectionName;
	private String roomName;
	private Integer floorSlNo;
	private Integer sectionSlNO;
	private Integer roomSlNO;
	private String roomType;
	private String createdBy;
	private List<LocationRoomModel> roomList = new ArrayList<LocationRoomModel>();
	private Integer roomCount;
	private Integer floorCount;
	
	public LocationRoomModel() {
		super();
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getFloorCode() {
		return floorCode;
	}

	public void setFloorCode(String floorCode) {
		this.floorCode = floorCode;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Integer getFloorSlNo() {
		return floorSlNo;
	}

	public void setFloorSlNo(Integer floorSlNo) {
		this.floorSlNo = floorSlNo;
	}

	public Integer getSectionSlNO() {
		return sectionSlNO;
	}

	public void setSectionSlNO(Integer sectionSlNO) {
		this.sectionSlNO = sectionSlNO;
	}

	public Integer getRoomSlNO() {
		return roomSlNO;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public void setRoomSlNO(Integer roomSlNO) {
		this.roomSlNO = roomSlNO;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public List<LocationRoomModel> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<LocationRoomModel> roomList) {
		this.roomList = roomList;
	}

	public Integer getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}

	public Integer getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(Integer floorCount) {
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
