package com.homedirect.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.homedirect.entity.Account;
import com.homedirect.repository.AccountRepository;

public class AccountDetailsServiceImpl implements UserDetailsService {

	AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.find(username);
		if (account == null) {
			throw new UsernameNotFoundException("Account:" + username + " k ton tai trong database");
		}
		List<String> roleName = accountRepository.findById(account.getId());
		return null;
	}

}
