package com.homedirect.processor;

import java.util.List;

import com.homedirect.exception.ATMException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountProcessor {
	
	AccountResponse login(AccountRequest request) throws ATMException;

	AccountResponse create(AccountRequest request) throws ATMException;

	List<AccountResponse> findAll() throws ATMException;

	AccountResponse get(int id) throws ATMException;

	AccountResponse changePassword(ChangePassRequest changePassRequest) throws ATMException;

	List<AccountResponse> search(String username, int pageNo, int pageSize) throws ATMException;
}
