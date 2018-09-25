package com.homedirect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.homedirect.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Optional<Account> findById(Integer id);
	
	List<Account> findByUsernameContaining(String username, Pageable pageable);
	
	Account findByUsername(String name);

	Account findByAccountNumber(String accountNumber);	

	//sử dụng query db 
	@Query(value = "SELECT * FROM account p where BINARY p.username = ?1", nativeQuery = true)
	Account find(@Param("username") String username);

	Account findByUsernameAndAccountNumber(String username, String accountNumber);

	List<Account> findAll();

}
