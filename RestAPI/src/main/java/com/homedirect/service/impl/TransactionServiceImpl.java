package com.homedirect.service.impl;

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
import com.homedirect.repository.TransactionRepository;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.transformer.TransactionHistoryTransformer;
import com.homedirect.validate.ValidatorInputATM;
import com.homedirect.validate.ValidatorStorageATM;

@Service
public class TransactionServiceImpl extends AbstractService<TransactionHistory> implements TransactionService {

	private @Autowired AccountServiceImpl accountService;
	private @Autowired AccountTransformer accountTransformer;
	private @Autowired ValidatorStorageATM validatorStorageATM;
	private @Autowired ValidatorInputATM validatorInputATM;
	private @Autowired TransactionRepository transactionRepository;
	private @Autowired TransactionHistoryTransformer transactionTransformer;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountResponse deposit(DepositRequest depositRequest) {
		Account account = accountService.findById(depositRequest.getId()).get();
		Double amount = depositRequest.getAmount();
		if (ValidatorInputATM.validatorDeposit(depositRequest.getAmount())) {
//			throw new AccountException("Nạp tiền thất bại \n So tien phai lon hon 0 va la boi so cua 10,000");
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
		if (ValidatorInputATM.validatorWithdraw(amount, account.getAmount())) {
//			throw new AccountException(
//					"Rút tiền thất bại! \n Số dư không đủ \n Hoặc số tiền phải lớn hơn 0 và là bội số của 10,000");
		}
		account.setAmount(account.getAmount() - (amount + ConstantTransaction.FEE_TRANSFER));
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_WITHDRAW, TransactionType.WITHDRAW);

		return accountTransformer.toResponse(accountService.save(account));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountResponse transfer(TransferRequest Request) {
		if (!validatorInputATM.isValidateInputTransfer(Request.getFromId(), Request.getToAccountNumber())) {
			System.out.println(Request.getFromId() + Request.getToAccountNumber());
//			throw new AccountException("số tài khoản không đúng");
		}

		Account fromAccount = accountService.findById(Request.getFromId()).get();
		Account toAccount = accountService.findByAccountNumber(Request.getToAccountNumber());
		if (!checkTransfer(toAccount.getId(), Request.getFromId())
				|| ValidatorInputATM.validatorWithdraw(Request.getAmount(), fromAccount.getAmount())) {
//			throw new AccountException(
//					"Chuyển tiền thất bại! \n Số dư không đủ \n Hoặc số tiền phải lớn hơn 0 và là bội số của 10,000");
		}

		fromAccount.setAmount(fromAccount.getAmount() - Request.getAmount() - ConstantTransaction.FEE_TRANSFER);
		toAccount.setAmount(toAccount.getAmount() + Request.getAmount());

		accountService.save(fromAccount);
		accountService.save(toAccount);

		saveHistoryTransfer(fromAccount.getAccountNumber(), Request.getToAccountNumber(), Request.getAmount(),
				ConstantTransaction.STATUS_SUCCESS, ConstantTransaction.CONTENT_TRANSFER, TransactionType.TRANSFER);

		return accountTransformer.toResponse(fromAccount);
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
			int pageNo, int pageSize) {
		Account account = accountService.findById(accountId).get();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		if (fromDate == null && toDate == null && type == null) {
			List<TransactionHistory> histories = transactionRepository.findByFromAccount(account.getAccountNumber(),
					pageable);
			return transactionTransformer.toResponse(histories);
		}
		if (fromDate == null && toDate == null) {
			List<TransactionHistory> histories = transactionRepository
					.findByFromAccountAndType(account.getAccountNumber(), type, pageable);
			return transactionTransformer.toResponse(histories);
		}
		if (type == null && toDate == null) {
			List<TransactionHistory> histories = transactionRepository
					.findByFromAccountAndTimeGreaterThan(account.getAccountNumber(), fromDate, pageable);
			return transactionTransformer.toResponse(histories);
		}
		if (type == null && fromDate == null) {
			List<TransactionHistory> histories = transactionRepository
					.findByFromAccountAndTimeLessThan(account.getAccountNumber(), toDate, pageable);
			return transactionTransformer.toResponse(histories);
		}
		if (type == null) {
			List<TransactionHistory> histories = transactionRepository
					.findByFromAccountAndTimeBetween(account.getAccountNumber(), fromDate, toDate, pageable);
			return transactionTransformer.toResponse(histories);
		}
		return null;
		//throw new AccountException("không tồn tại giao dịch!");
	}
}
