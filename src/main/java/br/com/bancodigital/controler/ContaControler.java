package br.com.bancodigital.controler;

import br.com.bancodigital.model.Conta;

import br.com.bancodigital.model.dto.TransferenciaPixRequest;
import br.com.bancodigital.model.dto.TransferenciaTedRequest;

import br.com.bancodigital.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta")
public class ContaControler {
    @Autowired
    private ContaService service;

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody Conta conta) {
        service.criarConta(conta);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transferencia-pix")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaPixRequest request) {
        return new ResponseEntity<>(service.transferenciaPix(request), HttpStatus.OK);
    }

    @PostMapping("/transferencia-ted")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaTedRequest request) {
        return new ResponseEntity<>(service.transferenciaTed(request), HttpStatus.OK);
    }

    @PutMapping("/{id}/deposito")
    public ResponseEntity<?> depositar(@PathVariable Long id, @RequestBody double valor) {
        service.depositar(id, valor);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}