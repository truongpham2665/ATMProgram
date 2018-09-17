//package com.homedirect.service.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.homedirect.constant.ConstantTransaction;
//import com.homedirect.entity.Account;
//import com.homedirect.entity.TransactionHistory;
//import com.homedirect.entity.TransactionHistory.TransactionType;
//import com.homedirect.repositories.TransactionRepository;
//import com.homedirect.request.DepositRequest;
//import com.homedirect.request.TransferRequest;
//import com.homedirect.request.WithdrawRequest;
//import com.homedirect.service.TransactionService;
//import com.homedirect.util.Notification;
//import com.homedirect.util.ValidatorATM;
//
//@Service
//public class TransactionServiceImpl extends ServiceAbstract<TransactionHistory> implements TransactionService<Account> {
//
//	private @Autowired TransactionRepository transactionRepository;
//	private @Autowired AccountServiceImpl accountService;
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean deposit(DepositRequest depositRequest) {
//		Account account = accountService.findById(depositRequest.getId());
//		Double amount = depositRequest.getAmount();
//		if (ValidatorATM.validatorDeposit(depositRequest.getAmount())) {
//			return false;
//		}
//
//		account.setAmount(account.getAmount() + amount);
//		accountService.save(account);
//		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
//				ConstantTransaction.CONTENT_DEPOSIT, TransactionType.DEPOSIT);
//
//		return true;
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean withdraw(WithdrawRequest withdrawRequest) {
//		Double amount = withdrawRequest.getAmount();
//		Account account = accountService.findById(withdrawRequest.getId());
//		if (ValidatorATM.validatorWithdraw(amount, account.getAmount())) {
//			return false;
//		}
//
//		account.setAmount(account.getAmount() - (amount + ConstantTransaction.FEE_TRANSFER));
//		accountService.save(account);
//		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
//				ConstantTransaction.CONTENT_WITHDRAW, TransactionType.WITHDRAW);
//
//		return true;
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean transfer(TransferRequest transferRequest) {
//		String sourceAccountNumber = transferRequest.getSourceAccountNumber();
//		String receiverAccountNumber = transferRequest.getReceiverAccountNumber();
//		String content = transferRequest.getContent();
//		Double amount = transferRequest.getAmount();
//		
//		Account sourceAccount = accountService.getAccountByAccountNumber(sourceAccountNumber);
//		try {
//
//			if (!checkTransfer(receiverAccountNumber, sourceAccountNumber)
//					|| ValidatorATM.validatorWithdraw(amount, sourceAccount.getAmount())) {
//				return false;
//			}
//
//			Account receiverAccount = accountService.getAccountByAccountNumber(receiverAccountNumber);
//			sourceAccount.setAmount(sourceAccount.getAmount() - amount - ConstantTransaction.FEE_TRANSFER);
//			receiverAccount.setAmount(receiverAccount.getAmount() + amount);
//
//			accountService.save(sourceAccount);
//			accountService.save(receiverAccount);
//			saveHistoryTransfer(sourceAccount.getAccountNumber(), receiverAccountNumber, amount,
//					ConstantTransaction.STATUS_SUCCESS, content, TransactionType.TRANSFER);
//
//			return true;
//
//		} catch (Exception e) {
//			saveHistoryTransfer(sourceAccount.getAccountNumber(), receiverAccountNumber, amount,
//					ConstantTransaction.STATUS_FAIL, content, TransactionType.TRANSFER);
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public void saveHistoryTransfer(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
//			String status, String content, Byte type) {
//
//		TransactionHistory history = new TransactionHistory(sourceAccountNumber, reciverAccountNumber, transferAmount,
//				ValidatorATM.getDate(), status, content, type);
//		save(history);
//	}
//
//	@Override
//	public List<TransactionHistory> showHistoryTransfer(int id) {
//		Account account = accountService.findById(id);
//		List<TransactionHistory> list = transactionRepository.findBySourceAccountNumberOrderByTimeDesc(account.getAccountNumber());
//		return list;
//
//	}
//
//	public boolean checkTransfer(String reciverAccountNumber, String sourceAccountNumber) {
//		if (reciverAccountNumber.equals(sourceAccountNumber)) {
//			Notification.alert("Khong the tu chuyen tien cho chinh minh");
//			return false;
//		}
//
//		if (accountService.checkAccountNumbers(reciverAccountNumber)) {
//			Notification.alert("So tai khoan nhan khong chinh xac");
//			return false;
//		}
//		return true;
//	}
//
//}
