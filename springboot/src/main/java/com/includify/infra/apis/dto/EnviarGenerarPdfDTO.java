package com.includify.infra.apis.dto;

import com.nimbusds.jose.shaded.gson.Gson;

import java.util.List;

public record EnviarGenerarPdfDTO(
        List<Integer> respuestas,
        String categoria,
        String nombre,
        String apellido,
        String telefono,
        String correo
) {
    public String generarJson(){
        Gson gson = new Gson();

        String jsonBody = gson.toJson(this);

        return jsonBody;
    }
}
