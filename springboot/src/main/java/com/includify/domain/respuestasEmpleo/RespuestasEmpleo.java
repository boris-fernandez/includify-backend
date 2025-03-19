package com.includify.domain.respuestasEmpleo;

import com.includify.domain.empleo.Empleo;
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
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleo")
    private Empleo id;

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
