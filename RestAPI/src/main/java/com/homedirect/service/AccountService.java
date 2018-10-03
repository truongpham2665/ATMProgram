package com.homedirect.service;

import java.util.List;

import com.homedirect.entity.Account;
import com.homedirect.entity.Page;
import com.homedirect.exception.ATMException;
import com.homedirect.request.AccountRequest;

public interface AccountService {

	Account creatAcc(AccountRequest request) throws ATMException;

	Account login(String username, String password) throws ATMException;

	Account changePassword(Account account, String password) throws ATMException;
	
	Page<Account> search(String username, int pageNo, int pageSize);
	
	Account findByAccountNumber(String accountNumber);

	Page<Account> findAll(int pageNo, int pageSize);
	
	Account findById(int id) throws ATMException;
	
	List<Account> findAll();
	
	String generateAccountNumber();

	Account save(Account account);
}
