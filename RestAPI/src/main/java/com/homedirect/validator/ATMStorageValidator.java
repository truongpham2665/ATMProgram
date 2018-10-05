package com.homedirect.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.AccountRepository;

@Component
public class ATMStorageValidator {

	private @Autowired AccountRepository repository;

	public boolean checkUserNameExist(String username) {
		if (repository.find(username) != null) {
			throw new ATMException(ErrorCode.USERNAME_EXIST, ErrorCode.USERNAME_EXIST_MES, username);
		}
		return true;
	}

	public boolean checkId(int id) {
		if (repository.findById(id) == null) {
			return true;
		}
		return false;
	}
	
	public Account validateId(int id) {
		Optional<Account> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new ATMException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND_MES, id);
		}

		return optional.get();
	}

	public boolean checkAccountNumbers(String accountNumber) {
		if (repository.findByAccountNumber(accountNumber) == null) {
			return true;
		}
		return false;
	}

	public boolean checkUsername(String username) {
		Account account = repository.find(username);
		if (account == null) {
			throw new ATMException(ErrorCode.NOT_FOUND_USERNAME, ErrorCode.NOT_FOUND_USERNAME_MES, username);
		}
		return true;
	}
	
	public Account validateUsername(String username) {
		Account account = repository.find(username);
		if (account == null) {
			throw new ATMException(ErrorCode.NOT_FOUND_USERNAME, ErrorCode.NOT_FOUND_USERNAME_MES, username);
		}
		return account;
	}

	public Account validateLogin(String username, String password) throws ATMException {
		Account account = repository.find(username);
		if (account == null) {
			throw new ATMException(ErrorCode.NOT_FOUND_USERNAME, ErrorCode.NOT_FOUND_USERNAME_MES, username);
		}
		ATMInputValidator.checkPasswordByAccount(password, account);
		return account;
	}

	public boolean validateChangePassword(String oldPassword, String newPassword, Account account) throws ATMException {
		if (oldPassword == null || newPassword == null) {
			throw new ATMException(ErrorCode.MISS_DATA, ErrorCode.MISS_DATA_MES);
		}
		ATMInputValidator.checkPasswordByAccount(oldPassword, account);
		if (!ATMInputValidator.isValidPassword(newPassword)) {
			throw new ATMException(ErrorCode.INVALID_INPUT_PASSWORD, ErrorCode.INVALID_INPUT_PASWORD_MES);
		}
		if (newPassword.equals(oldPassword)) {
			throw new ATMException(ErrorCode.DUPLICATE_PASSWORD, ErrorCode.DUPLICATE_PASSWORD_MES);
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
