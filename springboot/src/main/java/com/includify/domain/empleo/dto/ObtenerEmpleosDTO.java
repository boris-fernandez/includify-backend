package com.includify.domain.empleo.dto;

import com.includify.domain.empleo.Empleo;

public record ObtenerEmpleosDTO(
        String textoOriginal,
        String video,
        String videoSenas,
        String nombreEmpresa,
        String categoria
) {
    public ObtenerEmpleosDTO(Empleo empleo, String nombreEmpresa){
        this(empleo.getTextoOriginal(), empleo.getVideo(), empleo.getVideoSenas(),
                empleo.getCategoria().getCategoria(), nombreEmpresa);
    }
}
