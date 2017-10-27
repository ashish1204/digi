package com.cg.digi.model;

public class UserOsDistribution {

	private int userOsDistributionId;
	private int marketId;
	private String os;
	private String month;
	private Double value;
	private String status;
	private String creationTime;
	private String createdBy;
	private String modifiedBy;
	private String modifiedTime;
	
	public UserOsDistribution(){}
	public UserOsDistribution(int userOsDistributionId, int marketId, String os, String month, Double value,
			String status, String creationTime, String createdBy, String modifiedBy, String modifiedTime) {
		super();
		this.userOsDistributionId = userOsDistributionId;
		this.marketId = marketId;
		this.os = os;
		this.month = month;
		this.value = value;
		this.status = status;
		this.creationTime = creationTime;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.modifiedTime = modifiedTime;
	}
	public int getUserOsDistributionId() {
		return userOsDistributionId;
	}
	public void setUserOsDistributionId(int userOsDistributionId) {
		this.userOsDistributionId = userOsDistributionId;
	}
	public int getMarketId() {
		return marketId;
	}
	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
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
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
		return "UserOsDistribution [userOsDistributionId=" + userOsDistributionId + ", marketId=" + marketId + ", os="
				+ os + ", month=" + month + ", value=" + value + ", status=" + status + ", creationTime=" + creationTime
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedTime=" + modifiedTime + "]";
	}
	
	
}
