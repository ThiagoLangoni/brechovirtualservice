package br.fiap.infrastructure;

import lombok.Data;

@Data
public class ProdutoServiceException extends Exception{
	
	public static final String OPERACAO_PROCURA_ERROR = "PROCURA_ERROR";
    public static final String PARAMETRO_INVALIDO_ERROR = "PARAMETRO_INVALIDO_ERROR";
    public static final String PRODUTO_NAO_LOCALIZADO_ERROR = "PRODUTO_NAO_LOCALIZADO_ERROR";
    public static final String OPERACACAO_INCLUSAO_ERROR = "INCLUSAO_ERROR";
    public static final String OPERACAO_BUSCA_ERROR = "BUSCA_ERROR";
    public static final String OPERACAO_ATUALIZACAO_ERROR = "ATUALIZACAO_ERROR";
    public static final String OPERACAO_EXCLUSAO_ERROR = "EXCLUSAO_ERROR";
    private final String operation;

    public ProdutoServiceException(String operation, Throwable cause) {
        super(cause);
        this.operation = operation;
    }

}
