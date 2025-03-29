package br.com.bancodigital.service;

import br.com.bancodigital.dao.ContaDao;
import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.Transferencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaDao contaDao;

    public void sacar(Long id, double valor) {
        valorMaiorQueZero(valor);
        Optional<Conta> contaOptional = buscarConta(id);
        if (contaOptional.isPresent()) {
            Conta conta = contaOptional.get();
            valorSuficiente(conta, valor);
            conta.setSaldo(conta.getSaldo() - valor);
            contaDao.save(conta);
        }
    }

    public double depositar(Long id, double valor) {
        valorMaiorQueZero(valor);
        Optional<Conta> contaOptional = buscarConta(id);
        if (contaOptional.isPresent()) {
            Conta conta = contaOptional.get();
            conta.setSaldo(conta.getSaldo() + valor);
            contaDao.save(conta);

        }
        throw new RuntimeException("Conta não encontrada");
    }

    public Transferencia transferenciaPix(Long idOrigem, String chavePix, double valor) {
        valorMaiorQueZero(valor);
        Optional<Conta> contaOrigemOptional = buscarConta(idOrigem);
        if (contaOrigemOptional.isPresent()) {
            valorSuficiente(contaOrigemOptional.get(), valor);

            Optional<Conta> contaDestinoOptional = buscarConta(buscaridConta(chavePix));
            if (contaDestinoOptional.isPresent()) {
                return contaDao.transferir(idOrigem, contaDestinoOptional.get().getId(), valor);
            }
            throw new RuntimeException("Conta destino não encontrada");
        }
        throw new RuntimeException("Conta origem não encontrada");
    }

    public Transferencia transferenciaTed(Long idOrigem, Long agencia, Long numero, double valor) {
        valorMaiorQueZero(valor);
        Optional<Conta> contaOrigemOptional = buscarConta(idOrigem);
        if (contaOrigemOptional.isPresent()) {
            valorSuficiente(contaOrigemOptional.get(), valor);

            Optional<Conta> contaDestinoOptional = buscarConta(buscaridConta(agencia, numero));
            if (contaDestinoOptional.isPresent()) {
                return contaDao.transferir(idOrigem, contaDestinoOptional.get().getId(), valor);
            }
            throw new RuntimeException("Conta destino não encontrada");
        }
        throw new RuntimeException("Conta origem não encontrada");
    }

    public double verSaldo(Long id) {
        Optional<Conta> conta = buscarConta(id);
        if (conta.isPresent()) {
            return conta.get().getSaldo();
        }
        throw new RuntimeException("Conta não encontrada");
    }

    public void criarConta(Conta conta) {
        verificarContaExiste(conta);
        contaDao.save(conta);
    }

    public void deletarConta(Long id) {
        Optional<Conta> conta = buscarConta(id);
        if (conta.isPresent()) {
            verificarContaExiste(conta.get());
            if (conta.get().getSaldo() == 0) {
                contaDao.deleteById(id);
            } else {
                throw new RuntimeException("Sua conta não pode ser deletada");
            }
        }
    }

    private void verificarContaExiste(Conta conta) {
        Optional<Long> idConta = contaDao.buscarIdConta(conta.getAgencia(), conta.getNumero());
        if (idConta.isPresent()) {
            throw new RuntimeException("Conta já cadastrada");
        }
    }

    private Optional<Conta> buscarConta(Long id) {
        return contaDao.findById(id);
    }

    private void valorMaiorQueZero(double valor) {
        if (valor < 0) {
            throw new RuntimeException("O valor deve ser maior que zero");
        }
    }

    private void valorSuficiente(Conta conta, double valor) {
        if (conta.getSaldo() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    private Long buscaridConta(String chavePix) {
        Optional<Long> idConta = contaDao.buscarIdConta(chavePix);
        if (idConta.isPresent()) {
            return idConta.get();
        }
        throw new RuntimeException("id da conta não encontrada");
    }

    private Long buscaridConta(Long agencia, Long numero) {
        Optional<Long> idConta = contaDao.buscarIdConta(agencia, numero);
        if (idConta.isPresent()) {
            return idConta.get();
        }
        throw new RuntimeException("id da conta não encontrada");
    }
}

