package com.homedirect.transformer.impl;

import static com.homedirect.constant.ConstantAccount.DEFAULT_AMOUNT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.validate.ValidatorATM;

// thêm method toResponseList

@Component
public class AccountTransformer {
	
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
	
	// Sửa hàm toResponseList(). 
	public List<AccountResponse> toResponseList(List<Account> accounts) {
		return accounts.stream().map(this::toResponse).collect(Collectors.toList());
	}
	
	public Iterable<AccountResponse> toResponseIterable(Iterable<Account> accountIterable) {
		if (accountIterable == null) return new ArrayList<>();
		List<AccountResponse> accountResponses = new ArrayList<>();
		accountIterable.forEach(account -> {
			AccountResponse response = new AccountResponse();
			response.setAccountNumber(account.getAccountNumber());
			response.setUsername(account.getUsername());
			response.setAmount(account.getAmount());
			accountResponses.add(response);
		});
		return accountResponses;
	}
}
