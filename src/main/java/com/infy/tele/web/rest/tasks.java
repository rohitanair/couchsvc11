package com.infy.tele.web.rest;
public class tasks {
	String connector;
	int task;
	
	
	
	public tasks() {
		super();
		// TODO Auto-generated constructor stub
	}
	public tasks(String connector, int task) {
		super();
		this.connector = connector;
		this.task = task;
	}
	public String getConnector() {
		return connector;
	}
	public void setConnector(String connector) {
		this.connector = connector;
	}
	public int getTask() {
		return task;
	}
	public void setTask(int task) {
		this.task = task;
	}
	

}