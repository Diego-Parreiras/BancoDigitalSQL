package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface EnderecoDao {

    void save(Endereco endereco);
}
