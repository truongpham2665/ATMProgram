package com.homedirect.exception;

public class ATMException extends Exception {

	private static final long serialVersionUID = 1L;
	private int code;
	
	public ATMException(String message) {
		super(message);
	}
	
	public ATMException(int code, String message) {
		super(message);
		this.code = code;
	}

	public int getCode() { return code; }
	public void setCode(int code) { this.code = code; }
}
