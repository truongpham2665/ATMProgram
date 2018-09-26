package com.homedirect.validator;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.exception.MessageException;
import com.homedirect.repository.AccountRepository;

@Component
public class ATMStorageValidator {

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

	// thêm điều kiện checkpw();
	public boolean validateChangePassword(String oldPassword, String newPassword, Account account) throws ATMException {
		if (oldPassword == null || newPassword == null) {
			throw new ATMException(MessageException.missField());
		}
		if (!BCrypt.checkpw(oldPassword, account.getPassword())) {
			throw new ATMException(MessageException.passwordIsValid());
		}
		if (!ATMInputValidator.isValidPassword(newPassword)) {
			throw new ATMException(MessageException.passwordIsValid());
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
