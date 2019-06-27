package br.edu.utfpr.servico;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.excecao.NomeClienteMenor5CaracteresException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vinicius
 */

@RestController
public class ServicoCliente {
    private List<ClienteDTO> clientes;

    @GetMapping ("/servico/cliente")
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(clientes);
    }

    @GetMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> listarPorId(@PathVariable int id) {
        Optional<ClienteDTO> clienteEncontrado = clientes.stream().filter(p -> p.getId() == id).findAny();

        return ResponseEntity.of(clienteEncontrado);
    }
    
    @PostMapping ("/servico/cliente")
    public ResponseEntity<ClienteDTO> criar (@RequestBody ClienteDTO cliente) {

        cliente.setId(clientes.size() + 1);
        clientes.add(cliente);

        return ResponseEntity.status(201).body(cliente);
    }

    @DeleteMapping ("/servico/cliente/{id}")
    public ResponseEntity excluir (@PathVariable int id) {
        
        if (clientes.removeIf(c -> c.getId() == id))
            return ResponseEntity.noContent().build();

        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> alterar (@PathVariable int id, ClienteDTO cliente){
        Optional<ClienteDTO> clienteEncontrado = clientes.stream().filter(c -> c.getId() == id).findAny();

        clienteEncontrado.ifPresent(new Consumer<ClienteDTO>(){
            @Override
            public void accept(ClienteDTO c){
                try {
                    c.setNome(cliente.getNome());
                } catch (NomeClienteMenor5CaracteresException ex) {
                    Logger.getLogger(ServicoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                c.setIdade(cliente.getIdade());
                c.setPais(cliente.getPais());
                c.setLimiteCredito(cliente.getLimiteCredito());
                c.setTelefone(cliente.getTelefone());
            }
        });

        return ResponseEntity.of(clienteEncontrado);
    }
}
