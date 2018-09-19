package com.homedirect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.entity.Account;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;

// Sửa method getAccountList
// Đổi kiểu trả về Account -> AccountResponse

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
	
	@GetMapping(value = "/show-account/{id}") 
	public AccountResponse showAccount(@PathVariable Integer id) {
		return accountService.getOneAccount(id);
	}

	@GetMapping(value = "/search-accounts")
	public AccountResponse searchAccounts(@RequestParam(value = "username", required = false) String username, 
												@RequestParam(value = "accountNumber", required = false) String accountNumber) {
		return accountService.searchAccount(username, accountNumber);
	}

	@PutMapping(value = "/change-password")
	public AccountResponse changeAccount(@RequestBody ChangePassRequest changePassRequest) {
		return accountService.changePassword(changePassRequest);
	}
	
	@GetMapping(value = "/search")
	public Iterable<Account> search(@RequestParam String q) {
		return accountService.searchAccounts(q);
	}
}
