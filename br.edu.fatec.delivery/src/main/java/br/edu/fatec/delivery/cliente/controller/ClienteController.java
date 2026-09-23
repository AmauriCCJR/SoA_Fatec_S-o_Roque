package br.edu.fatec.delivery.cliente.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.fatec.delivery.cliente.dto.ClienteRequest;
import br.edu.fatec.delivery.cliente.dto.ClienteResponse;
import br.edu.fatec.delivery.cliente.dto.ClienteStatusRequest;
import br.edu.fatec.delivery.cliente.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar( @Valid @RequestBody ClienteRequest request) {
        ClienteResponse cliente = service.criar(request);

        return ResponseEntity
                .created(URI.create("/api/clientes/" + cliente.id()))
                .body(cliente);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse consultar( @PathVariable Long id) {
        return service.consultar(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar( @PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/status")
    public ClienteResponse alterarStatus(@PathVariable Long id, @RequestBody ClienteStatusRequest request) {
        return service.alterarStatus( id, request.ativo() );
    }
}
