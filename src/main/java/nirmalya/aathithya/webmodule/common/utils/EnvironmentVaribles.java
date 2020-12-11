package nirmalya.aathithya.webmodule.common.utils;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Nirmalya Labs
 *
 */
public class EnvironmentVaribles {

	@Value("${service.url.recruitment}")
	private String recruitment;
	
	@Value("${service.url.planning}")
	private String planningUrl;
	
	@Value("${service.url.user}")
	private String userUrl;

	@Value("${service.url.restaurant}")
	private String restaurantUrl;

	@Value("${service.url.property}")
	private String propertyUrl;

	@Value("${service.url.maintenance}")
	private String maintenanceUrl;

	@Value("${service.url.gatepass}")
	private String gatepassUrl;

	@Value("${service.url.inventory}")
	private String inventoryUrl;

	@Value("${service.url.housekeeping}")
	private String housekeepingUrl;

	@Value("${service.url.frontdesk}")
	private String frontdeskUrl;

	@Value("${service.url.master}")
	private String masterUrl;

	@Value("${service.url.laundry}")
	private String laundryUrl;

	@Value("${service.url.gym}")
	private String gymUrl;

	@Value("${service.url.roomservice}")
	private String roomserviceUrl;

	@Value("${service.url.otherservices}")
	private String otherservicesUrl;

	@Value("${service.url.kitchen}")
	private String kitchenUrl;

	@Value("${service.url.asset}")
	private String assetUrl;

	@Value("${service.url.member}")
	private String memberUrl;

	@Value("${service.url.spa}")
	private String spaUrl;

	@Value("${service.url.beautyparlour}")
	private String beautyparlourUrl;

	@Value("${service.url.parking}")
	private String parkingUrl;

	@Value("${service.url.saloon}")
	private String saloonUrl;

	@Value("${service.url.sales}")
	private String salesUrl;

	@Value("${service.url.account}")
	private String accountUrl;

	@Value("${service.url.reimbursement}")
	private String reimbursementUrl;

	@Value("${service.url.employee}")
	private String employeeUrl;

	@Value("${service.url.nightclub}")
	private String nightclubUrl;

	@Value("${service.url.document}")
	private String documentUrl;

	@Value("${service.url.leave}")
	private String leaveUrl;

	@Value("${service.url.attendance}")
	private String attendanceUrl;

	@Value("${service.url.fileUpload-laundry}")
	private String fileUploadLaundry;

	@Value("${service.url.fileUpload-restaurant}")
	private String fileUploadRestaurant;

	@Value("${service.url.fileUpload-hotel}")
	private String fileUploadHotel;

	@Value("${service.url.fileUpload-property}")
	private String fileUploadProperty;

	@Value("${service.url.fileUpload-sales}")
	private String fileUploadSales;

	@Value("${service.url.fileUpload-inventory}")
	private String fileUploadInventory;

	@Value("${service.url.fileUpload-beautyparlour}")
	private String fileUploadBParlour;

	@Value("${service.url.fileUpload-master}")
	private String fileUploadMaster;

	@Value("${service.url.baseURL}")
	private String baseURL;

	@Value("${service.url.hrms}")
	private String hrmsUrl;

	@Value("${service.url.fileUpload-uploadEmployee}")
	private String fileUploadEmployee;

	@Value("${service.url.fileUpload-reimbursement}")
	private String fileUploadReimbursementUrl;

	@Value("${service.url.fileUpload-document}")
	private String fileUploadDocumenttUrl;

	@Value("${service.url.fileUpload-store}")
	private String fileUploadStoreUrl;

	@Value("${service.url.qualitycontrol}")
	private String qualitycontrol;

	@Value("${service.url.production}")
	private String production;

	@Value("${service.url.challan}")
	private String challan;

	@Value("${service.url.gatePass}")
	private String gatePass;
	
	@Value("${service.url.fileUpload-uploadItem}")
	private String fileUploadItem;
	
	@Value("${service.url.barcodes}")
	private String barcodesUpload;
	
	public String getBarcodesUpload() {
		return barcodesUpload;
	}

	public String getFileUploadStoreUrl() {
		return fileUploadStoreUrl;
	}

	public String getFileUploadDocumenttUrl() {
		return fileUploadDocumenttUrl;
	}

	public EnvironmentVaribles() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserUrl() {
		return userUrl;
	}

	public String getRestaurantUrl() {
		return restaurantUrl;
	}

	public String getPropertyUrl() {
		return propertyUrl;
	}

	public String getMaintenanceUrl() {
		return maintenanceUrl;
	}

	public String getGatepassUrl() {
		return gatepassUrl;
	}

	public String getFileUploadRestaurant() {
		return fileUploadRestaurant;
	}

	public String getInventoryUrl() {
		return inventoryUrl;
	}

	public String getHousekeepingUrl() {
		return housekeepingUrl;
	}

	public String getFrontdeskUrl() {
		return frontdeskUrl;
	}

	public String getLaundryUrl() {
		return laundryUrl;
	}

	public String getGymUrl() {
		return gymUrl;
	}

	public String getRoomserviceUrl() {
		return roomserviceUrl;
	}

	public String getOtherservicesUrl() {
		return otherservicesUrl;
	}

	public String getKitchenUrl() {
		return kitchenUrl;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	public String getMemberUrl() {
		return memberUrl;
	}

	public String getSpaUrl() {
		return spaUrl;
	}

	public String getBeautyparlourUrl() {
		return beautyparlourUrl;
	}

	public String getParkingUrl() {
		return parkingUrl;
	}

	public String getSaloonUrl() {
		return saloonUrl;
	}

	public String getSalesUrl() {
		return salesUrl;
	}

	public String getAccountUrl() {
		return accountUrl;
	}

	public String getReimbursementUrl() {
		return reimbursementUrl;
	}

	public String getLeaveUrl() {
		return leaveUrl;
	}

	public String getAttendanceUrl() {
		return attendanceUrl;
	}

	public String getFileUploadLaundry() {
		return fileUploadLaundry;
	}

	public String getfileUploadRestaurant() {
		return fileUploadRestaurant;
	}

	public String getFileUploadHotel() {
		return fileUploadHotel;
	}

	public String getFileUploadProperty() {
		return fileUploadProperty;
	}

	public String getMasterUrl() {
		return masterUrl;
	}

	public String getFileUploadMaster() {
		return fileUploadMaster;
	}

	public String getFileUploadSales() {
		return fileUploadSales;
	}

	public String getFileUploadInventory() {
		return fileUploadInventory;
	}

	public String getFileUploadBParlour() {
		return fileUploadBParlour;
	}

	public String getBaseUrlPath() {
		return baseURL;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getHrmsUrl() {
		return hrmsUrl;
	}

	public String getFileUploadEmployee() {
		return fileUploadEmployee;
	}

	public String getFileUploadItem() {
		return fileUploadItem;
	}
	
	public String getFileUploadReimbursementUrl() {
		return fileUploadReimbursementUrl;
	}

	public String getEmployeeUrl() {
		// TODO Auto-generated method stub
		return employeeUrl;
	}

	public String getQualitycontrol() {
		return qualitycontrol;
	}

	public String getProduction() {
		return production;
	}

	public String getNightclubUrl() {
		return nightclubUrl;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public String getChallan() {
		return challan;
	}

	public String getFileUploadGatePassUrl() {
		return gatePass;
	}

	public String getPlanningUrl() {
		return planningUrl;
	}

	public String getGatePass() {
		return gatePass;
	}

	public String getRecruitment() {
		return recruitment;
	}
}
