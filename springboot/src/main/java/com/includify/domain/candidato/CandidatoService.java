package com.includify.domain.candidato;

import com.includify.domain.MensajeDTO;
import com.includify.domain.candidato.dto.ActualizarCvDTO;
import com.includify.domain.candidato.dto.ObtenerCandidatoDTO;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import java.util.Map;

import java.util.Optional;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Value("${cludinary.url}")
    private String cludinaryUrl;

    public ObtenerCandidatoDTO obtenerCandidato(@NonNull Long id){
        Optional<Candidato> candidato = candidatoRepository.findById(id);
        return candidato.map(ObtenerCandidatoDTO::new)
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));
    }

    public MensajeDTO ActualizarCv(ActualizarCvDTO dto) {
        Cloudinary cloudinary = new Cloudinary(cludinaryUrl);
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    dto.file().getInputStream(), ObjectUtils.asMap(
                            "resource_type", "raw",
                            "use_filename", true,
                            "unique_filename", false,
                            "overwrite", true
                    ));

            String cvUrl = (String) uploadResult.get("secure_url");

            Candidato candidato = candidatoRepository.findById(dto.id())
                    .orElseThrow(() -> new RuntimeException("Candidato no encontrado"));

            candidato.actualizarCv(cvUrl);
            candidatoRepository.save(candidato);

            return new MensajeDTO("CV actualizado exitoamente!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
