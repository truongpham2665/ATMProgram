package com.homedirect.controller;

import static com.homedirect.constant.ErrorMyCode.FALSE;
import static com.homedirect.constant.ErrorMyCode.SUCCESS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.exception.ATMException;
import com.homedirect.exception.MessageException;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.TransactionTransformer;

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractController<TransactionResponse> {

	private @Autowired TransactionService service;
	private @Autowired TransactionTransformer transformer;

	@PutMapping(value = "/deposit")
	public ATMReponse<?> transactionDeposit(@RequestBody DepositRequest depositRequest) {
		try {
			return success(transformer.toResponse(service.deposit(depositRequest)));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@PutMapping(value = "/withdrawal")
	public ATMReponse<?> withdrawal(@RequestBody WithdrawRequest withdrawRequest) {
		try {
			return success(transformer.toResponse(service.withdraw(withdrawRequest)));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@PutMapping(value = "/transfer")
	public ATMReponse<?> TransactionTransfer(@RequestBody TransferRequest transferRequest) {
		try {
			return success(transformer.toResponse(service.transfer(transferRequest)));
		} catch (Exception e) {
			return errorFalse(e.getMessage());
		}
	}

	@GetMapping(value = "/search")
	public ATMReponse<?> search(@RequestParam(value = "accountId") Integer accountId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "type", required = false) Byte type,
			@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) throws ATMException {
		try {
			return showHistorySuccess(service.search(accountId, fromDate, toDate, type, pageNo, pageSize));
		} catch (Exception e) {
			return showHistoryFailure(MessageException.notFound());
		}
	}

	private ATMReponse<?> showHistorySuccess(List<TransactionResponse> data) {
		return new ATMReponse<>(SUCCESS, MessageException.success(), data);
	}

	private ATMReponse<?> showHistoryFailure(String message) {
		return new ATMReponse<>(FALSE, message, null);
	}
}
