package com.homedirect.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor 
public class SearchAccountRequest {

	private String username;
	private int pageNo;
	private int pageSize;
}
