package com.homedirect.service;

import java.util.List;

import com.homedirect.entity.Account;
import com.homedirect.message.ATMException;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.TransactionResponse;

public interface TransactionService {

	public Account deposit(DepositRequest depositRequest) throws ATMException;

	public Account withdraw(WithdrawRequest withdrawRequest) throws ATMException;

	public Account transfer(TransferRequest transferRequest) throws ATMException, ATMException;

	void saveHistoryTransfer(String fromAccountNumber, String toAccountNumber, Double transferAmount, String status,
			String content, Byte type);

	List<TransactionResponse> searchHistory(Integer accountId, String fromDate, String toDate, Byte type, int pageNo, int pageSize) throws ATMException;
}
