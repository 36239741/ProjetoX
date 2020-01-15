INSERT INTO public.plano_contratado(id,
             created, updated, ativo, horario_entrada, horario_saida, 
            sessao, tipo_contrato, valor_plano, valor_total, usuario_id, 
            contrato_id, servico_id)
    VALUES (1, current_timestamp, null, true, '08:45:00', '09:45:00', 
            5, 0, 1000.00, 2000.00, null, 
            1, 4);

INSERT INTO public.plano_contratado(id,
             created, updated, ativo, horario_entrada, horario_saida, 
            sessao, tipo_contrato, valor_plano, valor_total, usuario_id, 
            contrato_id, servico_id)
    VALUES (2 ,current_timestamp, null, true, '16:10:00', '17:10:00', 
            4, 1, 1000.00, 2000.00, null, 
            1, 4);

INSERT INTO public.plano_contratado(id,
             created, updated, ativo, horario_entrada, horario_saida, 
            sessao, tipo_contrato, valor_plano, valor_total, usuario_id, 
            contrato_id, servico_id)
    VALUES (3, current_timestamp, null, true, current_timestamp, '23:10:00', 
            5, 0, 1000.00, 2000.00, null, 
            1, 3);


INSERT INTO public.plano_contratado(id,
             created, updated, ativo, horario_entrada, horario_saida, 
            sessao, tipo_contrato, valor_plano, valor_total, usuario_id, 
            contrato_id, servico_id)
    VALUES (4, current_timestamp, null, true, current_timestamp, '23:10:00', 
            5, 1, 10.00, 2000.00, null, 
            1, 3);


