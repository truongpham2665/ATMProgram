package com.homedirect.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.homedirect.util.ValidatorATM;

@Entity
@Table(name = "transactionhistory")
public class TransactionHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	private String sourceAccountNumber;
	private String reciverAccountNumber;
	private Double transferAmount;
	private Date time;
	private String status;
	private String content;
	private Byte type;
	
	public class TransactionType {
		public static final byte DEPOSIT = 1;
		public static final byte WITHDRAW = 2;
		public static final byte TRANSFER = 3;
		
	}
	
	public TransactionHistory() {
	}

	public TransactionHistory(String sourceAccountNumber, String reciverAccountNumber, Double transferAmount,
			Date time, String status, String content, Byte type) {
		this.sourceAccountNumber = sourceAccountNumber;
		this.reciverAccountNumber = reciverAccountNumber;
		this.transferAmount = transferAmount;
		this.time = time;
		this.status = status;
		this.content = content;
		this.type = type;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getSourceAccountNumber() {
		return sourceAccountNumber;
	}

	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}

	public String getReciverAccountNumber() {
		return reciverAccountNumber;
	}

	public void setReciverAccountNumber(String reciverAccountNumber) {
		this.reciverAccountNumber = reciverAccountNumber;
	}

	public Double getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String printTransfer() {
		return "STK chuyen: " + sourceAccountNumber + "\tSTK nhan: " + reciverAccountNumber + "\tSo tien: "
				+ ValidatorATM.formatAmount(transferAmount) + " VND\tNoi dung: " + content + "\tThoi gian: " + time
				+ "\tTrang thai: " + status;
	}

	public String printWithdraw() {
		return "STK: " + sourceAccountNumber + "\tSo tien: " + ValidatorATM.formatAmount(transferAmount)
				+ " VND\tNoi dung: " + content + "\tThoi gian: " + time + "\tTrang thai: " + status;
	}

	public String printDeposit() {
		return "STK: " + sourceAccountNumber + "\tSo tien: " + ValidatorATM.formatAmount(transferAmount)
				+ " VND\tNoi dung: " + content + "\tThoi gian: " + time + "\tTrang thai: " + status;
	}
}
