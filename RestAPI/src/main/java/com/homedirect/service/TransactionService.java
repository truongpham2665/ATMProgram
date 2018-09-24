package com.homedirect.service;

import java.util.List;

import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;

public interface TransactionService {

	public AccountResponse deposit(DepositRequest depositRequest);

	public AccountResponse withdraw(WithdrawRequest withdrawRequest);

	public AccountResponse transfer(TransferRequest transferRequest);

	void saveHistoryTransfer(String fromAccountNumber, String toAccountNumber, Double transferAmount, String status,
			String content, Byte type);

	List<TransactionResponse> searchHistory(Integer accountId, String fromDate, String toDate, Byte type, int pageNo, int pageSize);
}
