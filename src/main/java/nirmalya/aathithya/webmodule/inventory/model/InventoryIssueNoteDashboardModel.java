/**
 * 
 */
package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class InventoryIssueNoteDashboardModel {

	private Integer todayIssueNote;

	private Integer totalIssueNote;

	private Integer totalOpenRequistion;

	private Integer todayClosedRequistion;

	/**
	 * CONSTRUCTOR SUPERCLASS
	 */

	public InventoryIssueNoteDashboardModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * GETTER SETTER
	 */

	public Integer getTodayIssueNote() {
		return todayIssueNote;
	}

	public void setTodayIssueNote(Integer todayIssueNote) {
		this.todayIssueNote = todayIssueNote;
	}

	public Integer getTotalIssueNote() {
		return totalIssueNote;
	}

	public void setTotalIssueNote(Integer totalIssueNote) {
		this.totalIssueNote = totalIssueNote;
	}

	public Integer getTotalOpenRequistion() {
		return totalOpenRequistion;
	}

	public void setTotalOpenRequistion(Integer totalOpenRequistion) {
		this.totalOpenRequistion = totalOpenRequistion;
	}

	public Integer getTodayClosedRequistion() {
		return todayClosedRequistion;
	}

	public void setTodayClosedRequistion(Integer todayClosedRequistion) {
		this.todayClosedRequistion = todayClosedRequistion;
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
