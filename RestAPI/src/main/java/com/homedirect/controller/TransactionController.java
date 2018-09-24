package com.homedirect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.request.DepositRequest;
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

	@GetMapping(value = "/show-history")
	public List<TransactionResponse> search(@RequestParam(value = "accountId") Integer accountId,
											@RequestParam(value = "fromDate", required = false) String fromDate,
											@RequestParam(value = "toDate", required = false) String toDate,
											@RequestParam(value = "type", required = false) Byte type,
											@RequestParam(defaultValue = "0") int pageNo,
											@RequestParam(defaultValue = "10") int pageSize) {
		return transactionService.searchHistory(accountId, fromDate, toDate, type, pageNo, pageSize);
	}
}
