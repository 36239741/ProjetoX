ALTER TABLE plano_contratado
ADD valor_atendimento double precision; 

ALTER TABLE plano_contratado 
    RENAME COLUMN valor_plano TO valor_sessao;

