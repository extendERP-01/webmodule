package nirmalya.aathithya.webmodule.inventory.model;

import java.util.List;

public class InventoryStockDailyReportModel {

	private String name;
	private List<Double> data;

	public InventoryStockDailyReportModel() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Double> getData() {
		return data;
	}

	public void setData(List<Double> data) {
		this.data = data;
	}

	 

}
