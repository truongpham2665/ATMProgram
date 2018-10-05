package com.homedirect.processor.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.AccountProcessor;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.request.PageRequest;
import com.homedirect.request.SearchAccountRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.validator.ATMInputValidator;
import com.homedirect.validator.ATMStorageValidator;

@Service
public class AccountProcessorImpl implements AccountProcessor {
	private @Autowired AccountService service;
	private @Autowired AccountTransformer transformer;
	private @Autowired ATMInputValidator atmInputValidtor;
	private @Autowired ATMStorageValidator storageValidtor;

	@Override
	public AccountResponse login(AccountRequest request) throws ATMException {
		Account account = service.login(request);
		storageValidtor.validateLogin(request, account);
		return transformer.toResponse(account);
	}

	public AccountResponse create(AccountRequest request) throws ATMException {
		atmInputValidtor.isValidCreateAccount(request.getUsername(), request.getPassword());
		Account account = service.creatAcc(request);
		return transformer.toResponse(account);
	}

	public Page<AccountResponse> findAll(PageRequest request) {
		Page<Account> accounts = service.findAll(request.getPageNo(), request.getPageSize());
		return transformer.toResponse(accounts);
	}

	@Override
	public AccountResponse get(int id) throws ATMException {
		Account account = service.findById(id);
		return transformer.toResponse(account);
	}

	@Override
	public AccountResponse changePassword(ChangePassRequest changePassRequest) throws ATMException {
		storageValidtor.validateChangePassword(changePassRequest);
		Account account = service.changePassword(changePassRequest);
		service.save(account);
		return transformer.toResponse(account);
	}

	public List<AccountResponse> findAlls() {
		List<Account> accounts = service.findAlls();
		return transformer.toResponseList(accounts);
	}

	@Override
	public Page<AccountResponse> search(SearchAccountRequest request) throws ATMException {
		return transformer.toResponse(service.search(request.getUsername(), request.getPageNo(), request.getPageSize()));
	}
	
	@Override
	public void exportCsv() throws Exception {
		service.exportCsv();
	}
}
