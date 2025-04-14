package com.cescdev.crud_fullstack_angular.service;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import com.cescdev.crud_fullstack_angular.exception.ResourceNotFoundException;
import com.cescdev.crud_fullstack_angular.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer con id " + id + " no se encuentra"));
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findByName(String name) {
        return customerRepository.findByFirstNameContainingIgnoreCase(name);
    }
}
