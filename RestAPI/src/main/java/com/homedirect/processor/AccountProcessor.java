package com.homedirect.processor;

import java.util.List;

import org.springframework.data.domain.Page;

import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountProcessor {
	AccountResponse login(AccountRequest request) throws ATMException;
	
	AccountResponse create(AccountRequest request) throws ATMException;

	List<AccountResponse> findAll() throws ATMException;
	
	AccountResponse changePassword(ChangePassRequest changePassRequest) throws ATMException;
	
	AccountResponse get(int id) throws ATMException;
	
	Page<Account> search(String username, int pageNo, int pageSize) throws ATMException;
}
