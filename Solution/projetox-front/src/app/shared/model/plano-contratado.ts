import { DiaConsulta } from "./dia-consulta";
import { Servico } from "./servico";
import { HorarioEntradaOrHorarioSaida, Contrato } from './Contrato';

export class PlanoContratado {
  id: number;
  updated?: null;
  valorSessao: number;
  horarioEntrada: HorarioEntradaOrHorarioSaida;
  horarioSaida: HorarioEntradaOrHorarioSaida;
  valorTotal: number;
  sessao: number;
  diaConsulta?: (DiaConsulta)[] | null;
  tipoContrato: string;
  servico: Servico;
  contrato: Contrato;
  ativo: boolean;
  valorAtendimento: number;
}

