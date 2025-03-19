package com.includify.infra.security;

import com.includify.domain.candidato.Candidato;
import com.includify.domain.candidato.CandidatoRepository;
import com.includify.domain.candidato.dto.RegistrarCandidatoDTO;
import com.includify.domain.empresa.Empresa;
import com.includify.domain.empresa.EmpresaRepository;
import com.includify.domain.empresa.dto.RegistrarEmpresaDTO;
import com.includify.domain.usuario.Usuario;
import com.includify.domain.usuario.UsuarioRepository;
import com.includify.domain.usuario.dto.LoginDTO;
import com.includify.domain.usuario.dto.MensajeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthenticacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TokenService tokenService;

    public HashMap<String, String> login(LoginDTO login) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<Usuario> user = usuarioRepository.findByCorreo(login.correo());

            if (user.isEmpty()){
                jwt.put("error", "User no registrado!");
                return jwt;
            }

            if (verificarContrasena(login.contrasena(), user.get().getContrasena())){
                jwt.put("jwt", tokenService.generarToken(user.get().getId()));
            }else {
                jwt.put("error", "Authetication failed");
            }
            return jwt;
        }catch (Exception e){
            throw new Exception(e.toString());
        }

    }

    public Candidato registerCandidato (RegistrarCandidatoDTO candidatoDTO) {
        Optional<Usuario> verificar = usuarioRepository.findByCorreo(candidatoDTO.correo());
        if (verificar.isPresent()){
            new MensajeDTO("El correo ya existe prueba con otro");
        }

        Usuario usuario = Usuario.builder()
                .correo(candidatoDTO.correo())
                .contrasena(candidatoDTO.contrasena())
                .build();

        Candidato candidato = Candidato.builder()
                .username(candidatoDTO.username())
                .apellidos(candidatoDTO.apellidos())
                .telefono(candidatoDTO.telefono())
                .cv(candidatoDTO.cv())
                .build();


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setContrasena(encoder.encode(usuario.getContrasena()));

        usuarioRepository.save(usuario);
        candidatoRepository.save(candidato);

        return candidato;
    }

    public Empresa registerEmpresa (RegistrarEmpresaDTO empresaDTO) {
        Optional<Usuario> verificar = usuarioRepository.findByCorreo(empresaDTO.correo());
        if (verificar.isPresent()){
            new MensajeDTO("El correo ya existe prueba con otro");
        }
        Usuario usuario = Usuario.builder()
                .correo(empresaDTO.correo())
                .contrasena(empresaDTO.contrasena())
                .build();

        Empresa empresa = Empresa.builder()
                .nombre(empresaDTO.nombre())
                .telefono(empresaDTO.telefono())
                .ciudad(empresaDTO.ciudad())
                .direccion(empresaDTO.direccion())
                .pais(empresaDTO.pais())
                .build();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setContrasena(encoder.encode(usuario.getContrasena()));

        usuarioRepository.save(usuario);
        empresaRepository.save(empresa);

        return empresa;
    }

    private boolean verificarContrasena(String enteredContrasena, String storedContrasena){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredContrasena, storedContrasena);
    }
}
