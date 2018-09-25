package com.homedirect.service;

import java.util.List;
import java.util.Optional;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountService {

	Account creatAcc(AccountRequest request);

	Account login(AccountRequest request);

	Account changePassword(ChangePassRequest changePassRequest);

	AccountResponse getAccount(AccountRequest request);

	AccountResponse getOneAccount(int id);

	List<AccountResponse> searchAccounts(String username, int pageNo, int pageSize);

	Optional<Account> findById(int id);

	Account findByAccountNumber(String accountNumber);

	List<AccountResponse> findAllAccount();

}
