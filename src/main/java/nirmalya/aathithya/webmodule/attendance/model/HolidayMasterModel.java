/**
 * Defines work holiday master model

 *
 */
package nirmalya.aathithya.webmodule.attendance.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class HolidayMasterModel {

	private String tHoliday;
	private String tHolidayName;
	private String tHolidayFromDate;
	private String tHolidayToDate;
	private String tHolidayStatus;
	private String tCompanyId;
	private String tHolidayCreatedBy;
	private String tHolidayCreatedOn;
	private String status;
	private String action;

	public HolidayMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettHoliday() {
		return tHoliday;
	}

	public void settHoliday(String tHoliday) {
		this.tHoliday = tHoliday;
	}

	public String gettHolidayName() {
		return tHolidayName;
	}

	public void settHolidayName(String tHolidayName) {
		this.tHolidayName = tHolidayName;
	}

	public String gettHolidayFromDate() {
		return tHolidayFromDate;
	}

	public void settHolidayFromDate(String tHolidayFromDate) {
		this.tHolidayFromDate = tHolidayFromDate;
	}

	public String gettHolidayToDate() {
		return tHolidayToDate;
	}

	public void settHolidayToDate(String tHolidayToDate) {
		this.tHolidayToDate = tHolidayToDate;
	}

	public String gettHolidayStatus() {
		return tHolidayStatus;
	}

	public void settHolidayStatus(String tHolidayStatus) {
		this.tHolidayStatus = tHolidayStatus;
	}

	public String gettCompanyId() {
		return tCompanyId;
	}

	public void settCompanyId(String tCompanyId) {
		this.tCompanyId = tCompanyId;
	}

	public String gettHolidayCreatedBy() {
		return tHolidayCreatedBy;
	}

	public void settHolidayCreatedBy(String tHolidayCreatedBy) {
		this.tHolidayCreatedBy = tHolidayCreatedBy;
	}

	public String gettHolidayCreatedOn() {
		return tHolidayCreatedOn;
	}

	public void settHolidayCreatedOn(String tHolidayCreatedOn) {
		this.tHolidayCreatedOn = tHolidayCreatedOn;
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
