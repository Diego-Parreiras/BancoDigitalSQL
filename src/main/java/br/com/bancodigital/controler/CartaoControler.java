package br.com.bancodigital.controler;

import br.com.bancodigital.model.Cartao;
import br.com.bancodigital.model.CartaoDeCredito;
import br.com.bancodigital.model.CartaoDeDebito;
import br.com.bancodigital.model.dto.PagamentoCartaoRequest;
import br.com.bancodigital.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartao")
public class CartaoControler {
    @Autowired
    private CartaoService service;

    @PostMapping("/novo-cartao-debito")
    public ResponseEntity<?> novoCartaoDebito(@RequestBody CartaoDeDebito cartao) {
        service.novoCartao(cartao);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/novo-cartao-credito")
    public ResponseEntity<?> novoCartaoCredito(@RequestBody CartaoDeCredito cartao) {
        service.novoCartao(cartao);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/buscar-cartao/{id}")
    public ResponseEntity<?> buscarCartao(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarId(id), HttpStatus.OK);
    }

    @PostMapping("/pagamento/{id}")
    public ResponseEntity<?> pagar(@PathVariable Long id, @RequestBody PagamentoCartaoRequest pagamento) {
        service.pagar(id, pagamento);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/aumenta-limite-credito/{id}")
    public ResponseEntity<?> aumentaLimiteCredito(@PathVariable Long id, @RequestBody double valor) {
        service.aumentarLimiteCredito(id, valor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/aumenta-limite-debito/{id}")
    public ResponseEntity<?> aumentaLimiteDebito(@PathVariable Long id, @RequestBody double valor) {
        service.aumentarLimiteDebito(id, valor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> status(@PathVariable Long id) {
        service.mudarStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/atualizar-senha/{id}")
    public ResponseEntity<?> atualizarSenha(@PathVariable Long id, @RequestBody Long senha) {
        service.atualizarSenha(id, senha);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/buscar-fatura/{id}")
    public ResponseEntity<?> buscarFatura(@PathVariable Long id) {

        return new ResponseEntity<>(service.buscarFatura(id), HttpStatus.OK);
    }

    @PostMapping("/pagar-fatura/{id}")
    public ResponseEntity<?> pagarFatura(@PathVariable Long id) {
        service.pagarFatura(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
