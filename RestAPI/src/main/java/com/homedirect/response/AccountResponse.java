package com.homedirect.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountResponse {

	private int id;
	private String accountNumber;
	private String username;
	private Double amount;
}
