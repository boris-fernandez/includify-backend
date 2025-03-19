package com.includify.domain.empleo;

import com.includify.domain.empleo.dto.ObtenerEmpleosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleoService {

    @Autowired
    private EmpleoRepository empleoRepository;

//    public ObtenerEmpleosDTO obtenerEmpleos(){
//        List<Empleo> empleos = empleoRepository.
//    };
}
