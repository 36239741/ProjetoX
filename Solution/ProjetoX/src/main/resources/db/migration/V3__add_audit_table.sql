
    create table contrato_aud (
       id bigserial not null,
        rev bigserial not null,
        revtype int2,
        ativo boolean,
        biometria bytea,
        desconto float8,
        nome_paciente varchar(90),
        numero varchar(255),
        valor_total float8,
        primary key (id, rev)
    );

    create table dia_consulta_aud (
       id bigserial not null,
        rev bigserial not null,
        revtype int2,
        dias_semana int4,
        primary key (id, rev)
    );


    create table plano_contratado_aud (
       id bigserial not null,
        rev bigserial not null,
        revtype int2,
        ativo boolean,
        horario_entrada time,
        horario_saida time,
        sessao int4,
        tipo_contrato int4,
        valor_plano float8,
        valor_total float8,
        contrato_id int8,
        servico_id int8,
        primary key (id, rev)
    );

    create table plano_contratado_dia_consulta_aud (
       rev bigserial not null,
        plano_contratado_id int8 not null,
        dia_consulta_id int8 not null,
        revtype int2,
        primary key (rev, plano_contratado_id, dia_consulta_id)
    );


    create table registro_aud (
       id bigserial not null,
        rev int8 not null,
        revtype int2,
        data_hora_entrada timestamp,
        data_hora_saida timestamp,
        situacao int4,
        tempo_total time,
        valor_total float8,
        contrato_id int8,
        plano_contratado_id int8,
        primary key (id, rev)
    );

    create table revision_class (
       id bigserial not null,
        date timestamp,
        user_name varchar(255),
        primary key (id)
    );



    create table servico_aud (
       id bigserial not null,
        rev int8 not null,
        revtype int2,
        servico varchar(255),
        valor float8,
        primary key (id, rev)
    );


    alter table if exists contrato_aud 
       add constraint FKk1ebigi041tqbu910gjcqj7yb 
       foreign key (rev) 
       references revision_class;

    alter table if exists dia_consulta_aud 
       add constraint FKb0mt3rm0aj8nnykdlvb61lf89 
       foreign key (rev) 
       references revision_class;


    alter table if exists plano_contratado_aud 
       add constraint FK2sdvdeg6hb5spsm2dsh0y36ma 
       foreign key (rev) 
       references revision_class;

    alter table if exists plano_contratado_dia_consulta_aud 
       add constraint FKmxeg9r2h5oxtwgkvq0m9wauii 
       foreign key (rev) 
       references revision_class;



    alter table if exists registro_aud 
       add constraint FKm54q0dcu8st6bvrmaapgv5yin 
       foreign key (rev) 
       references revision_class;

    alter table if exists servico_aud 
       add constraint FKlgn94252r616cp01ook0ft5ct
       foreign key (rev) 
       references revision_class;

   
