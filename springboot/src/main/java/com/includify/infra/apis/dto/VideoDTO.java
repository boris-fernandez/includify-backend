package com.includify.infra.apis.dto;

import java.util.List;

public record VideoDTO(
        String video,
        String videoSeñas,
        List<Integer> respuestas,
        String categoria
)implements JsonValidacion {
}
