/******************************************************************************
Copyright � 2016 Capgemini Group of companies. All rights reserved
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

public class OS_Names {

	private  int os_id;
	private  String os_nm;
	private  String desc;
	
	public int getOs_id() {
		return os_id;
	}
	public void setOs_id(int os_id) {
		this.os_id = os_id;
	}
	public String getOs_nm() {
		return os_nm;
	}
	public void setOs_nm(String os_nm) {
		this.os_nm = os_nm;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return "OS_Names [os_id=" + os_id + ", os_nm=" + os_nm + ", desc="
				+ desc + "]";
	}
	
}
