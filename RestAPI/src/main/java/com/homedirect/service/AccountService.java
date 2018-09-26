package com.homedirect.service;

import java.util.List;
import java.util.Optional;

import com.homedirect.entity.Account;
import com.homedirect.message.ATMException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.AccountResponse;

public interface AccountService {

	Account creatAcc(AccountRequest request) throws ATMException;

	Account login(AccountRequest request) throws ATMException;

	Account changePassword(ChangePassRequest changePassRequest) throws ATMException;

	Account getOneAccount(int id);

	List<ATMReponse> searchAccounts(String username, int pageNo, int pageSize);

	Optional<Account> findById(int id);

	Account findByAccountNumber(String accountNumber);

	List<AccountResponse> findAllAccount();

}
