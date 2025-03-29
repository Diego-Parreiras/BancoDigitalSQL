package br.com.bancodigital.dao;

import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaDao extends JpaRepository<Conta, Long> {

    Transferencia transferir(Long idOrigem, Long idDestino, double valor);

    Optional<Long> buscarIdConta(String chavePix);

    Optional<Long> buscarIdConta(Long agencia, Long numero);

}
