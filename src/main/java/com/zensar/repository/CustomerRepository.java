package com.zensar.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zensar.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	public Customer findByCustomerId(Long customerId);
}
