package com.homedirect.processor;

import java.text.ParseException;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.TransactionResponse;

public interface TransactionProcessor {
	
	TransactionResponse deposit(@RequestBody DepositRequest depositRequest) throws ATMException;
	
	TransactionResponse withdrawal(@RequestBody WithdrawRequest withdrawRequest) throws ATMException;

	TransactionResponse  transfer(@RequestBody TransferRequest transferRequest) throws ATMException;
	
	Page<Transaction> search(@RequestParam("accountNumber") String accountNumber,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "type", required = false) Byte type,
			@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) throws ATMException, ParseException;
	
}
