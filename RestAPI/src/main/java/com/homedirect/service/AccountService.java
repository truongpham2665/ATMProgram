package com.homedirect.service;

import java.util.List;
import java.util.Optional;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountService {

	public AccountResponse creatAcc(AccountRequest request);

	public AccountResponse login(AccountRequest request);

	public AccountResponse changePassword(ChangePassRequest changePassRequest);

	AccountResponse getAccount(AccountRequest request);

	AccountResponse getOneAccount(int id);

	List<AccountResponse> searchAccounts(String username, int pageNo, int pageSize);

	Optional<Account> findById(int id);

	Account findByAccountNumber(String accountNumber);
}
