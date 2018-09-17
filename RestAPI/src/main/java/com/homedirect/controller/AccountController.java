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
import com.homedirect.request.ChangePassRequest;
import com.homedirect.service.impl.AccountServiceImpl;

@RestController
@RequestMapping("/")
public class AccountController {

	@Autowired
	private AccountServiceImpl accountService;

	@PostMapping(value = "/login")
	public Account login(@RequestBody Account account) {
		return accountService.getAccount(account.getUserName(), account.getPassWord());
	}

	@PostMapping(value = "/accounts")
	public boolean addAccount(@RequestBody Account account) {
		accountService.creatAcc(account.getUserName(), account.getPassWord());
		return true;
	}

	@GetMapping(value = "/accounts")
	public List<Account> getListAccount() {
		return accountService.findAll();
	}

	@GetMapping(value = "/accounts/{id}")
	public Account getOneAccount(@PathVariable("id") int id) {
		Optional<Account> acc; 
		acc = accountService.findById(id);
		System.out.println(acc);
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
