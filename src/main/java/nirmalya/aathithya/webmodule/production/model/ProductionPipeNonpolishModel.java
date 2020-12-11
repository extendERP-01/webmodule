package nirmalya.aathithya.webmodule.production.model;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductionPipeNonpolishModel {
	private String tMotherCoilBatch;
	private String tMotherCoilThickness;
	private String tMotherCoilGrade;
	private String tPipeSlitBatch;
	private String tPipeSlitWidth;
	private Double tPipeSlitQty;
	private String slitSubGroup;
	private String tPipeCreatedBy;
	private String tPipeSize;
	private Double productionQty;
	private Double productionWt;
	private Double polishingQty;
	private Double polishingWt;
	private Double nonPolishingQty;
	private Double nonPolishingWt;
	
	public ProductionPipeNonpolishModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String gettMotherCoilBatch() {
		return tMotherCoilBatch;
	}
	public void settMotherCoilBatch(String tMotherCoilBatch) {
		this.tMotherCoilBatch = tMotherCoilBatch;
	}
	public String gettMotherCoilThickness() {
		return tMotherCoilThickness;
	}
	public void settMotherCoilThickness(String tMotherCoilThickness) {
		this.tMotherCoilThickness = tMotherCoilThickness;
	}
	public String gettMotherCoilGrade() {
		return tMotherCoilGrade;
	}
	public void settMotherCoilGrade(String tMotherCoilGrade) {
		this.tMotherCoilGrade = tMotherCoilGrade;
	}
	public String gettPipeSlitBatch() {
		return tPipeSlitBatch;
	}
	public void settPipeSlitBatch(String tPipeSlitBatch) {
		this.tPipeSlitBatch = tPipeSlitBatch;
	}
	public String gettPipeSlitWidth() {
		return tPipeSlitWidth;
	}
	public void settPipeSlitWidth(String tPipeSlitWidth) {
		this.tPipeSlitWidth = tPipeSlitWidth;
	}
	public Double gettPipeSlitQty() {
		return tPipeSlitQty;
	}
	public void settPipeSlitQty(Double tPipeSlitQty) {
		this.tPipeSlitQty = tPipeSlitQty;
	}
	public String getSlitSubGroup() {
		return slitSubGroup;
	}
	public void setSlitSubGroup(String slitSubGroup) {
		this.slitSubGroup = slitSubGroup;
	}
	public String gettPipeCreatedBy() {
		return tPipeCreatedBy;
	}
	public void settPipeCreatedBy(String tPipeCreatedBy) {
		this.tPipeCreatedBy = tPipeCreatedBy;
	}
	public String gettPipeSize() {
		return tPipeSize;
	}
	public void settPipeSize(String tPipeSize) {
		this.tPipeSize = tPipeSize;
	}
	public Double getProductionQty() {
		return productionQty;
	}
	public void setProductionQty(Double productionQty) {
		this.productionQty = productionQty;
	}
	public Double getProductionWt() {
		return productionWt;
	}
	public void setProductionWt(Double productionWt) {
		this.productionWt = productionWt;
	}
	public Double getPolishingQty() {
		return polishingQty;
	}
	public void setPolishingQty(Double polishingQty) {
		this.polishingQty = polishingQty;
	}
	public Double getPolishingWt() {
		return polishingWt;
	}
	public void setPolishingWt(Double polishingWt) {
		this.polishingWt = polishingWt;
	}
	public Double getNonPolishingQty() {
		return nonPolishingQty;
	}
	public void setNonPolishingQty(Double nonPolishingQty) {
		this.nonPolishingQty = nonPolishingQty;
	}
	public Double getNonPolishingWt() {
		return nonPolishingWt;
	}
	public void setNonPolishingWt(Double nonPolishingWt) {
		this.nonPolishingWt = nonPolishingWt;
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
