package com.homedirect.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.mindrot.jbcrypt.BCrypt;
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
import com.homedirect.exception.MessageException;
import com.homedirect.repository.TransactionRepository;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.service.AbstractService;
import com.homedirect.service.TransactionService;
import com.homedirect.validator.ATMInputValidator;
import com.homedirect.validator.ATMStorageValidator;
import com.querydsl.core.BooleanBuilder;

@Service
public class TransactionServiceImpl extends AbstractService<Transaction> implements TransactionService {

	private @Autowired AccountServiceImpl accountService;
	private @Autowired ATMStorageValidator validatorStorageATM;
	private @Autowired ATMInputValidator validatorInputATM;
	private @Autowired TransactionRepository repository;

	@Override
	public Transaction deposit(DepositRequest depositRequest) throws ATMException {
		Account account = accountService.findById(depositRequest.getId()).get();
		Double amount = depositRequest.getAmount();
		if (ATMInputValidator.validatorDeposit(depositRequest.getAmount())) {
			throw new ATMException(MessageException.depositFalse());
		}

		account.setAmount(account.getAmount() + amount);
		return saveTransaction(account.getAccountNumber(), null, amount, Transaction.Constant.STATUS_SUCCESS,
				Transaction.Constant.CONTENT_DEPOSIT, TransactionType.DEPOSIT);
	}

	@Override
	public Transaction withdraw(WithdrawRequest withdrawRequest) throws ATMException {
		Double amount = withdrawRequest.getAmount();
		Account account = accountService.findById(withdrawRequest.getId()).get();
		if (ATMInputValidator.validatorWithdraw(amount, account.getAmount())) {
			throw new ATMException(MessageException.withdrawFalse());
		}
//		if (!account.getPassword().equals(withdrawRequest.getPassword())) {
//			return null;
//		}
		if (!BCrypt.checkpw(withdrawRequest.getPassword(), account.getPassword())) {
			throw new ATMException(MessageException.passwordIsValid());
		}
		account.setAmount(account.getAmount() - (amount + Transaction.Constant.FEE_TRANSFER));
		return saveTransaction(account.getAccountNumber(), null, amount, Transaction.Constant.STATUS_SUCCESS,
				Transaction.Constant.CONTENT_WITHDRAW, TransactionType.WITHDRAW);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Transaction transfer(TransferRequest request) throws ATMException {
		if (!validatorInputATM.isValidateInputTransfer(request.getFromId(), request.getToAccountNumber())) {
			return null;
		}
		Account fromAccount = accountService.findById(request.getFromId()).get();
		Account toAccount = accountService.findByAccountNumber(request.getToAccountNumber());
		if (!checkTransfer(toAccount.getId(), request.getFromId())
				|| ATMInputValidator.validatorWithdraw(request.getAmount(), fromAccount.getAmount())) {
			throw new ATMException(MessageException.transferFalse());
		}
		if (!fromAccount.getPassword().equals(request.getPassword())) {
			throw new ATMException(MessageException.passwordIsValid());
		}

		fromAccount.setAmount(fromAccount.getAmount() - request.getAmount() - Transaction.Constant.FEE_TRANSFER);
		toAccount.setAmount(toAccount.getAmount() + request.getAmount());

		accountService.save(fromAccount);
		accountService.save(toAccount);

		return saveTransaction(fromAccount.getAccountNumber(), request.getToAccountNumber(), request.getAmount(),
				Transaction.Constant.STATUS_SUCCESS, Transaction.Constant.CONTENT_TRANSFER, TransactionType.TRANSFER);
	}

	@Override
	public Transaction saveTransaction(String fromAccountNumber, String toAccountNumber, Double transferAmount,
			String status, String content, Byte type) {

		Transaction transaction = new Transaction(fromAccountNumber, toAccountNumber, transferAmount,
				ATMInputValidator.getDate(), status, content, type);
		return save(transaction);
	}

	public boolean checkTransfer(Integer toId, Integer fromId) {
		if (toId == fromId) {
			return false;
		}

		if (validatorStorageATM.checkId(toId)) {
			return false;
		}

		if (toId == null || fromId == null) {
			return false;
		}
		return true;
	}

	// Đổi kiểu trả về list sang Page.
	@Override
	public Page<Transaction> search(String accountNumber, String fromDate, String toDate,
									Byte type, int pageNo, int pageSize) throws ParseException {
		
		QTransaction transaction = QTransaction.transaction;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		BooleanBuilder where = new BooleanBuilder();
		
		if (fromDate != null) {
			where.and(transaction.time.after(format.parse(toDate)));
		}
		if (toDate !=null) {
			where.and(transaction.time.before(format.parse(fromDate)));
		}
		if (type != null) {
			where.and(transaction.type.eq(type));
		}
		if (accountNumber != null) {
			where.and(transaction.fromAccount.eq(accountNumber));
		}
		
		return repository.findAll(where, pageable);
	}
}
