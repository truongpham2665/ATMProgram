package com.homedirect.request;

public class TransferRequest {

//	private int toId;
	private int fromId;
	private String toAccountNumber;
//	private String fromAccountNumber;
	private Double amount;
	private String content;

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

//	public String getToAccountNumber() {
//		return toAccountNumber;
//	}
//
//	public void setToAccountNumber(String toAccountNumber) {
//		this.toAccountNumber = toAccountNumber;
//	}
//
//	public String getFromAccountNumber() {
//		return fromAccountNumber;
//	}
//
//	public void setFromAccountNumber(String fromAccountNumber) {
//		this.fromAccountNumber = fromAccountNumber;
//	}
}
