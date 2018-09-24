package com.homedirect.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Account;
import com.homedirect.repository.AccountRepository;

@Component
public class ValidatorStorageATM {

	private @Autowired AccountRepository accountRepository;

	public boolean checkUserName(String username) {
		if (accountRepository.findByUsername(username) != null) {
			return false;
		}
		return true;
	}

	public boolean checkId(int id) {
		if (accountRepository.findById(id) == null) {
			return true;
		}
		return false;
	}

	public boolean checkAccountNumbers(String accountNumber) {
		if (accountRepository.findByAccountNumber(accountNumber) == null) {
			return true;
		}
		return false;
	}

	public boolean validateChangePassword(String oldPassword, String newPassword, Account account) {
		if ((!oldPassword.equals(account.getPassword())
				|| newPassword == null)
				|| !ValidatorInputATM.isValidPassword(newPassword)) {
			return false;
		}
		return true;
	}

	public static boolean validatorDeposit(Double amount) {
		if (amount == null) {
			return true;
		}
		if (amount <= 0 || amount % 10000 != 0) {
			return true;
		}
		return false;
	}
}
