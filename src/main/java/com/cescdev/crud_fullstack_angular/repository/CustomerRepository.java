package com.cescdev.crud_fullstack_angular.repository;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad {@link Customer}.
 *
 * Extiende de:
 *   - {@link JpaRepository<Customer, Integer>} para operaciones CRUD básicas:
 *       • save(), findById(), findAll(), deleteById(), etc.
 *   - {@link JpaSpecificationExecutor<Customer>} para búsquedas dinámicas
 *     basadas en {@link org.springframework.data.jpa.domain.Specification}
 *     (paginadas o no).
 *
 * Al estar anotado con {@link Repository}, Spring lo detecta y registra
 * como un bean de acceso a datos.
 *
 * Para añadir consultas personalizadas, basta con declarar nuevos métodos
 * siguiendo la convención de nombres (query methods) o usar {@code @Query}.
 */

@Repository
public interface CustomerRepository
        extends JpaRepository<Customer, Integer>,
        JpaSpecificationExecutor<Customer> {

    // Ejemplo de método derivado (no es necesario para Specifications):
    // Page<Customer> findByFirstNameContainingIgnoreCase(String name, Pageable pageable);

    // Si en el futuro necesitas una consulta JPQL o nativa, podrías hacer:
    //
    // @Query("SELECT c FROM Customer c WHERE c.email = :email")
    // Optional<Customer> findByEmail(@Param("email") String email);
}
