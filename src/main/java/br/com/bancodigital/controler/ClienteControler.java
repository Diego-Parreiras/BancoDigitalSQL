package br.com.bancodigital.controler;

import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteControler {
    @Autowired
    private ClienteService service;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Cliente cliente){
        service.cadastrar(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        return new ResponseEntity<>(service.buscarId(id), HttpStatus.OK);
    }
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cliente cliente){
        service.atualizar(id, cliente);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id){
        service.apagar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/buscar-todos")
    public ResponseEntity<?> buscarTodos(){
        return new ResponseEntity<>(service.buscarTodos(), HttpStatus.OK);
    }

}
