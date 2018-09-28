package com.homedirect.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homedirect.entity.Account;
import com.homedirect.entity.QTransaction;
import com.homedirect.entity.Transaction;
import com.homedirect.entity.Transaction.TransactionType;
import com.homedirect.exception.ATMException;
import com.homedirect.request.DepositRequest;
import com.homedirect.service.AbstractService;
import com.homedirect.service.TransactionService;
import com.homedirect.validator.ATMInputValidator;
import com.querydsl.core.BooleanBuilder;

//chuyen validate transaction qua TransactionProcessorImpl
// chuyen checkTransfer qua ATMInputValidator
@Service
public class TransactionServiceImpl extends AbstractService<Transaction> implements TransactionService {

	private @Autowired AccountServiceImpl accountService;

	@Override
	public Transaction deposit(DepositRequest depositRequest) throws ATMException {
		Account account = accountService.findById(depositRequest.getId());
		Double amount = depositRequest.getAmount();
		account.setAmount(account.getAmount() + amount);
		return saveTransaction(account.getAccountNumber(), null, amount, Transaction.Constant.STATUS_SUCCESS,
				Transaction.Constant.CONTENT_DEPOSIT, TransactionType.DEPOSIT);
	}

	@Override
	public Transaction withdraw(Account account, Double amount) throws ATMException {
		account.setAmount(account.getAmount() - (amount + Transaction.Constant.FEE_TRANSFER));
		return saveTransaction(account.getAccountNumber(), null, amount, Transaction.Constant.STATUS_SUCCESS,
				Transaction.Constant.CONTENT_WITHDRAW, TransactionType.WITHDRAW);
	}

	/// missField -> Notfound
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Transaction transfer(Account fromAccount, Account toAccount, Double amount, String content)
			throws ATMException {
		fromAccount.setAmount(fromAccount.getAmount() - amount - Transaction.Constant.FEE_TRANSFER);
		toAccount.setAmount(toAccount.getAmount() + amount);

		accountService.save(fromAccount);
		accountService.save(toAccount);

		return saveTransaction(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount,
				Transaction.Constant.STATUS_SUCCESS, content, TransactionType.TRANSFER);
	}

	@Override
	public Transaction saveTransaction(String fromAccountNumber, String toAccountNumber, Double transferAmount,
			String status, String content, Byte type) {

		Transaction transaction = new Transaction(fromAccountNumber, toAccountNumber, transferAmount,
				ATMInputValidator.getDate(), status, content, type);
		return save(transaction);
	}

	// Đổi kiểu trả về list sang Page.
	@Override
	public Page<Transaction> search(String accountNumber, String fromDate, String toDate, Byte type, int pageNo,
			int pageSize) throws ParseException {

		QTransaction transaction = QTransaction.transaction;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		BooleanBuilder where = new BooleanBuilder();

		if (fromDate != null) {
			where.and(transaction.time.after(format.parse(toDate)));
		}
		if (toDate != null) {
			where.and(transaction.time.before(format.parse(fromDate)));
		}
		if (type != null) {
			where.and(transaction.type.eq(type));
		}
		if (accountNumber != null) {
			where.and(transaction.fromAccount.eq(accountNumber));
		}
		return findAll(where, pageable);
	}
}
