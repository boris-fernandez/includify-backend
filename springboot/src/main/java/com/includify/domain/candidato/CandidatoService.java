package com.includify.domain.candidato;

import com.includify.domain.ValidacionException;
import com.includify.domain.candidato.dto.ActualizarCvDTO;
import com.includify.domain.candidato.dto.MensajeExito;
import com.includify.domain.candidato.dto.ObtenerCandidatoDTO;
import com.includify.domain.usuario.Usuario;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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
        if (candidato.isEmpty()){
            throw new ValidacionException("El id del usuario no existe");
        }
        return candidato.map(ObtenerCandidatoDTO::new)
                .orElseThrow(() -> new ValidacionException("El usuario no existe"));
    }

    public void ActualizarCv(MultipartFile file) {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional <Candidato> candidato = candidatoRepository.findByUsuario_Id(usuarioAutenticado.getId());

        if (candidato.isEmpty()){
            throw new ValidacionException("No existe el candidato");
        }

        Cloudinary cloudinary = new Cloudinary(cludinaryUrl);

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "cv",
                            "use_filename", false,
                            "unique_filename", true,
                            "overwrite", true,
                            "resource_type", "raw",
                            "format", "pdf"
                    )
            );

            String cvUrl = (String) uploadResult.get("secure_url");
            candidato.get().actualizarCv(cvUrl);
            candidatoRepository.save(candidato.get());
        } catch (Exception e) {
            throw new ValidacionException("Ocurrio un error inesperado");
        }
    }
}
