import { DiaConsulta } from "./dia-consulta";
import { Registro } from "./registro";
import { Contrato } from "./Contrato";
import { Servico } from "./servico";

export interface PlanoContratado{
  id: number;
  valorPlano: number;
  horarioEntrada: string;
  horarioSaida: string;
  valorTotal: number;
  diaConsulta: DiaConsulta;
  tipoContrato: string;
  servico: Servico;
  registro: Registro;
  contrato: Contrato;
}
