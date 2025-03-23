package com.includify.domain.usuario;

import com.includify.domain.empleo.Empleo;
import com.includify.domain.empresa.Empresa;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "USUARIOS")
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String correo;

    private String contrasena;
}
