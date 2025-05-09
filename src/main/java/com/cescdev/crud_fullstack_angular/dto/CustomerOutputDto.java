package com.cescdev.crud_fullstack_angular.dto;

public class CustomerOutputDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    public CustomerOutputDto(Integer id, String fn, String ln, String em) {
        this.id = id;
        this.firstName = fn;
        this.lastName = ln;
        this.email = em;
    }

    // Getters (no setters necesarios si sólo lo usas para salida)
    public Integer getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
}
