package com.homedirect.request;

import lombok.Getter;

@Getter
public class WithdrawRequest {

	private int id;
	private Double amount;
	private String password;
}
