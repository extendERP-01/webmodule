package nirmalya.aathithya.webmodule.production.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MotherCoilSlitModel {

	private String tMotherCoilBatch;
	private String tMotherCoilThickness;
	private String tMotherCoilGrade;
	private String tPipeSlitBatch;
	private Double tPipeScrapWeight;
	private String tPipeSlitStartDate;
	private String tPipeSlitEndDate;
	private String tPipeSlitWidth;
	private Double tPipeSlitQty;
	private String tPipeSize;
	private String tPipeCreatedBy;
	private String action;
	private String batchId;
	private String editId;
	private String slitSubGroup;
	private Byte status;
	private String tMotherCoilThicknessName;
	private String tMotherCoilGradeName;
	private String tPipeSizeName;
	private String tPipeSlitWidthName;

	public MotherCoilSlitModel() {
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

	public Double gettPipeScrapWeight() {
		return tPipeScrapWeight;
	}

	public void settPipeScrapWeight(Double tPipeScrapWeight) {
		this.tPipeScrapWeight = tPipeScrapWeight;
	}

	public String gettPipeSlitStartDate() {
		return tPipeSlitStartDate;
	}

	public void settPipeSlitStartDate(String tPipeSlitStartDate) {
		this.tPipeSlitStartDate = tPipeSlitStartDate;
	}

	public String gettPipeSlitEndDate() {
		return tPipeSlitEndDate;
	}

	public void settPipeSlitEndDate(String tPipeSlitEndDate) {
		this.tPipeSlitEndDate = tPipeSlitEndDate;
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

	public String gettPipeSize() {
		return tPipeSize;
	}

	public void settPipeSize(String tPipeSize) {
		this.tPipeSize = tPipeSize;
	}

	public String gettPipeCreatedBy() {
		return tPipeCreatedBy;
	}

	public void settPipeCreatedBy(String tPipeCreatedBy) {
		this.tPipeCreatedBy = tPipeCreatedBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getSlitSubGroup() {
		return slitSubGroup;
	}

	public void setSlitSubGroup(String slitSubGroup) {
		this.slitSubGroup = slitSubGroup;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String gettMotherCoilThicknessName() {
		return tMotherCoilThicknessName;
	}

	public String gettMotherCoilGradeName() {
		return tMotherCoilGradeName;
	}

	public void settMotherCoilThicknessName(String tMotherCoilThicknessName) {
		this.tMotherCoilThicknessName = tMotherCoilThicknessName;
	}

	public void settMotherCoilGradeName(String tMotherCoilGradeName) {
		this.tMotherCoilGradeName = tMotherCoilGradeName;
	}

	public String gettPipeSizeName() {
		return tPipeSizeName;
	}

	public void settPipeSizeName(String tPipeSizeName) {
		this.tPipeSizeName = tPipeSizeName;
	}

	public String gettPipeSlitWidthName() {
		return tPipeSlitWidthName;
	}

	public void settPipeSlitWidthName(String tPipeSlitWidthName) {
		this.tPipeSlitWidthName = tPipeSlitWidthName;
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
