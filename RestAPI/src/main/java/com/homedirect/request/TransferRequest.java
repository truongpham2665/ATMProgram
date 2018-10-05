package com.homedirect.request;

import lombok.Getter;

@Getter
public class TransferRequest {

	private int fromId;
	private String toAccountNumber;
	private Double amount;
	private String content;
	private String password;
}
