package com.cescdev.crud_fullstack_angular.repository;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Integer> {
    List<Customer> findByFirstNameContainingIgnoreCase(String name);
}
