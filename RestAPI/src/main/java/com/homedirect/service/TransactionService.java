package com.homedirect.service;

import org.springframework.data.domain.Page;

import com.homedirect.request.DepositRequest;
import com.homedirect.request.SearchTransactionHistoryRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;

// bỏ generic
//thay Iterable -> Page
public interface TransactionService {
	
	public AccountResponse deposit(DepositRequest depositRequest);

	public AccountResponse withdraw(WithdrawRequest withdrawRequest);

	public AccountResponse transfer(TransferRequest transferRequest);

	void saveHistoryTransfer(String fromAccountNumber , String toAccountNumber, Double transferAmount,
			String status, String content, Byte type);
	
	Page<TransactionResponse> searchHistory(SearchTransactionHistoryRequest q);
}
