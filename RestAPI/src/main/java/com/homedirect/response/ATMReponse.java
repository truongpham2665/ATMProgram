package com.homedirect.response;

import com.homedirect.message.MyException;

public class ATMReponse {

	private int code;
	private String message;
	private Object object;

	public ATMReponse() {
	}

	public ATMReponse(MyException code, Object object) {
		this.code = code.getCode();
		this.message = code.getDescription();
		this.object = object;
	}

	public ATMReponse(int code, String message, Object object) {
		this.code = code;
		this.message = message;
		this.object = object;
	}

	public ATMReponse(Object object) {
		super();
		this.object = object;
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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
