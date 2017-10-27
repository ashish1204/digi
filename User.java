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
public class User {

	private  String userid;
	private  String userName;
	private  String userPassword;
	private  String firstName;
	private  String lastName;
	private  String email;
	private  String phoneNumber;
	private  String roleid;
	private  String projectid;
	private String rolename;
	private  String status;

public User() {
}

/**
 * @return the userid
 */
public String getUserid() {
	return userid;
}

/**
 * @param userid the userid to set
 */
public void setUserid(String userid) {
	this.userid = userid;
}

/**
 * @return the userName
 */
public String getUserName() {
	return userName;
}

/**
 * @param userName the userName to set
 */
public void setUserName(String userName) {
	this.userName = userName;
}

/**
 * @return the userPassword
 */
public String getUserPassword() {
	return userPassword;
}

/**
 * @param userPassword the userPassword to set
 */
public void setUserPassword(String userPassword) {
	this.userPassword = userPassword;
}

/**
 * @return the firstName
 */
public String getFirstName() {
	return firstName;
}

/**
 * @param firstName the firstName to set
 */
public void setFirstName(String firstName) {
	this.firstName = firstName;
}

/**
 * @return the lastName
 */
public String getLastName() {
	return lastName;
}

/**
 * @param lastName the lastName to set
 */
public void setLastName(String lastName) {
	this.lastName = lastName;
}

/**
 * @return the email
 */
public String getEmail() {
	return email;
}

/**
 * @param email the email to set
 */
public void setEmail(String email) {
	this.email = email;
}

/**
 * @return the phoneNumber
 */
public String getPhoneNumber() {
	return phoneNumber;
}

/**
 * @param phoneNumber the phoneNumber to set
 */
public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}

/**
 * @return the roleid
 */
public String getRoleid() {
	return roleid;
}

/**
 * @param roleid the roleid to set
 */
public void setRoleid(String roleid) {
	this.roleid = roleid;
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

/**
 * @return the rolename
 */
public String getRolename() {
	return rolename;
}

/**
 * @param rolename the rolename to set
 */
public void setRolename(String rolename) {
	this.rolename = rolename;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "User ["
			+ (userid != null ? "userid=" + userid + ", " : "")
			+ (userName != null ? "userName=" + userName + ", " : "")
			+ (userPassword != null ? "userPassword=" + userPassword + ", "
					: "")
			+ (firstName != null ? "firstName=" + firstName + ", " : "")
			+ (lastName != null ? "lastName=" + lastName + ", " : "")
			+ (email != null ? "email=" + email + ", " : "")
			+ (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "")
			+ (roleid != null ? "roleid=" + roleid + ", " : "")
			+ (projectid != null ? "projectid=" + projectid + ", " : "")
			+ (rolename != null ? "rolename=" + rolename + ", " : "")
			+ (status != null ? "status=" + status + ", " : "")
			+ (getUserid() != null ? "getUserid()=" + getUserid() + ", " : "")
			+ (getUserName() != null ? "getUserName()=" + getUserName() + ", "
					: "")
			+ (getUserPassword() != null ? "getUserPassword()="
					+ getUserPassword() + ", " : "")
			+ (getFirstName() != null ? "getFirstName()=" + getFirstName()
					+ ", " : "")
			+ (getLastName() != null ? "getLastName()=" + getLastName() + ", "
					: "")
			+ (getEmail() != null ? "getEmail()=" + getEmail() + ", " : "")
			+ (getPhoneNumber() != null ? "getPhoneNumber()="
					+ getPhoneNumber() + ", " : "")
			+ (getRoleid() != null ? "getRoleid()=" + getRoleid() + ", " : "")
			+ (getProjectid() != null ? "getProjectid()=" + getProjectid()
					+ ", " : "")
			+ (getStatus() != null ? "getStatus()=" + getStatus() + ", " : "")
			+ (getRolename() != null ? "getRolename()=" + getRolename() + ", "
					: "")
			+ (getClass() != null ? "getClass()=" + getClass() + ", " : "")
			+ "hashCode()="
			+ hashCode()
			+ ", "
			+ (super.toString() != null ? "toString()=" + super.toString() : "")
			+ "]";
}
 



}
