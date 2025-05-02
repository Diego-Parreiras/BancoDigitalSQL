package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Conta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaDao {
    void save(Conta conta);

    void deleteById(Long id);

    Optional<Conta> findById(Long id);

    Optional<Conta> findByChavePix(String chavePix);

    Optional<Conta> findByAgenciaAndNumero(Long agencia, Long numero);
}