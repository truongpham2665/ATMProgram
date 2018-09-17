package com.homedirect.service;

import com.homedirect.entity.Account;
import com.homedirect.request.ChangePassRequest;

public interface AccountService {
	
	public boolean creatAcc(String userName, String passWord);

	public Account login();

	public boolean changePassWord(ChangePassRequest changePassRequest);

}
