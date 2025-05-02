package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Cliente;
import br.com.bancodigital.model.rowmapper.ClienteRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface ClienteDao {
    boolean existsByCpf(String cpf);

    void save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    List<Cliente> findAll();

    void delete(Long id);

}


