package com.includify.domain.empleo.respuestasEmpleo;

import com.includify.domain.empleo.Empleo;
import com.includify.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "RESPUESTAS_EMPLEO")
@EqualsAndHashCode(of = "id")
@Entity
public class RespuestasEmpleo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleo")
    private Empleo empleo;

    private int r1;

    private int r2;

    private int r3;

    private int r4;

    private int r5;

    private int r6;

    private int r7;

    private int r8;

    private int r9;

    private int r10;
}
