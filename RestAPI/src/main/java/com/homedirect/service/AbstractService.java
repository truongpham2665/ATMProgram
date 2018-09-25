package com.homedirect.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractService<T> {

	@Autowired
	protected JpaRepository<T, Integer> jpaRepository;

	public T save(T t) {
		return jpaRepository.save(t);
	}

	public List<T> findAll() {
		return jpaRepository.findAll();
	}

	public Optional<T> findById(int id) {
		return jpaRepository.findById(id);
	}
}
