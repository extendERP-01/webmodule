package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BatchCodeModel {

	private String batchId;
	private String grnId;
	private String grn;
	private String gradeId;
	private String grade;
	private String thicknessId;
	private String thickness;
	private String pipeSizeId;
	private String pipeSize;
	private String batchCode;;
	private Double quantity;
	private String createdBy;
	private String action;

	public BatchCodeModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getGrnId() {
		return grnId;
	}

	public void setGrnId(String grnId) {
		this.grnId = grnId;
	}

	public String getGrn() {
		return grn;
	}

	public void setGrn(String grn) {
		this.grn = grn;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getThicknessId() {
		return thicknessId;
	}

	public void setThicknessId(String thicknessId) {
		this.thicknessId = thicknessId;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public String getPipeSizeId() {
		return pipeSizeId;
	}

	public void setPipeSizeId(String pipeSizeId) {
		this.pipeSizeId = pipeSizeId;
	}

	public String getPipeSize() {
		return pipeSize;
	}

	public void setPipeSize(String pipeSize) {
		this.pipeSize = pipeSize;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
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
