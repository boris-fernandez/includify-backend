package com.includify.domain.usuario.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record CreateUserDTO(
        @NotBlank
        String correo,
        @NotBlank
        String contrasena
) {
        @JsonCreator
        public CreateUserDTO {
                // Este constructor es utilizado por Jackson para deserializar
        }
}
