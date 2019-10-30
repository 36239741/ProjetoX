import { Contrato } from './Contrato';
export interface Contrato {
    id: number;
    numero: string;
    nomePaciente: string;
    valorTotal: number;
    biometria?: any;
}

export interface Sort {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
}

export interface Pageable {
    sort: Sort;
    pageSize: number;
    pageNumber: number;
    offset: number;
    paged: boolean;
    unpaged: boolean;
}

export interface Sort2 {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
}

export interface PageContrato {
    contrato: Contrato[];
    pageable: Pageable;
    totalPages: number;
    totalElements: number;
    last: boolean;
    first: boolean;
    sort: Sort2;
    numberOfElements: number;
    size: number;
    number: number;
    empty: boolean;
}

