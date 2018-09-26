package com.homedirect.transformer;

import static com.homedirect.constant.ConstantAccount.DEFAULT_AMOUNT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homedirect.constant.ErrorMyCode;
import com.homedirect.entity.Account;
import com.homedirect.message.MessageException;
import com.homedirect.request.AccountRequest;
import com.homedirect.response.ATMReponse;
import com.homedirect.response.AccountResponse;
import com.homedirect.validate.ValidatorInputATM;

@Component
public class AccountTransformer {

	private @Autowired ValidatorInputATM validatorInputATM;

	public Account toAccount(AccountRequest request) {
		Account newAccount = new Account();
		newAccount.setId(request.getId());
		newAccount.setAccountNumber(validatorInputATM.generateAccountNumber());
		newAccount.setUsername(request.getUsername());
		newAccount.setPassword(request.getPassword());
		newAccount.setAmount(DEFAULT_AMOUNT);
		return newAccount;
	}

	public AccountResponse toResponse(Account account) {
		AccountResponse response = new AccountResponse();
		response.setId(account.getId());
		response.setAccountNumber(account.getAccountNumber());
		response.setUsername(account.getUsername());
		response.setAmount(account.getAmount());
		return response;
	}
	
	public ATMReponse toATMResponse(AccountResponse accountResponse) {
		ATMReponse atmResponse = new ATMReponse();
		atmResponse.setCode(ErrorMyCode.SUCCESS);
		atmResponse.setMessage(MessageException.success());
		atmResponse.setAccountResponse(accountResponse);
		return atmResponse;
	}

	public List<AccountResponse> toResponseList(List<Account> accounts) {
		return accounts.stream().map(this::toResponse).collect(Collectors.toList());
	}
	
	public List<ATMReponse> toATMResponseList(List<AccountResponse> accountResponses) {
		return accountResponses.stream().map(this::toATMResponse).collect(Collectors.toList());
	}
}
