package com.includify.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record LoginDTO(
        @NotBlank
        String correo,
        @NotBlank
        String contrasena) {
}
