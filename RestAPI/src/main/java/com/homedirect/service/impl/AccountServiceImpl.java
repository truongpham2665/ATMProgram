package com.homedirect.service.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.entity.Page;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.service.AbstractService;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.PasswordEncryption;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository repository;

	@Autowired
	private AccountServiceImpl(AccountRepository accountRepository) {
		this.repository = accountRepository;
	}

	// mã hóa password = toMD5();chuyển valid sang AccountProcessorImpl.
	@Override
//<<<<<<< HEAD
//	public Account creatAcc(AccountRequest request) {
//		if (!validatorInputATM.isValidCreateAccount(request.getUsername(), request.getPassword())) {
//			throw new ATMException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND_MES);
//		}
//=======
	public Account creatAcc(AccountRequest request) throws ATMException {
//>>>>>>> e5a0ec732aade688f2909f888ba5bdeb9049fbd8
		Account newAccount = new Account();
		newAccount.setId(request.getId());
		newAccount.setAccountNumber(generateAccountNumber());
		newAccount.setUsername(request.getUsername());
		newAccount.setPassword(request.getPassword());
		newAccount.setAmount(Account.Constant.DEFAULT_AMOUNT);
		newAccount.setPassword(PasswordEncryption.toMD5(newAccount.getPassword()));
		repository.save(newAccount);
		return newAccount;
	}

	// login = password mã hóa bằng checkpw().
	@Override
	public Account login(AccountRequest request) throws ATMException {
		Account account = repository.find(request.getUsername());
		if (account == null) {
			throw new ATMException(ErrorCode.NOT_FOUND_USERNAME, ErrorCode.NOT_FOUND_USERNAME_MES,
					request.getUsername());
		}

		if (!BCrypt.checkpw(request.getPassword(), account.getPassword())) {
			throw new ATMException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASWORD_MES, request.getPassword());
		}
		return account;
	}

	// mã hóa password sau khi đổi . chuyển valid sang AccountProcessorImpl.
	@Override
	public Account changePassword(ChangePassRequest changePassRequest) throws ATMException {
		Account account = repository.findById(changePassRequest.getId()).get();
		account.setPassword(PasswordEncryption.toMD5(changePassRequest.getNewPassword()));
		repository.save(account);
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

	// đổi kiểu trả về từ list -> Page
//	@Override
//	public Page<Account> search(String username, int pageNo, int pageSize) {
//		QAccount account = QAccount.account;
//		BooleanBuilder where = new BooleanBuilder();
//		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("username"));
//		if (username != null) {
//			where.and(account.username.containsIgnoreCase(username));
//		}
//		return repository.findAll(where, pageable);
//	}

	// tạo class Page "thay famework Page"
	@Override
	public Page<Account> search(String username, int pageNo, int pageSize) {
		List<Account> accounts = repository.findByUsernameContaining(username);
		return new Page<>(pageNo, pageSize, accounts.size(), accounts);
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return repository.findByAccountNumber(accountNumber);
	}

	@Override
	public Page<Account> findAll(int pageNo, int pageSize) {
		List<Account> accounts = repository.findAll();
		return new Page<>(pageNo, pageSize, accounts.size(), accounts);
	}

	@Override
	public List<Account> findAll() {
		return repository.findAll();
	}
}
