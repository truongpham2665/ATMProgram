package com.homedirect.service.impl;

import java.text.DecimalFormat;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homedirect.entity.Account;
import com.homedirect.repositories.AccountRepository;
import com.homedirect.request.AccountRequest;
import com.homedirect.request.ChangePassRequest;
import com.homedirect.response.AccountResponse;
import com.homedirect.service.AccountService;
import com.homedirect.transformer.impl.AccountTransformerImpl;
import com.homedirect.util.Notification;
import com.homedirect.validate.ValidatorATM;

// viết thêm method isValidCreateAccount(), 

@Service
public class AccountServiceImpl extends ServiceAbstract<Account> implements AccountService {

	private @Autowired AccountRepository accountRepository;
	private @Autowired AccountTransformerImpl accountTransformer;

	@Override
	public AccountResponse creatAcc(AccountRequest request) {
		if (!isValidCreateAccount(request.getUsername(), request.getPassword())) {
			return null;
		}
		Account account = accountTransformer.toAccount(request);
		accountRepository.save(account);
		return accountTransformer.toResponse(account);
		 
	}

	@Override
	public AccountResponse login(AccountRequest request) {
		Account account = accountRepository.findByUserNameAndPassWord(request.getUsername(), request.getPassword());
		if (account == null) {
			Notification.alert("Dang nhap that bai");
			return null;
		}
		Notification.alert("Dang nhap thanh cong");
		return accountTransformer.toResponse(account);
	}

	@Override
	public AccountResponse changePassWord(ChangePassRequest changePassRequest) {
		Account account = accountRepository.findById(changePassRequest.getId()).get();
		if (!changePassRequest.getOldPassword().equals(account.getPassWord())) {
			return null;
		}
		
		account.setPassWord(changePassRequest.getNewPassword());
		accountRepository.save(account);
		return accountTransformer.toResponse(account);
	}

	public String generateAccountNumber() {
		String pattern = "22";
		Random rd = new Random();
		int max = 9999;
		int accountNumber = rd.nextInt(max);
		DecimalFormat format = new DecimalFormat("0000");
		String outAccountNumber = pattern + format.format(accountNumber);
		while (!checkAccountNumbers(outAccountNumber)) {
			generateAccountNumber();
		}
		return outAccountNumber;
	}

	public boolean checkAccountNumbers(String accountNumber) {
		if (accountRepository.findByAccountNumber(accountNumber) == null) {
			return true;
		}
		return false;
	}
	
	public AccountResponse getOneAccount(int id) {
		return getOneAccount(id);
	}
	
	public Account findByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	public Account getAccountByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	public boolean checkUserName(String username) {
		if (accountRepository.findByUserName(username) != null) {
			return false;
		}
		return true;
	}

	public AccountResponse getAccount(AccountRequest request) {
		Account account = accountRepository.findByUserNameAndPassWord(request.getUsername(), request.getPassword());
		return accountTransformer.toResponse(account);
	}

	private boolean isValidCreateAccount(String username, String password) {
		if(ValidatorATM.validateUsername(username) == null) {
			return false;
		}
		
		if (ValidatorATM.validatePassword(password) == null) {
			return false;
		}
		if (username == null || password == null) {
			return false;
		}
		
		if (!checkUserName(username)) {
			Notification.alert("Tai khoan da ton tai");
			return false;
		}
		
		return true;
	}
}
