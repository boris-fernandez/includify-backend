create table empleo_categoria(
    id_empleo bigint,
    id_categoria bigint,
    foreign key (id_empleo) references EMPLEOS(id),
    foreign key (id_categoria) references CATEGORIAS(id),
    primary key(id_empleo, id_categoria)
)