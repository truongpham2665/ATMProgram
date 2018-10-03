package com.homedirect.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homedirect.entity.Account;
import com.homedirect.entity.Page;
import com.homedirect.entity.QTransaction;
import com.homedirect.entity.Transaction;
import com.homedirect.entity.Transaction.TransactionType;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.TransactionRepository;
import com.homedirect.service.AbstractService;
import com.homedirect.service.AccountService;
import com.homedirect.service.TransactionService;
import com.homedirect.validator.ATMInputValidator;
import com.querydsl.core.BooleanBuilder;

@Service
public class TransactionServiceImpl extends AbstractService<Transaction> implements TransactionService {

	private @Autowired TransactionRepository repository;
	private @Autowired AccountService accountService;

	@Override
	public Transaction deposit(Account account, Double amount) throws ATMException {
		account.setAmount(account.getAmount() + amount);
		return saveTransaction(account.getAccountNumber(), Transaction.Constant.NULL, amount,
				Transaction.Constant.STATUS_SUCCESS, Transaction.Constant.CONTENT_DEPOSIT, TransactionType.DEPOSIT);
	}

	@Override
	public Transaction withdraw(Account account, Double amount) throws ATMException {
		account.setAmount(account.getAmount() - (amount + Transaction.Constant.FEE_TRANSFER));
		return saveTransaction(account.getAccountNumber(), Transaction.Constant.NULL, amount,
				Transaction.Constant.STATUS_SUCCESS, Transaction.Constant.CONTENT_WITHDRAW, TransactionType.WITHDRAW);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Transaction transfer(Account fromAccount, Account toAccount, Double amount, String content)
			throws ATMException {
		fromAccount.setAmount(fromAccount.getAmount() - amount - Transaction.Constant.FEE_TRANSFER);
		toAccount.setAmount(toAccount.getAmount() + amount);

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

	@Override
	public List<Transaction> findTransactionByAccountNumber(String accountNumber) {
		return repository.findTransactionByAccountNumber(accountNumber);
	}

	public List<Transaction> findAll() {
		return repository.findAll();
	}

	// bo pageable và trả về kiểu Page(entity)
	@Override
	public Page<Transaction> search(int accountId, String fromDate, String toDate, Byte type, int pageNo, int pageSize)
			throws ATMException {
		BooleanBuilder where = null;
		try {
			Account account = accountService.findById(accountId);
			QTransaction transaction = QTransaction.transaction;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			where = new BooleanBuilder();
			if (account.getAccountNumber() != null) {
				where.and(transaction.fromAccount.eq(account.getAccountNumber()));
			}
			if (fromDate != null) {
				where.and(transaction.time.after(format.parse(fromDate))
						.and(transaction.fromAccount.likeIgnoreCase(account.getAccountNumber())));
			}
			if (toDate != null) {
				where.and(transaction.time.before(format.parse(toDate))
						.and(transaction.fromAccount.likeIgnoreCase(account.getAccountNumber())));
			}
			if (type != null) {
				where.and(transaction.type.eq(type)
						.and(transaction.fromAccount.likeIgnoreCase(account.getAccountNumber())));
			}
		} catch (ParseException e) {
			e.getMessage();
		}
		List<Transaction> transactions = toList(repository.findAll(where));
		System.out.println(transactions);
		float totalElement = transactions.size();
		float totalPage = (float) Math.ceil(totalElement/pageSize);
		return new Page<>(pageNo, pageSize, totalElement, totalPage, transactions);
	}
}