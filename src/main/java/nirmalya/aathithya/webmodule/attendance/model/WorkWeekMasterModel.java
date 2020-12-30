/**
 * Defines work week master model
 *
 */
package nirmalya.aathithya.webmodule.attendance.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class WorkWeekMasterModel {

	private String tWorkDay;
	private String tWorkDayName;
	private String tWorkDayStatus;
	private String tCompanyId;
	private String tWorkDayCreatedBy;
	private String tWorkDayCreatedOn;
	private String status;
	private String action;

	public WorkWeekMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettWorkDay() {
		return tWorkDay;
	}

	public void settWorkDay(String tWorkDay) {
		this.tWorkDay = tWorkDay;
	}

	public String gettWorkDayName() {
		return tWorkDayName;
	}

	public void settWorkDayName(String tWorkDayName) {
		this.tWorkDayName = tWorkDayName;
	}

	public String gettWorkDayStatus() {
		return tWorkDayStatus;
	}

	public void settWorkDayStatus(String tWorkDayStatus) {
		this.tWorkDayStatus = tWorkDayStatus;
	}

	public String gettCompanyId() {
		return tCompanyId;
	}

	public void settCompanyId(String tCompanyId) {
		this.tCompanyId = tCompanyId;
	}

	public String gettWorkDayCreatedBy() {
		return tWorkDayCreatedBy;
	}

	public void settWorkDayCreatedBy(String tWorkDayCreatedBy) {
		this.tWorkDayCreatedBy = tWorkDayCreatedBy;
	}

	public String gettWorkDayCreatedOn() {
		return tWorkDayCreatedOn;
	}

	public void settWorkDayCreatedOn(String tWorkDayCreatedOn) {
		this.tWorkDayCreatedOn = tWorkDayCreatedOn;
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
