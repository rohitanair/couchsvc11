package com.infy.tele.web.rest;

import java.util.List;

public class ResponseModel {
	String name;
	Config config;
	List<tasks> tasks;
	String type;

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

	public List<tasks> getTasks() {
		return tasks;
	}

	public void setTasks(List<tasks> tasks) {
		this.tasks = tasks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}