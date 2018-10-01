package com.homedirect.util;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homedirect.controller.AbstractController;
import com.homedirect.exception.ATMException;
import com.homedirect.processor.impl.AccountProcessorImpl;
import com.homedirect.response.AccountResponse;

@RestController
@RequestMapping("/excel")
public class DownloadFileAccount extends AbstractController<AccountProcessorImpl> {

	@GetMapping(value = "/accounts")
	public String write() throws IOException, ATMException {
		WriteFile writeFile = new WriteFile();
		List<AccountResponse> accountResponses = processor.findAll();
		writeFile.writeListAccountResponsetoExcel(accountResponses);
		return "success";
	}
}
