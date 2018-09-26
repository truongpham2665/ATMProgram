package com.homedirect.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.homedirect.constant.ErrorMyCode;
import com.homedirect.entity.Account;
import com.homedirect.message.MessageException;
import com.homedirect.response.ATMReponse;
import com.homedirect.transformer.AccountTransformer;

public abstract class AbstractMyException {

	private @Autowired AccountTransformer accountTransformer;

	protected ATMReponse success(Account account) {
		return new ATMReponse(ErrorMyCode.SUCCESS, MessageException.success(), accountTransformer.toResponse(account));
	}

	protected ATMReponse errorFalse(String message) {
		return new ATMReponse(99, message, null);
	}
}
