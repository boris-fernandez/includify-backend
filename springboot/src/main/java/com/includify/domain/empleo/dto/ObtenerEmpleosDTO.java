package com.includify.domain.empleo.dto;

import com.includify.domain.empleo.Empleo;

public record ObtenerEmpleosDTO(
        String nombreEmpresa,
        String textoOriginal,
        String video,
        String videoSenas,
        String categoria
) {
    public ObtenerEmpleosDTO(Empleo empleo){
        this(empleo.getEmpresa().getNombre(),empleo.getTextoOriginal(), empleo.getVideo(), empleo.getVideoSenas(),
                empleo.getCategoria().getCategoria());
    }
}
