package com.includify.controller;

import com.includify.domain.candidato.Candidato;
import com.includify.domain.candidato.dto.RegistrarCandidatoDTO;
import com.includify.domain.empresa.Empresa;
import com.includify.domain.empresa.dto.RegistrarEmpresaDTO;
import com.includify.domain.usuario.dto.LoginDTO;
import com.includify.domain.usuario.dto.MensajeDTO;
import com.includify.domain.usuario.dto.RespuestaJWTDTO;
import com.includify.infra.security.AuthenticacionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    @Autowired
    private AuthenticacionService service;

    @PostMapping("/registrar/candidato")
    @Transactional
    public ResponseEntity<MensajeDTO> registrarCandidatos (@RequestBody @Valid RegistrarCandidatoDTO registro, UriComponentsBuilder builder) {
        Candidato candidato = service.registerCandidato(registro);
        URI uri = builder.path("/candidato/{id}").buildAndExpand(candidato.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/registrar/empresa")
    @Transactional
    public ResponseEntity<MensajeDTO> registrarEmpresa (@RequestBody @Valid RegistrarEmpresaDTO registro, UriComponentsBuilder builder) {
        Empresa empresa = service.registerEmpresa(registro);
        URI uri = builder.path("/empresa/{id}").buildAndExpand(empresa.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaJWTDTO> login (@RequestBody @Valid LoginDTO login) throws Exception {
        return ResponseEntity.ok(service.login(login));
    }
}
