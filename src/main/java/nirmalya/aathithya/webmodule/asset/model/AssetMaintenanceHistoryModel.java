package nirmalya.aathithya.webmodule.asset.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetMaintenanceHistoryModel {

	private String assetCode;
	
	private Integer policyId;
	
	private String freqId;
	
	private String desc;
	
	private Double price;
	
	private String performedDate;
	
	private String createdBy;
	
	private String service;
	
	private String taskPerform;
	
	private String nextPerformDate;
	
	private Double gTotal;
	
	private String serviceType;
	
	private Double kmHr;
	
	private Double policyKmHr;

	public AssetMaintenanceHistoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public String getFreqId() {
		return freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPerformedDate() {
		return performedDate;
	}

	public void setPerformedDate(String performedDate) {
		this.performedDate = performedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTaskPerform() {
		return taskPerform;
	}

	public void setTaskPerform(String taskPerform) {
		this.taskPerform = taskPerform;
	}

	public String getNextPerformDate() {
		return nextPerformDate;
	}

	public void setNextPerformDate(String nextPerformDate) {
		this.nextPerformDate = nextPerformDate;
	}

	public Double getgTotal() {
		return gTotal;
	}

	public void setgTotal(Double gTotal) {
		this.gTotal = gTotal;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Double getKmHr() {
		return kmHr;
	}

	public void setKmHr(Double kmHr) {
		this.kmHr = kmHr;
	}

	public Double getPolicyKmHr() {
		return policyKmHr;
	}

	public void setPolicyKmHr(Double policyKmHr) {
		this.policyKmHr = policyKmHr;
	}

	@Override
	public String toString() {
		ObjectMapper  mapperObj=new ObjectMapper();
		String jsonStr;
		try{
			jsonStr=mapperObj.writeValueAsString(this);
		}catch(IOException ex){
			
			jsonStr=ex.toString();
		}
		return jsonStr;
	}
}
