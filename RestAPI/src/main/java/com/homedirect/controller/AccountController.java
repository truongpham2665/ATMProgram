package com.homedirect.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.impl.AccountServiceImpl;

@RestController
@RequestMapping("/")
public class AccountController {

	@Autowired
	private AccountServiceImpl accountService;
	
	// thay Account = AccountRequest 
	// trả về kiểu AccountResponse
	@PostMapping(value = "/login")
	public AccountResponse login(@RequestBody AccountRequest request) {
		return accountService.login(request);
	}

	@PostMapping(value = "/accounts")
	public AccountResponse addAccount(@RequestBody AccountRequest request) {
		return accountService.creatAcc(request);
	}

	@GetMapping(value = "/accounts")
	public List<Account> getListAccount() {
		return accountService.findAll();
	}

	@GetMapping(value = "/accounts/{id}")
	public AccountResponse getOneAccount(@PathVariable("id") int id) {
		return accountService.getOneAccount(id);
	}

	@PutMapping(value = "/accounts/changePass")
	public AccountResponse changeAccount(@RequestBody ChangePassRequest changePassRequest) {
		return accountService.changePassWord(changePassRequest);
	}
}
