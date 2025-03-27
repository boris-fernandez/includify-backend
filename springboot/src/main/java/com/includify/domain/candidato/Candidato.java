package com.includify.domain.candidato;

import com.includify.domain.candidato.dto.ActualizarCvDTO;
import com.includify.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "CANDIDATOS")
@EqualsAndHashCode(of = "id")
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String apellidos;

    private String telefono;

    private String pais;

    private String cv;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public void actualizarCv(String cv){
        this.cv = cv;
    }
}
