package com.includify.domain.empresa.dto;

public record RegistrarEmpresaDTO(
        String nombre,
        String correo,
        String contrasena,
        String telefono,
        String ciudad,
        String direccion,
        String pais
) {
}
