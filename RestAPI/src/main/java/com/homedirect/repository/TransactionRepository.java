package com.homedirect.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.homedirect.entity.TransactionHistory;

@Repository
public interface TransactionRepository
		extends JpaRepository<TransactionHistory, Integer>, QuerydslPredicateExecutor<TransactionHistory> {

	List<TransactionHistory> findByFromAccount(String accountNumber, Pageable pageable);

	List<TransactionHistory> findByFromAccountAndTimeLessThan(String accountNumber, Date toDate, Pageable pageable);
	
	List<TransactionHistory> findByFromAccountAndTypeAndTime(String accountNumber, Byte type, Date DateTime);
	
	List<TransactionHistory> findByFromAccountAndTimeBetween(String accountNumber, Date fromDate, Date toDate, Pageable pageable);

	List<TransactionHistory> findByFromAccountAndType(String accountNumber, Byte type, Pageable pageable);
	
	List<TransactionHistory> findByFromAccountAndTimeGreaterThan(String accountNumber, Date fromDate, Pageable pageable);
}
