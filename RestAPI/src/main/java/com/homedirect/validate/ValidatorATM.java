package com.homedirect.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.repositories.AccountRepository;

@Component
public class ValidatorATM {

	private @Autowired AccountRepository accountRepository;

	public boolean checkUserName(String username) {
		if (accountRepository.findByUsername(username) != null) {
			return false;
		}
		return true;
	}

	public boolean checkAccountNumbers(String accountNumber) {
		if (accountRepository.findByAccountNumber(accountNumber) == null) {
			return true;
		}
		return false;
	}
}
