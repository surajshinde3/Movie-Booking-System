package com.zensar.service;

import com.zensar.model.Customer;

public interface CustomerService {

	public abstract Iterable<Customer> getAllCustomers();

	public abstract Customer getCustomerById(Long customerId);

	public abstract Customer saveCustomer(Customer customer);

	public abstract void deleteCustomerById(Long customerId);
}
