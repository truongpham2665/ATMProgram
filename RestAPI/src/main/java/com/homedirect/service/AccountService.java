package com.homedirect.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;

public interface AccountService {

	Account creatAcc(AccountRequest request) throws ATMException;

	Account login(AccountRequest request) throws ATMException;

	Account changePassword(ChangePassRequest changePassRequest) throws ATMException;
	
	Page<Account> search(String username, int pageNo, int pageSize);
	
	Account findByAccountNumber(String accountNumber);

	List<Account> findAll();
	
	Account findById(int id) throws ATMException;
	
	String generateAccountNumber();
}
