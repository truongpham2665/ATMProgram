package com.homedirect.service.impl;

import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homedirect.entity.Account;
import com.homedirect.entity.QTransaction;
import com.homedirect.entity.Transaction;
import com.homedirect.entity.Transaction.TransactionType;
import com.homedirect.exception.ATMException;
import com.homedirect.repository.TransactionRepository;
import com.homedirect.service.AbstractService;
import com.homedirect.service.TransactionService;
import com.homedirect.util.CsvUtil;
import com.homedirect.validator.ATMInputValidator;
import com.querydsl.core.BooleanBuilder;

@Service
public class TransactionServiceImpl extends AbstractService<Transaction> implements TransactionService {

	private @Autowired AccountServiceImpl accountService;
	private @Autowired TransactionRepository repository;

	@Override
	public Transaction deposit(Account account, Double amount) throws ATMException {
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
		return repository.findByFromAccount(accountNumber);
	}

	@Override
	public List<Transaction> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Transaction> search(int accountId, String fromDate, String toDate, Byte type, int pageNo, int pageSize)
			throws ATMException {
		Pageable pageable = null;
		BooleanBuilder where = null;
		try {
			Account account = accountService.findById(accountId);
			QTransaction transaction = QTransaction.transaction;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			pageable = PageRequest.of(pageNo, pageSize);
			where = new BooleanBuilder();
			if (account.getAccountNumber() != null) {
				where.and(transaction.fromAccount.eq(account.getAccountNumber()));
			}
			if (type != null) {
				where.and(transaction.type.eq(type)
						.and(transaction.fromAccount.likeIgnoreCase(account.getAccountNumber())));
			}
			if (fromDate != null && fromDate.equals(toDate)) {
				where.and(transaction.time.goe(format.parse(fromDate)));
				return repository.findAll(where, pageable);
			}
			if (fromDate != null & toDate != null) {
				where.and(transaction.time.between(format.parse(fromDate), format.parse(toDate)));
			}
		} catch (ParseException e) {
			e.getMessage();
		}
		return repository.findAll(where, pageable);
	}

	@Override
	@Scheduled(cron = "0 47 13 ? * MON-FRI")
	public void exportCsv() throws Exception {
		String fileCsv = System.getProperty("user.dir") + "/src/main/resources/Transaction.csv";
		List<Transaction> transactions = findAll();
		FileWriter writeFile = new FileWriter(fileCsv);
		CsvUtil.writerLine(writeFile, Arrays.asList("Id", "FromAccount", "ToAccount", "Type", "Content", "Status",
				"TransferAmount", "DateTime"));
		for (Transaction transaction : transactions) {
			List<String> list = new ArrayList<>();
			list.add(String.valueOf(transaction.getId()));
			list.add(transaction.getFromAccount());
			list.add(chooseToAccount(transaction.getToAccount()));
			list.add(String.valueOf(transaction.getType()));
			list.add(transaction.getContent());
			list.add(transaction.getStatus());
			list.add(String.valueOf(transaction.getTransferAmount()));
			list.add(String.valueOf(transaction.getTime()));
			CsvUtil.writerLine(writeFile, list);
		}
		writeFile.flush();
		writeFile.close();
	}

	private String chooseToAccount(String toAccount) {
		if (toAccount == null) {
			return " ";
		}
		return toAccount;
	}
}