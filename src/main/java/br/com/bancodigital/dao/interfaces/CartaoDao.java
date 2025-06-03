package br.com.bancodigital.dao.interfaces;

import br.com.bancodigital.model.dto.PagamentoCartaoRequest;
import br.com.bancodigital.model.entity.Cartao;
import java.util.Optional;

public interface CartaoDao{

    boolean existsByNumero(Long numero);
    Optional<Cartao> findByNumero(Long numero);
    void save(Cartao cartao);
    Optional<Cartao> findById(Long id);

    void mudarStatus(Long id);

    double buscarFatura(Long id);

    void pagar(long id, PagamentoCartaoRequest pagamento);

    void pagarFatura(Long id,Long senha);

    void atualizarSenha(Long id, long senha);

    void aumentarLimiteDebito(Long id, double valor);
}
