package com.includify.domain.empleo;

import com.includify.domain.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleoRepository extends JpaRepository<Empleo, Long> {
    Optional<Empresa> findEmpresaById(Long id);
}
