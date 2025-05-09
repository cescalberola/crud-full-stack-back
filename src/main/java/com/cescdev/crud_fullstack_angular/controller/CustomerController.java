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

@RestController // Marca la clase como controlador REST
@RequestMapping("/api/customers") // Prefijo común para todos los endpoints
@CrossOrigin(origins = "http://localhost:4200") // Permite peticiones desde Angular en el puerto 4200
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService cs) {
        this.customerService = cs;
    }

    // ----------- ENDPOINTS -----------
    /**
     * Crea un nuevo Customer.
     * POST http://localhost:8080/api/customers
     *
     * @param inputDto DTO con los datos de entrada.
     *                 @Valid activa las validaciones definidas en CustomerInputDto.
     * @return ResponseEntity con:
     *         - HTTP 201 Created
     *         - Header Location: URI del recurso creado (/api/customers/{id})
     *         - Body: CustomerOutputDto con los datos del nuevo cliente
     */

    @PostMapping
    public ResponseEntity<CustomerOutputDto> save(
            @RequestBody @Valid CustomerInputDto inputDto
    ) {
        // Mapear DTO de entrada a entidad JPA
        Customer toSave = toEntity(inputDto);
        // Guardar la entidad (INSERT en la base de datos)
        Customer saved = customerService.save(toSave);
        // Construir el header Location con la URI del nuevo recurso
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Base: /api/customers
                .path("/{id}")        // Añade /{id}
                .buildAndExpand(saved.getId()) // Sustituye {id} por el valor generado
                .toUri();

        // Devolver 201 Created + Location + DTO de salida
        return ResponseEntity.created(location)
                .body(toDto(saved));
    }

    /**
     * Obtiene una página de CustomerOutputDto.
     * GET http://localhost:8080/api/customers
     *
     * @param name     (opcional) palabra clave para búsqueda en
     *                 firstName, lastName o email.
     * @param pageable Parámetros de paginación (page, size), con tamaño por defecto 5.
     * @return 200 OK con un Page<CustomerOutputDto> que contiene:
     *         - content: lista de clientes
     *         - totalElements, totalPages, number, first, last...
     */

    @GetMapping
    public ResponseEntity<Page<CustomerOutputDto>> findAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        // Llamamos al servicio; si name está presente, aplicamos search(), si no, findAll()
        Page<Customer> page = (name != null && !name.isBlank())
                ? customerService.search(name, pageable)
                : customerService.findAll(pageable);

        // Convertimos cada Customer a CustomerOutputDto usando map
        Page<CustomerOutputDto> dtoPage = page.map(this::toDto);

        // Devolvemos código 200 OK con la página de DTOs
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Recupera un único cliente por su ID.
     * GET http://localhost:8080/api/customers/{id}
     *
     * @param id Identificador único del cliente en la URI.
     * @return 200 OK + CustomerOutputDto si existe, o 404 Not Found si no.
     */

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOutputDto> findById(@PathVariable Integer id) {
        // customerService.findById lanza ResourceNotFoundException si no existe
        Customer c = customerService.findById(id);
        // Mapear entidad a DTO de salida y devolver 200 OK
        return ResponseEntity.ok(toDto(c));
    }

    /**
     * Actualiza un cliente existente.
     * PUT http://localhost:8080/api/customers/{id}
     *
     * @param id       ID del cliente a actualizar.
     * @param inputDto DTO con los nuevos valores (se validan).
     * @return 200 OK + CustomerOutputDto con los datos actualizados.
     */

    @PutMapping("/{id}")
    public ResponseEntity<CustomerOutputDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerInputDto inputDto
    ) {
        // Obtenemos la entidad existente o lanzamos excepción si no existe
        Customer existing = customerService.findById(id);

        // Actualizamos los campos de la entidad con los datos del DTO
        existing.setFirstName(inputDto.getFirstName());
        existing.setLastName(inputDto.getLastName());
        existing.setEmail(inputDto.getEmail());

        // Guardamos los cambios (UPDATE en la base de datos)
        Customer updated = customerService.update(existing);

        // Guardamos los cambios (UPDATE en la base de datos)
        return ResponseEntity.ok(toDto(updated));
    }

    /**
     * Elimina un cliente por su ID.
     * DELETE http://localhost:8080/api/customers/{id}
     *
     * @param id ID del cliente a eliminar.
     * @return 204 No Content si la eliminación fue exitosa.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        // Ejecuta el DELETE; si no existe, lanza excepción
        customerService.deleteById(id);
        // 204 No Content: la petición fue exitosa pero sin body
        return ResponseEntity.noContent().build();
    }

    // ----------- MÉTODOS DE MAPEADO (ENTIDAD ↔ DTO) -----------

    /**
     * Convierte un CustomerInputDto (datos de entrada) en la entidad Customer.
     * - No incluye el ID porque se genera en el INSERT.
     */

    private Customer toEntity(CustomerInputDto dto) {
        Customer c = new Customer();
        c.setFirstName(dto.getFirstName()); // Mapea firstName
        c.setLastName(dto.getLastName()); // Mapea lastName
        c.setEmail(dto.getEmail()); // Mapea email
        return c;
    }

    /**
     * Convierte una entidad Customer en un CustomerOutputDto (datos de salida).
     * - Incluye ID, firstName, lastName y email.
     */

    private CustomerOutputDto toDto(Customer c) {
        return new CustomerOutputDto(
                c.getId(), // ID generado por la base de datos
                c.getFirstName(), // Nombre
                c.getLastName(), // Apellido
                c.getEmail() // Correo
        );
    }
}
