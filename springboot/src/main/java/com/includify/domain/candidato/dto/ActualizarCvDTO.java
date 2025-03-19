package com.includify.domain.candidato.dto;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public record ActualizarCvDTO(
        @NonNull
        MultipartFile  file,
        @NonNull
        Long id
) {
}
