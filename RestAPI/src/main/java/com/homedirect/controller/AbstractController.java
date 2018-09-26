package com.homedirect.controller;

import java.util.List;

import com.homedirect.constant.ErrorMyCode;
import com.homedirect.message.MessageException;
import com.homedirect.response.ATMReponse;

public abstract class AbstractController<T> {

	protected ATMReponse<T> success(T data) {
		return new ATMReponse<T>(ErrorMyCode.SUCCESS, MessageException.success(), data);
	}
	
	protected ATMReponse<List<T>> success(List<T> data) {
		return new ATMReponse<List<T>>(ErrorMyCode.SUCCESS, MessageException.success(), data);
	}
	
	protected ATMReponse errorFalse(String message) {
		return new ATMReponse(ErrorMyCode.FALSE, message, null);
	}

	protected ATMReponse notFound(String message) {
		return new ATMReponse(ErrorMyCode.NOT_FOUND, message, null);
	}
}
