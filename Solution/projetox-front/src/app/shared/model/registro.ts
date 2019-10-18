import { Contrato } from "./Contrato";
import { PlanoContratado } from "./plano-contradao";

export interface Registro{
  id : number;
  dataHoraEntrada: Date;
  dataHoraSaida: Date;
  tempoTotal: string;
  valorTotal: number;
  situacao: string;
  contrato: Contrato;
  planoContratado: PlanoContratado;
}
