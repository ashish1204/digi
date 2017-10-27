package com.cg.digi.model;

public class DesktopBrowserDistribution {

	private int desktopBrowserDistribution;
	private int marketId;
	private String browserName;
	private String month;
	private Double value;
	private String status;
	private String createdBy;
	private String creationTime;
	private String modifiedBy;
	private String modifiedTime;
	
	public DesktopBrowserDistribution(){}
	public DesktopBrowserDistribution(int desktopBrowserDistribution, int marketId, String browserName, String month,
			Double value, String status, String createdBy, String creationTime, String modifiedBy,
			String modifiedTime) {
		super();
		this.desktopBrowserDistribution = desktopBrowserDistribution;
		this.marketId = marketId;
		this.browserName = browserName;
		this.month = month;
		this.value = value;
		this.status = status;
		this.createdBy = createdBy;
		this.creationTime = creationTime;
		this.modifiedBy = modifiedBy;
		this.modifiedTime = modifiedTime;
	}
	public int getDesktopBrowserDistribution() {
		return desktopBrowserDistribution;
	}
	public void setDesktopBrowserDistribution(int desktopBrowserDistribution) {
		this.desktopBrowserDistribution = desktopBrowserDistribution;
	}
	public int getMarketId() {
		return marketId;
	}
	public void setMarketId(int marketId) {
		this.marketId = marketId;
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
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	@Override
	public String toString() {
		return "DesktopBrowserDistribution [desktopBrowserDistribution=" + desktopBrowserDistribution + ", marketId="
				+ marketId + ", browserName=" + browserName + ", month=" + month + ", value=" + value + ", status="
				+ status + ", createdBy=" + createdBy + ", creationTime=" + creationTime + ", modifiedBy=" + modifiedBy
				+ ", modifiedTime=" + modifiedTime + "]";
	}
	
	
	
}
