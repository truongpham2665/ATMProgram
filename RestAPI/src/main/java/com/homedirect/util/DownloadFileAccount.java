package com.homedirect.util;

import java.io.IOException;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.constant.ErrorCode;
import com.homedirect.controller.AbstractController;
import com.homedirect.entity.Account;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.impl.AccountProcessorImpl;

@RestController
@RequestMapping("/excel")
public class DownloadFileAccount extends AbstractController<AccountProcessorImpl> {

	@Scheduled(cron = "0 40 10 ? * MON-FRI")
	@GetMapping(value = "/accounts")
	public String write() throws IOException, ATMException {
		WriteFile writeFile = new WriteFile();
		List<Account> accounts = processor.findAll();
		writeFile.writeListAccountResponsetoExcel(accounts);
		return ErrorCode.SUCCESS_MES;
	}
}