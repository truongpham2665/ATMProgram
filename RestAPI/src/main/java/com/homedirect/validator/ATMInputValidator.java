package com.homedirect.validator;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.entity.Account;
import com.homedirect.entity.Transaction;
import com.homedirect.exception.ATMException;
import com.homedirect.exception.MessageException;
import com.homedirect.service.AccountService;

@Component
public class ATMInputValidator {

	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{3,15}$";
	private static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
	private DecimalFormat decimalFormat;
	private @Autowired ATMStorageValidator validatorStorageATM;
	private @Autowired AccountService accountService;

	public static boolean validateUsername(String userName) {
		return isValidUsername(userName);
	}

	public static boolean validatePassword(String password) {
		return isValidPassword(password);
	}

	public static boolean validatorDeposit(Double amount) {
		if (amount == null) {
			return true;
		}
		if (amount <= 0 || amount % 10000 != 0) {
			return true;
		}
		return false;
	}

	public static boolean validatorWithdraw(Double amount, Double oldAmount) {
		if (amount == null) {
			return true;
		}
		if (amount <= 0 || amount % 10000 != 0 || amount > Transaction.Constant.MAX_AMOUNT_WITHDRAW) {
			System.out.println("Số tiền phải lớn hơn 0, nhỏ hơn 10,000,000 và là bội số của 10,000");
			return true;
		}
		if (oldAmount - amount - Transaction.Constant.FEE_TRANSFER < Transaction.Constant.DEFAULT_BALANCE) {
			System.out.println("Số dư tài khoản hiện không đủ");
			return true;
		}
		return false;
	}

	public static boolean isValidUsername(String username) {
		return username.matches(USERNAME_PATTERN);
	}

	public static boolean isValidPassword(String password) {
		return password.matches(PASSWORD_PATTERN);
	}
	
	public boolean isValidCreateAccount(String username, String password) throws ATMException {
		if (!validateUsername(username)) {
			throw new ATMException(MessageException.usernameIsValid());
		}
		if (!validatePassword(password)) {
			throw new ATMException(MessageException.passwordIsValid());
		}
		if (username == null || password == null) {
			throw new ATMException(MessageException.missField());
		}
		if (!validatorStorageATM.checkUserName(username)) {
			throw new ATMException(MessageException.accountIsExist());
		}
		return true;
	}

	public static Date getDate() {
		java.util.Date currentime = new Date();
		Timestamp date = new Timestamp(currentime.getTime());
		return date;
	}

	public static String formatAmount(Double amount) {
		DecimalFormat myFormatter = new DecimalFormat("###,###.00");
		String output = myFormatter.format(amount);
		return output;
	}

	public String generateAccountNumber() {
		String pattern = "22";
		Random rd = new Random();
		int max = 9999;
		int accountNumber = rd.nextInt(max);
		DecimalFormat format = new DecimalFormat("0000");
		String outAccountNumber = pattern + format.format(accountNumber);
		while (!validatorStorageATM.checkAccountNumbers(outAccountNumber)) {
			generateAccountNumber();
		}
		return outAccountNumber;
	}

	public boolean isValidateInputTransfer(int fromId, String toAccountNumber) {
		Optional<Account> fromAccount = accountService.findById(fromId);
		if (fromAccount == null) {
			return false;
		}
		Account toAccount = accountService.findByAccountNumber(toAccountNumber);
		if (toAccount == null) {
			return false;
		}
		return true;
	}

	public String numberFormat(String pattern, double value) {
		decimalFormat = new DecimalFormat(pattern);
		String output = decimalFormat.format(value);
		return output;
	}

	public String setAmount(double amount) {
		String pattern = "###,###,###";
		return numberFormat(pattern, amount);
	}
}