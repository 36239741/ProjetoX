import { DiaConsulta } from "./dia-consulta";
import { Servico } from "./servico";
import { HorarioEntradaOrHorarioSaida } from './Contrato';

export class PlanoContratado {
  id: number;
  valorPlano: number;
  horarioEntrada: HorarioEntradaOrHorarioSaida;
  horarioSaida: HorarioEntradaOrHorarioSaida;
  valorTotal: number;
  sessao: number;
  diaConsulta: DiaConsulta[];
  tipoContrato: string;
  servico?: Servico;
}

