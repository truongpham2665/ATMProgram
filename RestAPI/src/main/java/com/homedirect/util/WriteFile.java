package com.homedirect.util;
//package com.homedirect.writeFileExcel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.ByteArrayHttpMessageConverter;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//public class WriteFile {
//	
//	public void fetchFile() {
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//		messageConverters.add(new ByteArrayHttpMessageConverter());
//		
//		RestTemplate restTemplate = new RestTemplate(messageConverters);
//		
//		HttpHeaders headers = new HttpHeaders();
//		HttpEntity<String> entity = new HttpEntity<String>(headers);
//		
//		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(localhost:8080/accounts/download, , requestEntity, responseType)
//	
//	}
//
//}