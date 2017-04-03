/******************************************************************************
Copyright © 2016 Capgemini Group of companies. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF Capgemini GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND Capgemini GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
******************************************************************************/

package com.cg.digi.model;


/**
 * @author hapemmas
 *
 */
public class Project {
	
	private String projectid;
	private String projectName;
	private String projectDescription;
	private String startDate;
	private String endDate;
	private String createdBy;
	private String creationTime;
	private String modifiedBy;
	private String modifiedTime;	
	private String accountid;
	private String status;
	
	
	public Project() {
	}


	/**
	 * @return the projectid
	 */
	public String getProjectid() {
		return projectid;
	}


	/**
	 * @param projectid the projectid to set
	 */
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}


	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}


	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	/**
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}


	/**
	 * @param projectDescription the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}


	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}


	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	/**
	 * @return the creationTime
	 */
	public String getCreationTime() {
		return creationTime;
	}


	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}


	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}


	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	/**
	 * @return the modifiedTime
	 */
	public String getModifiedTime() {
		return modifiedTime;
	}


	/**
	 * @param modifiedTime the modifiedTime to set
	 */
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}


	/**
	 * @return the accountid
	 */
	public String getAccountid() {
		return accountid;
	}


	/**
	 * @param accountid the accountid to set
	 */
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Project ["
				+ (projectid != null ? "projectid=" + projectid + ", " : "")
				+ (projectName != null ? "projectName=" + projectName + ", "
						: "")
				+ (projectDescription != null ? "projectDescription="
						+ projectDescription + ", " : "")
				+ (startDate != null ? "startDate=" + startDate + ", " : "")
				+ (endDate != null ? "endDate=" + endDate + ", " : "")
				+ (createdBy != null ? "createdBy=" + createdBy + ", " : "")
				+ (creationTime != null ? "creationTime=" + creationTime + ", "
						: "")
				+ (modifiedBy != null ? "modifiedBy=" + modifiedBy + ", " : "")
				+ (modifiedTime != null ? "modifiedTime=" + modifiedTime + ", "
						: "")
				+ (accountid != null ? "accountid=" + accountid + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (getProjectid() != null ? "getProjectid()=" + getProjectid()
						+ ", " : "")
				+ (getProjectName() != null ? "getProjectName()="
						+ getProjectName() + ", " : "")
				+ (getProjectDescription() != null ? "getProjectDescription()="
						+ getProjectDescription() + ", " : "")
				+ (getStartDate() != null ? "getStartDate()=" + getStartDate()
						+ ", " : "")
				+ (getEndDate() != null ? "getEndDate()=" + getEndDate() + ", "
						: "")
				+ (getCreatedBy() != null ? "getCreatedBy()=" + getCreatedBy()
						+ ", " : "")
				+ (getCreationTime() != null ? "getCreationTime()="
						+ getCreationTime() + ", " : "")
				+ (getModifiedBy() != null ? "getModifiedBy()="
						+ getModifiedBy() + ", " : "")
				+ (getModifiedTime() != null ? "getModifiedTime()="
						+ getModifiedTime() + ", " : "")
				+ (getAccountid() != null ? "getAccountid()=" + getAccountid()
						+ ", " : "")
				+ (getStatus() != null ? "getStatus()=" + getStatus() + ", "
						: "")
				+ (getClass() != null ? "getClass()=" + getClass() + ", " : "")
				+ "hashCode()="
				+ hashCode()
				+ ", "
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}

	
	
	
}
