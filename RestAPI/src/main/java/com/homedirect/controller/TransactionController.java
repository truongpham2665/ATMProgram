package com.homedirect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.request.DepositRequest;
import com.homedirect.request.SearchTransactionHistoryRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	private @Autowired TransactionService transactionService;

	@PutMapping(value = "/deposit")
	public AccountResponse transactionDeposit(@RequestBody DepositRequest depositRequest) {
		return transactionService.deposit(depositRequest);
	}

	@PutMapping(value = "/withdrawal")
	public AccountResponse withdrawal(@RequestBody WithdrawRequest withdrawRequest) {
		return transactionService.withdraw(withdrawRequest);
	}

	@PutMapping(value = "/transfer")
	public AccountResponse TransactionTransfer(@RequestBody TransferRequest transferRequest) {
		return transactionService.transfer(transferRequest);
	}

	@PostMapping(value = "/show-history")
	public Page<TransactionResponse> search(@RequestBody SearchTransactionHistoryRequest q) {
		return transactionService.searchHistory(q);
	}
}
