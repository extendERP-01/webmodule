package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * @author NirmalyaLabs
 *
 *
 */
public class InventoryStockReportModel {
	private String costCenter;
	private String item;
	private String itemSubCat;
	private Double reqQuantity;
	private Double recvQuantity;
	private Double issueQuantity;
	private Double avlblQuantity;
	private String store;
	private String godown;
	private String curDate;
	private String printedBy;
	private String status;
	private String action;
	
	public InventoryStockReportModel() {
		super();
		 
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getGodown() {
		return godown;
	}
	public void setGodown(String godown) {
		this.godown = godown;
	}
		
	public String getCurDate() {
		return curDate;
	}
	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
	public String getPrintedBy() {
		return printedBy;
	}
	public void setPrintedBy(String printedBy) {
		this.printedBy = printedBy;
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
	
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public String getItemSubCat() {
		return itemSubCat;
	}
	public void setItemSubCat(String itemSubCat) {
		this.itemSubCat = itemSubCat;
	}
	
	public Double getReqQuantity() {
		return reqQuantity;
	}
	public void setReqQuantity(Double reqQuantity) {
		this.reqQuantity = reqQuantity;
	}
	public Double getRecvQuantity() {
		return recvQuantity;
	}
	public void setRecvQuantity(Double recvQuantity) {
		this.recvQuantity = recvQuantity;
	}
	public Double getIssueQuantity() {
		return issueQuantity;
	}
	public void setIssueQuantity(Double issueQuantity) {
		this.issueQuantity = issueQuantity;
	}
	public Double getAvlblQuantity() {
		return avlblQuantity;
	}
	public void setAvlblQuantity(Double avlblQuantity) {
		this.avlblQuantity = avlblQuantity;
	}
	
	
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
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
