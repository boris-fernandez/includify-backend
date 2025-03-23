package com.includify.domain.empleo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record CreateVideoDTO(
        @NotBlank
        String textoOriginal
) {
}
