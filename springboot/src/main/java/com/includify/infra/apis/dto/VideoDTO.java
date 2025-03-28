package com.includify.infra.apis.dto;

import com.includify.domain.empleo.categoria.Categoria;
import com.includify.domain.empleo.dto.CategoriaDTO;

import java.util.List;

public record VideoDTO(
        String video,
        String videoSenas,
        List<Integer> calificaciones,
        CategoriaDTO categoria
)implements JsonValidacion {
}
