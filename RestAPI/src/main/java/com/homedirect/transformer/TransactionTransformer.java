package com.homedirect.transformer;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Transaction;
import com.homedirect.response.TransactionResponse;

@Component
public class TransactionTransformer {

	public TransactionResponse toResponse(Transaction transaction) {
		TransactionResponse response = new TransactionResponse();
		response.setId(transaction.getId());
		response.setFromAccountNumber(transaction.getFromAccount());
		response.setToAccountNumber(transaction.getToAccount());
		response.setTransferAmount(transaction.getTransferAmount());
		response.setTime(new Date());
		response.setStatus(transaction.getStatus());
		response.setContent(transaction.getContent());
		response.setType(transaction.getType());
		return response;
	}

	public List<TransactionResponse> toResponse(List<Transaction> transactionHistories) {
		return transactionHistories.stream().map(this::toResponse).collect(Collectors.toList());
	}
	
	public Page<TransactionResponse> toResponse(Page<Transaction> page) {
		return page.map(transaction -> toResponse(transaction));
	}
}
