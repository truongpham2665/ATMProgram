package com.homedirect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.processor.AccountProcessor;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.request.SearchAccountRequest;
import com.homedirect.response.ATMResponse;

@RestController
@RequestMapping("/accounts")
public class AccountController extends AbstractController<AccountProcessor>{

	// sử dụng phương thức apply(...) abstractController đã có try/catch. nên bỏ
	// try/catch .
	@PostMapping(value = "/login")
	public ATMResponse<?> login(@RequestBody AccountRequest request) {
		return apply(request, processor::login);
	}

	@PostMapping
	public ATMResponse<?> create(@RequestBody AccountRequest request) {
		return apply(request, processor::create);
	}

	@GetMapping(value = "/{id}")
	public ATMResponse<?> get(@PathVariable int id) {
		return apply(id, processor::get);
	}

	@PutMapping(value = "/change-password")
	public ATMResponse<?> changePassword(@RequestBody ChangePassRequest request) {
		return apply(request, processor::changePassword);
	}

	@GetMapping(value = "/search")
	public ATMResponse<?> search(@RequestParam(value = "username", required = false) String username,
			@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		return apply(new SearchAccountRequest(username, pageNo, pageSize), processor::search);
	}

	@GetMapping(value = "/dowload")
	public String dowloadCsv() throws Exception {
		processor.exportCsv();
		return "dowload complete!";
	}
}
