package com.homedirect.transformer.impl;

import static com.homedirect.constant.ConstantAccount.DEFAULT_AMOUNT;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.validate.ValidatorATM;

// thêm method toResponseList

@Component
public class AccountTransformerImpl {
	
	private @Autowired ValidatorATM validator;
	
	public Account toAccount(AccountRequest request) {
		Account newAccount = new Account();
		newAccount.setAccountNumber(validator.generateAccountNumber());
		newAccount.setUsername(request.getUsername());
		newAccount.setPassword(request.getPassword());
		newAccount.setAmount(DEFAULT_AMOUNT);
		return newAccount;
	}

	public AccountResponse toResponse(Account account) {
		AccountResponse response = new AccountResponse();
		response.setAccountNumber(account.getAccountNumber());
		response.setUsername(account.getUsername());
		response.setAmount(account.getAmount());
		return response;
	}
	
	public List<AccountResponse> toResponseList(List<Account> accounts) {
		if (accounts == null) return new ArrayList<>();
		List<AccountResponse> accountResponses = new ArrayList<>();
		accounts.forEach(account -> {
			AccountResponse response = new AccountResponse();
			response.setAccountNumber(account.getAccountNumber());
			response.setUsername(account.getUsername());
			response.setAmount(account.getAmount());
			accountResponses.add(response);
		});
		
		return accountResponses;
	}
}
