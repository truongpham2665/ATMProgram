package com.homedirect.transformer.impl;

import static com.homedirect.constant.ConstantAccount.DEFAULT_AMOUNT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.impl.AccountServiceImpl;

@Component
public class AccountTransformerImpl {
	
	private @Autowired AccountServiceImpl accountService;
	
	public Account toAccount(AccountRequest request) {
		Account newAccount = new Account(request.getUsername(), accountService.generateAccountNumber(), request.getPassword(), DEFAULT_AMOUNT);
		return newAccount;
	}

	public AccountResponse toResponse(Account account) {
		AccountResponse response = new AccountResponse();
		response.setId(account.getId());
		response.setAccountNumber(account.getAccountNumber());
		response.setUsername(account.getUserName());
		response.setAmount(account.getAmount());
		return response;
	}
}
