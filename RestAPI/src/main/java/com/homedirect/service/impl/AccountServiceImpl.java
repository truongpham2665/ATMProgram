package com.homedirect.service.impl;

import com.homedirect.response.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homedirect.entity.Account;
import com.homedirect.entity.QAccount;
import com.homedirect.repositories.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.impl.AccountTransformerImpl;
import com.querydsl.core.BooleanBuilder;
import com.homedirect.validate.ValidatorATM;

// viết thêm method isValidCreateAccount(), 

@Service
public class AccountServiceImpl extends ServiceAbstract<Account> implements AccountService {

	private @Autowired AccountRepository accountRepository;
	private @Autowired AccountTransformerImpl accountTransformer;

	@Override
	public AccountResponse creatAcc(AccountRequest request) {
		if (!isValidCreateAccount(request.getUsername(), request.getPassword())) {
			return new AccountResponse();
		}
		Account account = accountTransformer.toAccount(request);
		accountRepository.save(account);

		return accountTransformer.toResponse(account);
	}

	// đăng nhập thất bại trả về 1 account null.
	@Override
	public AccountResponse login(AccountRequest request) {
		Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
		if (account == null) {
			new AccountResponse();
			throw new AccountException("Dang nhap that bai");
		}
		return accountTransformer.toResponse(account);
	}

	// nếu sai trả về tài khoản hiện tại. thay return null = return
	// accountTransfomer.toResponse(account)
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

	public Iterable<Account> searchAccounts(String q) {
		QAccount account = QAccount.account;
		BooleanBuilder where = new BooleanBuilder();
		if (q == null || q == "") {
			return null;
		}
		where.or(account.username.containsIgnoreCase(q)).or(account.accountNumber.containsIgnoreCase(q));
		return accountRepository.findAll(where);
	}

	@Override
	public AccountResponse getOneAccount(Integer id) {
		Account account = accountRepository.findById(id).get();
		return accountTransformer.toResponse(account);
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	public boolean checkUserName(String username) {
		if (accountRepository.findByUsername(username) != null) {
			return false;
		}
		return true;
	}

	public AccountResponse getAccount(AccountRequest request) {
		Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
		return accountTransformer.toResponse(account);
	}

	public boolean checkAccountNumbers(String accountNumber) {
		if (accountRepository.findByAccountNumber(accountNumber) == null) {
			return true;
		}
		return false;
	}

	private boolean isValidCreateAccount(String username, String password) {
		if (!ValidatorATM.validateUsername(username)) {
			throw new AccountException("username không hợp lệ");
		}
		if (!ValidatorATM.validatePassword(password)) {
			throw new AccountException("password không hợp lệ");
		}
		if (username == null || password == null) {
			throw new AccountException("yêu cầu nhập đầy đủ thông tin ");
		}
		if (!checkUserName(username)) {
			throw new AccountException("Tài khoản đã tồn tại ");
		}
		return true;
	}

	public AccountResponse getAccountById(int id) {
		return accountTransformer.toResponse(accountRepository.findAll().get(id));
	}

	@Override
	public AccountResponse searchAccount(String username, String accountNumber) {
		if (username == null) {
			return accountTransformer.toResponse(accountRepository.findByAccountNumber(accountNumber));
		}
		if (accountNumber == null) {
			return accountTransformer.toResponse(accountRepository.findByUsername(username));
		}
		return accountTransformer.toResponse(accountRepository.findByUsernameAndAccountNumber(username, accountNumber));
	}
}
