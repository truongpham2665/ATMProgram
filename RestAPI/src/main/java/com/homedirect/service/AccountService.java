package com.homedirect.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.response.AccountResponse;

public interface AccountService {

	Account creatAcc(AccountRequest request);

	Account login(String username, String password);

	Account changePassword(Account account, String password);

	Page<AccountResponse> search(String username, int pageNo, int pageSize);

	Account findByAccountNumber(String accountNumber);

	Account findById(int id);

	List<Account> findAll();

	String generateAccountNumber();

	Account save(Account account);
	
	Account updateAccount(Account account, String username);
	
	void deleteAccount(Account account);
}
