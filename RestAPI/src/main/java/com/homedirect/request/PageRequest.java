package com.homedirect.request;

import lombok.Getter;

@Getter
public class PageRequest {

	private int pageNo;
	private int pageSize;

	public PageRequest(int pageNo, int pageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
}
