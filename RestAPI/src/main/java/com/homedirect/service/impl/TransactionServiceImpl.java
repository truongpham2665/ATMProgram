package com.homedirect.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homedirect.constant.ConstantTransaction;
import com.homedirect.entity.Account;
import com.homedirect.entity.TransactionHistory;
import com.homedirect.entity.TransactionHistory.TransactionType;
import com.homedirect.message.ATMException;
import com.homedirect.message.MessageException;
import com.homedirect.repository.TransactionRepository;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.AbstractService;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.TransactionHistoryTransformer;
import com.homedirect.validate.ValidatorInputATM;
import com.homedirect.validate.ValidatorStorageATM;

@Service
public class TransactionServiceImpl extends AbstractService<TransactionHistory> implements TransactionService {

	private @Autowired AccountServiceImpl accountService;
	private @Autowired ValidatorStorageATM validatorStorageATM;
	private @Autowired ValidatorInputATM validatorInputATM;
	private @Autowired TransactionRepository transactionRepository;
	private @Autowired TransactionHistoryTransformer transactionTransformer;

	@Override
	public Account deposit(DepositRequest depositRequest) throws ATMException {
		Account account = accountService.findById(depositRequest.getId()).get();
		Double amount = depositRequest.getAmount();
		if (ValidatorInputATM.validatorDeposit(depositRequest.getAmount())) {
			throw new ATMException(MessageException.depositFalse());
		}

		account.setAmount(account.getAmount() + amount);
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_DEPOSIT, TransactionType.DEPOSIT);

		return account;
	}

	@Override
	public Account withdraw(WithdrawRequest withdrawRequest) throws ATMException {
		Double amount = withdrawRequest.getAmount();
		Account account = accountService.findById(withdrawRequest.getId()).get();
		if (ValidatorInputATM.validatorWithdraw(amount, account.getAmount())) {
			throw new ATMException(MessageException.withdrawFalse());
		}
		if (!account.getPassword().equals(withdrawRequest.getPassword())) {
			return null;
		}
		account.setAmount(account.getAmount() - (amount + ConstantTransaction.FEE_TRANSFER));
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_WITHDRAW, TransactionType.WITHDRAW);

		return account;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Account transfer(TransferRequest request) throws ATMException, ATMException {
		if (!validatorInputATM.isValidateInputTransfer(request.getFromId(), request.getToAccountNumber())) {
			return null;
		}
		Account fromAccount = accountService.findById(request.getFromId()).get();
		Account toAccount = accountService.findByAccountNumber(request.getToAccountNumber());
		if (!checkTransfer(toAccount.getId(), request.getFromId())
				|| ValidatorInputATM.validatorWithdraw(request.getAmount(), fromAccount.getAmount())) {
			throw new ATMException(MessageException.transferFalse());
		}
		if (!fromAccount.getPassword().equals(request.getPassword())) {
			throw new ATMException(MessageException.passwordIsValid());
		}

		fromAccount.setAmount(fromAccount.getAmount() - request.getAmount() - ConstantTransaction.FEE_TRANSFER);
		toAccount.setAmount(toAccount.getAmount() + request.getAmount());

		accountService.save(fromAccount);
		accountService.save(toAccount);

		saveHistoryTransfer(fromAccount.getAccountNumber(), request.getToAccountNumber(), request.getAmount(),
				ConstantTransaction.STATUS_SUCCESS, ConstantTransaction.CONTENT_TRANSFER, TransactionType.TRANSFER);

		return fromAccount;
	}

	@Override
	public void saveHistoryTransfer(String fromAccountNumber, String toAccountNumber, Double transferAmount,
			String status, String content, Byte type) {

		TransactionHistory history = new TransactionHistory(fromAccountNumber, toAccountNumber, transferAmount,
				ValidatorInputATM.getDate(), status, content, type);
		save(history);
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
	public List<TransactionResponse> searchHistory(Integer accountId, String fromDate, String toDate, Byte type,
			int pageNo, int pageSize) throws ATMException {
		Account account = accountService.findById(accountId).get();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);
			if (fromDate == null && toDate == null && type == null) {
				List<TransactionHistory> histories = transactionRepository.findByFromAccountContaining(account.getAccountNumber(),
						pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (fromDate == null && toDate == null) {
				List<TransactionHistory> histories = transactionRepository
						.findByFromAccountAndTypeLike(account.getAccountNumber(), type, pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (type == null && toDate == null) {
				List<TransactionHistory> histories = transactionRepository.findByFromAccountAndTimeLike(
						account.getAccountNumber(), format.parse(fromDate), pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (type == null && fromDate == null) {
				List<TransactionHistory> histories = transactionRepository
						.findByFromAccountAndTimeLike(account.getAccountNumber(), format.parse(toDate), pageable);
				return transactionTransformer.toResponse(histories);
			}
			if (fromDate == null) {
				List<TransactionHistory> histories = transactionRepository
						.findByFromAccountAndTypeAndTime(account.getAccountNumber(), type, format.parse(toDate));
				return transactionTransformer.toResponse(histories);
			}
			if (toDate == null) {
				List<TransactionHistory> histories = transactionRepository
						.findByFromAccountAndTypeAndTime(account.getAccountNumber(), type, format.parse(fromDate));
				return transactionTransformer.toResponse(histories);
			}
			if (type == null) {
				List<TransactionHistory> histories = transactionRepository.findByFromAccountAndTimeBetween(
						account.getAccountNumber(), format.parse(fromDate), format.parse(toDate), pageable);
				return transactionTransformer.toResponse(histories);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		throw new ATMException(MessageException.haveNotTransaction());
	}
}
