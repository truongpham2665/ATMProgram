package com.homedirect.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.homedirect.constant.ConstantTransaction;
import com.homedirect.entity.Account;
import com.homedirect.entity.QTransactionHistory;
import com.homedirect.entity.TransactionHistory;
import com.homedirect.entity.TransactionHistory.TransactionType;
import com.homedirect.message.AccountException;
import com.homedirect.repositories.TransactionRepository;
import com.homedirect.request.DepositRequest;
import com.homedirect.request.SearchTransactionHistoryRequest;
import com.homedirect.request.TransferRequest;
import com.homedirect.request.WithdrawRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.response.TransactionResponse;
import com.homedirect.service.TransactionService;
import com.homedirect.transformer.AccountTransformer;
import com.homedirect.transformer.TransactionHistoryTransformer;
import com.homedirect.validate.*;
import com.querydsl.core.BooleanBuilder;

// thay String username... = request.get.getUsername...
// String sourceAccountNumber = transferRequest.getSourceAccountNumber();
// String receiverAccountNumber = transferRequest.getReceiverAccountNumber();
// String content = transferRequest.getContent();
// Double amount = transferRequest.getAmount();
// đổi kiểu trả về từ Account -> AccountResponse
// thay return null == throws New AccountException
// thêm điều kiện dòng 75, 76



//chia Validator -> 2: ValidatorATM(check voi database) && ValidatorInputATM (check input)
//ValidateATM: checkAccountNumber && checkUserName

@Service
public class TransactionServiceImpl extends AbstractService<TransactionHistory> implements TransactionService {

	private @Autowired AccountServiceImpl accountService;
	private @Autowired AccountTransformer accountTransformer;
	private @Autowired ValidatorATM validatorATM;
	private @Autowired ValidatorInputATM validatorInputATM;
	private @Autowired TransactionRepository transactionRepository;
	private @Autowired TransactionHistoryTransformer transactionTransformer;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountResponse deposit(DepositRequest depositRequest) {
		Account account = accountService.findById(depositRequest.getId()).get(); // thêm get() line 28,46;
		Double amount = depositRequest.getAmount();
		if (ValidatorInputATM.validatorDeposit(depositRequest.getAmount())) {
			throw new AccountException("Nạp tiền thất bại \n So tien phai lon hon 0 va la boi so cua 10,000");
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
			throw new AccountException(
					"Rút tiền thất bại! \n Số dư không đủ \n Hoặc số tiền phải lớn hơn 0 và là bội số của 10,000");
		}
		account.setAmount(account.getAmount() - (amount + ConstantTransaction.FEE_TRANSFER));
		saveHistoryTransfer(account.getAccountNumber(), null, amount, ConstantTransaction.STATUS_SUCCESS,
				ConstantTransaction.CONTENT_WITHDRAW, TransactionType.WITHDRAW);

		return accountTransformer.toResponse(accountService.save(account));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountResponse transfer(TransferRequest Request) {
		if (!validatorInputATM.isValidateAccountNumber(Request.getFromAccountNumber(), Request.getToAccountNumber())) {
			throw new AccountException("số tài khoản không đúng");
		}
		Account fromAccount = accountService.findByAccountNumber(Request.getFromAccountNumber());
		Account toAccount = accountService.findByAccountNumber(Request.getToAccountNumber());
		if (!checkTransfer(Request.getToAccountNumber(), Request.getFromAccountNumber())
				|| ValidatorInputATM.validatorWithdraw(Request.getAmount(), fromAccount.getAmount())) {
			throw new AccountException(
					"Chuyển tiền thất bại! \n Số dư không đủ \n Hoặc số tiền phải lớn hơn 0 và là bội số của 10,000");
		}

		fromAccount.setAmount(fromAccount.getAmount() - Request.getAmount() - ConstantTransaction.FEE_TRANSFER);
		toAccount.setAmount(toAccount.getAmount() + Request.getAmount());

		accountService.save(fromAccount);
		accountService.save(toAccount);
		saveHistoryTransfer(toAccount.getAccountNumber(), Request.getToAccountNumber(), Request.getAmount(),
				ConstantTransaction.STATUS_SUCCESS, Request.getContent(), TransactionType.TRANSFER);

		return accountTransformer.toResponse(fromAccount);
	}

	@Override
	public void saveHistoryTransfer(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
			String status, String content, Byte type) {

		TransactionHistory history = new TransactionHistory(sourceAccountNumber, reciverAccountNumber, transferAmount,
				ValidatorInputATM.getDate(), status, content, type);
		save(history);
	}

	public boolean checkTransfer(String toAccountNumber, String fromAccountNumber) {
		if (toAccountNumber.equals(fromAccountNumber)) {
			return false;
		}

		if (validatorATM.checkAccountNumbers(toAccountNumber)) {
			return false;
		}

		if (toAccountNumber == null || fromAccountNumber == null) {
			return false;
		}
		return true;
	}

	// Sửa kiểu trả về TransactionHistory -> TransactionResponse.
	// Bỏ hàm showHistoryTransfer().
	@Override
	public Iterable<TransactionResponse> searchHistory(SearchTransactionHistoryRequest q) {
		if (q == null) {
			return null;
		}
		QTransactionHistory history = QTransactionHistory.transactionHistory;
		BooleanBuilder where = new BooleanBuilder();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date fromDate = format.parse(q.getFromDate());
			Date toDate = format.parse(q.getToDate());
			Date sqlFromDate = new Date(fromDate.getTime());
			Date sqlToDate = new Date(toDate.getTime());
			where.and(history.fromAccount.eq(q.getAccountNumber())).and(history.type.eq(q.getType()))
					.and(history.time.between(sqlFromDate, sqlToDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return transactionTransformer.toResponseIterable(transactionRepository.findAll(where));
	}
//	public Page<TransactionResponse> searchHistory(SearchTransactionHistoryRequest q) {
//		if (q == null) {
//			return null;
//		}
//		QTransactionHistory history = QTransactionHistory.transactionHistory;
//		BooleanBuilder where = new BooleanBuilder();
//		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//		try {
//			Date fromDate = format.parse(q.getFromDate());
//			Date toDate = format.parse(q.getToDate());
//			Date sqlFromDate = new Date(fromDate.getTime());
//			Date sqlToDate = new Date(toDate.getTime());
//			where.and(history.fromAccount.eq(q.getAccountNumber())).and(history.type.eq(q.getType()))
//					.and(history.time.between(sqlFromDate, sqlToDate));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return (Page<TransactionResponse>) transactionTransformer.toResponseIterable(transactionRepository.findAll(where, q.getId()));
//	}
}
