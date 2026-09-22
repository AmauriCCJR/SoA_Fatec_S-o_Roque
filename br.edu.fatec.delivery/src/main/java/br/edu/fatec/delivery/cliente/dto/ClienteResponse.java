package br.edu.fatec.delivery.cliente.dto;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        boolean ativo   
    ) {
}