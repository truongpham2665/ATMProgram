//package com.homedirect.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import com.homedirect.entity.TransactionHistory;
//import com.homedirect.request.DepositRequest;
//import com.homedirect.request.TransferRequest;
//import com.homedirect.request.WithdrawRequest;
//import com.homedirect.service.impl.TransactionServiceImpl;
//
//@RestController
//@RequestMapping("/transactions")
//public class TransactionController {
//
//	private @Autowired TransactionServiceImpl transactionService;
//
//	@PutMapping(value = "/deposit")
//	public boolean deposit(@RequestBody DepositRequest depositRequest) {
//		if (transactionService.deposit(depositRequest)) {
//			return true;
//		}
//		return false;
//	}
//
//	@PutMapping(value = "/withdrawal")
//	public boolean withdraw(@RequestBody WithdrawRequest withdrawRequest) {
//		if (transactionService.withdraw(withdrawRequest)) {
//			return true;
//		}
//		return false;
//	}
//
//	@PutMapping(value = "/transfer")
//	public boolean transfer(@RequestBody TransferRequest transferRequest) {
//		if (transactionService.transfer(transferRequest)) {
//			return true;
//		}
//		return false;
//	}
//
//	@GetMapping(value = "/showHistory/{id}")
//	@ResponseBody
//	public List<TransactionHistory> showHistoryTransfer(@PathVariable("id") int id) {
//		try {
//			return transactionService.showHistoryTransfer(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//}
