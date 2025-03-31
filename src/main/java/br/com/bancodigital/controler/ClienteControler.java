package br.com.bancodigital.controler;

import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class ClienteControler {
    @Autowired
    private ClienteService service;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Cliente cliente){
        service.cadastrar(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
