package com.homedirect.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.exception.ATMException;
import com.homedirect.processor.AccountProcessor;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.ATMResponse;
import com.homedirect.response.AccountResponse;
import com.homedirect.util.WriteFile;

@RestController
@RequestMapping("/accounts")
public class AccountController extends AbstractController<AccountProcessor> {

	@PostMapping(value = "/login")
	public ATMResponse<?> login(@RequestBody AccountRequest request) {
		try {
			return toResponse(processor.login(request));
		} catch (Exception e) {

			return toResponse(e);
		}
	}

	@PostMapping
	public ATMResponse<?> create(@RequestBody AccountRequest request) {
		try {
			return toResponse(processor.create(request));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@GetMapping
	public ATMResponse<?> findAll() throws ATMException {
		try {
			return toResponse(processor.findAll());
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@GetMapping(value = "/{id}")
	public ATMResponse<?> get(@PathVariable int id) {
		try {
			return toResponse(processor.get(id));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@PutMapping(value = "/change-password")
	public ATMResponse<?> changePassword(@RequestBody ChangePassRequest changePassRequest) {
		try {
			return toResponse(processor.changePassword(changePassRequest));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@GetMapping(value = "/search")
	public ATMResponse<?> search(@RequestParam(value = "username", required = false) String username,
			@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		try {
			return toResponse(processor.search(username, pageNo, pageSize));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@GetMapping(value = "/downloadExcel")
	public String write() throws IOException, ATMException {
		WriteFile writeFile = new WriteFile();
		List<AccountResponse> accountResponses = processor.findAll();
		writeFile.writeListAccountResponsetoExcel(accountResponses);
		return "success";
	}
}
