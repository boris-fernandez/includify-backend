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
import com.includify.infra.apis.dto.EnviarAnuncioDTO;
import com.includify.infra.apis.dto.VideoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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

    private ConcurrentHashMap<Long, List<ObtenerEmpleosDTO>> cacheEmpleos = new ConcurrentHashMap<>();

    public Empleo createVideo(CreateVideoDTO createVideoDTO) {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        VideoDTO videoDTO = consultaApi.videos(new EnviarAnuncioDTO(createVideoDTO.textoOriginal()));
        Optional<Categoria> categoria = categoriaRepository.findByCategoria(videoDTO.categoria().categoria());
        if (categoria.isEmpty()){
            Categoria newCategoria = Categoria.builder()
                    .categoria(videoDTO.categoria().categoria())
                    .build();
            categoriaRepository.save(newCategoria);
        }

        Empresa empresa = empresaRepository.findByUsuario_Id(usuarioAutenticado.getId());

        Empleo empleo = Empleo.builder()
                .textoOriginal(createVideoDTO.textoOriginal())
                .video(videoDTO.video())
                .videoSenas(videoDTO.videoSenas())
                .empresa(empresa)
                .categoria(new Categoria().builder().categoria(videoDTO.categoria().categoria()).build())
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
        empleoRepository.save(empleo);
    }

    public Page<ObtenerEmpleosDTO> obtenerEmpleos(Pageable pageable) {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ObtenerEmpleosDTO> listEmpleos = cacheEmpleos.get(usuarioAutenticado.getId());

        if (listEmpleos == null) {
            List<Integer> empleosDTOS = consultaApi.match(usuarioAutenticado.getId().intValue()).recomendacion();
            listEmpleos = new ArrayList<>();

            for (Integer empleoId : empleosDTOS) {
                Optional<Empleo> empleoOptional = empleoRepository.buscarEmpleos(empleoId.longValue());

                List<ObtenerEmpleosDTO> finalListEmpleos = listEmpleos;
                empleoOptional.ifPresent(empleo -> finalListEmpleos.add(new ObtenerEmpleosDTO(empleo)));
            }

            cacheEmpleos.put(usuarioAutenticado.getId(), listEmpleos);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), listEmpleos.size());

        List<ObtenerEmpleosDTO> paginatedList = listEmpleos.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, listEmpleos.size());
    }

}
