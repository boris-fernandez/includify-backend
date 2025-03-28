package com.includify.domain.candidato.dto;

import com.includify.domain.usuario.Usuario;
import com.includify.domain.usuario.dto.CreateUserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RegistrarCandidatoDTO(
        @Valid
        CreateUserDTO usuario,
        @NotBlank
        String nombre,
        @NotBlank
        String pais,
        @NotBlank
        String apellidos,
        @NotBlank
        String telefono,
        @Size(min = 10, max = 10, message = "Deben haber excatamenta 10 respuetas")
        List< @Min(value = 0, message = "Cada respuesta no debe ser menor a 0")
        @Max(value = 3, message = "Cada respuesta no puede ser mayor a 3") Integer> respuestas,
        @NotBlank
        String categoria
) {
}
