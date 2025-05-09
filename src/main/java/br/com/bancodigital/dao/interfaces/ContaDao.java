package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Conta;
import java.util.Optional;

public interface ContaDao {
    void save(Conta conta);

    void deleteById(Long id);

    Optional<Conta> findById(Long id);

    Optional<Conta> findByChavePix(String chavePix);

    Optional<Conta> findByAgenciaAndNumero(Long agencia, Long numero);
}