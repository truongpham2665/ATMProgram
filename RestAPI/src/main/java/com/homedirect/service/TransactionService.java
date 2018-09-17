package com.homedirect.service;

import java.util.List;

import com.homedirect.entity.Account;
import com.homedirect.entity.TransactionHistory;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;

public interface TransactionService<T> {

	public Account deposit(DepositRequest depositRequest);

	public Account withdraw(WithdrawRequest withdrawRequest);

	public Account transfer(TransferRequest transferRequest);

	void saveHistoryTransfer(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
			String status, String content, Byte type);

	List<TransactionHistory> showHistoryTransfer(int id);
}
