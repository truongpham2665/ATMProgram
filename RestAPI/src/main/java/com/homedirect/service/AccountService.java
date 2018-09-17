package com.homedirect.service;

import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountService {
	
	public AccountResponse creatAcc(AccountRequest request);

	public AccountResponse login(AccountRequest request);

	public AccountResponse changePassWord(ChangePassRequest changePassRequest);
}
