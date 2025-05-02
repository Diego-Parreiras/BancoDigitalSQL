package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CartaoDao{

    boolean existsByNumero(Long numero);
    Optional<Cartao> findByNumero(Long numero);
    void save(Cartao cartao);
    Optional<Cartao> findById(Long id);
}
