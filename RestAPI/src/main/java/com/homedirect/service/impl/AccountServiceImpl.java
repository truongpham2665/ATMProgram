package com.homedirect.service.impl;

import java.text.DecimalFormat;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homedirect.constant.ConstantAccount;
import com.homedirect.entity.Account;
import com.homedirect.entity.QAccount;
import com.homedirect.repositories.AccountRepository;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.service.AccountService;
import com.homedirect.util.Input;
import com.homedirect.util.Notification;
import com.homedirect.util.ValidatorATM;
import com.querydsl.core.BooleanBuilder;

@Service
public class AccountServiceImpl extends ServiceAbstract<Account> implements AccountService {

	private @Autowired AccountRepository accountRepository;
	private String pattern = "22";

	@Override
	public boolean creatAcc(String userName, String passWord) {
		ValidatorATM.validateUsername(userName);
		ValidatorATM.validatePassword(passWord);
		if (userName == null || passWord == null) {
			return false;
		}
		if (!checkUserName(userName)) {
			Notification.alert("Tai khoan da ton tai");
			return false;
		}

		Account newAccount = new Account(userName, generateAccountNumber(pattern), passWord,
				ConstantAccount.DEFAULT_AMOUNT);
		save(newAccount);
		return true;
	}

	@Override
	public Account login() {
		String userName = Input.inputString("Ten tai khoan: ");
		String passWord = Input.inputString("Mat khau: ");
		Account account = getAccount(userName, passWord);
		if (account == null) {
			Notification.alert("Dang nhap that bai");
			return account;
		}
		Notification.alert("Dang nhap thanh cong");
		return account;
	}

	@Override
	public boolean changePassWord(ChangePassRequest changePassRequest) {
		Account account = accountRepository.findById(changePassRequest.getId()).get();
		if (changePassRequest.getOldPass().equals(account.getPassWord())) {
			account.setPassWord(changePassRequest.getNewPass());
			accountRepository.save(account);
			return true;
		}
		return false;
	}
	
	public Iterable<Account> searchAccounts(String q) {
		QAccount account = QAccount.account;
		BooleanBuilder where = new BooleanBuilder();
		if (q != null) {
			where.and(account.userName.containsIgnoreCase(q));
		}
		return accountRepository.findAll(where);
	}

	public String generateAccountNumber(String pattern) {
		Random rd = new Random();
		int max = 9999;
		int accountNumber = rd.nextInt(max);
		DecimalFormat format = new DecimalFormat("0000");
		String outAccountNumber = pattern + format.format(accountNumber);
		while (!checkAccountNumbers(outAccountNumber)) {
			generateAccountNumber(pattern);
		}
		return outAccountNumber;
	}

	public boolean checkAccountNumbers(String accountNumber) {
		if (accountRepository.findByAccountNumber(accountNumber) == null) {
			return true;
		}
		return false;
	}

	public Account getAccountByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	public boolean checkUserName(String userName) {
		if (accountRepository.findByUserName(userName) == null) {
			return true;
		}
		return false;
	}

	public Account getAccount(String userName, String passWord) {
		return accountRepository.findByUserNameAndPassWord(userName, passWord);
	}

}
