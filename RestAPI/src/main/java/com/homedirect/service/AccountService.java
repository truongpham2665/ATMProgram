package com.homedirect.service;

import java.util.Optional;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountService {

	public AccountResponse creatAcc(AccountRequest request);

	public AccountResponse login(AccountRequest request);

	public AccountResponse changePassword(ChangePassRequest changePassRequest);

	AccountResponse getAccount(AccountRequest request);

	AccountResponse getOneAccount(int id);

	Iterable<AccountResponse> searchAccounts(String q);

	Optional<Account> findById(int id);

	Account findByAccountNumber(String accountNumber);
	
	void deleteAccountById(int id);
	
}
