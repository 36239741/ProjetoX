 create table configuracao_parametro (
       id  bigserial not null,
        created timestamp not null,
        updated timestamp,
        tempo_sessao time,
        tempo_tolerancia_atraso time,
        valor_minuto_adicional float8,
        usuario_id int8,
        primary key (id)
    );

    create table contrato (
       id  bigserial not null,
        created timestamp not null,
        updated timestamp,
        ativo boolean not null,
        biometria bytea,
        desconto float8,
        nome_paciente varchar(90),
        numero varchar(255) not null,
        valor_total float8 not null,
        usuario_id int8,
        primary key (id)
    );

    create table contrato_registro (
       contrato_id int8 not null,
        registro_id int8 not null
    );

    create table dia_consulta (
       id  bigserial not null,
        created timestamp not null,
        updated timestamp,
        dias_semana int4,
        usuario_id int8,
        primary key (id)
    );

    create table plano_contratado (
       id  bigserial not null,
        created timestamp not null,
        updated timestamp,
        ativo boolean not null,
        horario_entrada time not null,
        horario_saida time not null,
        sessao int4 not null,
        tipo_contrato int4,
        valor_atendimento float8 not null,
        valor_sessao float8 not null,
        valor_total float8 not null,
        usuario_id int8,
        contrato_id int8,
        servico_id int8 not null,
        primary key (id)
    );

    create table plano_contratado_dia_consulta (
       plano_contratado_id int8 not null,
        dia_consulta_id int8 not null
    );

    create table registro (
       id  bigserial not null,
        created timestamp not null,
        updated timestamp,
        data_hora_entrada timestamp,
        data_hora_saida timestamp,
        situacao int4,
        tempo_total time,
        valor_total float8,
        usuario_id int8,
        plano_contratado_id int8 not null,
        primary key (id)
    );

    create table servico (
       id  bigserial not null,
        created timestamp not null,
        updated timestamp,
        servico varchar(255) not null,
        valor float8,
        usuario_id int8,
        primary key (id)
    );


    alter table if exists contrato 
       add constraint UK_o47tsrckfirkjntx0mop5mehs unique (numero);

    alter table if exists contrato_registro 
       add constraint UK_h01ukcyp3vxfobkrb416glt8h unique (registro_id);

    alter table if exists plano_contratado_dia_consulta 
       add constraint UK_2xorvq9n6x0p32gskbdp06xv1 unique (dia_consulta_id);

    alter table if exists servico 
       add constraint UK_htta2atu2a2dnxx17vsht68d5 unique (servico);

    alter table if exists configuracao_parametro 
       add constraint FKa6ompe275pjo6xu3ngw6jh1g 
       foreign key (usuario_id) 
       references "user";

    alter table if exists contrato 
       add constraint FKjk83wy5pq0a7hufodligiop2k 
       foreign key (usuario_id) 
       references "user";

    alter table if exists contrato_registro 
       add constraint FKlitpbv3gtos68i7r5g8yagbsy 
       foreign key (registro_id) 
       references registro;

    alter table if exists contrato_registro 
       add constraint FKhlobflf8tctglbsladwkhvj5b 
       foreign key (contrato_id) 
       references contrato;

    alter table if exists dia_consulta 
       add constraint FKee8uof7n75el392jl54ehn8sk 
       foreign key (usuario_id) 
       references "user";

    alter table if exists plano_contratado 
       add constraint FK9l4fulk5f6jqyyyhues7edipw 
       foreign key (usuario_id) 
       references "user";

    alter table if exists plano_contratado 
       add constraint FKtrv2euxdqf9m08hh73v0ohtq1 
       foreign key (contrato_id) 
       references contrato;

    alter table if exists plano_contratado 
       add constraint FKqv9npru1hmtv2k9yi0in033ls 
       foreign key (servico_id) 
       references servico;

    alter table if exists plano_contratado_dia_consulta 
       add constraint FKikxh5ytxst7twom5esgoicdut 
       foreign key (dia_consulta_id) 
       references dia_consulta;

    alter table if exists plano_contratado_dia_consulta 
       add constraint FKid4lo4te4c9nydc6xxahp1hgp 
       foreign key (plano_contratado_id) 
       references plano_contratado;

    alter table if exists registro 
       add constraint FKqfbdwuu2isbwwnx1uky39930w 
       foreign key (usuario_id) 
       references "user";

    alter table if exists registro 
       add constraint FKo7l8j2w5i5ovcem33vlk7cxcq 
       foreign key (plano_contratado_id) 
       references plano_contratado;

    alter table if exists servico 
       add constraint FK3uw8e0wahiecte74b5qj8exmn 
       foreign key (usuario_id) 
       references "user";

   -------------------------------
    --------Dados Padrao-----------
    -------------------------------


    -----------Servico----------

INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(1,current_timestamp,null,null,'Fonoaudiologia',40.00);
INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(2,current_timestamp,null,null,'Psicologia',30.00);
INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(3,current_timestamp,null,null,'Terapia Ocupacional',45.00);
INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(4,current_timestamp,null,null,'Neuropsicopedagogia',100.00);

  -------------Config-----------

  INSERT INTO public.configuracao_parametro(
            id, created, updated, tempo_sessao, tempo_tolerancia_atraso, 
            valor_minuto_adicional, usuario_id)
    VALUES (1, current_timestamp , null, '0:50', '0:05', 
            2.30, 1);


