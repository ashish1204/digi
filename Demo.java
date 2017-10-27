package com.cg.digi.model;

public class Demo {

	private File file;
	private String name;
	
	public Demo(){}
	public Demo(File file, String name) {
		super();
		this.file = file;
		this.name = name;
	}

	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Demo [file=" + file + ", name=" + name + "]";
	}
	
}
