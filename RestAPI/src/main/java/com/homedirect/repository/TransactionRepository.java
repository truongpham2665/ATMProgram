package com.homedirect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.homedirect.entity.Transaction;

@Repository
public interface TransactionRepository
		extends JpaRepository<Transaction, Integer>, QuerydslPredicateExecutor<Transaction> {
	
}
