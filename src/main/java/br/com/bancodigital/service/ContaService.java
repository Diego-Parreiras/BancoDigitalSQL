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
    private ContaDao contaDao;                              //injecao de dependencia

    public void sacar(Long id, double valor) {
        valorMaiorQueZero(valor);                           //verifica de o valor nao e menor que zero
        Optional<Conta> contaOptional = buscarConta(id);    //verifica se a conta existe
        if (contaOptional.isPresent()) {                    //se a conta existir
            Conta conta = contaOptional.get();              //pega a conta
            valorSuficiente(conta, valor);                  //verifica se tem saldo suficiente
            conta.setSaldo(conta.getSaldo() - valor);       //diminui o saldo
            contaDao.save(conta);                           //salva a conta
        }
        throw new RuntimeException("Conta não encontrada");
    }

    public double depositar(Long id, double valor) {        //metodo para depositar
        valorMaiorQueZero(valor);                           //verifica se o valor nao e menor que zero
        Optional<Conta> contaOptional = buscarConta(id);    //verifica se a conta existe
        if (contaOptional.isPresent()) {                    //se a conta existir
            Conta conta = contaOptional.get();              //pega a conta
            conta.setSaldo(conta.getSaldo() + valor);       //adiciona o saldo
            contaDao.save(conta);                           //salva a conta
            return conta.getSaldo();                        //retorna o saldo

        }
        throw new RuntimeException("Conta não encontrada"); //se a conta nao existir
    }

    public Transferencia transferenciaPix(Long idOrigem, String chavePix, double valor) {   //metodo para transferencia pix
        valorMaiorQueZero(valor);                                                           //verifica se o valor nao e menor que zero
        Optional<Conta> contaOrigemOptional = buscarConta(idOrigem);                        //verifica se a conta origem existe
        if (contaOrigemOptional.isPresent()) {                                              //se a conta origem existir
            valorSuficiente(contaOrigemOptional.get(), valor);                              //verifica se tem saldo suficiente

            Optional<Conta> contaDestinoOptional = buscarConta(buscaridConta(chavePix));    //verifica se a conta destino existe
            if (contaDestinoOptional.isPresent()) {                                         //se a conta destino existir
                return contaDao.transferir(idOrigem, contaDestinoOptional.get().getId(), valor);//retorna a transferencia
            }
            throw new RuntimeException("Conta destino não encontrada");                     //se a conta destino nao existir
        }
        throw new RuntimeException("Conta origem não encontrada");                          //se a conta origem nao existir
    }

    public Transferencia transferenciaTed(Long idOrigem, Long agencia, Long numero, double valor) {//metodo para transferencia por ted
        valorMaiorQueZero(valor);                                                          //verifica se o valor nao e menor que zero
        Optional<Conta> contaOrigemOptional = buscarConta(idOrigem);                       //verifica se a conta origem existe
        if (contaOrigemOptional.isPresent()) {                                              //se a conta origem existir
            valorSuficiente(contaOrigemOptional.get(), valor);                              //verifica se tem saldo suficiente

            Optional<Conta> contaDestinoOptional = buscarConta(buscaridConta(agencia, numero)); //verifica se a conta destino existe
            if (contaDestinoOptional.isPresent()) {                                             //se a conta destino existir
                return contaDao.transferir(idOrigem, contaDestinoOptional.get().getId(), valor);//retorna a transferencia
            }
            throw new RuntimeException("Conta destino não encontrada");                     //se a conta destino nao existir
        }
        throw new RuntimeException("Conta origem não encontrada");                          //se a conta origem nao existir
    }

    public double verSaldo(Long id) {                            //metodo para ver o saldo
        Optional<Conta> conta = buscarConta(id);                 //verifica se a conta existe
        if (conta.isPresent()) {                                 //se a conta existir
            return conta.get().getSaldo();                       //retorna o saldo
        }
        throw new RuntimeException("Conta não encontrada");      //se a conta nao existir
    }

    public void criarConta(Conta conta) {                       //metodo para criar uma conta
        verificarContaExiste(conta);                            //verifica se a conta ja existe
        contaDao.save(conta);                                   //salva a conta
    }

    public void deletarConta(Long id) {                         //metodo para deletar uma conta
        Optional<Conta> conta = buscarConta(id);                //verifica se a conta existe
        if (conta.isPresent()) {                                //se a conta existir
            verificarContaExiste(conta.get());                  //verifica se a conta ja existe
            if (conta.get().getSaldo() == 0) {                  //se o saldo da conta for zero
                contaDao.deleteById(id);                        //deletar a conta
            } else {                                            //se o saldo da conta nao for zero
                throw new RuntimeException("Sua conta não pode ser deletada"); //exibe uma mensagem de erro caso o saldo nao seja zero
            }
        }
    }

    private void verificarContaExiste(Conta conta) {             //metodo para verificar se a conta ja existe
        Optional<Long> idConta = contaDao.buscarIdConta(conta.getAgencia(), conta.getNumero());    //verifica se a conta ja existe
        if (idConta.isPresent()) {                              //se a conta ja existir
            throw new RuntimeException("Conta já cadastrada"); //exibe uma mensagem de erro
        }
    }

    private Optional<Conta> buscarConta(Long id) {              //metodo para buscar uma conta
        return contaDao.findById(id);                           //busca a conta pelo id
    }

    private void valorMaiorQueZero(double valor) {               //metodo para verificar se o valor nao e menor que zero
        if (valor < 0) {
            throw new RuntimeException("O valor deve ser maior que zero");
        }
    }

    private void valorSuficiente(Conta conta, double valor) {     //metodo para verificar se tem saldo suficiente
        if (conta.getSaldo() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    private Long buscaridConta(String chavePix) {                //metodo para buscar o id da conta
        Optional<Long> idConta = contaDao.buscarIdConta(chavePix);  //busca o id da conta pelo chave pix
        if (idConta.isPresent()) {                                  //se o id da conta existir
            return idConta.get();
        }
        throw new RuntimeException("id da conta não encontrada");
    }

    private Long buscaridConta(Long agencia, Long numero) {      //metodo para buscar o id da conta por agencia e numero
        Optional<Long> idConta = contaDao.buscarIdConta(agencia, numero);    //busca o id da conta pelo agencia e numero
        if (idConta.isPresent()) {                                  //se o id da conta existir
            return idConta.get();
        }
        throw new RuntimeException("id da conta não encontrada");
    }
}

