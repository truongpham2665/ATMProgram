package com.homedirect.request;

import lombok.Getter;

@Getter
public class AccountRequest {
	private Integer id;
	private String accountNumber;
	private String username;
	private String password;
	private Double amount;
}
