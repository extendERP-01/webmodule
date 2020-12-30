package nirmalya.aathithya.webmodule.inventory.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryDataForDblValModel {
	private String key;

	private Double minStock;
	private Double totalReq;
	private Double availQnt;
	private String subGruop;
	private String saleSubGruop;

	public InventoryDataForDblValModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getMinStock() {
		return minStock;
	}

	public void setMinStock(Double minStock) {
		this.minStock = minStock;
	}

	public Double getTotalReq() {
		return totalReq;
	}

	public void setTotalReq(Double totalReq) {
		this.totalReq = totalReq;
	}

	public Double getAvailQnt() {
		return availQnt;
	}

	public void setAvailQnt(Double availQnt) {
		this.availQnt = availQnt;
	}

	public String getSubGruop() {
		return subGruop;
	}

	public void setSubGruop(String subGruop) {
		this.subGruop = subGruop;
	}

	public String getSaleSubGruop() {
		return saleSubGruop;
	}

	public void setSaleSubGruop(String saleSubGruop) {
		this.saleSubGruop = saleSubGruop;
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
