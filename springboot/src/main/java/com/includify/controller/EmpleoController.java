package com.includify.controller;

import com.includify.domain.empleo.Empleo;
import com.includify.domain.empleo.EmpleoService;
import com.includify.domain.empleo.dto.CreateVideoDTO;
import com.includify.domain.empleo.dto.ObtenerEmpleosDTO;
import com.includify.domain.usuario.dto.MensajeDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<ObtenerEmpleosDTO>> obtenerEmpleos(@PageableDefault(size = 1) Pageable pageable){
        Page<ObtenerEmpleosDTO> empleosDTOS = empleoService.obtenerEmpleos(pageable);
        return ResponseEntity.ok(empleosDTOS);
    }

    //Falta
    @PostMapping("/create-video")
    @Transactional
    public ResponseEntity<MensajeDTO> createVideo(@RequestBody @Valid CreateVideoDTO videoDTO, UriComponentsBuilder builder){
        Empleo empleo = empleoService.createVideo(videoDTO);
        URI uri = builder.path("/empleo/{id}").buildAndExpand(empleo.getId()).toUri();
        return ResponseEntity.created(uri).body(new MensajeDTO("Video creado exitosamente!"));
    }

    @PutMapping("update-status/{id}")
    @Transactional
    public ResponseEntity<?> actualizarStatus(@PathVariable @NonNull Long id){
        empleoService.updateStatus(id);
        return ResponseEntity.noContent().build();
    }
}
