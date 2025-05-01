package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaDao  {
    Transferencia save(Transferencia transferencia);
}
