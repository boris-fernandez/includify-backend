package com.includify.domain.empresa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findByUsuario_Id(Long usuarioId);

}
