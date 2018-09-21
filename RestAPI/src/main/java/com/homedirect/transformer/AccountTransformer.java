package com.homedirect.transformer;

import static com.homedirect.constant.ConstantAccount.DEFAULT_AMOUNT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.validate.ValidatorInputATM;

@Component
public class AccountTransformer {

	private @Autowired ValidatorInputATM validatorInputATM;

	public Account toAccount(AccountRequest request) {
		Account newAccount = new Account();
		newAccount.setId(request.getId());
		newAccount.setAccountNumber(validatorInputATM.generateAccountNumber());
		newAccount.setUsername(request.getUsername());
		newAccount.setPassword(request.getPassword());
		newAccount.setAmount(DEFAULT_AMOUNT);
		return newAccount;
	}

	public AccountResponse toResponse(Account account) {
		AccountResponse response = new AccountResponse();
		response.setId(account.getId());
		response.setAccountNumber(account.getAccountNumber());
		response.setUsername(account.getUsername());
		response.setAmount(account.getAmount());
		return response;
	}

	public List<AccountResponse> toResponseList(List<Account> accounts) {
		return accounts.stream().map(this::toResponse).collect(Collectors.toList());
	}

	public Iterable<AccountResponse> toResponseIterable(Iterable<Account> accountIterable) {
		if (accountIterable == null)
			return new ArrayList<>();
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
