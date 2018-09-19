package com.homedirect.transformer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;
import com.homedirect.entity.TransactionHistory;
import com.homedirect.response.TransactionResponse;

@Component
public class TransactionHistoryTransformerImpl {

	public TransactionResponse toTransactionHistory(TransactionHistory transactionHistory) {
		TransactionResponse response = new TransactionResponse();
		response.setId(transactionHistory.getId());
		response.setFromAccountNumber(transactionHistory.getFromAccount());
		response.setToAccountNumber(transactionHistory.getToAccount());
		response.setTransferAmount(transactionHistory.getTransferAmount());
		response.setTime(new Date());
		response.setStatus(transactionHistory.getStatus());
		response.setContent(transactionHistory.getContent());
		response.setType(transactionHistory.getType());
		return response;
	}

	public List<TransactionResponse> toResponse(List<TransactionHistory> transactionHistories) {
		if(transactionHistories == null) return new ArrayList<>();
		List<TransactionResponse> responses = new ArrayList<TransactionResponse>();
		transactionHistories.forEach(transaction -> {
			TransactionResponse response = new TransactionResponse();
			response.setId(transaction.getId());
			response.setFromAccountNumber(transaction.getFromAccount());
			response.setToAccountNumber(transaction.getToAccount());
			response.setTransferAmount(transaction.getTransferAmount());
			response.setTime(transaction.getTime());
			response.setStatus(transaction.getStatus());
			response.setContent(transaction.getContent());
			response.setType(transaction.getType());
			responses.add(response);
		});
		
		return responses;
	}
}