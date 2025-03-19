package com.includify.controller;

import com.includify.domain.candidato.Candidato;
import com.includify.domain.candidato.dto.RegistrarCandidatoDTO;
import com.includify.domain.empresa.Empresa;
import com.includify.domain.empresa.dto.RegistrarEmpresaDTO;
import com.includify.domain.usuario.Usuario;
import com.includify.domain.usuario.dto.LoginDTO;
import com.includify.domain.usuario.dto.MensajeDTO;
import com.includify.infra.security.AuthenticacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    @Autowired
    private AuthenticacionService service;

    @PostMapping("/registrar/candidato")
    private ResponseEntity<MensajeDTO> registrarCandidatos (@RequestBody RegistrarCandidatoDTO registro, UriComponentsBuilder builder) {
        Candidato candidato = service.registerCandidato(registro);
        URI uri = builder.path("/candidato/{id}").buildAndExpand(candidato.getId()).toUri();
        return ResponseEntity.created(uri).body(new MensajeDTO("Se creo exitosamente el candidato"));
    }

    @PostMapping("/registrar/empresa")
    private ResponseEntity<MensajeDTO> registrarEmpresa (@RequestBody RegistrarEmpresaDTO registro, UriComponentsBuilder builder) {
        Empresa empresa = service.registerEmpresa(registro);
        URI uri = builder.path("/empresa/{id}").buildAndExpand(empresa.getId()).toUri();
        return ResponseEntity.created(uri).body(new MensajeDTO("Se creo exitosamente la empresa"));
    }

    @PostMapping("/login")
    private ResponseEntity<HashMap<String, String>> login (@RequestBody LoginDTO login) throws Exception {
        HashMap<String, String> iniciarSesion = service.login(login);
        if(iniciarSesion.containsKey("jwt")){
            return ResponseEntity.ok(iniciarSesion);
        }
        return ResponseEntity.ofNullable(iniciarSesion);
    }
}
