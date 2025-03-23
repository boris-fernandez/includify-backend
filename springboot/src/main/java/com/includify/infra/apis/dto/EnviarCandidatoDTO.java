package com.includify.infra.apis.dto;

import java.util.List;

public record EnviarCandidatoDTO(
        List<Integer> respuestas,
        String categoria,
        String nombre,
        String apellido,
        String telefono,
        String correo
) {
}
