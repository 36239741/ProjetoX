import { Registro } from "./registro";
import { PlanoContratado } from "./plano-contradao";

export interface Contrato{
  id: number;
  numero: number;
  nomePaciente: string;
  valorTotal: number;
  registro: Registro;
  planoContratado: PlanoContratado;
}
