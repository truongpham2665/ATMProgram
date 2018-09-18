package com.homedirect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.entity.Account;
import com.homedirect.entity.TransactionHistory;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.service.impl.TransactionServiceImpl;;

//chuyen cac method boolean sang Account /
//	if (transactionService.deposit(depositRequest)) {
//	return null;
//} bỏ thay bằng return transactionService.deposit(depositRequest);

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private @Autowired TransactionServiceImpl transactionService;
	
	@PutMapping(value = "/deposit")
	public Account deposit(@RequestBody DepositRequest depositRequest) {

		return transactionService.deposit(depositRequest);
	}

	@PutMapping(value = "/withdrawal")
	public Account withdraw(@RequestBody WithdrawRequest withdrawRequest) {
		return transactionService.withdraw(withdrawRequest);
	}

	@PutMapping(value = "/transfer")
	public Account transfer(@RequestBody TransferRequest transferRequest) {
		return transactionService.transfer(transferRequest);
	}

	@GetMapping(value = "/showHistory/{id}")
	@ResponseBody
	public List<TransactionHistory> showHistoryTransfer(@PathVariable("id") int id) {
		return transactionService.showHistoryTransfer(id);
	}
}
