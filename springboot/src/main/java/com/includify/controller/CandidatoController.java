package com.includify.controller;

import com.includify.domain.MensajeDTO;
import com.includify.domain.candidato.CandidatoService;
import com.includify.domain.candidato.dto.ActualizarCvDTO;
import com.includify.domain.candidato.dto.ObtenerCandidatoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidato")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @GetMapping("{id}")
    public ResponseEntity<ObtenerCandidatoDTO> obtenerCandidato(@PathVariable Long id){
        return ResponseEntity.ok(candidatoService.obtenerCandidato(id));
    }

    @PostMapping("update/cv")
    public ResponseEntity<MensajeDTO> actualizarCv (@RequestBody @Valid ActualizarCvDTO dto){
        return ResponseEntity.ok(candidatoService.ActualizarCv(dto));
    }
}
