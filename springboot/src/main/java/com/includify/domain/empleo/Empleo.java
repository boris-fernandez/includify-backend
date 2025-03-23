package com.includify.domain.empleo;

import com.includify.domain.empleo.categoria.Categoria;
import com.includify.domain.empresa.Empresa;
import com.includify.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "EMPLEOS")
@EqualsAndHashCode(of = "id")
public class Empleo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_original")
    private String textoOriginal;

    private String video;

    @Column(name = "video_señas")
    private String videoSenas;

    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empresa")
    private Empresa empresa ;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public void updateStatus(){
        this.status = false;
    }
}
