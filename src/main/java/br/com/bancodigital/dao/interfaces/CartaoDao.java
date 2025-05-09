package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Cartao;
import java.util.Optional;

public interface CartaoDao{

    boolean existsByNumero(Long numero);
    Optional<Cartao> findByNumero(Long numero);
    void save(Cartao cartao);
    Optional<Cartao> findById(Long id);
}
