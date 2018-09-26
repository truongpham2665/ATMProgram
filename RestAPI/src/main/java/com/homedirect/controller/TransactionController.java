package com.homedirect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.entity.Account;
import com.homedirect.message.ATMException;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.AbstractMyException;
import com.homedirect.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractMyException {
	private @Autowired TransactionService transactionService;

	@PutMapping(value = "/deposit")
	public ATMReponse transactionDeposit(@RequestBody DepositRequest depositRequest) {
		try {
			Account account = transactionService.deposit(depositRequest);
			return success(account);
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@PutMapping(value = "/withdrawal")
	public ATMReponse withdrawal(@RequestBody WithdrawRequest withdrawRequest) {
		try {
			Account account = transactionService.withdraw(withdrawRequest);
			return success(account);
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@PutMapping(value = "/transfer")
	public ATMReponse TransactionTransfer(@RequestBody TransferRequest transferRequest) {
		try {
			Account account = transactionService.transfer(transferRequest);
			return success(account);
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@GetMapping(value = "/show-history")
	public List<TransactionResponse> search(@RequestParam(value = "accountId") Integer accountId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "type", required = false) Byte type, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) throws ATMException {
		return transactionService.searchHistory(accountId, fromDate, toDate, type, pageNo, pageSize);
	}

}
