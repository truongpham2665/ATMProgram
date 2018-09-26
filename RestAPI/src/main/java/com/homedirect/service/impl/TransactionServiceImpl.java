package com.homedirect.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homedirect.entity.Account;
import com.homedirect.entity.Transaction;
import com.homedirect.entity.Transaction.TransactionType;
import com.homedirect.exception.ATMException;
import com.homedirect.exception.MessageException;
import com.homedirect.repository.TransactionRepository;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.AbstractService;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.TransactionTransformer;
import com.homedirect.validator.ATMInputValidator;
import com.homedirect.validator.ATMStorageValidator;

@Service
public class TransactionServiceImpl extends AbstractService<Transaction> implements TransactionService {

	private @Autowired AccountServiceImpl accountService;
	private @Autowired ATMStorageValidator validatorStorageATM;
	private @Autowired ATMInputValidator validatorInputATM;
	private @Autowired TransactionRepository transactionRepository;
	private @Autowired TransactionTransformer transactionTransformer;

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

	@Override
	public List<TransactionResponse> search(Integer accountId, String fromDate, String toDate, Byte type,
			int pageNo, int pageSize) throws ATMException {
		Account account = accountService.findById(accountId).get();
		if (account == null) {
			throw new ATMException(MessageException.haveNotTransaction());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);
			if (fromDate == null && toDate == null && type == null) {
				List<Transaction> histories = transactionRepository.findByFromAccountContaining(account.getAccountNumber(),
						pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (fromDate == null && toDate == null) {
				List<Transaction> histories = transactionRepository
						.findByFromAccountAndTypeLike(account.getAccountNumber(), type, pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (type == null && toDate == null) {
				List<Transaction> histories = transactionRepository.findByFromAccountAndTimeLike(
						account.getAccountNumber(), format.parse(fromDate), pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (type == null && fromDate == null) {
				List<Transaction> histories = transactionRepository
						.findByFromAccountAndTimeLike(account.getAccountNumber(), format.parse(toDate), pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (fromDate == null) {
				List<Transaction> histories = transactionRepository
						.findByFromAccountAndTypeAndTime(account.getAccountNumber(), type, format.parse(toDate));
				return transactionTransformer.toResponse(histories);
			}
			if (toDate == null) {
				List<Transaction> histories = transactionRepository
						.findByFromAccountAndTypeAndTime(account.getAccountNumber(), type, format.parse(fromDate));
				return transactionTransformer.toResponse(histories);
			}
			if (type == null) {
				List<Transaction> histories = transactionRepository.findByFromAccountAndTimeBetween(
						account.getAccountNumber(), format.parse(fromDate), format.parse(toDate), pageable);
				return transactionTransformer.toResponse(histories);
			}
		} catch (Exception e) {
			throw new ATMException(MessageException.haveNotTransaction());
		}
		throw new ATMException(MessageException.haveNotTransaction());
	}
}
