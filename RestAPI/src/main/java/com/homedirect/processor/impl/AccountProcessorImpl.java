package com.homedirect.processor.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.AccountProcessor;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;

@Service
public class AccountProcessorImpl implements AccountProcessor {
	private @Autowired AccountService accountService;
	private @Autowired AccountTransformer transformer;
	
	@Override
	public AccountResponse login(@RequestBody AccountRequest request) throws ATMException {
		Account account = accountService.login(request);
		return transformer.toResponse(account);
	}
	
	public AccountResponse create(@RequestBody AccountRequest request) throws ATMException {
		Account account = accountService.creatAcc(request);
		return transformer.toResponse(account);
	}
	
	public List<AccountResponse> findAll() {
		List<Account> accounts = accountService.findAll();
		return transformer.toResponseList(accounts);
	}

	@Override
	public AccountResponse get(int id) throws ATMException {
		Account account = accountService.findById(id);
		return transformer.toResponse(account);
	}

	@Override
	public AccountResponse changePassword(ChangePassRequest changePassRequest) throws ATMException {
		Account account = accountService.changePassword(changePassRequest);
		return transformer.toResponse(account);
	}

	@Override
	public List<AccountResponse> search(String username, int pageNo, int pageSize) throws ATMException {
		List<Account> accounts = accountService.searchAccounts(username, pageNo, pageSize);
		return transformer.toResponseList(accounts);
	}
}
