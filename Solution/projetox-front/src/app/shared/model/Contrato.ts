  import { PlanoContratado } from './plano-contradao';

  export interface HorarioEntradaOrHorarioSaida {
      hour: number;
      minute: number;
      second: number;
      nano: number;
  }

  export interface Servico {
      id: number;
      servico: string;
      valor: number;
  }


  export class Contrato {
      id: number;
      numero: string;
      nomePaciente: string;
      valorTotal: number;
      biometria?: any;
      tipoContratoTransient: string;
      planoContratado: PlanoContratado[];
  }

  export interface Sort {
      unsorted: boolean;
      sorted: boolean;
      empty: boolean;
  }

  export interface Pageable {
      sort: Sort;
      pageSize: number;
      pageNumber: number;
      offset: number;
      unpaged: boolean;
      paged: boolean;
  }

  export interface Sort2 {
      unsorted: boolean;
      sorted: boolean;
      empty: boolean;
  }

  export interface PageContrato {
      contrato: Contrato;
      pageable: Pageable;
      totalElements: number;
      last: boolean;
      totalPages: number;
      first: boolean;
      sort: Sort2;
      numberOfElements: number;
      size: number;
      number: number;
      empty: boolean;
  }



