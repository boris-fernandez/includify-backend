package com.includify.infra.errors;

import org.springframework.validation.FieldError;

public record DatosErrorValidacion(String campo, String error) {

    public DatosErrorValidacion(FieldError fieldError){
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
