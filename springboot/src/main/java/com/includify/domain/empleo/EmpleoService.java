package com.includify.domain.empleo;

import com.includify.domain.ValidacionException;
import com.includify.domain.empleo.categoria.Categoria;
import com.includify.domain.empleo.categoria.CategoriaRepository;
import com.includify.domain.empleo.dto.CreateVideoDTO;
import com.includify.domain.empleo.dto.ObtenerEmpleosDTO;
import com.includify.domain.empleo.respuestasEmpleo.RespuestasEmpleoRepository;
import com.includify.domain.empresa.Empresa;
import com.includify.domain.empresa.EmpresaRepository;
import com.includify.domain.usuario.Usuario;
import com.includify.infra.apis.ConsultaApi;
import com.includify.infra.apis.dto.VideoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleoService {

    @Autowired
    private EmpleoRepository empleoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private RespuestasEmpleoRepository respuestasEmpleoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ConsultaApi consultaApi;

    public Empleo createVideo(CreateVideoDTO createVideoDTO) {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        VideoDTO videoDTO = consultaApi.videos(createVideoDTO.textoOriginal());

        Optional<Categoria> categoria = categoriaRepository.findByCategoria(videoDTO.categoria().getCategoria());
        if (categoria.isEmpty()){
            Categoria newCategoria = Categoria.builder()
                    .categoria(videoDTO.categoria().getCategoria())
                    .build();
            categoriaRepository.save(newCategoria);
        }

        Empresa empresa = empresaRepository.findUsuarioById(usuarioAutenticado.getId());

        Empleo empleo = Empleo.builder()
                .textoOriginal(createVideoDTO.textoOriginal())
                .video(videoDTO.video())
                .videoSenas(videoDTO.videoSeñas())
                .empresa(empresa)
                .categoria(videoDTO.categoria())
                .build();

        empleoRepository.save(empleo);

        return empleo;
    }

    public void updateStatus(Long id) {
        Optional<Empleo> empleoOptional = empleoRepository.findById(id);
        if (empleoOptional.isEmpty()){
            throw new ValidacionException("No existe el empleo");
        }
        Empleo empleo = empleoOptional.get();
        empleo.updateStatus();
    }

    public List<ObtenerEmpleosDTO> obtenerEmpleos() {
        List<ObtenerEmpleosDTO> listEmpleos = new ArrayList<>();

        List<Integer> empleosDTOS = consultaApi.match().list();

        for (Integer idEmpleos : empleosDTOS){
            Optional<Empleo> empleoOptional = empleoRepository.findById((long) idEmpleos);
            if (empleoOptional.isEmpty()){
                throw new ValidacionException("No existe el empleo");
            }

            Optional<Empresa> empresaOptional = empleoRepository.findEmpresaById((long) idEmpleos);

            if (empleoOptional.isEmpty()){
                throw new ValidacionException("No existe la empresa");
            }

            ObtenerEmpleosDTO empleosDTO = new ObtenerEmpleosDTO(empleoOptional.get(), empresaOptional.get().getNombre());

            listEmpleos.add(empleosDTO);
        }
        return listEmpleos;
    }
}
