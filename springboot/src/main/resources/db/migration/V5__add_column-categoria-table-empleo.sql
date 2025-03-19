alter table EMPLEOS
add categoria bigint,
ADD CONSTRAINT fk_categoria foreign key (categoria) references CATEGORIAS(id)