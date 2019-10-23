INSERT INTO tbl_servico(servico_id,servico, valor) VALUES(1,'Fonoaudiologia',120.00);
INSERT INTO tbl_servico(servico_id,servico, valor) VALUES(2,'Psicologia',120.00);
INSERT INTO tbl_servico(servico_id,servico, valor) VALUES(3,'Terapia Oculpacional',120.00);
INSERT INTO tbl_servico(servico_id,servico, valor) VALUES(4,'Neuropsicopedagogia',45.00);

INSERT INTO public.tbl_contrato(
            contrato_id, biometria, nome_paciente, numero, valor_total)
    VALUES (1, '232123131', 'Henrique', 1, 1600.00);

INSERT INTO public.tbl_plano(
            plano_id, horario_entrada, horario_saida, sessao, tipo_contrato, 
            valor_plano, valor_total, contrato_contrato_id, servico_servico_id)
    VALUES (1, '10:00', '10:40', 5, 0, 
            100.00, 500.00, 1, 1);

INSERT INTO public.tbl_dia_consulta(
            id, dias_semana, plano_id)
    VALUES (1, 1, 1);


INSERT INTO public.tbl_dia_consulta(
            id, dias_semana, plano_id)
    VALUES (2, 2, 1);




