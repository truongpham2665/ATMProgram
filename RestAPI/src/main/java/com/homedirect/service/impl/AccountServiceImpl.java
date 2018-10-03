package com.homedirect.service.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.entity.Page;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.service.AbstractService;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.PasswordEncryption;
import com.homedirect.validator.ATMStorageValidator;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository repository;
	private @Autowired ATMStorageValidator validatorStorageATM;

	@Autowired
	private AccountServiceImpl(AccountRepository accountRepository) {
		this.repository = accountRepository;
	}

	public Account creatAcc(AccountRequest request) throws ATMException {
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
	public Account login(String username, String password) throws ATMException {
		return validatorStorageATM.validateLogin(username, password);
	}

	@Override
	public Account changePassword(Account account, String password) throws ATMException {
		account.setPassword(PasswordEncryption.toMD5(password));
		return account;
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
	public Page<Account> search(String username, int pageNo, int pageSize) {
		List<Account> accounts = repository.findByUsernameContaining(username);
		float totalElement = accounts.size();
		float totalPage = (float) Math.ceil(totalElement/pageSize);
		return new Page<>(pageNo, pageSize, totalElement, totalPage, accounts);
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return repository.findByAccountNumber(accountNumber);
	}

	@Override
	public Page<Account> findAll(int pageNo, int pageSize) {
		List<Account> accounts = repository.findAll();
		int totalElement = accounts.size();
		int totalPage = (int) Math.ceil(totalElement/pageSize);
		System.out.println(totalPage);
		return new Page<>(pageNo, pageSize, totalElement, totalPage, accounts);
	}

	@Override
	public List<Account> findAll() {
		return repository.findAll();
	}

	@Override
	public Account findById(int id) throws ATMException {
		Optional<Account> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new ATMException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND_MES, id);
		}

		return optional.get();
	}
}
