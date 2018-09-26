package com.homedirect.controller;

import java.util.List;

import static com.homedirect.constant.ErrorMyCode.*;
import com.homedirect.message.MessageException;
import com.homedirect.response.ATMReponse;

public abstract class AbstractController<T> {

	protected ATMReponse<T> success(T data) {
		return new ATMReponse<T>(SUCCESS, MessageException.success(), data);
	}
	
	protected ATMReponse<List<T>> success(List<T> data) {
		return new ATMReponse<List<T>>(SUCCESS, MessageException.success(), data);
	}
	
	protected ATMReponse<T> errorFalse(String message) {
		return new ATMReponse<T>(FALSE, message, null);
	}

	protected ATMReponse<T> notFound(String message) {
		return new ATMReponse<T>(NOT_FOUND, message, null);
	}
}
