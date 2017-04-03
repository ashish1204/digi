package com.cg.digi.model;

public class Browser_Marketshare {

	private int serialNo;
	private String browserName;
	private String month;
	private double value;
	
	
	
	public int getSerialNo() {
		return serialNo;
	}



	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}



	public String getBrowserName() {
		return browserName;
	}



	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}



	public String getMonth() {
		return month;
	}



	public void setMonth(String month) {
		this.month = month;
	}



	public double getValue() {
		return value;
	}



	public void setValue(double value) {
		this.value = value;
	}



	@Override
	public String toString() {
		return "BrowserMarketspace [serialNo=" + serialNo + ", browserName=" + browserName + ", month=" + month
				+ ", value=" + value + "]";
	}
	
	
}
