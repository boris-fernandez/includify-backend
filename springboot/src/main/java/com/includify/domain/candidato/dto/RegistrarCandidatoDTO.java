package com.includify.domain.candidato.dto;

import lombok.NonNull;

public record RegistrarCandidatoDTO(
        String username,
        String correo,
        String contrasena,
        String apellidos,
        String telefono,
        @NonNull
        String cv
) {
}
