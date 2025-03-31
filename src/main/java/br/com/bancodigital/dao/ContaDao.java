package br.com.bancodigital.dao;

import br.com.bancodigital.model.Conta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaDao extends JpaRepository<Conta, Long> {

    Optional<Conta> findByChavePix(String chavePix);

    Optional<Conta> findByAgenciaAndNumero(Long agencia, Long numero);
}