package com.homedirect.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.homedirect.entity.Transaction;

@Repository
public interface TransactionRepository
		extends JpaRepository<Transaction, Integer> {

	List<Transaction> findByFromAccountContaining(String accountNumber, Pageable pageable);

	List<Transaction> findByFromAccountAndTime(String accountNumber, Date toDate, Pageable pageable);
	
	List<Transaction> findByFromAccountAndTypeAndTime(String accountNumber, Byte type, Date DateTime);
	
	List<Transaction> findByFromAccountAndTimeBetween(String accountNumber, Date fromDate, Date toDate, Pageable pageable);

	List<Transaction> findByFromAccountAndTypeLike(String accountNumber, Byte type, Pageable pageable);
	
	List<Transaction> findByFromAccountAndTimeLike(String accountNumber, Date fromDate, Pageable pageable);
}
