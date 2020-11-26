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
public class InventoryRequisitionDashboardModel {

	private Integer todayRequisition;

	private Integer pastDueRequisition;

	private Integer todayClosedRequisition;

	private Integer totalClosedRequisition;

	/**
	 * CONSTRUCTOR SUPERCLASS
	 */

	public InventoryRequisitionDashboardModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * GETTER SETTER
	 */
	public Integer getTodayRequisition() {
		return todayRequisition;
	}

	public void setTodayRequisition(Integer todayRequisition) {
		this.todayRequisition = todayRequisition;
	}

	public Integer getPastDueRequisition() {
		return pastDueRequisition;
	}

	public void setPastDueRequisition(Integer pastDueRequisition) {
		this.pastDueRequisition = pastDueRequisition;
	}

	public Integer getTodayClosedRequisition() {
		return todayClosedRequisition;
	}

	public void setTodayClosedRequisition(Integer todayClosedRequisition) {
		this.todayClosedRequisition = todayClosedRequisition;
	}

	public Integer getTotalClosedRequisition() {
		return totalClosedRequisition;
	}

	public void setTotalClosedRequisition(Integer totalClosedRequisition) {
		this.totalClosedRequisition = totalClosedRequisition;
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
