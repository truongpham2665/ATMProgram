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
import com.homedirect.message.MyException;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.AccountTransformer;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	private @Autowired TransactionService transactionService;
	private @Autowired AccountTransformer accountTransformer;

	@PutMapping(value = "/deposit")
	public ATMReponse transactionDeposit(@RequestBody DepositRequest depositRequest) throws ATMException {
		Account account = transactionService.deposit(depositRequest);
		try {
			new ATMReponse(accountTransformer.toResponse(account));
		} catch (Exception e) {
			new ATMReponse(99, e.getMessage(), null);
		}
		return success(account);
	}

	@PutMapping(value = "/withdrawal")
	public ATMReponse withdrawal(@RequestBody WithdrawRequest withdrawRequest) throws ATMException {
		Account account = transactionService.withdraw(withdrawRequest);
		try {
			new ATMReponse(accountTransformer.toResponse(account));
		} catch (Exception e) {
			new ATMReponse(99, e.getMessage(), null);
		}
		return success(account);
	}

	@PutMapping(value = "/transfer")
	public ATMReponse TransactionTransfer(@RequestBody TransferRequest transferRequest) throws ATMException, ATMException {
		Account account = transactionService.transfer(transferRequest);
		try {
			new ATMReponse(accountTransformer.toResponse(account));
		} catch (Exception e) {
			new ATMReponse(99, e.getMessage(), null);
		}
		return success(account);
	}

	@GetMapping(value = "/show-history")
	public List<TransactionResponse> search(@RequestParam(value = "accountId") Integer accountId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "type", required = false) Byte type, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) throws ATMException {
		return transactionService.searchHistory(accountId, fromDate, toDate, type, pageNo, pageSize);
	}

	private ATMReponse success(Account account) {
		return new ATMReponse(MyException.SUCCESS, accountTransformer.toResponse(account));
	}
}
