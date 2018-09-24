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

	@GetMapping(value = "/show-account/{id}")
	public AccountResponse showAccount(@PathVariable int id) {
		return accountService.getOneAccount(id);
	}

	@PutMapping(value = "/change-password")
	public AccountResponse changeAccount(@RequestBody ChangePassRequest changePassRequest) {
		return accountService.changePassword(changePassRequest);
	}

	@GetMapping(value = "/search")
	public Iterable<AccountResponse> search(@RequestParam String q) {
		return accountService.searchAccounts(q);
	}

//	@DeleteMapping(value = "/delete/{id}")
//		public ResponseEntity<Void> delete(@PathVariable("id") int id) {
//			try {
//				accountService.deleteAccountById(id);
//				return new ResponseEntity<Void>(HttpStatus.OK);
//			} catch (Exception e) {
//			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
//		}
//	}
}
