package com.homedirect.validate;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.homedirect.constant.ConstantAccount.*;
import com.homedirect.constant.ConstantTransaction;
import com.homedirect.service.AccountService;

@Component
public class ValidatorATM {
	
	private @Autowired AccountService accountService;

	public static String validateUsername(String userName) {

		if (userName.length() < USERNAME_LENG || !checkWhiteSpace(userName)) {
			return null;
		}
		return userName;
	}

	public static String validatePassword(String passWord) {

		if (passWord.length() < PASSWORD_LENG || !checkWhiteSpace(passWord)
				|| !checkLetterAndDigit(passWord)) {
			return null;
		}
		return passWord;
	}

	public static boolean validatorDeposit(Double amount) {
		if (amount == null) {
			return true;
		}
		if (amount <= 0 || amount % 10000 != 0) {
			System.out.println("So tien phai lon hon 0 va la boi so cua 10,000");
			return true;
		}
		return false;
	}

	public static boolean validatorWithdraw(Double amount, Double oldAmount) {
		if (amount == null) {
			return true;
		}
		if (amount <= 0 || amount % 10000 != 0 || amount > ConstantTransaction.MAX_AMOUNT_WITHDRAW) {
			System.out.println("So tien phai lon hon 0, nho hon 10,000,000 va la boi so cua 10,000");
			return true;
		}
		if (oldAmount - amount - ConstantTransaction.FEE_TRANSFER < 50000) {
			System.out.println("So du tai khoan hien tai khong du");
			return true;
		}
		return false;
	}
	
	public static boolean checkWhiteSpace(String string) {
		char ch;
		for (int i = 0; i < string.length(); i++) {
			ch = string.charAt(i);
			if (Character.isWhitespace(ch)) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkLetterAndDigit(String string) {
		char ch;
		int i = 0;
		boolean checkDigit = false;
		boolean checkUpperCase = false;
		boolean checkLowerCase = false;
		do {
			ch = string.charAt(i);
			if (Character.isDigit(ch)) {
				checkDigit = true;
			}
			if (Character.isUpperCase(ch)) {
				checkUpperCase = true;
			}
			if (Character.isLowerCase(ch)) {
				checkLowerCase = true;
			}
			i++;
		} while (i < string.length());
		if (checkDigit && checkUpperCase && checkLowerCase) {
			return true;
		}
		return false;
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
		while (!accountService.checkAccountNumbers(outAccountNumber)) {
			generateAccountNumber();
		}
		return outAccountNumber;
	}
}
