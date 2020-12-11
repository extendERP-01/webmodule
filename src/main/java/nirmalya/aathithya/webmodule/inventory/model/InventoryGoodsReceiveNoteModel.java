/**
 * 
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author USER
 *
 */
public class InventoryGoodsReceiveNoteModel {

	private String gRNInvoiceId;
	private String gRNPurchaseOrderId;
	private String gRNItmId;
	private String gRnInvoiceNumber;
	private Float gRnInvoicePrice;
	private Float gRnInvoiceQuantity;
	private String gRnInvoiceDescription;
	private Boolean gRnInvoiceActive;
	private Date gRnCreatedOn;
	private Date gRnUpdatedOn;
	private String action;
	private String status;
	private String itmCategory;
	private String itmSubCategory;
	private String gRnInvoiceItmName;
	private Float total;
	private Double gTotal;
	private String vehicleNo;
	private String driverName;
	private Integer mobileNo;
	private String rembFile;
	public InventoryGoodsReceiveNoteModel() {
		super();
		// TODO Auto-generated constructor stub
	}



	public InventoryGoodsReceiveNoteModel(String gRNInvoiceId, String gRNPurchaseOrderId, String gRNItmId,
			String gRnInvoiceNumber, Float gRnInvoicePrice, Float gRnInvoiceQuantity, String gRnInvoiceDescription,
			Boolean gRnInvoiceActive, Date gRnCreatedOn, Date gRnUpdatedOn, String action, String status,
			String itmCategory, String itmSubCategory, String gRnInvoiceItmName,Float total,Double gTotal,
			String vehicleNo,String  driverName,Integer mobileNo ) {
		super();
		this.gRNInvoiceId = gRNInvoiceId;
		this.gRNPurchaseOrderId = gRNPurchaseOrderId;
		this.gRNItmId = gRNItmId;
		this.gRnInvoiceNumber = gRnInvoiceNumber;
		this.gRnInvoicePrice = gRnInvoicePrice;
		this.gRnInvoiceQuantity = gRnInvoiceQuantity;
		this.gRnInvoiceDescription = gRnInvoiceDescription;
		this.gRnInvoiceActive = gRnInvoiceActive;
		this.gRnCreatedOn = gRnCreatedOn;
		this.gRnUpdatedOn = gRnUpdatedOn;
		this.action = action;
		this.status = status;
		this.itmCategory = itmCategory;
		this.itmSubCategory = itmSubCategory;
		this.gRnInvoiceItmName = gRnInvoiceItmName;
		this.total = total;
		this.gTotal = gTotal;
		this.vehicleNo = vehicleNo;
		this.driverName = driverName;
		this.mobileNo = mobileNo;
	}



	public String getRembFile() {
		return rembFile;
	}



	public void setRembFile(String rembFile) {
		this.rembFile = rembFile;
	}



	public String getgRNInvoiceId() {
		return gRNInvoiceId;
	}


	public void setgRNInvoiceId(String gRNInvoiceId) {
		this.gRNInvoiceId = gRNInvoiceId;
	}


	public String getgRNPurchaseOrderId() {
		return gRNPurchaseOrderId;
	}


	public void setgRNPurchaseOrderId(String gRNPurchaseOrderId) {
		this.gRNPurchaseOrderId = gRNPurchaseOrderId;
	}


	public String getgRNItmId() {
		return gRNItmId;
	}


	public void setgRNItmId(String gRNItmId) {
		this.gRNItmId = gRNItmId;
	}


	public String getVehicleNo() {
		return vehicleNo;
	}



	public String getDriverName() {
		return driverName;
	}



	public Integer getMobileNo() {
		return mobileNo;
	}



	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}



	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}



	public void setMobileNo(Integer mobileNo) {
		this.mobileNo = mobileNo;
	}



	public String getgRnInvoiceNumber() {
		return gRnInvoiceNumber;
	}


	public void setgRnInvoiceNumber(String gRnInvoiceNumber) {
		this.gRnInvoiceNumber = gRnInvoiceNumber;
	}


	public Float getgRnInvoicePrice() {
		return gRnInvoicePrice;
	}


	public void setgRnInvoicePrice(Float gRnInvoicePrice) {
		this.gRnInvoicePrice = gRnInvoicePrice;
	}


	public Float getgRnInvoiceQuantity() {
		return gRnInvoiceQuantity;
	}


	public void setgRnInvoiceQuantity(Float gRnInvoiceQuantity) {
		this.gRnInvoiceQuantity = gRnInvoiceQuantity;
	}


	public String getgRnInvoiceDescription() {
		return gRnInvoiceDescription;
	}


	public void setgRnInvoiceDescription(String gRnInvoiceDescription) {
		this.gRnInvoiceDescription = gRnInvoiceDescription;
	}


	public Boolean getgRnInvoiceActive() {
		return gRnInvoiceActive;
	}


	public void setgRnInvoiceActive(Boolean gRnInvoiceActive) {
		this.gRnInvoiceActive = gRnInvoiceActive;
	}


	public Date getgRnCreatedOn() {
		return gRnCreatedOn;
	}


	public void setgRnCreatedOn(Date gRnCreatedOn) {
		this.gRnCreatedOn = gRnCreatedOn;
	}


	public Date getgRnUpdatedOn() {
		return gRnUpdatedOn;
	}


	public void setgRnUpdatedOn(Date gRnUpdatedOn) {
		this.gRnUpdatedOn = gRnUpdatedOn;
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


	public String getItmCategory() {
		return itmCategory;
	}


	public void setItmCategory(String itmCategory) {
		this.itmCategory = itmCategory;
	}


	public String getItmSubCategory() {
		return itmSubCategory;
	}


	public void setItmSubCategory(String itmSubCategory) {
		this.itmSubCategory = itmSubCategory;
	}


	public String getgRnInvoiceItmName() {
		return gRnInvoiceItmName;
	}


	public void setgRnInvoiceItmName(String gRnInvoiceItmName) {
		this.gRnInvoiceItmName = gRnInvoiceItmName;
	}

	

	public Float getTotal() {
		return total;
	}



	public void setTotal(Float total) {
		this.total = total;
	}


	

	public Double getgTotal() {
		return gTotal;
	}



	public void setgTotal(Double gTotal) {
		this.gTotal = gTotal;
	}



	@Override
	public String toString()
	{
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonStr;
		try 
		{
			jsonStr = mapperObj.writeValueAsString(this);
		} catch (IOException ex)
		{

			jsonStr = ex.toString();
		}
		return jsonStr;

	}
}
