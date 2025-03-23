package com.includify.infra.apis.dto;

import com.includify.domain.empleo.categoria.Categoria;

import java.util.List;

public record VideoDTO(
        String video,
        String videoSeñas,
        List<Integer> calificaciones,
        Categoria categoria
)implements JsonValidacion {
}
