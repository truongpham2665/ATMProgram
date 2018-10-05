package com.homedirect.service.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.homedirect.entity.Account;
import com.homedirect.entity.QAccount;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AbstractService;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.transformer.PasswordEncryption;
import com.homedirect.validator.ATMStorageValidator;
import com.querydsl.core.BooleanBuilder;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository repository;
	private @Autowired ATMStorageValidator validatorStorageATM;
	private @Autowired AccountTransformer transformer;

	@Autowired
	private AccountServiceImpl(AccountRepository accountRepository) {
		this.repository = accountRepository;
	}

	public Account creatAcc(AccountRequest request) {
		Account newAccount = new Account();
		newAccount.setId(request.getId());
		newAccount.setAccountNumber(generateAccountNumber());
		newAccount.setUsername(request.getUsername());
		newAccount.setPassword(request.getPassword());
		newAccount.setAmount(Account.Constant.DEFAULT_AMOUNT);
		newAccount.setPassword(PasswordEncryption.toMD5(newAccount.getPassword()));
		return newAccount;
	}

	// chuyển check cũ trong Login -> validatorLogin
	@Override
	public Account login(String username, String password) {
		return validatorStorageATM.validateLogin(username, password);
	}

	@Override
	public Account changePassword(Account account, String password) {
		account.setPassword(PasswordEncryption.toMD5(password));
		return account;
	}

	@Override
	public Account updateAccount(Account account, String username) {
		account.setUsername(username);
		return account;
	}
	
	@Override
	public void deleteAccount(Account account) {
		repository.delete(account);
	}

	public String generateAccountNumber() {
		String pattern = "22";
		Random rd = new Random();
		int max = 9999;
		int accountNumber = rd.nextInt(max);
		DecimalFormat format = new DecimalFormat("0000");
		String outAccountNumber = pattern + format.format(accountNumber);
		return outAccountNumber;
	}

	@Override
	public Page<AccountResponse> search(String username, int pageNo, int pageSize) {
		Pageable pageable = null;
		BooleanBuilder where = null;
		QAccount qAccount = QAccount.account;
		pageable = PageRequest.of(pageNo, pageSize);
		where = new BooleanBuilder();
		where.and(qAccount.username.eq(username));
		return transformer.toResponse(repository.findAll(where, pageable));
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return repository.findByAccountNumber(accountNumber);
	}

	@Override
	public List<Account> findAll() {
		return repository.findAll();
	}

	@Override
	public Account findById(int id) {
		return validatorStorageATM.validateId(id);
	}
}
