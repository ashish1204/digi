package com.cg.digi.model;

public class Market {

	private int marketId;
	private String marketName;
	private String status;
	private String createdBy;
	private String creationTime;
	private String modifiedBy;
	private String modifiedTime;
	
	public Market(){
	}
	
	public Market(int marketId, String marketName, String status, String createdBy, String creationTime,
			String modifiedBy, String modifiedTime) {
		super();
		this.marketId = marketId;
		this.marketName = marketName;
		this.status = status;
		this.createdBy = createdBy;
		this.creationTime = creationTime;
		this.modifiedBy = modifiedBy;
		this.modifiedTime = modifiedTime;
	}
	public int getMarketId() {
		return marketId;
	}
	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
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
		return "Market [marketId=" + marketId + ", marketName=" + marketName + ", status=" + status + ", createdBy="
				+ createdBy + ", creationTime=" + creationTime + ", modifiedBy=" + modifiedBy + ", modifiedTime="
				+ modifiedTime + "]";
	}
	
	
	
	
}
