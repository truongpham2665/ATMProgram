package com.homedirect.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransactionResponse {

	private int id;
	private String fromAccountNumber;
	private String toAccountNumber;
	private String content;
	private String status;
	private Date time;
	private Double transferAmount;
	private int type;
}
