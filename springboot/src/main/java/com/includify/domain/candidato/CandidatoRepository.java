package com.includify.domain.candidato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
}
