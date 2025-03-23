package com.includify.domain.candidato;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    Optional<Candidato>  findByUsuario_Id(long usuarioId);
}
