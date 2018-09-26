package com.homedirect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.homedirect.constant.ErrorMyCode.*;
import com.homedirect.entity.Account;
import com.homedirect.message.ATMException;
import com.homedirect.message.MessageException;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.AccountTransformer;

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractController<AccountResponse> {

	private @Autowired TransactionService transactionService;
	private @Autowired AccountTransformer accountTransformer;

	@PutMapping(value = "/deposit")
	public ATMReponse<?> transactionDeposit(@RequestBody DepositRequest depositRequest) {
		try {
			Account account = transactionService.deposit(depositRequest);
			return success(accountTransformer.toResponse(account));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@PutMapping(value = "/withdrawal")
	public ATMReponse<?> withdrawal(@RequestBody WithdrawRequest withdrawRequest) {
		try {
			Account account = transactionService.withdraw(withdrawRequest);
			return success(accountTransformer.toResponse(account));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@PutMapping(value = "/transfer")
	public ATMReponse<?> TransactionTransfer(@RequestBody TransferRequest transferRequest) {
		try {
			Account account = transactionService.transfer(transferRequest);
			return success(accountTransformer.toResponse(account));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@GetMapping(value = "/show-history")
	public ATMReponse<?> search(@RequestParam(value = "accountId") Integer accountId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "type", required = false) Byte type,
			@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) throws ATMException {
		try {
			return showHistorySuccess(transactionService.searchHistory(accountId, fromDate, toDate, type, pageNo, pageSize));
		} catch (Exception e) {
			return showHistoryFailure(e.getMessage());
		}
	}

	private ATMReponse<?> showHistorySuccess(List<TransactionResponse> data) {
		return new ATMReponse<>(SUCCESS, MessageException.success(), data);
	}

	private ATMReponse<?> showHistoryFailure(String message) {
		return new ATMReponse<>(FALSE, message, null);
	}
}
