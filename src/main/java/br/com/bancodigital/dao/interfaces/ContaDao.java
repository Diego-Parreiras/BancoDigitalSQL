package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.entity.Conta;
import br.com.bancodigital.model.entity.Transferencia;

import java.util.Optional;

public interface ContaDao {
    void save(Conta conta);

    void deleteById(Long id);

    Optional<Conta> findById(Long id);

    Optional<Conta> findByChavePix(String chavePix);

    Optional<Conta> findByAgenciaAndNumero(Long agencia, Long numero);

    void depositor(Long id, double valor);

    void sacar(Long id, double valor);

    Transferencia tranferir(Long idOrigem, Long idDestino, double valor);

    void aplicarTaxaManutencao(Long id);

    void aplicarTaxaRendimento(Long id);
}