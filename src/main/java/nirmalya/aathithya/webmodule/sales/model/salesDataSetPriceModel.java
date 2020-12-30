package nirmalya.aathithya.webmodule.sales.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class salesDataSetPriceModel {

	private String ptype1;

	private String ptype2;

	private String itemCat;

	private String itemSubCat;

	private String key;

	private String name;

	private Double price;

	private Double quantity;

	private Double gstRate;

	private Boolean taxType;

	private Double deliverdItemNo;

	private Double advAmount;

	public salesDataSetPriceModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Double getDeliverdItemNo() {
		return deliverdItemNo;
	}

	public void setDeliverdItemNo(Double deliverdItemNo) {
		this.deliverdItemNo = deliverdItemNo;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getGstRate() {
		return gstRate;
	}

	public void setGstRate(Double gstRate) {
		this.gstRate = gstRate;
	}

	public Boolean getTaxType() {
		return taxType;
	}

	public void setTaxType(Boolean taxType) {
		this.taxType = taxType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPtype1() {
		return ptype1;
	}

	public void setPtype1(String ptype1) {
		this.ptype1 = ptype1;
	}

	public String getPtype2() {
		return ptype2;
	}

	public void setPtype2(String ptype2) {
		this.ptype2 = ptype2;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemCat() {
		return itemCat;
	}

	public String getItemSubCat() {
		return itemSubCat;
	}

	public void setItemCat(String itemCat) {
		this.itemCat = itemCat;
	}

	public void setItemSubCat(String itemSubCat) {
		this.itemSubCat = itemSubCat;
	}

	public Double getAdvAmount() {
		return advAmount;
	}

	public void setAdvAmount(Double advAmount) {
		this.advAmount = advAmount;
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
