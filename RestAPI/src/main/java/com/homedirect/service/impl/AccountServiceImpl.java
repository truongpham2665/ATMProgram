package com.homedirect.service.impl;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.service.AbstractService;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.transformer.PasswordEncryption;
import com.homedirect.validator.ATMInputValidator;
import com.homedirect.validator.ATMStorageValidator;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository accountRepository;
	private @Autowired AccountTransformer accountTransformer;
	private @Autowired ATMStorageValidator validatorStorageATM;
	private @Autowired ATMInputValidator validatorInputATM;

	@Autowired
	private AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	// mã hóa password = toMD5();
	@Override
	public Account creatAcc(AccountRequest request) throws ATMException {
		if (!validatorInputATM.isValidCreateAccount(request.getUsername(), request.getPassword())) {
			throw new ATMException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND_MES);
		}
		Account account = accountTransformer.toAccount(request);
		account.setPassword(PasswordEncryption.toMD5(account.getPassword()));
		accountRepository.save(account);
		return account;
	}

	// login = password mã hóa bằng checkpw().
	@Override
	public Account login(AccountRequest request) throws ATMException {
		Account account = accountRepository.find(request.getUsername());
		if (account == null) {
			throw new ATMException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND_MES);
		}

		if (!BCrypt.checkpw(request.getPassword(), account.getPassword())) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_DATA_MES);
		}
		return account;
	}

	// mã hóa password sau khi đổi .
	@Override
	public Account changePassword(ChangePassRequest changePassRequest) throws ATMException {
		Account account = accountRepository.findById(changePassRequest.getId()).get();
		if (!validatorStorageATM.validateChangePassword(changePassRequest.getOldPassword(),
				changePassRequest.getNewPassword(), account)) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_DATA_MES);
		}

		account.setPassword(PasswordEncryption.toMD5(changePassRequest.getNewPassword()));
		accountRepository.save(account);
		return account;
	}

	@Override
	public List<Account> searchAccounts(String username, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("username"));
		return accountRepository.findByUsernameContaining(username, pageable);
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}
}
