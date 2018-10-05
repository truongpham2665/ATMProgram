package com.homedirect.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;

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

//<<<<<<< HEAD
//	public boolean validateChangePassword(ChangePassRequest request) throws ATMException {
//		if (request.getOldPassword() == null || request.getNewPassword() == null) {
//			throw new ATMException(ErrorCode.MISS_DATA, ErrorCode.MISS_DATA_MES);
//		}
//		if (!ATMInputValidator.isValidPassword(request.getNewPassword())) {
//=======
//	public Account validateLogin(String username, String password) throws ATMException {
//		Account account = accountRepository.find(username);
//		if (account == null) {
//			throw new ATMException(ErrorCode.NOT_FOUND_USERNAME, ErrorCode.NOT_FOUND_USERNAME_MES, username);
//		}
//		ATMInputValidator.checkPasswordByAccount(password, account);
//		return account;
//	}

	public boolean validateChangePassword(String oldPassword, String newPassword, Account account) throws ATMException {
		if (oldPassword == null || newPassword == null) {
			throw new ATMException(ErrorCode.MISS_DATA, ErrorCode.MISS_DATA_MES);
		}
		ATMInputValidator.checkPasswordByAccount(oldPassword, account);
		if (!ATMInputValidator.isValidPassword(newPassword)) {
			throw new ATMException(ErrorCode.INVALID_INPUT_PASSWORD, ErrorCode.INVALID_INPUT_PASWORD_MES);
		}
		return true;
	}

	public boolean validateLogin(AccountRequest request, Account account) {
		if (account == null) {
			throw new ATMException(ErrorCode.NOT_FOUND_USERNAME, ErrorCode.NOT_FOUND_USERNAME_MES,
					request.getUsername());
		}
		ATMInputValidator.checkPasswordByAccount(request.getPassword(), account);
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
