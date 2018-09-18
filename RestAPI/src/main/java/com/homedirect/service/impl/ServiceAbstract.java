package com.homedirect.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;

public abstract class ServiceAbstract<T> {


	protected @Autowired JpaRepository<T, Integer> jpaRepository;
	
	private EntityManager entityManager;
	
	protected JPAQuery<T> from(EntityPath<?>...entityPaths ) {
		return new JPAQuery<T>(entityManager).from(entityPaths);
	}

	protected T save(T t) {
		return jpaRepository.save(t);
	}
	
	public List<T> findAll() {
		return jpaRepository.findAll();
	}
	
	public Optional<T> findById(int id) {
		return jpaRepository.findById(id);
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
