package br.com.bancodigital.dao;

import br.com.bancodigital.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDao extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
}
