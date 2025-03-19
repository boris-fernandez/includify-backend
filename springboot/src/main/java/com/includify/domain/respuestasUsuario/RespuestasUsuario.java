package com.includify.domain.respuestasUsuario;

import com.includify.domain.empleo.Categoria;
import com.includify.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "RESPUESTAS_CANDIDATO")
@EqualsAndHashCode(of = "id")
@Entity
public class RespuestasUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
}
