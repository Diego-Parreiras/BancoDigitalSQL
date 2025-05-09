package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Cliente;
import java.util.List;
import java.util.Optional;


public interface ClienteDao {
    boolean existsByCpf(String cpf);

    void save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    List<Cliente> findAll();

    void delete(Long id);

}


