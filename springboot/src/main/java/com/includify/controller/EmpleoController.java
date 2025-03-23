package com.includify.controller;

import com.includify.domain.empleo.Empleo;
import com.includify.domain.empleo.EmpleoService;
import com.includify.domain.empleo.dto.CreateVideoDTO;
import com.includify.domain.empleo.dto.ObtenerEmpleosDTO;
import com.includify.domain.usuario.dto.MensajeDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empleo")
public class EmpleoController {

    @Autowired
    private EmpleoService empleoService;

    @GetMapping
    public ResponseEntity<?> obtenerEmpleos(){
        List<ObtenerEmpleosDTO> empleosDTOS = empleoService.obtenerEmpleos();
        return ResponseEntity.ok(empleosDTOS);
    }

    @PostMapping("/create-video")
    @Transactional
    public ResponseEntity<MensajeDTO> createVideo(@RequestBody @Valid CreateVideoDTO videoDTO, UriComponentsBuilder builder){
        Empleo empleo = empleoService.createVideo(videoDTO);
        URI uri = builder.path("/empleo/{id}").buildAndExpand(empleo.getId()).toUri();
        return ResponseEntity.created(uri).body(new MensajeDTO("Video creado exitosamente!"));
    }

    @PutMapping("update-status/{id}")
    public ResponseEntity<?> actualizarStatus(@PathVariable Long id){
        empleoService.updateStatus(id);
        return ResponseEntity.noContent().build();
    }
}
