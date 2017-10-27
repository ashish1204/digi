package com.cg.digi.model;

public class MobileBrowserDistribution {

	private int mobileBrowserDistributionId;
	private int marketId;
	private String browserName;
	private String month;
	private double value;
	private String status;
	private String createdBy;
	private String creationTime;
	private String modifiedBy;
	private String modifiedTime;
	
	public MobileBrowserDistribution(){
		
	}
	public MobileBrowserDistribution(int mobileBrowserDistributionId, int marketId, String browserName, String month,
			Double value, String status, String createdBy, String creationTime, String modifiedBy,
			String modifiedTime) {
		super();
		this.mobileBrowserDistributionId = mobileBrowserDistributionId;
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
	public int getMobileBrowserDistributionId() {
		return mobileBrowserDistributionId;
	}
	public void setMobileBrowserDistributionId(int mobileBrowserDistributionId) {
		this.mobileBrowserDistributionId = mobileBrowserDistributionId;
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
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
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
		return "MobileBrowserDistribution [mobileBrowserDistributionId=" + mobileBrowserDistributionId + ", marketId="
				+ marketId + ", browserName=" + browserName + ", month=" + month + ", value=" + value + ", status="
				+ status + ", createdBy=" + createdBy + ", creationTime=" + creationTime + ", modifiedBy=" + modifiedBy
				+ ", modifiedTime=" + modifiedTime + "]";
	}
	
	
}
