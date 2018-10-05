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
//	
//	public Page<TransactionResponse> toTransactionResponse(Page<Transaction> page) {
//		if (page == null) return new Page<TransactionResponse>();
//		return new Page<>(page.getPageNo(), page.getPageSize(), page.getTotalElements(), 
//				page.getTotalPage(),toResponse( page.getContent()));
//	}
	
//	public Page<TransactionResponse> toPageTransactionRespone(Page<Transaction> page) {
//		if (page == null) {
//			throw new ATMException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND_MES);
//		}
//		return new Page<TransactionResponse>(page.getSize(), page.getTotalElements(), page.getTotalPages(), page.getContent());
//	}
	
	public Page<TransactionResponse> toTransactionResponse(Page<Transaction> page) {
		return page.map(transaction -> toResponse(transaction));
	}
}
