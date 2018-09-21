package com.homedirect.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
