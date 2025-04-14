package com.cescdev.crud_fullstack_angular.service;

import com.cescdev.crud_fullstack_angular.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer save(Customer customer);
    List<Customer> findAll();
    Customer findById (Integer id);
    void deleteById(Integer id);
    Customer update(Customer customer);
    List<Customer> findByName(String name);
}
