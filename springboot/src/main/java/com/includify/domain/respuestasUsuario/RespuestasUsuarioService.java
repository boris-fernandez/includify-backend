package com.includify.domain.respuestasUsuario;

import com.includify.domain.respuestasUsuario.dto.RespuestasUsuarioDTO;
import com.includify.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RespuestasUsuarioService {

    @Autowired
    private RespuestasUsuarioRepository repository;

    public void subirRespuestas(RespuestasUsuarioDTO respuestasUsuarioDTO) {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RespuestasUsuario respuestasUsuario = RespuestasUsuario.builder()
                .idUsuario(usuarioAutenticado)
                .r1(respuestasUsuarioDTO.respuestas().get(0))
                .r2(respuestasUsuarioDTO.respuestas().get(1))
                .r3(respuestasUsuarioDTO.respuestas().get(2))
                .r4(respuestasUsuarioDTO.respuestas().get(3))
                .r5(respuestasUsuarioDTO.respuestas().get(4))
                .r6(respuestasUsuarioDTO.respuestas().get(5))
                .r7(respuestasUsuarioDTO.respuestas().get(6))
                .r8(respuestasUsuarioDTO.respuestas().get(7))
                .r9(respuestasUsuarioDTO.respuestas().get(8))
                .r10(respuestasUsuarioDTO.respuestas().get(9))
                .build();

        repository.save(respuestasUsuario);
    }
}
