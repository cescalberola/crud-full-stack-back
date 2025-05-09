package com.cescdev.crud_fullstack_angular.service;

import com.cescdev.crud_fullstack_angular.entity.Customer;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

public class CustomerSpecifications {

    /**
     * Devuelve una Specification que filtra Customer por firstName, lastName o email
     * conteniendo la palabra clave (case-insensitive).
     */

    public static Specification<Customer> containsKeywordInFields(String keyword) {
        return (root, query, cb) -> {
            // 1) Sin palabra clave => no filtro
            if (keyword == null || keyword.isBlank()) {
                // Sin filtro: devuelve siempre true
                return cb.conjunction(); // WHERE 1 = 1
            }

            // 2) Preparo el patrón para LIKE en minúsculas
            String likePattern = "%" + keyword.toLowerCase() + "%";

            /**
            root
            Representa la entidad Customer en la consulta. Con root.get("firstName") accedes al campo firstName.

            cb (CriteriaBuilder)
            Es la fábrica de expresiones: te permite crear Predicate, like, lower, or, etc.

            cb.conjunction()
            Devuelve un Predicate que siempre es verdadero (equivale a no aplicar ningún filtro).

            cb.lower(...)
            Para forzar minúsculas en comparación, logrando ILIKE.

            cb.like(expr, pattern)
            Crea la condición SQL expr LIKE pattern.

            cb.or(...)
            Combina varios Predicate en (...) OR (...) OR (...).
            */

            // 3) Creo los Predicates para cada campo
            Predicate firstNameMatch =
                    cb.like(cb.lower(root.get("firstName")), likePattern);
            Predicate lastNameMatch  =
                    cb.like(cb.lower(root.get("lastName"  )), likePattern);
            Predicate emailMatch     =
                    cb.like(cb.lower(root.get("email"     )), likePattern);

            // Combina los tres con OR
            return cb.or(firstNameMatch, lastNameMatch, emailMatch);
        };
    }
}
