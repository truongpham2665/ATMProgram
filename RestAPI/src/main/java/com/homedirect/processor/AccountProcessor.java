package com.homedirect.processor;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.homedirect.exception.ATMException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;

public interface AccountProcessor {
	AccountResponse login(AccountRequest request) throws ATMException;
	
	AccountResponse create(@RequestBody AccountRequest request) throws ATMException;
	
	List<AccountResponse> findAll() throws ATMException;
	
	AccountResponse get(@PathVariable int id) throws ATMException;
	
	AccountResponse changePassword(@RequestBody ChangePassRequest changePassRequest) throws ATMException;
	
	List<AccountResponse> search(@RequestParam String username, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) throws ATMException;
}
