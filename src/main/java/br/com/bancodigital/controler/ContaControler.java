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

    @PostMapping("/criar-conta")
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

    @PutMapping("/depositar/{id}")
    public ResponseEntity<?> depositar(@PathVariable Long id, @RequestBody double valor) {
        service.depositar(id, valor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/sacar/{id}")
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestBody double valor) {
        service.sacar(id, valor);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/consultar-saldo/{id}")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long id) {
        return new ResponseEntity<>(service.exibirSaldo(id), HttpStatus.OK);
    }

    @GetMapping("/minha-conta/{id}")
    public ResponseEntity<?> minhaConta(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarContaPorId(id), HttpStatus.OK);
    }
    @DeleteMapping("/fechar-conta/{id}")
    public ResponseEntity<?> fechar(@PathVariable Long id) {
        service.fecharConta(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/manutencao/{id}")
    public ResponseEntity<?> AplicarTaxaManutencao(@PathVariable Long id) {
        service.aplicicarTaxaManutencao(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/rendimento/{id}")
    public ResponseEntity<?> AplicarTaxaRendimento(@PathVariable Long id) {
        service.aplicarTaxaRendimento(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}