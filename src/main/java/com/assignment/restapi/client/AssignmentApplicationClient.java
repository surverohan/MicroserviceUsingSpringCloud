package com.assignment.restapi.client;

import java.io.File;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.assignment.restapi.model.FileMetaData;

public class AssignmentApplicationClient {
	private static final Logger logger = LoggerFactory
			.getLogger(AssignmentApplicationClient.class);
	private RestTemplate restTemplate;
	// private static final String REST_SERVICE_URI =
	// "http://localhost:8080/SpringBootRestApi/fileService";
	private static final String REST_SERVICE_URI = "http://localhost:8080/fileService";

	private RestTemplate getRestTemplate() {
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		return restTemplate;
	}

	private String getServiceUrl() {

		return REST_SERVICE_URI;
	}

	public String getAllEmployees() {
		return getRestTemplate().getForObject(getServiceUrl() + "/employees",
				String.class);
	}

	public void upLoad(String fileLocation, String userName) {

		String val = getAllEmployees();
		File file = new File(fileLocation);
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", file);

		FileMetaData fileMetaData = getRestTemplate().postForObject(
				getServiceUrl() + "/uploadfile?user={name}", parts,
				FileMetaData.class, userName);
		logger.info(fileMetaData.toString());

	}

	public void findDocuments(String userName) {
		RestTemplate restTemplate = getRestTemplate();
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays
				.asList(MediaType.APPLICATION_JSON,
						MediaType.APPLICATION_OCTET_STREAM));
		restTemplate.getMessageConverters().add(
				mappingJackson2HttpMessageConverter);
		FileMetaData[] result = (FileMetaData[]) restTemplate.getForObject(
				getServiceUrl() + "/findMetaData?user={name}",
				FileMetaData[].class, userName);
		Arrays.stream(result).forEach(data -> logger.info(data.toString()));

	}

	public static void main(String[] args) {
		AssignmentApplicationClient client = new AssignmentApplicationClient();
		// client.upLoad("C:\\Users\\rsurve\\Desktop\\resume\\Rohan_Surve.docx","xyxabc")
		// ;
		client.findDocuments("xyxabc");

	}
}
