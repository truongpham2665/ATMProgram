package com.homedirect.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.TransactionProcessor;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.ATMResponse;
import com.homedirect.util.WriteFile;

//them method write 

@RestController
@RequestMapping("/transactions")
public class TransactionController extends AbstractController<TransactionProcessor> {

	@PutMapping(value = "/deposit")
	public ATMResponse<?> transactionDeposit(@RequestBody DepositRequest depositRequest) {
		try {
			return toResponse(processor.deposit(depositRequest));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@PutMapping(value = "/withdrawal")
	public ATMResponse<?> withdrawal(@RequestBody WithdrawRequest withdrawRequest) {
		try {
			return toResponse(processor.withdrawal(withdrawRequest));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@PutMapping(value = "/transfer")
	public ATMResponse<?> TransactionTransfer(@RequestBody TransferRequest transferRequest) {
		try {
			return toResponse(processor.transfer(transferRequest));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@GetMapping(value = "/search")
	public ATMResponse<?> search(@RequestParam("accountId") int id,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "type", required = false) Byte type, 
			@RequestParam(defaultValue = "0") int pageNo) {
		try {
			return toResponse(processor.search(id, toDate, fromDate, type, pageNo, Transaction.Constant.PAGESIZE));
		} catch (Exception e) {
			return toResponse(e);
		}
	}

	@GetMapping(value = "/downloadExcel")
	public ATMResponse<?> write(@RequestParam("accountId") int id) throws ATMException, IOException {
		try {
			WriteFile writeFile = new WriteFile();
			List<Transaction> transactions = processor.findTransactionByAccountId(id);
			writeFile.writeListTransactiontoExcel(transactions);
			return toResponse(transactions);
		} catch (Exception e) {
			return toResponse(e);
		}
		
	}
	
}
