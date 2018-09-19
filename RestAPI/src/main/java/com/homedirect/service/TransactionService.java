package com.homedirect.service;

import com.homedirect.request.DepositRequest;
import com.homedirect.request.SearchTransactionHistoryRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;

// bỏ generic
public interface TransactionService {
	
	public AccountResponse deposit(DepositRequest depositRequest);

	public AccountResponse withdraw(WithdrawRequest withdrawRequest);

	public AccountResponse transfer(TransferRequest transferRequest);

	void saveHistoryTransfer(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
			String status, String content, Byte type);
	
	Iterable<TransactionResponse> searchHistory(SearchTransactionHistoryRequest q);
}
