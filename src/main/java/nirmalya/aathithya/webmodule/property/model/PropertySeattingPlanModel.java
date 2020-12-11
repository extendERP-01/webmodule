package nirmalya.aathithya.webmodule.property.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertySeattingPlanModel {
	private String seatingPlan;
	private String propertyCategory;
	private String planName;
	private String themeCategoryName;
	private String planlogo;
	private String planDescription;
	private Boolean planActive;
	private Date createdDate;
	private Date updatedDate;
	private String action;
	private String delete;
	private String stausName;
	private String createdBy;

	public PropertySeattingPlanModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PropertySeattingPlanModel(String seatingPlan, String propertyCategory, String planName, String themeCategoryName ,String planlogo,
			String planDescription, Boolean planActive, Date createdDate, Date updatedDate) {
		super();
		this.seatingPlan = seatingPlan;
		this.propertyCategory = propertyCategory;
		this.planName = planName;
		this.themeCategoryName=themeCategoryName;
		this.planlogo = planlogo;
		this.planDescription = planDescription;
		this.planActive = planActive;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public String getSeatingPlan() {
		return seatingPlan;
	}

	public void setSeatingPlan(String seatingPlan) {
		this.seatingPlan = seatingPlan;
	}

	public String getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanlogo() {
		return planlogo;
	}

	public void setPlanlogo(String planlogo) {
		this.planlogo = planlogo;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	public Boolean getPlanActive() {
		return planActive;
	}

	public void setPlanActive(Boolean planActive) {
		this.planActive = planActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	
	public String getThemeCategoryName() {
		return themeCategoryName;
	}

	public void setThemeCategoryName(String themeCategoryName) {
		this.themeCategoryName = themeCategoryName;
	}

	
	public String getStausName() {
		return stausName;
	}

	public void setStausName(String stausName) {
		this.stausName = stausName;
	}

	public String getCreatedBy() {
		return createdBy;
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
