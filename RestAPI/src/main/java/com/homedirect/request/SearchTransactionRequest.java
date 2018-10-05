package com.homedirect.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchTransactionRequest {

	private int accountId;
	private String fromDate;
	private String toDate;
	private Byte type;
	private int pageNo;
	private int pageSize;
}
