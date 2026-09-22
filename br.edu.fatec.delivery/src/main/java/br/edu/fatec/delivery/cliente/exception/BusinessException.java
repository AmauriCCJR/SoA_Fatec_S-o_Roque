package br.edu.fatec.delivery.cliente.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final String codigo;
    private final HttpStatus status;

    public BusinessException(
            String codigo,
            String mensagem,
            HttpStatus status) {

        super(mensagem);
        this.codigo = codigo;
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public HttpStatus getStatus() {
        return status;
    }
}