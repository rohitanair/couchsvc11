package com.infy.tele.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {
	@JsonProperty("connector.class")
	String connector_class;
	@JsonProperty("tasks.max")
    String tasks_max;
    String topics;
    @JsonProperty("internal.key.converter")
    String internal_key_converter;
    @JsonProperty("internal.value.converter")
	String internal_value_converter;
    @JsonProperty("internal.key.converter.schemas.enable")
	boolean internal_key_converter_schemas_enable;
    @JsonProperty("internal.value.converter.schemas.enable")
	boolean internal_value_converter_schemas_enable;
	
	
	public Config() {
		super();
		// TODO Auto-generated constructor stub
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
	public String getTopics() {
		return topics;
	}
	public void setTopics(String topics) {
		this.topics = topics;
	}
	public String getInternal_key_converter() {
		return internal_key_converter;
	}
	public void setInternal_key_converter(String internal_key_converter) {
		this.internal_key_converter = internal_key_converter;
	}
	public String getInternal_value_converter() {
		return internal_value_converter;
	}
	public void setInternal_value_converter(String internal_value_converter) {
		this.internal_value_converter = internal_value_converter;
	}
	public boolean isInternal_key_converter_schemas_enable() {
		return internal_key_converter_schemas_enable;
	}
	public void setInternal_key_converter_schemas_enable(boolean internal_key_converter_schemas_enable) {
		this.internal_key_converter_schemas_enable = internal_key_converter_schemas_enable;
	}
	public boolean isInternal_value_converter_schemas_enable() {
		return internal_value_converter_schemas_enable;
	}
	public void setInternal_value_converter_schemas_enable(boolean internal_value_converter_schemas_enable) {
		this.internal_value_converter_schemas_enable = internal_value_converter_schemas_enable;
	}
	
}