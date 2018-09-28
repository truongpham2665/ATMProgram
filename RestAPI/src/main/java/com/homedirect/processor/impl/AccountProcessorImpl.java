package com.homedirect.processor.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.AccountProcessor;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
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
	public AccountResponse login(@RequestBody AccountRequest request) throws ATMException {
		Account account = accountService.login(request);
		return transformer.toResponse(account);
	}

	public AccountResponse create(@RequestBody AccountRequest request) throws ATMException {
		if (!validatorInputATM.isValidCreateAccount(request.getUsername(), request.getPassword())) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_INPUT_MES);
		}

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
		if (!validatorStorageATM.validateChangePassword(changePassRequest.getOldPassword(),
				changePassRequest.getNewPassword(), account)) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_DATA_MES);
		}

		return transformer.toResponse(account);
	}

	@Override
	public Page<Account> search(String username, int pageNo, int pageSize) throws ATMException {
		return accountService.search(username, pageNo, pageSize);
	}
}
