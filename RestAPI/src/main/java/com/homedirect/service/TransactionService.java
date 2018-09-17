package com.homedirect.service;

import java.util.List;

import com.homedirect.entity.TransactionHistory;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;

public interface TransactionService<T> {

	public boolean deposit(DepositRequest depositRequest);

	public boolean withdraw(WithdrawRequest withdrawRequest);

	public boolean transfer(TransferRequest transferRequest);

	void saveHistoryTransfer(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
			String status, String content, Byte type);

	List<TransactionHistory> showHistoryTransfer(int id);
}
