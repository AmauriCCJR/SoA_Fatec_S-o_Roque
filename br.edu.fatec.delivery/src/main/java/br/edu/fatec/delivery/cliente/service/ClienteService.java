
package br.edu.fatec.delivery.cliente.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.edu.fatec.delivery.cliente.dto.ClienteRequest;
import br.edu.fatec.delivery.cliente.dto.ClienteResponse;
import br.edu.fatec.delivery.cliente.entity.Cliente;
import br.edu.fatec.delivery.cliente.exception.BusinessException;
import br.edu.fatec.delivery.cliente.repository.ClienteRepository;


@Service
public class ClienteService {

    private final ClienteRepository repository;
    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteResponse criar(ClienteRequest request) {

        repository.findByEmail(request.email())

                .ifPresent(c -> {
                    throw new BusinessException(
                            "EMAIL_JA_CADASTRADO",
                            "Já existe um cliente com este e-mail",
                            HttpStatus.CONFLICT
                    );
                });

        Cliente cliente = new Cliente(
                request.nome(),
                request.email(),
                request.telefone()
        );

        return converter(repository.save(cliente));
    }


    public List<ClienteResponse> listar() {
        return repository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public ClienteResponse consultar(Long id) {
        return converter(buscar(id));
    }

    public ClienteResponse atualizar(
            Long id,
            ClienteRequest request) {

        Cliente cliente = buscar(id);

        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());

        return converter(repository.save(cliente));
    }

    public ClienteResponse alterarStatus(
            Long id,
            boolean ativo) {

        Cliente cliente = buscar(id);
        cliente.setAtivo(ativo);
        return converter(repository.save(cliente));
    }

    private Cliente buscar(Long id) {


        return repository.findById(id)

                .orElseThrow(() ->
                        new BusinessException(
                                "CLIENTE_NAO_ENCONTRADO",
                                "Cliente " + id + " não encontrado",
                                HttpStatus.NOT_FOUND
                        ));
    }

    private ClienteResponse converter(Cliente cliente) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.isAtivo()
        );
    }
}