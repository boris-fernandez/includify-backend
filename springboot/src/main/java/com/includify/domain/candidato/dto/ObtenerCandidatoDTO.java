package com.includify.domain.candidato.dto;

import com.includify.domain.candidato.Candidato;
import com.includify.domain.usuario.Usuario;

public record ObtenerCandidatoDTO(
        String username,
        String apellidos,
        String telefono,
        String cv
) {

    public ObtenerCandidatoDTO(Candidato candidato) {
        this(candidato.getUsername(), candidato.getApellidos(), candidato.getTelefono(), candidato.getCv());
    }
}
