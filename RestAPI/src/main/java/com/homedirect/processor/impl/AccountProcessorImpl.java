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
import com.homedirect.request.SearchAccountRequest;
import com.homedirect.request.UpdateAccountRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.validator.ATMInputValidator;
import com.homedirect.validator.ATMStorageValidator;

@Service
public class AccountProcessorImpl implements AccountProcessor {

	private @Autowired AccountService service;
	private @Autowired AccountTransformer transformer;
	private @Autowired ATMInputValidator validatorInputATM;
	private @Autowired ATMStorageValidator validatorStorageATM;

	@Override
	public AccountResponse login(AccountRequest request) throws ATMException {
		Account account = service.login(request.getUsername(), request.getPassword());
		return transformer.toResponse(account);
	}

	public AccountResponse create(AccountRequest request) throws ATMException {
		validatorInputATM.isValidCreateAccount(request.getUsername(), request.getPassword());
		Account account = service.creatAcc(request);
		service.save(account);
		return transformer.toResponse(account);
	}

	public List<Account> findAll() {
		return service.findAll();
	}

	@Override
	public AccountResponse get(int id) throws ATMException {
		Account account = service.findById(id);
		return transformer.toResponse(account);
	}

	@Override
	public AccountResponse changePassword(ChangePassRequest changePassRequest) throws ATMException {
		Account account = service.findById(changePassRequest.getId());
		validatorStorageATM.validateChangePassword(changePassRequest.getOldPassword(),
				changePassRequest.getNewPassword(), account);
		Account saveAccont = service.changePassword(account, changePassRequest.getNewPassword());
		service.save(saveAccont);
		return transformer.toResponse(account);
	}

	@Override
	public Page<AccountResponse> search(SearchAccountRequest request) throws ATMException {
		Account account = validatorStorageATM.validateUsername(request.getUsername());
		return service.search(account.getUsername(), request.getPageNo(), request.getPageSize());
	}

	@Override
	public AccountResponse updateAccount(UpdateAccountRequest request) {
		Account account = service.findById(request.getId());
		validatorStorageATM.checkUserNameExist(request.getUsername());
		Account updateAccount = service.updateAccount(account, request.getUsername());
		service.save(updateAccount);
		return transformer.toResponse(account);
	}

	@Override
	public void deleteById(int id) {
		Account account = service.findById(id);
		service.deleteAccount(account);
	}
}
