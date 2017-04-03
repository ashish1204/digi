package com.cg.digi.model;

public class DesktopBrowser_Version {

	private int id;
	private String version;
	private double value;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return "DesktopBrowser_Version [id=" + id + ", version=" + version + ", value=" + value + "]";
	}


}
