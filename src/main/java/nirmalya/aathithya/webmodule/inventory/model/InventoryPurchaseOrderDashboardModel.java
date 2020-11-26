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
public class InventoryPurchaseOrderDashboardModel {

	private Integer todayOpenPurchaseOrder;

	private Integer totalOpenPurchaseOrder;

	private Integer todayClosedPurchaseOrder;

	private Integer totalClosedPurchaseOrder;

	/**
	 * CONSTRUCTOR SUPERCLASS
	 */

	public InventoryPurchaseOrderDashboardModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * GETTER SETTER
	 */

	public Integer getTodayOpenPurchaseOrder() {
		return todayOpenPurchaseOrder;
	}

	public void setTodayOpenPurchaseOrder(Integer todayOpenPurchaseOrder) {
		this.todayOpenPurchaseOrder = todayOpenPurchaseOrder;
	}

	public Integer getTotalOpenPurchaseOrder() {
		return totalOpenPurchaseOrder;
	}

	public void setTotalOpenPurchaseOrder(Integer totalOpenPurchaseOrder) {
		this.totalOpenPurchaseOrder = totalOpenPurchaseOrder;
	}

	public Integer getTodayClosedPurchaseOrder() {
		return todayClosedPurchaseOrder;
	}

	public void setTodayClosedPurchaseOrder(Integer todayClosedPurchaseOrder) {
		this.todayClosedPurchaseOrder = todayClosedPurchaseOrder;
	}

	public Integer getTotalClosedPurchaseOrder() {
		return totalClosedPurchaseOrder;
	}

	public void setTotalClosedPurchaseOrder(Integer totalClosedPurchaseOrder) {
		this.totalClosedPurchaseOrder = totalClosedPurchaseOrder;
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
