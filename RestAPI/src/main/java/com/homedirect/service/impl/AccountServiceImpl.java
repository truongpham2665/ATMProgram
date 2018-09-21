package com.homedirect.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homedirect.entity.Account;
import com.homedirect.entity.QAccount;
import com.homedirect.message.AccountException;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.validate.ValidatorStorageATM;
import com.homedirect.validate.ValidatorInputATM;
import com.querydsl.core.BooleanBuilder;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository accountRepository;
	private @Autowired AccountTransformer accountTransformer;
	private @Autowired ValidatorStorageATM validatorATM;

	@Override
	public AccountResponse creatAcc(AccountRequest request) {
		if (!isValidCreateAccount(request.getUsername(), request.getPassword())) {
			return new AccountResponse();
		}
		Account account = accountTransformer.toAccount(request);
		accountRepository.save(account);

		return accountTransformer.toResponse(account);
	}

	@Override
	public AccountResponse login(AccountRequest request) {
		Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
		if (account == null) {
			new AccountResponse();
			throw new AccountException("Đăng nhập thất bại");
		}
		return accountTransformer.toResponse(account);
	}

	@Override
	public AccountResponse changePassword(ChangePassRequest changePassRequest) {
		Account account = accountRepository.findById(changePassRequest.getId()).get();
		if (!changePassRequest.getOldPassword().equals(account.getPassword())
				|| changePassRequest.getNewPassword() == null) {
			accountTransformer.toResponse(account);
			throw new AccountException("Đổi mật khẩu không thành công!");
		}

		account.setPassword(changePassRequest.getNewPassword());
		accountRepository.save(account);
		return accountTransformer.toResponse(account);
	}

	@Override
	public Iterable<AccountResponse> searchAccounts(String q) {
		QAccount account = QAccount.account;
		BooleanBuilder where = new BooleanBuilder();
		if (q == null || q == "") {
			return null;
		}
		where.or(account.username.containsIgnoreCase(q)).or(account.accountNumber.containsIgnoreCase(q));
		return accountTransformer.toResponseIterable(accountRepository.findAll(where));
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

	private boolean isValidCreateAccount(String username, String password) {
		if (!ValidatorInputATM.validateUsername(username)) {
			throw new AccountException("username không hợp lệ");
		}
		if (!ValidatorInputATM.validatePassword(password)) {
			throw new AccountException("password không hợp lệ");
		}
		if (username == null || password == null) {
			throw new AccountException("yêu cầu nhập đầy đủ thông tin ");
		}
		if (!validatorATM.checkUserName(username)) {
			throw new AccountException("Tài khoản đã tồn tại ");
		}
		return true;
	}
}
