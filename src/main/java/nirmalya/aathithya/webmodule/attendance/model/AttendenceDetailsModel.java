/**
 * Defines attendence details model
 * 
 * 
 */
package nirmalya.aathithya.webmodule.attendance.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class AttendenceDetailsModel {

	private String tEmployee;
	private String tAttndncDate;
	private String tAttndncPunchIn;
	private Integer tAttndncPunchInLoc;
	private String tAttndncPunchInNote;
	private String tAttndncPunchOut;
	private String tAttndncPunchOutNote;
	private Integer tAttndncPunchOut_Loc;
	private String tAttndncCreatedOn;
	private String tAttndncCreatedBy;
	private String status;
	private String action;

	public AttendenceDetailsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettEmployee() {
		return tEmployee;
	}

	public void settEmployee(String tEmployee) {
		this.tEmployee = tEmployee;
	}

	public String gettAttndncDate() {
		return tAttndncDate;
	}

	public void settAttndncDate(String tAttndncDate) {
		this.tAttndncDate = tAttndncDate;
	}

	public String gettAttndncPunchIn() {
		return tAttndncPunchIn;
	}

	public void settAttndncPunchIn(String tAttndncPunchIn) {
		this.tAttndncPunchIn = tAttndncPunchIn;
	}

	public Integer gettAttndncPunchInLoc() {
		return tAttndncPunchInLoc;
	}

	public void settAttndncPunchInLoc(Integer tAttndncPunchInLoc) {
		this.tAttndncPunchInLoc = tAttndncPunchInLoc;
	}

	public String gettAttndncPunchInNote() {
		return tAttndncPunchInNote;
	}

	public void settAttndncPunchInNote(String tAttndncPunchInNote) {
		this.tAttndncPunchInNote = tAttndncPunchInNote;
	}

	public String gettAttndncPunchOut() {
		return tAttndncPunchOut;
	}

	public void settAttndncPunchOut(String tAttndncPunchOut) {
		this.tAttndncPunchOut = tAttndncPunchOut;
	}

	public String gettAttndncPunchOutNote() {
		return tAttndncPunchOutNote;
	}

	public void settAttndncPunchOutNote(String tAttndncPunchOutNote) {
		this.tAttndncPunchOutNote = tAttndncPunchOutNote;
	}

	public Integer gettAttndncPunchOut_Loc() {
		return tAttndncPunchOut_Loc;
	}

	public void settAttndncPunchOut_Loc(Integer tAttndncPunchOut_Loc) {
		this.tAttndncPunchOut_Loc = tAttndncPunchOut_Loc;
	}

	public String gettAttndncCreatedOn() {
		return tAttndncCreatedOn;
	}

	public void settAttndncCreatedOn(String tAttndncCreatedOn) {
		this.tAttndncCreatedOn = tAttndncCreatedOn;
	}

	public String gettAttndncCreatedBy() {
		return tAttndncCreatedBy;
	}

	public void settAttndncCreatedBy(String tAttndncCreatedBy) {
		this.tAttndncCreatedBy = tAttndncCreatedBy;
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
