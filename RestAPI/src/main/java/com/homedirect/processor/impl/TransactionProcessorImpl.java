package com.homedirect.processor.impl;

import java.text.ParseException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.homedirect.constant.ErrorCode;
import com.homedirect.entity.Account;
import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.TransactionProcessor;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.AccountService;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.TransactionTransformer;
import com.homedirect.validator.ATMInputValidator;

@Service
public class TransactionProcessorImpl implements TransactionProcessor {

	private @Autowired TransactionService service;
	private @Autowired TransactionTransformer transformer;
	private @Autowired AccountService accountService;
	private @Autowired ATMInputValidator validatorInputATM;

	@Override
	public TransactionResponse deposit(DepositRequest depositRequest) throws ATMException {
		if (ATMInputValidator.validatorDeposit(depositRequest.getAmount())) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_INPUT_MES);
		}

		Transaction transaction = service.deposit(depositRequest);
		return transformer.toResponse(transaction);
	}

	@Override
	public TransactionResponse withdrawal(WithdrawRequest withdrawRequest) throws ATMException {
		Double amount = withdrawRequest.getAmount();
		Account account = accountService.findById(withdrawRequest.getId());
		if (ATMInputValidator.validatorWithdraw(amount, account.getAmount())) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_INPUT_MES);
		}
		if (!BCrypt.checkpw(withdrawRequest.getPassword(), account.getPassword())) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_INPUT_MES);
		}

		Transaction transaction = service.withdraw(account, amount);
		return transformer.toResponse(transaction);
	}

	@Override
	public TransactionResponse transfer(TransferRequest request) throws ATMException {
		if (!validatorInputATM.isValidateInputTransfer(request.getFromId(), request.getToAccountNumber())) {
			throw new ATMException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND_MES);
		}
		Account fromAccount = accountService.findById(request.getFromId());
		Account toAccount = accountService.findByAccountNumber(request.getToAccountNumber());
		if (!validatorInputATM.checkTransfer(toAccount.getId(), request.getFromId())
				|| ATMInputValidator.validatorWithdraw(request.getAmount(), fromAccount.getAmount())) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_INPUT_MES);
		}

		if (!BCrypt.checkpw(request.getPassword(), fromAccount.getPassword())) {
			throw new ATMException(ErrorCode.INVALID_DATA, ErrorCode.INVALID_INPUT_MES);
		}

		Transaction transaction = service.transfer(fromAccount, toAccount, request.getAmount(), request.getContent());
		return transformer.toResponse(transaction);
	}

	@Override
	public Page<Transaction> search(String accountNumber, String toDate, String fromDate, Byte type, int pageNo,
			int pageSize) throws ParseException {
		return service.search(accountNumber, fromDate, toDate, type, pageNo, pageSize);
	}
}
