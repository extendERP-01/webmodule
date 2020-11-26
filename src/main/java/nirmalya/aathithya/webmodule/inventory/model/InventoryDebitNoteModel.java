/**
 * 
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author USER
 *
 */
public class InventoryDebitNoteModel {
	private String debitNote;
	private String orderNo;
	private String invoiceNo;
	private String costCenter;
	private String vendor;
	private String organization;
	private String goodsReturnNote;
	private String description;
	private String noteDate;
	private Double subTotal;	
	private Boolean dActive;
	private String createdOn;
	private String createdBy;
	private String status;
	public String getDebitNote() {
		return debitNote;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public String getVendor() {
		return vendor;
	}

	public String getOrganization() {
		return organization;
	}

	public String getGoodsReturnNote() {
		return goodsReturnNote;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public String getNoteDate() {
		return noteDate;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public Boolean getdActive() {
		return dActive;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setDebitNote(String debitNote) {
		this.debitNote = debitNote;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setGoodsReturnNote(String goodsReturnNote) {
		this.goodsReturnNote = goodsReturnNote;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setNoteDate(String noteDate) {
		this.noteDate = noteDate;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public void setdActive(Boolean dActive) {
		this.dActive = dActive;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
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
