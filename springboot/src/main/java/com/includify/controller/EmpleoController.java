package com.includify.controller;

import com.includify.domain.empleo.EmpleoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empleo")
public class EmpleoController {

    @Autowired
    private EmpleoService empleoService;

//    public ResponseEntity<?> obtenerEmpleos(){
//        return empleoService.obtenerEmpleos;
//    }
}
