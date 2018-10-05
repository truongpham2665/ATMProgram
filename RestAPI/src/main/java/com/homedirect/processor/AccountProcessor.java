package com.homedirect.processor;

import org.springframework.data.domain.Page;
import com.homedirect.exception.ATMException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.request.SearchAccountRequest;
import com.homedirect.request.UpdateAccountRequest;
import com.homedirect.response.AccountResponse;

public interface AccountProcessor {

	AccountResponse login(AccountRequest request) throws ATMException;

	AccountResponse create(AccountRequest request) throws ATMException;

	AccountResponse changePassword(ChangePassRequest changePassRequest) throws ATMException;

	AccountResponse get(int id) throws ATMException;

	void exportCsv() throws Exception;

	Page<AccountResponse> search(SearchAccountRequest request) throws ATMException;

	AccountResponse updateAccount(UpdateAccountRequest request);

	void deleteById(int id);
}
