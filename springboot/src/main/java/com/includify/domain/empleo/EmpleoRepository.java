package com.includify.domain.empleo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface EmpleoRepository extends JpaRepository<Empleo, Long> {

    @Query("""
       select e from Empleo e
       where e.id = :id and e.status = true
       """)
    Optional<Empleo> buscarEmpleos(Long id);
}
