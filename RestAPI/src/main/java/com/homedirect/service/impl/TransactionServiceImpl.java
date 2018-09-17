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
import com.homedirect.service.TransactionService;
import com.homedirect.util.Notification;
import com.homedirect.util.ValidatorATM;

// thay = request.get....
//String sourceAccountNumber = transferRequest.getSourceAccountNumber();
//String receiverAccountNumber = transferRequest.getReceiverAccountNumber();
//String content = transferRequest.getContent();
//Double amount = transferRequest.getAmount();

@Service
public class TransactionServiceImpl extends ServiceAbstract<TransactionHistory> implements TransactionService<Account> {

	private @Autowired TransactionRepository transactionRepository;
	private @Autowired AccountServiceImpl accountService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Account deposit(DepositRequest depositRequest) {
		Account account = accountService.findById(depositRequest.getId()).get(); // thêm get() line 28,46;
		Double amount = depositRequest.getAmount();
		if (ValidatorATM.validatorDeposit(depositRequest.getAmount())) {
			return null;
		}

		account.setAmount(account.getAmount() + amount);
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_DEPOSIT, TransactionType.DEPOSIT);

		return accountService.save(account);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Account withdraw(WithdrawRequest withdrawRequest) {
		Double amount = withdrawRequest.getAmount();
		Account account = accountService.findById(withdrawRequest.getId()).get();
		if (ValidatorATM.validatorWithdraw(amount, account.getAmount())) {
			return null;
		}

		account.setAmount(account.getAmount() - (amount + ConstantTransaction.FEE_TRANSFER));
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_WITHDRAW, TransactionType.WITHDRAW);

		return accountService.save(account);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Account transfer(TransferRequest Request) {

		Account sourceAccount = accountService.getAccountByAccountNumber(Request.getSourceAccountNumber());
		try {

			if (!checkTransfer(Request.getReceiverAccountNumber(), Request.getSourceAccountNumber())
					|| ValidatorATM.validatorWithdraw(Request.getAmount(), sourceAccount.getAmount())) {
				return null;
			}

			Account receiverAccount = accountService.getAccountByAccountNumber(Request.getReceiverAccountNumber());
			sourceAccount.setAmount(sourceAccount.getAmount() - Request.getAmount() - ConstantTransaction.FEE_TRANSFER);
			receiverAccount.setAmount(receiverAccount.getAmount() + Request.getAmount());

			accountService.save(sourceAccount);
			accountService.save(receiverAccount);
			saveHistoryTransfer(sourceAccount.getAccountNumber(), Request.getReceiverAccountNumber(),
					Request.getAmount(), ConstantTransaction.STATUS_SUCCESS, Request.getContent(),
					TransactionType.TRANSFER);

		} catch (Exception e) {
			saveHistoryTransfer(Request.getSourceAccountNumber(), Request.getReceiverAccountNumber(),
					Request.getAmount(), ConstantTransaction.STATUS_FAIL, Request.getContent(),
					TransactionType.TRANSFER);
			e.printStackTrace();
			return null;
		}
		return sourceAccount;
	}

	@Override
	public void saveHistoryTransfer(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
			String status, String content, Byte type) {

		TransactionHistory history = new TransactionHistory(sourceAccountNumber, reciverAccountNumber, transferAmount,
				ValidatorATM.getDate(), status, content, type);
		save(history);
	}

	@Override
	public List<TransactionHistory> showHistoryTransfer(int id) {
		Account account = accountService.findById(id).get();
		List<TransactionHistory> list = transactionRepository
				.findBySourceAccountNumberOrderByTimeDesc(account.getAccountNumber());
		return list;

	}

	public boolean checkTransfer(String reciverAccountNumber, String sourceAccountNumber) {
		if (reciverAccountNumber.equals(sourceAccountNumber)) {
			Notification.alert("Khong the tu chuyen tien cho chinh minh");
			return false;
		}

		if (accountService.checkAccountNumbers(reciverAccountNumber)) {
			Notification.alert("So tai khoan nhan khong chinh xac");
			return false;
		}
		return true;
	}

}
