package com.includify.domain.candidato.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public record ActualizarCvDTO(
        @NotBlank
        MultipartFile  file
) {
}
