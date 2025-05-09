package com.cescdev.crud_fullstack_angular.service;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import com.cescdev.crud_fullstack_angular.exception.ResourceNotFoundException;
import com.cescdev.crud_fullstack_angular.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Importamos la Specification estática que construimos
import static com.cescdev.crud_fullstack_angular.service.CustomerSpecifications.containsKeywordInFields;

@Service // Indica que esta clase es un servicio de Spring; se registra como bean
public class CustomerServiceImpl implements CustomerService {

    // Repositorio JPA para Customer, inyectado en el constructor
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Guarda un nuevo Customer en la base de datos.
     * customerRepository.save(...):
     *   - Si el objeto no tiene ID, hace INSERT.
     *   - Si tiene ID existente, hace UPDATE.
     */

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Recupera todos los Customer con paginación.
     * findAll(pageable):
     *   - Ejecuta SELECT * FROM customers
     *   - Aplica OFFSET y LIMIT según pageable
     * Devuelve un Page<Customer> con contenido y metadata (totalElements, totalPages…).
     */

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    /**
     * Busca un Customer por su ID.
     * findById(id):
     *   - Devuelve Optional<Customer>.
     *   - Si no existe, lanzamos ResourceNotFoundException.
     */

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer con id " + id + " no se encuentra"));
    }

    /**
     * Borra un Customer por su ID.
     * deleteById(id):
     *   - Ejecuta DELETE FROM customers WHERE id = ?
     *   - Si no existe, JPA lanza EmptyResultDataAccessException.
     */

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }

    /**
     * Actualiza un Customer existente.
     * save(customer) en JPA hace UPSERT:
     *   - Si existe ID, hace UPDATE de todos los campos.
     */

    @Override
    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Búsqueda paginada dinámica usando Specifications:
     * findAll(spec, pageable):
     *   - Specification construye el WHERE según keyword.
     *   - Pageable define página y tamaño.
     *   - Devuelve Page<Customer> filtrado.
     */

    @Override
    public Page<Customer> search(String keyword, Pageable pageable) {
        return customerRepository.findAll(
                containsKeywordInFields(keyword),
                pageable
        );
    }
}
