package com.homedirect.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.homedirect.entity.TransactionHistory;

@Repository
public interface TransactionRepository
		extends JpaRepository<TransactionHistory, Integer> {

	List<TransactionHistory> findByFromAccountContaining(String accountNumber, Pageable pageable);

	List<TransactionHistory> findByFromAccountAndTime(String accountNumber, Date toDate, Pageable pageable);
	
	List<TransactionHistory> findByFromAccountAndTypeAndTime(String accountNumber, Byte type, Date DateTime);
	
	List<TransactionHistory> findByFromAccountAndTimeBetween(String accountNumber, Date fromDate, Date toDate, Pageable pageable);

	List<TransactionHistory> findByFromAccountAndTypeLike(String accountNumber, Byte type, Pageable pageable);
	
	List<TransactionHistory> findByFromAccountAndTimeLike(String accountNumber, Date fromDate, Pageable pageable);
}
