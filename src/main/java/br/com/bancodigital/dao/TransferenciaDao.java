package br.com.bancodigital.dao;

import br.com.bancodigital.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaDao extends JpaRepository<Transferencia, Long> {
}
