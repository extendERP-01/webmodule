package nirmalya.aathithya.webmodule.master.model;

import java.util.ArrayList;
import java.util.List;

public class ZoneMasterModel {
private String zoneId;
private String warehouseId;
private String zoneCode;
private String zoneName;
private String createdBy;
private Integer zoneSlNo;
private List<ZoneRackModel> rackList = new ArrayList<ZoneRackModel>();
public ZoneMasterModel() {
	super();
	// TODO Auto-generated constructor stub
}
public String getZoneId() {
	return zoneId;
}

public String getZoneCode() {
	return zoneCode;
}

public String getWarehouseId() {
	return warehouseId;
}
public void setWarehouseId(String warehouseId) {
	this.warehouseId = warehouseId;
}
public void setZoneCode(String zoneCode) {
	this.zoneCode = zoneCode;
}
public void setZoneId(String zoneId) {
	this.zoneId = zoneId;
}
public String getZoneName() {
	return zoneName;
}
public void setZoneName(String zoneName) {
	this.zoneName = zoneName;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public Integer getZoneSlNo() {
	return zoneSlNo;
}
public void setZoneSlNo(Integer zoneSlNo) {
	this.zoneSlNo = zoneSlNo;
}
public List<ZoneRackModel> getRackList() {
	return rackList;
}
public void setRackList(List<ZoneRackModel> rackList) {
	this.rackList = rackList;
}


}
