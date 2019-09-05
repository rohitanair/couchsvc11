package com.infy.tele.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectorAndTopic {
	String name;
	String topic;
	String connector_class;
	String tasks_max;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getConnector_class() {
		return connector_class;
	}
	public void setConnector_class(String connector_class) {
		this.connector_class = connector_class;
	}
	public String getTasks_max() {
		return tasks_max;
	}
	public void setTasks_max(String tasks_max) {
		this.tasks_max = tasks_max;
	}
	
	
}

