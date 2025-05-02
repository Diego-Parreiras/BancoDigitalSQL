package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TransferenciaDao  {
    Transferencia save(Transferencia transferencia);
}
