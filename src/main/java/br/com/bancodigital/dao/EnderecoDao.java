package br.com.bancodigital.dao;

import br.com.bancodigital.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoDao extends JpaRepository<Endereco, Long> {
}
