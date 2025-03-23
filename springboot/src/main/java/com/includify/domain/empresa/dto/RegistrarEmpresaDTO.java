package com.includify.domain.empresa.dto;

import com.includify.domain.usuario.dto.CreateUserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record RegistrarEmpresaDTO(
        @NotBlank
        String nombre,
        @Valid
        CreateUserDTO usuario,
        @NotBlank
        String telefono,
        @NotBlank
        String pais
) {
}
