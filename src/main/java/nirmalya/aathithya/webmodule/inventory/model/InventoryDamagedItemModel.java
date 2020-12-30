package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class InventoryDamagedItemModel {

	private Integer tDamagedItemId;
	private String tItem;
	private String tItemSlNo;
	private String tDamagedDesc;
	private Boolean tStatus;
	private Double tItemReSaleValue;
	private String tVendor;
	private String tCreatedBy;
	private String status;
	private String action;

	public InventoryDamagedItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer gettDamagedItemId() {
		return tDamagedItemId;
	}

	public void settDamagedItemId(Integer tDamagedItemId) {
		this.tDamagedItemId = tDamagedItemId;
	}

	public String gettItem() {
		return tItem;
	}

	public void settItem(String tItem) {
		this.tItem = tItem;
	}

	public String gettItemSlNo() {
		return tItemSlNo;
	}

	public void settItemSlNo(String tItemSlNo) {
		this.tItemSlNo = tItemSlNo;
	}

	public String gettDamagedDesc() {
		return tDamagedDesc;
	}

	public void settDamagedDesc(String tDamagedDesc) {
		this.tDamagedDesc = tDamagedDesc;
	}

	public Boolean gettStatus() {
		return tStatus;
	}

	public void settStatus(Boolean tStatus) {
		this.tStatus = tStatus;
	}

	public Double gettItemReSaleValue() {
		return tItemReSaleValue;
	}

	public void settItemReSaleValue(Double tItemReSaleValue) {
		this.tItemReSaleValue = tItemReSaleValue;
	}

	public String gettVendor() {
		return tVendor;
	}

	public void settVendor(String tVendor) {
		this.tVendor = tVendor;
	}

	public String gettCreatedBy() {
		return tCreatedBy;
	}

	public void settCreatedBy(String tCreatedBy) {
		this.tCreatedBy = tCreatedBy;
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
