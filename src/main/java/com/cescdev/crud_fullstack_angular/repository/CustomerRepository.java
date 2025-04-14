package com.cescdev.crud_fullstack_angular.repository;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("""
        SELECT c FROM Customer c WHERE 
        LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
        LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
        LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Customer> search(@Param("keyword") String keyword, Pageable pageable);
}
