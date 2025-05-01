package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoDao{
    boolean existsByNumero(Long numero);
}
