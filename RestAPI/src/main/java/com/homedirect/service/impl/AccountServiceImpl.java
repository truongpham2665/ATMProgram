package com.homedirect.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.homedirect.entity.Account;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.validate.ValidatorInputATM;
import com.homedirect.validate.ValidatorStorageATM;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository accountRepository;
	private @Autowired AccountTransformer accountTransformer;
	private @Autowired ValidatorStorageATM validatorStorageATM;
	private @Autowired ValidatorInputATM validatorInputATM;

	@Override
	public Account creatAcc(AccountRequest request) {
		if (!validatorInputATM.isValidCreateAccount(request.getUsername(), request.getPassword())) {
			return null;
		}
		Account account = accountTransformer.toAccount(request);
		accountRepository.save(account);
		return account;
	}

	@Override
	public Account login(AccountRequest request) {
		Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
		if (account == null) {
			System.out.println(account);
			return null;
		}
		return account;
	}

//	@Override
//	public Account changePassword(ChangePassRequest changePassRequest) {
//		Account account = accountRepository.findById(changePassRequest.getId()).get();
//		if (!validatorStorageATM.validateChangePassword(changePassRequest.getOldPassword(),
//				changePassRequest.getNewPassword(), account)) {
//			accountTransformer.toResponse(account);
//		}
//		account.setPassword(changePassRequest.getNewPassword());
//		accountRepository.save(account);
//		return account;
//	}

	@Override
	public Account changePassword(ChangePassRequest changePassRequest) {
		Account account = accountRepository.findById(changePassRequest.getId()).get();
		if (!validatorStorageATM.validateChangePassword(changePassRequest.getOldPassword(),
				changePassRequest.getNewPassword(), account)) {
			return null;
		}

		account.setPassword(changePassRequest.getNewPassword());
		accountRepository.save(account);
		return account;
	}

	@Override
	public List<AccountResponse> searchAccounts(String username, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("username"));
		List<Account> accounts = accountRepository.findByUsernameContaining(username, pageable);
		return accountTransformer.toResponseList(accounts);
	}

	@Override
	public AccountResponse getOneAccount(int id) {
		Optional<Account> account = accountRepository.findById(id);
		return accountTransformer.toResponse(account.get());
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	@Override
	public AccountResponse getAccount(AccountRequest request) {
		Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
		return accountTransformer.toResponse(account);
	}

	@Autowired
	private AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public List<AccountResponse> findAllAccount() {
		List<Account> accounts = accountRepository.findAll();
		return accountTransformer.toResponseList(accounts);
	}
}
