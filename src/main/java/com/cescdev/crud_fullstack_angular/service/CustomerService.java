package com.cescdev.crud_fullstack_angular.service;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Customer save(Customer customer);
    Page<Customer> findAll(Pageable pageable);
    Customer findById (Integer id);
    void deleteById(Integer id);
    Customer update(Customer customer);
    Page<Customer> search(String keyword, Pageable pageable);
}
