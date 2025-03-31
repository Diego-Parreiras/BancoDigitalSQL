package br.com.bancodigital.service;

import br.com.bancodigital.dao.ContaDao;
import br.com.bancodigital.dao.TransferenciaDao;
import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.Transferencia;
import br.com.bancodigital.model.dto.TransferenciaPixRequest;
import br.com.bancodigital.model.dto.TransferenciaTedRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    @Autowired
    private ContaDao contaDao;

    @Autowired
    private TransferenciaDao transferenciaDao;

    private final Random random = new Random();

    public void criarConta(Conta conta) {
        verificarContaExiste(conta);
        popularDadosConta(conta);
        contaDao.save(conta);
    }

    private void popularDadosConta(Conta conta) {
        conta.setSaldo(0);
        conta.setNumero(random.nextInt(900000) + 100000);
        conta.setAgencia(1221);
        String regexCPF="\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}";
        if(conta.getChavePix()==null || conta.getChavePix().isEmpty()||!conta.getChavePix().matches(regexCPF)){
            conta.setChavePix(generateChavePix(30));
        }
    }

    private String generateChavePix(int i) {
        final String CARACTERES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            int index = random.nextInt(CARACTERES.length());
            sb.append(CARACTERES.charAt(index));
        }
        return sb.toString();
    }

    @Transactional
    public Transferencia transferenciaPix(TransferenciaPixRequest request){
        varificarValor(request.getValor());
        Optional<Conta> contaOrigemOptional = buscarConta(request.getIdContaOrigem());
        if(contaOrigemOptional.isPresent()) {
            verificarSaldo(contaOrigemOptional.get().getSaldo(), request.getValor());

            Optional<Conta> contaDestinoOptional = buscarConta(buscarIdConta(request.getChavePix()));
            if(contaDestinoOptional.isPresent()) {

                if(contaOrigemOptional.get().getSenha() != request.getSenha()){
                    throw new RuntimeException("Senha incorreta");
                }
                sacar(contaOrigemOptional.get().getId(), request.getValor());
                depositar(contaDestinoOptional.get().getId(), request.getValor());

                return registrarTransferencia(request.getIdContaOrigem(), contaDestinoOptional.get().getId(), request.getValor());
            }
            throw new RuntimeException("ContaDestino não encontrada");
        }
        throw new RuntimeException("ContaOrigem não encontrada");
    }
    @Transactional
    public Transferencia transferenciaTed(TransferenciaTedRequest request){
        varificarValor(request.getValor());
        Optional<Conta> contaOrigemOptional = buscarConta(request.getIdOrigem());
        if(contaOrigemOptional.isPresent()) {
            verificarSaldo(contaOrigemOptional.get().getSaldo(), request.getValor());

            Optional<Conta> contaDestinoOptional = buscarConta(buscarIdConta(request.getAgenciaDestino(), request.getNumeroContaDestino()));
            if(contaDestinoOptional.isPresent()) {
                if(contaOrigemOptional.get().getSenha() != request.getSenha()){
                    throw new RuntimeException("Senha incorreta");
                }

                sacar(contaOrigemOptional.get().getId(), request.getValor());
                depositar(contaDestinoOptional.get().getId(), request.getValor());

                return registrarTransferencia(request.getIdOrigem(), contaDestinoOptional.get().getId(), request.getValor());
            }
            throw new RuntimeException("ContaDestino não encontrada");
        }
        throw new RuntimeException("ContaOrigem não encontrada");
    }

    public void fecharConta(Long id) {
        Optional<Conta> conta = buscarConta(id);
        if (conta.isPresent()) {
            verificarContaExiste(conta.get());
            if(conta.get().getSaldo()!=0) {
                contaDao.deleteById(id);
            }
            else{
                throw new RuntimeException("Conta com saldo não pode ser fechada");
            }
        }
    }
    public double exibirSaldo(Long id) {
        Optional<Conta> conta = buscarConta(id);
        if (conta.isPresent()) {
            return conta.get().getSaldo();
        }
        throw new RuntimeException("Conta não encontrada");
    }

    public void depositar(Long id, double valor) {
        varificarValor(valor);
        Optional<Conta> contaOptional = buscarConta(id);
        if (contaOptional.isPresent()) {
            Conta conta = contaOptional.get();
            conta.setSaldo(conta.getSaldo() + valor);
            contaDao.save(conta);
        }
    }

    public void sacar(Long id, double valor) {
        varificarValor(valor);
        Optional<Conta> contaOptional = buscarConta(id);
        if (contaOptional.isPresent()) {
            Conta conta = contaOptional.get();
            verificarSaldo(conta.getSaldo(), valor);
            conta.setSaldo(conta.getSaldo() - valor);
            contaDao.save(conta);
        }
    }

    private Transferencia registrarTransferencia(long idContaOrigem, long idContaDestino, double valor) {
        Transferencia transferencia = new Transferencia();
        transferencia.setIdContaOrigem(idContaOrigem);
        transferencia.setIdContaDestino(idContaDestino);
        transferencia.setValor(valor);
        transferencia.setDataTransferencia(LocalDateTime.now());
        transferenciaDao.save(transferencia);
        return transferencia;
    }

    private Optional<Conta> buscarConta(Long id) {

        return contaDao.findById(id);

    }
    private Long buscarIdConta(String chavePix) {
        Optional<Conta> conta = contaDao.findByChavePix(chavePix);
        if(conta.isPresent()){
            return conta.get().getId();
        }
        throw new RuntimeException("Conta não encontrada");
    }
    private Long buscarIdConta(Long agencia, Long numero) {
        Optional<Conta> conta = contaDao.findByAgenciaAndNumero(agencia, numero);
        if(conta.isPresent()){
            return conta.get().getId();
        }
        throw new RuntimeException("Conta não encontrada");
    }
    private void verificarContaExiste(Conta conta) {

        Optional<Conta> contaOptional = contaDao.findByAgenciaAndNumero(conta.getAgencia(), conta.getNumero());

        if (contaOptional.isPresent()) {
            throw new RuntimeException("Conta já cadastrada");
        }

    }
    private void varificarValor(double valor){
        if(valor < 0){
            throw new RuntimeException("Valor não pode ser negativo");
        }
    }

    private void verificarSaldo(double saldo, double valor){
        if(saldo< 0){
            throw new RuntimeException("Saldo insuficiente");
        }
    }

}