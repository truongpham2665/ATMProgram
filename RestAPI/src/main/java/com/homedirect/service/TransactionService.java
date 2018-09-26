package com.homedirect.service;

import java.util.List;

import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.TransactionResponse;

public interface TransactionService {

	public Transaction deposit(DepositRequest depositRequest) throws ATMException;

	public Transaction withdraw(WithdrawRequest withdrawRequest) throws ATMException;

	public Transaction transfer(TransferRequest transferRequest) throws ATMException, ATMException;

	Transaction saveTransaction(String fromAccountNumber, String toAccountNumber, Double transferAmount, String status,
			String content, Byte type);

	List<TransactionResponse> search(Integer accountId, String fromDate, String toDate, Byte type, int pageNo, int pageSize) throws ATMException;
}
