package com.homedirect.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.homedirect.constant.ConstantTransaction;
import com.homedirect.entity.Account;
import com.homedirect.entity.TransactionHistory;
import com.homedirect.entity.TransactionHistory.TransactionType;
import com.homedirect.repositories.TransactionRepository;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.impl.AccountTransformerImpl;
import com.homedirect.transformer.impl.TransactionHistoryTransformerImpl;
import com.homedirect.util.Notification;
import com.homedirect.validate.*;

// thay String username... = request.get.getUsername...
// String sourceAccountNumber = transferRequest.getSourceAccountNumber();
// String receiverAccountNumber = transferRequest.getReceiverAccountNumber();
// String content = transferRequest.getContent();
// Double amount = transferRequest.getAmount();
// đổi kiểu trả về từ Account -> AccountResponse

@Service
public class TransactionServiceImpl extends ServiceAbstract<TransactionHistory> implements TransactionService {

	private @Autowired TransactionRepository transactionRepository;
	private @Autowired AccountServiceImpl accountService;
	private @Autowired TransactionHistoryTransformerImpl transactionTransformer;
	private @Autowired AccountTransformerImpl accountTransformer;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountResponse deposit(DepositRequest depositRequest) {
		Account account = accountService.findById(depositRequest.getId()).get(); // thêm get() line 28,46;
		Double amount = depositRequest.getAmount();
		if (ValidatorATM.validatorDeposit(depositRequest.getAmount())) {
			return null;
		}

		account.setAmount(account.getAmount() + amount);
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_DEPOSIT, TransactionType.DEPOSIT);

		return accountTransformer.toResponse(accountService.save(account));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountResponse withdraw(WithdrawRequest withdrawRequest) {
		Double amount = withdrawRequest.getAmount();
		Account account = accountService.findById(withdrawRequest.getId()).get();
		if (ValidatorATM.validatorWithdraw(amount, account.getAmount())) {
			return null;
		}

		account.setAmount(account.getAmount() - (amount + ConstantTransaction.FEE_TRANSFER));
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_WITHDRAW, TransactionType.WITHDRAW);

		return accountTransformer.toResponse(accountService.save(account));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountResponse transfer(TransferRequest Request) {
		Account fromAccount = accountService.findByAccountNumber(Request.getFromAccountNumber());
		try {
			if (!checkTransfer(Request.getToAccountNumber(), Request.getFromAccountNumber())
					|| ValidatorATM.validatorWithdraw(Request.getAmount(), fromAccount.getAmount())) {
				return null;
			}

			Account toAccount = accountService.findByAccountNumber(Request.getToAccountNumber());
			fromAccount.setAmount(fromAccount.getAmount() - Request.getAmount() - ConstantTransaction.FEE_TRANSFER);
			toAccount.setAmount(toAccount.getAmount() + Request.getAmount());

			accountService.save(fromAccount);
			accountService.save(toAccount);
			saveHistoryTransfer(toAccount.getAccountNumber(), Request.getToAccountNumber(),Request.getAmount(),
					ConstantTransaction.STATUS_SUCCESS, Request.getContent(),TransactionType.TRANSFER);

		} catch (Exception e) {
			saveHistoryTransfer(Request.getFromAccountNumber(), Request.getToAccountNumber(),Request.getAmount(),
					ConstantTransaction.STATUS_FAIL, Request.getContent(),TransactionType.TRANSFER);
			e.printStackTrace();
			return null;
		}
		return accountTransformer.toResponse(fromAccount);
	}

	@Override
	public void saveHistoryTransfer(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
			String status, String content, Byte type) {

		TransactionHistory history = new TransactionHistory(sourceAccountNumber, reciverAccountNumber, transferAmount,
				ValidatorATM.getDate(), status, content, type);
		save(history);
	}

	public boolean checkTransfer(String toAccountNumber, String fromAccountNumber) {
		if (toAccountNumber.equals(fromAccountNumber)) {
			Notification.alert("Khong the tu chuyen tien cho chinh minh");
			return false;
		}

		if (accountService.checkAccountNumbers(toAccountNumber)) {
			Notification.alert("So tai khoan nhan khong chinh xac");
			return false;
		}
		return true;
	}
	
	@Override
	public List<TransactionResponse> showHistory(Integer id) {
		Account account = accountService.findById(id).get();
		if (account == null) {
			return null;
		}
		return transactionTransformer.toResponse(transactionRepository.findByFromAccount(account.getAccountNumber()));
	}

	// thay đổi hàm showHistoryTransaction
	@Override
	public List<TransactionResponse> showHistoryTransfer(String accountNumber, Byte type) {
		if(accountNumber == null && type == null) {
			List<TransactionHistory> transactionHistory = transactionRepository.findAll();
			return transactionTransformer.toResponse(transactionHistory);
		}
		
		if(accountNumber == null) {
			List<TransactionHistory> transactionHistory = transactionRepository.findByType(type);
			return transactionTransformer.toResponse(transactionHistory);
		}
		
		if(type == null) {
			List<TransactionHistory> transactionHistory = transactionRepository.findByFromAccount(accountNumber);
			return transactionTransformer.toResponse(transactionHistory);
		}
		
		List<TransactionHistory> transactionHistory = transactionRepository.findByFromAccountAndType(accountNumber, type);
		return transactionTransformer.toResponse(transactionHistory);
	}
}
