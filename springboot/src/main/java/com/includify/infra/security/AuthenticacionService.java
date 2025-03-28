package com.includify.infra.security;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.includify.domain.ValidacionException;
import com.includify.domain.candidato.Candidato;
import com.includify.domain.candidato.CandidatoRepository;
import com.includify.domain.candidato.dto.RegistrarCandidatoDTO;
import com.includify.domain.candidato.respuestasCandidatos.RespuestasCandidato;
import com.includify.domain.candidato.respuestasCandidatos.RespuestasUsuarioRepository;
import com.includify.domain.empleo.categoria.Categoria;
import com.includify.domain.empleo.categoria.CategoriaRepository;
import com.includify.domain.empresa.Empresa;
import com.includify.domain.empresa.EmpresaRepository;
import com.includify.domain.empresa.dto.RegistrarEmpresaDTO;
import com.includify.domain.usuario.Usuario;
import com.includify.domain.usuario.UsuarioRepository;
import com.includify.domain.usuario.dto.LoginDTO;
import com.includify.domain.usuario.dto.MensajeDTO;
import com.includify.domain.usuario.dto.RespuestaJWTDTO;
import com.includify.infra.apis.ConsultaApi;
import com.includify.infra.apis.dto.CvDTO;
import com.includify.infra.apis.dto.EnviarCandidatoDTO;
import com.nimbusds.jose.JOSEException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
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
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RespuestasUsuarioRepository respuestasUsuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ConsultaApi consultaApi;

    @Value("${cludinary.url}")
    private String cludinaryUrl;

    public RespuestaJWTDTO login(LoginDTO login) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, JOSEException {
        Optional<Usuario> user = usuarioRepository.findByCorreo(login.correo());
        if (user.isEmpty()) {
            throw new ValidacionException("Usuario no encontrado");
        }

        // Verificar si la contraseña ingresada coincide con la contrase de la base de datos
        if (verificarContrasena(login.contrasena(), user.get().getContrasena())){
            RespuestaJWTDTO respuestaJWTDTO = new RespuestaJWTDTO(tokenService.generarToken(user.get().getId()));
            return respuestaJWTDTO;
        }
        throw new ValidacionException("Authenticacion fallida");
    }

    public Candidato registerCandidato (RegistrarCandidatoDTO candidatoDTO) {
        Optional<Usuario> verificar = usuarioRepository.findByCorreo(candidatoDTO.usuario().correo());
        if (verificar.isPresent()){
            throw new ValidacionException("El correo ya existe prueba con otro");
        }

        // Guardar usuario
        Usuario usuario = Usuario.builder()
                .correo(candidatoDTO.usuario().correo())
                .contrasena(candidatoDTO.usuario().contrasena())
                .build();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setContrasena(encoder.encode(usuario.getContrasena()));

        // Guardar candidato
        EnviarCandidatoDTO enviarCandidato = new EnviarCandidatoDTO(
                candidatoDTO.respuestas(),candidatoDTO.categoria(),candidatoDTO.nombre(), candidatoDTO.apellidos(), candidatoDTO.telefono(),
                candidatoDTO.usuario().correo());
        CvDTO cvDTO = consultaApi.cv(enviarCandidato);
        Candidato candidato = Candidato.builder()
                .username(candidatoDTO.nombre())
                .pais(candidatoDTO.pais())
                .apellidos(candidatoDTO.apellidos())
                .telefono(candidatoDTO.telefono())
                .cv(cvDTO.pdf_url())
                .usuario(usuario)
                .build();

        // Guardar respuetas candidato
        Optional<Categoria> optionalCategoria = categoriaRepository.findByCategoria(candidatoDTO.categoria());
        if (optionalCategoria.isEmpty()){
            throw new ValidacionException("La categoria no existe");
        }
        RespuestasCandidato respuestasUsuario = RespuestasCandidato.builder()
                .idCandidato(candidato)
                .r1(candidatoDTO.respuestas().get(0))
                .r2(candidatoDTO.respuestas().get(1))
                .r3(candidatoDTO.respuestas().get(2))
                .r4(candidatoDTO.respuestas().get(3))
                .r5(candidatoDTO.respuestas().get(4))
                .r6(candidatoDTO.respuestas().get(5))
                .r7(candidatoDTO.respuestas().get(6))
                .r8(candidatoDTO.respuestas().get(7))
                .r9(candidatoDTO.respuestas().get(8))
                .r10(candidatoDTO.respuestas().get(9))
                .categoria(optionalCategoria.get())
                .build();

        // Guardara todo en la base de datos
        respuestasUsuarioRepository.save(respuestasUsuario);
        usuarioRepository.save(usuario);
        candidatoRepository.save(candidato);
        return candidato;
    }

    public Empresa registerEmpresa (RegistrarEmpresaDTO empresaDTO) {
        Optional<Usuario> verificar = usuarioRepository.findByCorreo(empresaDTO.usuario().correo());
        if (verificar.isPresent()){
            throw new ValidacionException("El correo ya existe prueba con otro");
        }

        // Guardar usuario
        Usuario usuario = Usuario.builder()
                .correo(empresaDTO.usuario().correo())
                .contrasena(empresaDTO.usuario().contrasena())
                .build();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setContrasena(encoder.encode(usuario.getContrasena()));

        // Guardar empresa
        Empresa empresa = Empresa.builder()
                .nombre(empresaDTO.nombre())
                .telefono(empresaDTO.telefono())
                .pais(empresaDTO.pais())
                .usuario(usuario)
                .build();

        usuarioRepository.save(usuario);
        empresaRepository.save(empresa);
        return empresa;
    }

    private boolean verificarContrasena(String enteredContrasena, String storedContrasena){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredContrasena, storedContrasena);
    }
}
