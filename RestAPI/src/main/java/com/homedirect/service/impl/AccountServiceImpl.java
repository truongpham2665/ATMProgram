package com.homedirect.service.impl;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.entity.QAccount;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.service.AbstractService;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.PasswordEncryption;
import com.homedirect.util.CsvUtil;
import com.querydsl.core.BooleanBuilder;

@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

	private @Autowired AccountRepository repository;

	@Autowired
	private AccountServiceImpl(AccountRepository accountRepository) {
		this.repository = accountRepository;
	}

	@Override
	public Account creatAcc(AccountRequest request) throws ATMException {
		Account newAccount = new Account();
		newAccount.setId(request.getId());
		newAccount.setAccountNumber(generateAccountNumber());
		newAccount.setUsername(request.getUsername());
		newAccount.setPassword(request.getPassword());
		newAccount.setAmount(Account.Constant.DEFAULT_AMOUNT);
		newAccount.setPassword(PasswordEncryption.encryp(newAccount.getPassword()));
		save(newAccount);
		return newAccount;
	}

	@Override
	public Account login(AccountRequest request) throws ATMException {
		Account account = repository.find(request.getUsername());
		return account;
	}

	@Override
	public Account changePassword(ChangePassRequest changePassRequest) throws ATMException {
		Account account = findById(changePassRequest.getId());
		if (!BCrypt.checkpw(changePassRequest.getOldPassword(), account.getPassword())) {
			throw new ATMException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASWORD_MES);
		}
		account.setPassword(PasswordEncryption.encryp(changePassRequest.getNewPassword()));
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
		QAccount account = QAccount.account;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		BooleanBuilder where = new BooleanBuilder();
		if (username != null) {
			where.and(account.username.containsIgnoreCase(username));
		}
		return repository.findAll(where, pageable);
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		return repository.findByAccountNumber(accountNumber);
	}

	@Override
	public Page<Account> findAll(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		BooleanBuilder where = new BooleanBuilder();
		return repository.findAll(where, pageable);
	}

	@Override
	public List<Account> findAlls() {
		return findAll();
	}
	
	@Override
	@Scheduled(cron = "0 47 13 ? * MON-FRI")
	public void exportCsv() throws Exception {
		String fileCsv = System.getProperty("user.dir")+ "/src/main/resources/Account.csv";
		List<Account> accountResponses = repository.findAll();
		FileWriter writeFile = new FileWriter(fileCsv);
		CsvUtil.writerLine(writeFile, Arrays.asList("id", "accountNumber", "username", "amount"));
		for (Account account : accountResponses) {
			List<String> list = new ArrayList<>();
			list.add(String.valueOf(account.getId()));
			list.add(account.getAccountNumber());
			list.add(account.getUsername());
			list.add(String.valueOf(account.getAmount()));
			CsvUtil.writerLine(writeFile, list);
		}
		writeFile.flush();
		writeFile.close();
	}
}
