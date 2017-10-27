package com.cg.digi.model;

public class IosDistribution {

	private int iosDistributionId;
	private String iosVersion;
	private Double rate;
	private String status;
	private String createdBy;
	private String creationTime;
	private String modifiedBy;
	private String modifiedTime;
	
	
	public IosDistribution(){}
	
	public IosDistribution(int iosDistributionId, String iosVersion, Double rate, String status, String createdBy,
			String creationTime, String modifiedBy, String modifiedTime) {
		super();
		this.iosDistributionId = iosDistributionId;
		this.iosVersion = iosVersion;
		this.rate = rate;
		this.status = status;
		this.createdBy = createdBy;
		this.creationTime = creationTime;
		this.modifiedBy = modifiedBy;
		this.modifiedTime = modifiedTime;
	}
	public int getIosDistributionId() {
		return iosDistributionId;
	}
	public void setIosDistributionId(int iosDistributionId) {
		this.iosDistributionId = iosDistributionId;
	}
	public String getIosVersion() {
		return iosVersion;
	}
	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
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
		return "IosDistribution [iosDistributionId=" + iosDistributionId + ", iosVersion=" + iosVersion + ", rate="
				+ rate + ", status=" + status + ", createdBy=" + createdBy + ", creationTime=" + creationTime
				+ ", modifiedBy=" + modifiedBy + ", modifiedTime=" + modifiedTime + "]";
	}
	
	
}
