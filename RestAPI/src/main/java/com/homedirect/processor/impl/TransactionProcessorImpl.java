package com.homedirect.processor.impl;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.TransactionProcessor;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.TransactionTransformer;

@Service
public class TransactionProcessorImpl implements TransactionProcessor {
	
	private @Autowired TransactionService service;
	private @Autowired TransactionTransformer transformer;

	@Override
	public TransactionResponse deposit(DepositRequest depositRequest) throws ATMException {
		Transaction transaction = service.deposit(depositRequest);
		return transformer.toResponse(transaction);
	}

	@Override
	public TransactionResponse withdrawal(WithdrawRequest withdrawRequest) throws ATMException {
		Transaction transaction = service.withdraw(withdrawRequest);
		return transformer.toResponse(transaction);
	}

	@Override
	public TransactionResponse transfer(TransferRequest transferRequest) throws ATMException {
		Transaction transaction = service.transfer(transferRequest);
		return transformer.toResponse(transaction);
	}

	@Override
	public Page<Transaction> search(String accountNumber, String toDate, String fromDate, Byte type, int pageNo,
			int pageSize) throws ParseException {
		return service.search(accountNumber, fromDate, toDate, type, pageNo, pageSize);
	}
}
