
package br.edu.fatec.delivery.cliente.exception;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.fatec.delivery.cliente.dto.ApiError;
import br.edu.fatec.delivery.cliente.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private String correlationId(HttpServletRequest request) {
        String id = request.getHeader("X-Correlation-Id");

        if (id == null || id.isBlank()) {
            return UUID.randomUUID().toString();
        }

        return id;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> tratarNegocio(
            BusinessException ex,
            HttpServletRequest request) {

        ApiError erro = new ApiError(
                ex.getCodigo(),
                "Não foi possível concluir a operação",
                ex.getMessage(),
                Instant.now(),
                correlationId(request)
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponse(erro));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");

        ApiError erro = new ApiError(
                "DADOS_INVALIDOS",
                "Não foi possível concluir a operação",
                detalhes,
                Instant.now(),
                correlationId(request)
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(erro));
    }
}