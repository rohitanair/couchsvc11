package com.infy.tele.web.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

//import com.example.connect.model.Config;
//import com.example.connect.model.connectorAndTopic;
//import com.example.connect.model.model;
//import com.example.connect.model.responseModel;

@RestController
public class KafkaConnect {
	
	@Value("${spring.connect.url}")
	private String connectUrl;
	@GET
	@RequestMapping("/getConnectors")
	@ResponseBody
	public List<String> getConnectors() throws Exception {
		RestTemplate restTemplate=new RestTemplate();
		List<String> ans;
		ans=restTemplate.getForObject(connectUrl, List.class);  //change it
		return ans;
	}
	@POST
	@RequestMapping("/createConnectors")
	@ResponseBody
	public ResponseModel createConnector(@RequestBody ConnectorAndTopic request) throws Exception{
		Model connectRequest=new Model();
		connectRequest.setName(request.getName());
		Config c=new Config();
		c.setConnector_class(request.getConnector_class());
		c.setTopics(request.getTopic());
		c.setTasks_max(request.getTasks_max());
		c.setInternal_key_converter("org.apache.kafka.connect.json.JsonConverter");
		c.setInternal_value_converter("org.apache.kafka.connect.json.JsonConverter");
		c.setInternal_key_converter_schemas_enable(false);
		c.setInternal_value_converter_schemas_enable(false);
		connectRequest.setConfig(c);
		RestTemplate restTemplate=new RestTemplate();
		ResponseModel ss=restTemplate.postForObject(connectUrl, connectRequest, ResponseModel.class); //change it
		return ss;
	}

}
