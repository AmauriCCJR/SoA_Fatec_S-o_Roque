package br.edu.fatec.delivery.cliente.exception;

import java.time.Instant;

public record ApiError(
        String codigo,
        String mensagem,
        String detalhes,
        Instant timestamp,
        String correlationId
) {
}