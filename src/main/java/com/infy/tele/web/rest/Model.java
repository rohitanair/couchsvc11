package com.infy.tele.web.rest;

public class Model {
	String name;
	Config config;
	
	
	
	public Model(String name, Config config) {
		super();
		this.name = name;
		this.config = config;
	}
	
	public Model() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	
}
