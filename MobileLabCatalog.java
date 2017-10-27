package com.cg.digi.model;

public class MobileLabCatalog {
	
	private String devicename;
	private String os;
	private String version;
	private String resolution;
	private String vendor;
	private String starttime;
	private String endtime;
	private String approvalStatus;
	private String devicecatalogid;
	private String reservationid;
	private String createdby;
	
	public MobileLabCatalog() {
		super();
	}

	
	
	public MobileLabCatalog(String devicename, String os, String version,
			String resolution, String vendor, String starttime, String endtime,
			String approvalStatus, String devicecatalogid,
			String reservationid, String createdby) {
		super();
		this.devicename = devicename;
		this.os = os;
		this.version = version;
		this.resolution = resolution;
		this.vendor = vendor;
		this.starttime = starttime;
		this.endtime = endtime;
		this.approvalStatus = approvalStatus;
		this.devicecatalogid = devicecatalogid;
		this.reservationid = reservationid;
		this.createdby = createdby;
	}



	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}



	public String getDevicecatalogid() {
		return devicecatalogid;
	}

	public void setDevicecatalogid(String devicecatalogid) {
		this.devicecatalogid = devicecatalogid;
	}

	public String getReservationid() {
		return reservationid;
	}

	public void setReservationid(String reservationid) {
		this.reservationid = reservationid;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}



	@Override
	public String toString() {
		return "MobileLabCatalog [devicename=" + devicename + ", os=" + os
				+ ", version=" + version + ", resolution=" + resolution
				+ ", vendor=" + vendor + ", starttime=" + starttime
				+ ", endtime=" + endtime + ", approvalStatus=" + approvalStatus
				+ ", devicecatalogid=" + devicecatalogid + ", reservationid="
				+ reservationid + ", createdby=" + createdby + "]";
	}

	


	


}
