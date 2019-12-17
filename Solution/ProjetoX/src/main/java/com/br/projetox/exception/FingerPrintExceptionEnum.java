package com.br.projetox.exception;

public enum FingerPrintExceptionEnum  {
	SUCCESS("Operação realizada com sucesso"),
	WARNING_ALREADY_INIT("Biblioteca já inicializada") ,	
	WARNING_NO_IDS_ON_DEVICE("Nenhum Template cadastrado") ,
	WARNING_OVERWRITING_TEMPLATE("Template foi sobrescrito") ,
	ERROR_UNKNOWN("Erro desconhecido") ,
	ERROR_NO_DEVICE("Dispositivo não encontrado") ,
	ERROR_NULL_ARGUMENT("Argumento nulo") ,
	ERROR_INVALID_ARGUMENT("Argumento inválido") ,
	ERROR_CAPTURE("Erro durante a captura") ,
	ERROR_CAPTURE_TIMEOUT("Tempo de captura expirado") ,
	ERROR_COMM_USB("Erro de comunicação USB") ,
	ERROR_IO_ON_HOST("Erro de comunicação do Host") ,
	ERROR_TEMPLATE_ALREADY_ENROLLED("Template já cadastrado") ,
	ERROR_MERGING("Falha no Merge") ,
	ERROR_MATCHING("Falha no Match") ,
	ERROR_INVALID_FW_FILE("Arquivo de Firmware inválido"),	
	ERROR_NO_SPACE_LEFT_ON_DEVICE("Espaço no dispositivo esgotado") ,
	ERROR_NO_TEMPLATE_WITH_ID("Template não cadastrado") ,
	ERROR_INVALID_ERRNO("Código de erro inválido") ,
	ERROR_UNAVAILABLE_FEATURE("Funcionalidade não disponível") ,
	ERROR_PREVIOUS_FW_VERSION("Versão do firmware é anterior à atual"),
	ERROR_NOT_IDENTIFIED("Template não identificado") ,
	ERROR_BUSY("Dispositivo esta ocupado"),
	ERROR_CAPTURE_CANCELED("Captura foi cancelada") ,
	ERROR_NO_FINGER_DETECTED("Digital não foi detectada") ;
	
	private String error;
	
	private FingerPrintExceptionEnum(String error) {
		this.error = error;
	}
	
	public String getDescricao() {
		return error;
	}
}