package com.homedirect.service.impl;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.homedirect.entity.Account;
import com.homedirect.message.AccountException;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AbstractService;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.transformer.PasswordEncryption;
import com.homedirect.validate.ValidatorInputATM;
import com.homedirect.validate.ValidatorStorageATM;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository accountRepository;
	private @Autowired AccountTransformer accountTransformer;
	private @Autowired ValidatorStorageATM validatorStorageATM;

	// mã hóa password = toMD5();
	@Override
	public AccountResponse creatAcc(AccountRequest request) {
		if (!isValidCreateAccount(request.getUsername(), request.getPassword())) {
			return new AccountResponse();
		}
		Account account = accountTransformer.toAccount(request);
		account.setPassword(PasswordEncryption.toMD5(account.getPassword()));
		accountRepository.save(account);

		return accountTransformer.toResponse(account);
	}

	// login = password mã hóa bằng checkpw().
	@Override
	public AccountResponse login(AccountRequest request) {
		Account account = accountRepository.find(request.getUsername());
		if (account == null) {
			throw new AccountException("Tài khoản không tồn tại!");
		}
		
		if (!BCrypt.checkpw(request.getPassword(), account.getPassword())) {
			throw new AccountException("Nhập sai password!");
		}
		return accountTransformer.toResponse(account);
	}

	// mã hóa password sau khi đổi .
	@Override
	public AccountResponse changePassword(ChangePassRequest changePassRequest) {
		Account account = accountRepository.findById(changePassRequest.getId()).get();
		if (!validatorStorageATM.validateChangePassword(changePassRequest.getOldPassword(),
				changePassRequest.getNewPassword(), account)) {
			accountTransformer.toResponse(account);
			throw new AccountException("Đổi mật khẩu không thành công!");
		}

		account.setPassword(PasswordEncryption.toMD5(changePassRequest.getNewPassword()));
		accountRepository.save(account);
		return accountTransformer.toResponse(account);
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
		Account account = accountRepository.find(request.getUsername());
		if (!BCrypt.checkpw(request.getPassword(), account.getPassword())) {
			throw new AccountException("Tài khoản không tồn tại");
		}
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
		if (!validatorStorageATM.checkUserName(username)) {
			throw new AccountException("Tài khoản đã tồn tại ");
		}
		return true;
	}

	@Autowired
	private AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public void deleteAccountById(int id) {
		accountRepository.deleteById(id);
	}

	@Override
	public List<AccountResponse> findAllAccount() {
		List<Account> accounts = accountRepository.findAll();
		return accountTransformer.toResponseList(accounts);
	}
}
