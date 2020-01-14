INSERT INTO public.usuario(id,
             created, email, authority_type, senha)
    VALUES (1, now(), 'henrique_nitatori@hotmail.com', 0, '{bcrypt}$2a$10$.7uL1NI/RC3n0bgDIIrOp.57vf35JCc9Pi19JkjIlcNZtv/r66wGa');

INSERT INTO public.usuario(id,
             created, email, authority_type, senha)
    VALUES (2, now(), 'marcieli.langer@mailinator.com', 0, '$2a$10$bAdAVLvM.k3DqPaPYi0gnO1OffPSHLref8MElAk.u.fFQ17v9YKC2');

INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(1,current_timestamp,null,null,'Fonoaudiologia',40.00);
INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(2,current_timestamp,null,null,'Psicologia',30.00);
INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(3,current_timestamp,null,null,'Terapia Ocupacional',45.00);
INSERT INTO servico(id,created,updated,usuario_id,servico,valor) VALUES(4,current_timestamp,null,null,'Neuropsicopedagogia',100.00);

INSERT INTO public.configuracao_parametro(
            id, created, updated, tempo_sessao, tempo_tolerancia_atraso, 
            valor_minuto_adicional, usuario_id)
    VALUES (1, current_timestamp , null, '0:40', '0:05', 
            2.30, 1);




