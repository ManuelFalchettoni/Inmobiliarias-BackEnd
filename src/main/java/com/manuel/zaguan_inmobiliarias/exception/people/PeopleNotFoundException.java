package com.manuel.zaguan_inmobiliarias.exception.people;

public class PeopleNotFoundException extends RuntimeException {
    public PeopleNotFoundException(Long id) {
        super("People with id: " + id + " not found.");
    }
}
