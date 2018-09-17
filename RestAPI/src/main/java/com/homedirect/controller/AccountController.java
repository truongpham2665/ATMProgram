package com.homedirect.controller;

import java.util.List;
import java.util.Optional;

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
import com.homedirect.service.impl.AccountServiceImpl;

@RestController
@RequestMapping("/")
public class AccountController {

	@Autowired
	private AccountServiceImpl accountService;

	
	// thay Account = AccountRequest 
	@PostMapping(value = "/login")
	public Account login(@RequestBody AccountRequest account) {
		return accountService.getAccount(account.getUsername(), account.getPassword());
	}

	@PostMapping(value = "/accounts")
	public boolean addAccount(@RequestBody AccountRequest account) {
		accountService.creatAcc(account.getUsername(), account.getPassword());
		return true;
	}

	@GetMapping(value = "/accounts")
	public List<Account> getListAccount() {
		return accountService.findAll();
	}

	@GetMapping(value = "/accounts/{id}")
	public Account getOneAccount(@PathVariable("id") int id) {
		Optional<Account> acc = accountService.findById(id);
		return acc.get();
	}

	@PutMapping(value = "/accounts/changePass")
	public boolean changeAccount(@RequestBody ChangePassRequest changePassRequest) {
		if (accountService.changePassWord(changePassRequest)) {
			return true;
		}
		return false;
	}
}
