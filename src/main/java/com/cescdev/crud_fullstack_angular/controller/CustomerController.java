package com.cescdev.crud_fullstack_angular.controller;

import com.cescdev.crud_fullstack_angular.dto.CustomerInputDto;
import com.cescdev.crud_fullstack_angular.dto.CustomerOutputDto;
import com.cescdev.crud_fullstack_angular.entity.Customer;
import com.cescdev.crud_fullstack_angular.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService cs) {
        this.customerService = cs;
    }

    // ----------- ENDPOINTS -----------

    @PostMapping
    public ResponseEntity<CustomerOutputDto> save(
            @RequestBody @Valid CustomerInputDto inputDto
    ) {
        // 1) convierto DTO a entidad
        Customer toSave = toEntity(inputDto);
        // 2) guardo
        Customer saved = customerService.save(toSave);
        // 3) construyo Location header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        // 4) devuelvo DTO de salida con 201 Created
        return ResponseEntity.created(location)
                .body(toDto(saved));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerOutputDto>> findAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        Page<Customer> page = (name != null && !name.isBlank())
                ? customerService.search(name, pageable)
                : customerService.findAll(pageable);
        // mapeo cada Customer a CustomerOutputDto
        Page<CustomerOutputDto> dtoPage = page.map(this::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOutputDto> findById(@PathVariable Integer id) {
        Customer c = customerService.findById(id);
        return ResponseEntity.ok(toDto(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerOutputDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerInputDto inputDto
    ) {
        Customer existing = customerService.findById(id);
        // actualizo campos
        existing.setFirstName(inputDto.getFirstName());
        existing.setLastName(inputDto.getLastName());
        existing.setEmail(inputDto.getEmail());
        // guardo cambios
        Customer updated = customerService.update(existing);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ----------- MÉTODOS DE MAPEADO (ENTIDAD ↔ DTO) -----------

    /**
     * Convierte un CustomerInputDto (datos de entrada) en la entidad Customer
     */
    private Customer toEntity(CustomerInputDto dto) {
        Customer c = new Customer();
        c.setFirstName(dto.getFirstName());
        c.setLastName(dto.getLastName());
        c.setEmail(dto.getEmail());
        return c;
    }

    /**
     * Convierte una entidad Customer en un CustomerOutputDto (datos de salida)
     */
    private CustomerOutputDto toDto(Customer c) {
        return new CustomerOutputDto(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail()
        );
    }
}
