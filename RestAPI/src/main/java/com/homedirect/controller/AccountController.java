package com.homedirect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.entity.Account;
import com.homedirect.message.ATMException;
import com.homedirect.message.MyException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;
	private @Autowired AccountTransformer accountTransformer;

	@PostMapping(value = "/login")
	public ATMReponse login(@RequestBody AccountRequest request) throws ATMException  {
		Account account = accountService.login(request);
		try {
			new ATMReponse(accountTransformer.toResponse(account));
		} catch (Exception e) {
			new ATMReponse(e.getMessage(), null);
		}
		return success(account);
	}

	@PostMapping(value = "/create")
	public ATMReponse addAccount(@RequestBody AccountRequest request) throws ATMException {
		Account account = accountService.creatAcc(request);
		try {
			new ATMReponse(accountTransformer.toResponse(account));
		} catch (NullPointerException e) {
			new ATMReponse(e.getMessage(), null);
		}
		return success(account);
	}

	@GetMapping(value = "/show-accounts")
	public List<AccountResponse> showAllAccount() {
		return accountService.findAllAccount();
	}

	@GetMapping(value = "/show-account")
	public AccountResponse showAccount(@RequestParam int id) {
		return accountService.getOneAccount(id);
	}

	@PutMapping(value = "/change-password")
	public ATMReponse changeAccount(@RequestBody ChangePassRequest changePassRequest) throws ATMException {
		Account account = accountService.changePassword(changePassRequest);
		try {
			new ATMReponse(accountTransformer.toResponse(account));
		} catch (Exception e) {
			new ATMReponse(e.getMessage(), null);
		}
		return success(account);
	}

	@GetMapping(value = "/search")
	public List<AccountResponse> search(@RequestParam String username, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		return accountService.searchAccounts(username, pageNo, pageSize);
	}

	private ATMReponse success(Account account) {
		return new ATMReponse(MyException.SUCCESS, accountTransformer.toResponse(account));
	}
}
