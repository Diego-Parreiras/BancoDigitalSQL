package br.com.bancodigital.dao;

import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaDao extends JpaRepository<Conta, Long> {

    Transferencia transferir(Long idOrigem, Long idDestino, double valor);  //metodo para transferir

    Optional<Long> buscarIdConta(String chavePix);                         //metodo para buscar id da conta por chave pix

    Optional<Long> buscarIdConta(Long agencia, Long numero);               //metodo para buscar id da conta por agencia e numero

}
