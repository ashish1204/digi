package com.cg.digi.model;

public class MobileBrowserVersionDistribution {

	private int mobileBrowserVersionDistributionId;
	private String version;
	private double rate;
	private String status;
	private String createdBy;
	private String creationTime;
	private String modifiedBy;
	private String modifiedTime;
	
	public MobileBrowserVersionDistribution(){}
	
	public MobileBrowserVersionDistribution(int mobileBrowserVersionDistributionId, String version, double rate,
			String status, String createdBy, String creationTime, String modifiedBy, String modifiedTime) {
		super();
		this.mobileBrowserVersionDistributionId = mobileBrowserVersionDistributionId;
		this.version = version;
		this.rate = rate;
		this.status = status;
		this.createdBy = createdBy;
		this.creationTime = creationTime;
		this.modifiedBy = modifiedBy;
		this.modifiedTime = modifiedTime;
	}

	public int getMobileBrowserVersionDistributionId() {
		return mobileBrowserVersionDistributionId;
	}

	public void setMobileBrowserVersionDistributionId(int mobileBrowserVersionDistributionId) {
		this.mobileBrowserVersionDistributionId = mobileBrowserVersionDistributionId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
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
		return "MobileBrowserVersionDistribution [mobileBrowserVersionDistributionId="
				+ mobileBrowserVersionDistributionId + ", version=" + version + ", rate=" + rate + ", status=" + status
				+ ", createdBy=" + createdBy + ", creationTime=" + creationTime + ", modifiedBy=" + modifiedBy
				+ ", modifiedTime=" + modifiedTime + "]";
	}
	
	
	
}
