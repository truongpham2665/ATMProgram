package com.homedirect.processor.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homedirect.entity.Account;
import com.homedirect.entity.Page;
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
	private @Autowired AccountService accountService;
	private @Autowired AccountTransformer transformer;
	private @Autowired ATMInputValidator validatorInputATM;
	private @Autowired ATMStorageValidator validatorStorageATM;

	@Override
	public AccountResponse login(AccountRequest request) throws ATMException {
		Account account = accountService.login(request);
		return transformer.toResponse(account);
	}

	public AccountResponse create(AccountRequest request) throws ATMException {
		validatorInputATM.isValidCreateAccount(request.getUsername(), request.getPassword());
		Account account = accountService.creatAcc(request);
		return transformer.toResponse(account);
	}

	public Page<AccountResponse> findAll(PageRequest request) {
		Page<Account> accounts = accountService.findAll(request.getPageNo(), request.getPageSize());
		return transformer.toResponse(accounts);
	}

	@Override
	public AccountResponse get(int id) throws ATMException {
		Account account = accountService.findById(id);
		return transformer.toResponse(account);
	}

	@Override
	public AccountResponse changePassword(ChangePassRequest changePassRequest) throws ATMException {
		Account account = accountService.changePassword(changePassRequest);
		validatorStorageATM.validateChangePassword(changePassRequest.getOldPassword(),
				changePassRequest.getNewPassword(), account);
		return transformer.toResponse(account);
	}

	public List<AccountResponse> findAlls() {
		List<Account> accounts = accountService.findAll();
		return transformer.toResponseList(accounts);
	}

//	đổi từ kiểu trả về Page<Account> thành Page<AccountResponse>
	@Override
	public Page<AccountResponse> search(SearchAccountRequest request) throws ATMException {
		return transformer
				.toResponse(accountService.search(request.getUsername(), request.getPageNo(), request.getPageSize()));
	}
}
