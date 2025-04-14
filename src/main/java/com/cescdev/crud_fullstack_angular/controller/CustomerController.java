package com.cescdev.crud_fullstack_angular.controller;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import com.cescdev.crud_fullstack_angular.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//http://localhost:8080/api/customers
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //http://localhost:8080/api/customers
    @PostMapping
    public Customer save(@RequestBody Customer customer){
        return customerService.save(customer);
    }

    //http://localhost:8080
    @GetMapping
    public List<Customer> findAll(@RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return customerService.findByName(name); // 🔍
        }
        return customerService.findAll();
    }

    //http://localhost:8080/api/customers/:id
    @GetMapping("/{id}")
    public Customer findById(@PathVariable Integer id){
        return customerService.findById(id);
    }

    //http://localhost:8080/api/customers/:id
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id){
        customerService.deleteById(id);
    }

    //http://localhost:8080/api/customers
    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer){
        Customer customerDb = customerService.findById(customer.getId());
        customerDb.setFirstName(customer.getFirstName());
        customerDb.setLastName(customer.getLastName());
        customerDb.setEmail(customer.getEmail());
        return customerService.update(customerDb);
    }
}
