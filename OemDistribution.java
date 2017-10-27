package com.cg.digi.model;

public class OemDistribution {

	private int oemDistributionId;
	private int marketId;
	private String vendorName;
	private String month;
	private Double value;
	private String status;
	private String creationTime;
	private String createdBy;
	private String modifiedBy;
	private String modifiedTime;
	
	public OemDistribution(){}
	
	public OemDistribution(int oemDistributionId, int marketId, String vendorName, String month, Double value,
			String status, String creationTime, String createdBy, String modifiedBy, String modifiedTime) {
		super();
		this.oemDistributionId = oemDistributionId;
		this.marketId = marketId;
		this.vendorName = vendorName;
		this.month = month;
		this.value = value;
		this.status = status;
		this.creationTime = creationTime;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.modifiedTime = modifiedTime;
	}

	public int getOemDistributionId() {
		return oemDistributionId;
	}

	public void setOemDistributionId(int oemDistributionId) {
		this.oemDistributionId = oemDistributionId;
	}

	public int getMarketId() {
		return marketId;
	}

	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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
		return "OemDistribution [oemDistributionId=" + oemDistributionId + ", marketId=" + marketId + ", vendorName="
				+ vendorName + ", month=" + month + ", value=" + value + ", status=" + status + ", creationTime="
				+ creationTime + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedTime="
				+ modifiedTime + "]";
	}
	
	
	
	
}
