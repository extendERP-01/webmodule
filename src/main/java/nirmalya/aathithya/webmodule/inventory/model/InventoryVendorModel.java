/*
*Class showing Vendor Entity
 * 
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InventoryVendorModel {
	private String vendor;
	private String[] itemCategory;
	private String vendorName;
	private String vendorDescription;
	private String vendorAddress;
	private String vendorGSTNO;
	private String vendorTINNO;
	private String vendorBankAc;
	private String vendorIFSC;
	private String vendorEmail;
	private String vendorPhone;
	private String vendorMobile;
	private Integer vendorPaymentDays;
	private Boolean vendorActive;
	private String causeOfInactive;
	private String createdBy;
	private String status;
	private String action;
	private String delete;
	private String vendorAddress2;
	private String vendorAddress3;
	private String vendorContactPerson;
	private String vendorPAN;
	private String vendorZipCode;
	private String vendorCity;
	private String vendorVAT;
	private String vendorServiceTax;
	private String vendorCountry;
	private String vendorState;
	private Integer vendorRate;
	private String vendorContract;
	private String fromDate;
	private String toDate;

	public InventoryVendorModel() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String[] getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String[] itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorDescription() {
		return vendorDescription;
	}

	public void setVendorDescription(String vendorDescription) {
		this.vendorDescription = vendorDescription;
	}



	public String getVendorAddress() {
		return vendorAddress;
	}



	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getVendorGSTNO() {
		return vendorGSTNO;
	}

	public void setVendorGSTNO(String vendorGSTNO) {
		this.vendorGSTNO = vendorGSTNO;
	}

	public String getVendorTINNO() {
		return vendorTINNO;
	}

	public void setVendorTINNO(String vendorTINNO) {
		this.vendorTINNO = vendorTINNO;
	}

	public String getVendorBankAc() {
		return vendorBankAc;
	}

	public void setVendorBankAc(String vendorBankAc) {
		this.vendorBankAc = vendorBankAc;
	}

	public String getVendorIFSC() {
		return vendorIFSC;
	}



	public void setVendorIFSC(String vendorIFSC) {
		this.vendorIFSC = vendorIFSC;
	}



	public String getVendorEmail() {
		return vendorEmail;
	}



	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}



	public String getVendorPhone() {
		return vendorPhone;
	}



	public void setVendorPhone(String vendorPhone) {
		this.vendorPhone = vendorPhone;
	}



	public String getVendorMobile() {
		return vendorMobile;
	}



	public void setVendorMobile(String vendorMobile) {
		this.vendorMobile = vendorMobile;
	}



	public Integer getVendorPaymentDays() {
		return vendorPaymentDays;
	}



	public void setVendorPaymentDays(Integer vendorPaymentDays) {
		this.vendorPaymentDays = vendorPaymentDays;
	}



	public Boolean getVendorActive() {
		return vendorActive;
	}



	public void setVendorActive(Boolean vendorActive) {
		this.vendorActive = vendorActive;
	}

	public String getCauseOfInactive() {
		return causeOfInactive;
	}

	public void setCauseOfInactive(String causeOfInactive) {
		this.causeOfInactive = causeOfInactive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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



	public String getVendorAddress2() {
		return vendorAddress2;
	}


	public void setVendorAddress2(String vendorAddress2) {
		this.vendorAddress2 = vendorAddress2;
	}


	public String getVendorAddress3() {
		return vendorAddress3;
	}


	public void setVendorAddress3(String vendorAddress3) {
		this.vendorAddress3 = vendorAddress3;
	}


	public String getVendorContactPerson() {
		return vendorContactPerson;
	}


	public void setVendorContactPerson(String vendorContactPerson) {
		this.vendorContactPerson = vendorContactPerson;
	}


	public String getVendorPAN() {
		return vendorPAN;
	}


	public void setVendorPAN(String vendorPAN) {
		this.vendorPAN = vendorPAN;
	}


	public String getVendorZipCode() {
		return vendorZipCode;
	}


	public void setVendorZipCode(String vendorZipCode) {
		this.vendorZipCode = vendorZipCode;
	}


	public String getVendorCity() {
		return vendorCity;
	}


	public void setVendorCity(String vendorCity) {
		this.vendorCity = vendorCity;
	}


	public String getVendorVAT() {
		return vendorVAT;
	}


	public void setVendorVAT(String vendorVAT) {
		this.vendorVAT = vendorVAT;
	}


	public String getVendorServiceTax() {
		return vendorServiceTax;
	}


	public void setVendorServiceTax(String vendorServiceTax) {
		this.vendorServiceTax = vendorServiceTax;
	}


	public String getVendorCountry() {
		return vendorCountry;
	}


	public void setVendorCountry(String vendorCountry) {
		this.vendorCountry = vendorCountry;
	}


	public String getVendorState() {
		return vendorState;
	}


	public void setVendorState(String vendorState) {
		this.vendorState = vendorState;
	}

	public Integer getVendorRate() {
		return vendorRate;
	}

	public void setVendorRate(Integer vendorRate) {
		this.vendorRate = vendorRate;
	}

	public String getVendorContract() {
		return vendorContract;
	}


	public void setVendorContract(String vendorContract) {
		this.vendorContract = vendorContract;
	}


	public String getFromDate() {
		return fromDate;
	}


	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}


	public String getToDate() {
		return toDate;
	}


	public void setToDate(String toDate) {
		this.toDate = toDate;
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
