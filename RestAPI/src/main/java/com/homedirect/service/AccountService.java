package com.homedirect.service;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountService {
	
	public AccountResponse creatAcc(AccountRequest request);

	public AccountResponse login(AccountRequest request);

	public AccountResponse changePassword(ChangePassRequest changePassRequest);
	
	AccountResponse getAccount(AccountRequest request);
	
	AccountResponse getOneAccount(Integer id);
	
	Account findByAccountNumber(String accountNumber);
	
	AccountResponse searchAccount(String username, String accountNumber);
	
	boolean checkAccountNumbers(String accountNumber);
}
