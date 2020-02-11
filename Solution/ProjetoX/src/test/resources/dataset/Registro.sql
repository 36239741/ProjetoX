INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (1, current_timestamp,null ,'2019-12-08 08:40:37.573', null, 3, null, 
            null, null, 1, 1);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (2, current_timestamp,null ,'2019-12-08 16:10:30.573', null, 3, null, 
            null, null, 1, 2);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (3, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573' ,'2019-12-08 16:10:30.573', null, 3, null, 
            null, null, 1, 2);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (4, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573' ,'2019-12-08 16:10:30.573', null, 3, null, 
            null, null, 1, 4);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (5, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573' ,'2019-12-05 16:10:30.573', '2019-12-08 17:10:30.573', 3, null, 
            null, null, 1, 4);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (6, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573' ,'2019-12-08 16:10:30.573', '2019-12-08 17:10:30.573', 2, null, 
            null, null, 1, 4);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (7, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573' ,'2019-12-08 16:10:30.573', null, 2, null, 
            null, null, 1, 4);
            
INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (8, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573', null, null, 2, null, 
            null, null, 1, 4);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (9, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573' ,'2019-12-08 16:10:30.573', '2019-12-08 17:10:30.573', 3, '00:50:00', 
            1000, null, 1, 6);
            
INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (10, '2019-12-08 08:40:37.573' , '2019-12-08 09:40:37.573' ,'2019-12-08 16:10:30.573', '2019-12-08 17:10:30.573', 3, '00:50:00', 
            1000, null, 1, 5);
            
INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (11, current_timestamp,null ,'2019-12-08 16:10:30.573', null, 3, null, 
            null, null, 1, 7);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (12, current_timestamp,null ,'2019-12-08 16:10:30.573', '2019-12-08 17:10:30.573', 0, null, 
            null, null, 1, 7);

INSERT INTO public.registro(
             id,created, updated, data_hora_entrada, data_hora_saida, situacao, 
            tempo_total, valor_total, usuario_id, contrato_id, plano_contratado_id)
    VALUES (13,current_timestamp,null ,'2019-12-08 16:10:30.573', null, 3, null, 
            null, null, 1, 7);
            
ALTER SEQUENCE registro_id_seq RESTART WITH 100;

