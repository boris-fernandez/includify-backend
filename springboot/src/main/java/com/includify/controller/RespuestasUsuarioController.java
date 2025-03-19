package com.includify.controller;

import com.includify.domain.respuestasUsuario.RespuestasUsuario;
import com.includify.domain.respuestasUsuario.RespuestasUsuarioService;
import com.includify.domain.respuestasUsuario.dto.RespuestasUsuarioDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/respuestas-usuario")
public class RespuestasUsuarioController {

    @Autowired
    private RespuestasUsuarioService service;

    @PostMapping()
    public ResponseEntity<?> subirRespuestas(@RequestBody @Valid RespuestasUsuarioDTO respuestasUsuarioDTO){
        service.subirRespuestas(respuestasUsuarioDTO);
        return ResponseEntity.noContent().build();
    }
}
