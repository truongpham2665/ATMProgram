package com.homedirect.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Account;
import com.homedirect.message.AccountException;
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
		if (oldPassword == null || newPassword == null) {
			throw new AccountException("Nhập thiếu trường pasword");
		}

		if (!oldPassword.equals(account.getPassword())) {
			throw new AccountException("Password hiện tại không đúng");
		}

		if (!ValidatorInputATM.isValidPassword(newPassword)) {
			throw new AccountException("Password phải có ít nhất 1 ký tự in hoa, 1 chữ thường, 1 ký tự đặc biệt, 1 chữ số và độ dài tối thiểu 8 ký tự");
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
