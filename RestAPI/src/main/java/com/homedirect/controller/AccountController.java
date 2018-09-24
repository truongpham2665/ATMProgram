package com.homedirect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@PostMapping(value = "/login")
	public AccountResponse login(@RequestBody AccountRequest request) {
		return accountService.login(request);
	}

	@PostMapping(value = "/create")
	public AccountResponse addAccount(@RequestBody AccountRequest request) {
		return accountService.creatAcc(request);
	}
	
	@GetMapping(value = "/show-accounts")
	public List<AccountResponse> showAllAccount() {
		return accountService.findAllAccount();
	}

	@GetMapping(value = "/show-account/{id}")
	public AccountResponse showAccount(@PathVariable int id) {
		return accountService.getOneAccount(id);
	}

	@PutMapping(value = "/change-password")
	public AccountResponse changeAccount(@RequestBody ChangePassRequest changePassRequest) {
		return accountService.changePassword(changePassRequest);
	}

	@GetMapping(value = "/search")
	public List<AccountResponse> search(@RequestParam String username,
										@RequestParam(defaultValue = "0") int pageNo,
										@RequestParam(defaultValue = "10") int pageSize) {
		return accountService.searchAccounts(username, pageNo, pageSize);
	}
}
