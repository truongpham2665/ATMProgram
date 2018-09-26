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
import com.homedirect.exception.MessageException;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.AccountTransformer;

//them AbstractMyException + MessageException + ErrorMyCode 

@RestController
@RequestMapping("/accounts")
public class AccountController extends AbstractController<AccountResponse> {

	private @Autowired AccountService accountService;
	private @Autowired AccountTransformer transformer;

	@PostMapping(value = "/login")
	public ATMReponse<?> login(@RequestBody AccountRequest request) {
		try {
			Account account = accountService.login(request);
			return success(transformer.toResponse(account));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@PostMapping
	public ATMReponse<?> create(@RequestBody AccountRequest request) {
		try {
			Account account = accountService.creatAcc(request);
			return success(transformer.toResponse(account));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@GetMapping
	public ATMReponse<?> showAllAccount() {
		try {
			return success(transformer.toResponseList(accountService.findAllAccount()));
		} catch (Exception e) {
			return notFound(e.getMessage());
		}
	}

	@GetMapping(value = "/{id}")
	public ATMReponse<?> showAccount(@PathVariable int id) {
		try {
			Account account = accountService.getOneAccount(id);
			return success(transformer.toResponse(account));
		} catch (Exception e) {
			return notFound(MessageException.notFound());
		}
	}

	@PutMapping(value = "/change-password")
	public ATMReponse<?> changeAccount(@RequestBody ChangePassRequest changePassRequest) {
		try {
			Account account = accountService.changePassword(changePassRequest);
			return success(transformer.toResponse(account));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@GetMapping(value = "/search")
	public ATMReponse<?> search(@RequestParam String username, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		try {
			return success(transformer.toResponseList(accountService.searchAccounts(username, pageNo, pageSize)));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}
}
