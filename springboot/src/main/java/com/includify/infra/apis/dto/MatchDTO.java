package com.includify.infra.apis.dto;

import java.util.List;

public record MatchDTO(
        List<Integer> recomendacion
) implements JsonValidacion{
}
