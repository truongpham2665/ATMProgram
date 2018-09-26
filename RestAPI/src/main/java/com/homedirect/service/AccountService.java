package com.homedirect.service;

import java.util.List;
import java.util.Optional;

import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;

public interface AccountService {

	Account creatAcc(AccountRequest request) throws ATMException;

	Account login(AccountRequest request) throws ATMException;

	Account changePassword(ChangePassRequest changePassRequest) throws ATMException;

	Account getOneAccount(int id);

	List<Account> searchAccounts(String username, int pageNo, int pageSize);

	Optional<Account> findById(int id);

	Account findByAccountNumber(String accountNumber);

	List<Account> findAllAccount();

}
