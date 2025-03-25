package com.includify.controller;

import com.includify.domain.candidato.CandidatoService;
import com.includify.domain.candidato.dto.ActualizarCvDTO;
import com.includify.domain.candidato.dto.MensajeExito;
import com.includify.domain.candidato.dto.ObtenerCandidatoDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/candidato")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @GetMapping("{id}")
    public ResponseEntity<ObtenerCandidatoDTO> obtenerCandidato(@PathVariable Long id){
        return ResponseEntity.ok(candidatoService.obtenerCandidato(id));
    }

    @PutMapping("update/pdf_url")
    @Transactional
    public ResponseEntity<MensajeExito> actualizarCv (@RequestParam("file") @Valid MultipartFile file){
        candidatoService.ActualizarCv(file);
        return ResponseEntity.noContent().build();
    }
}
