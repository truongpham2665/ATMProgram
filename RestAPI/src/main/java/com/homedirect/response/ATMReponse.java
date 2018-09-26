package com.homedirect.response;

public class ATMReponse {

	private int code;
	private String message;
	private AccountResponse accountResponse;

	public ATMReponse() {
	}

	public ATMReponse(int code, String message, AccountResponse accountResponse) {
		this.code = code;
		this.message = message;
		this.accountResponse = accountResponse;
	}

	public ATMReponse(AccountResponse accountResponse) {
		this.accountResponse = accountResponse;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AccountResponse getAccountResponse() {
		return accountResponse;
	}

	public void setAccountResponse(AccountResponse accountResponse) {
		this.accountResponse = accountResponse;
	}

}
