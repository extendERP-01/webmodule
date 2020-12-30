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
public class InventoryGoodsReturnNoteDashboardModel {

	private Integer todayGoodsReturnNote;

	private Integer totalGoodsReturnNote;

	private Integer todayGoodsReturnNotePrice;

	private Integer totalGoodsReturnNotePrice;

	/**
	 * CONSTRUCTOR SUPERCLASS
	 */

	public InventoryGoodsReturnNoteDashboardModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * GETTER SETTER
	 */

	public Integer getTodayGoodsReturnNote() {
		return todayGoodsReturnNote;
	}

	public void setTodayGoodsReturnNote(Integer todayGoodsReturnNote) {
		this.todayGoodsReturnNote = todayGoodsReturnNote;
	}

	public Integer getTotalGoodsReturnNote() {
		return totalGoodsReturnNote;
	}

	public void setTotalGoodsReturnNote(Integer totalGoodsReturnNote) {
		this.totalGoodsReturnNote = totalGoodsReturnNote;
	}

	public Integer getTodayGoodsReturnNotePrice() {
		return todayGoodsReturnNotePrice;
	}

	public void setTodayGoodsReturnNotePrice(Integer todayGoodsReturnNotePrice) {
		this.todayGoodsReturnNotePrice = todayGoodsReturnNotePrice;
	}

	public Integer getTotalGoodsReturnNotePrice() {
		return totalGoodsReturnNotePrice;
	}

	public void setTotalGoodsReturnNotePrice(Integer totalGoodsReturnNotePrice) {
		this.totalGoodsReturnNotePrice = totalGoodsReturnNotePrice;
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
