package com.includify.domain.respuestasUsuario.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.util.List;

public record RespuestasUsuarioDTO(
        @NotNull(message = "Las respuestas no pueden ser nulas")
        @Size(min = 10, max = 10, message = "Deben haber excatamenta 10 respuetas")
        List< @Min(value = 1, message = "Cada respuesta no debe ser menor a 1")
        @Max(value = 4, message = "Cada respuesta no puede ser mayor a 4") Integer> respuestas
) {
    public RespuestasUsuarioDTO {
        if (respuestas.size() != 10) {
            throw new IllegalArgumentException("Debe haber exactamente 10 respuestas");
        }
    }
}
