package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.ContaDao;
import br.com.bancodigital.model.entity.Transferencia;
import br.com.bancodigital.constantutils.SqlUtils;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Cartao;
import br.com.bancodigital.model.entity.Conta;
import br.com.bancodigital.model.rowmapper.CartaoRowMapper;
import br.com.bancodigital.model.rowmapper.ContaRowMapper;
import br.com.bancodigital.constantutils.ServiceUtils;
import br.com.bancodigital.model.rowmapper.TransferenciaRowMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.rmi.server.ServerCloneException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class ContaDaoImplements implements ContaDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ContaRowMapper contaRowMapper;
    @Autowired
    private CartaoRowMapper cartaoRowMapper;
    @Autowired
    private TransferenciaRowMapper transferenciaRowMapper;

    private final Logger logger = LoggerFactory.getLogger(ContaDaoImplements.class);

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataTransferencia = LocalDateTime.now();

    @Override
    public void save(Conta conta) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_CONTA_INSERT,
                    conta.getAgencia(),
                    conta.getChavePix(),
                    conta.getNumero(),
                    conta.getSaldo(),
                    conta.getSenha(),
                    conta.getTipoConta().getValor(),
                    conta.getCliente().getId());
        } catch (Exception e) {
            throw new JavaException(ServiceUtils.ERRO_AO_SALVAR, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_CONTA_DELETE, id);
        } catch (Exception e) {
            throw new JavaException(ServiceUtils.ERRO_AO_DELETAR, HttpStatus.NOT_FOUND.value());
        }
    }

    @Override
    public Optional<Conta> findById(Long id) {
        try {
            Conta conta = jdbcTemplate.queryForObject(SqlUtils.SQL_CONTA_FIND_BY_ID, contaRowMapper, id);
            List<Cartao> listaDeCartaoes = jdbcTemplate.query(SqlUtils.SQL_CONTA_FIND_ALL_CARTOES, cartaoRowMapper, id);
            if (listaDeCartaoes.isEmpty()) {
                logger.info(ServiceUtils.NENHUM_CARTAO_ENCONTRADO);
               conta.setListaCartoes(new ArrayList<>());
               return Optional.of(conta);
            }
            conta.setListaCartoes(listaDeCartaoes);
            return Optional.of(conta);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Conta> findByChavePix(String chavePix) {
        try {
            Conta conta = jdbcTemplate.queryForObject(SqlUtils.SQL_CONTA_FIND_BY_PIX, contaRowMapper, chavePix);
            return Optional.of(conta);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Conta> findByAgenciaAndNumero(Long agencia, Long numero) {
        try {
            Conta conta = jdbcTemplate.queryForObject(SqlUtils.SQL_CONTA_FIND_BY_AGENCIA_NUMERO, contaRowMapper, agencia, numero);
            return Optional.of(conta);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void depositor(Long id, double valor) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_CONTA_DEPOSITAR,id, valor);
        } catch (Exception e) {
            throw new JavaException(ServiceUtils.NAO_FOI_POSSIVEL_REALIZAR_ACAO, HttpStatus.NOT_ACCEPTABLE.value());
        }

    }

    @Override
    public void sacar(Long id, double valor) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_CONTA_SACAR, id,valor);
        } catch (Exception e) {
            System.out.println("=== ERRO REAL: " + e.getClass().getSimpleName() + " ===");
            throw new JavaException(ServiceUtils.NAO_FOI_POSSIVEL_REALIZAR_ACAO, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @Override
    public Transferencia tranferir(Long idOrigem, Long idDestino, double valor) {
        try {
            Transferencia transferencia = jdbcTemplate.queryForObject(SqlUtils.SQL_TRANSFERENCIA_SAVE ,transferenciaRowMapper,idOrigem,idDestino,valor);
            return transferencia;
        } catch (Exception e) {
            throw new JavaException(ServiceUtils.NAO_FOI_POSSIVEL_REALIZAR_ACAO, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @Override
    public void aplicarTaxaManutencao(Long id) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_APLICAR_TAXA_MANUTENCAO,id);
        } catch (Exception e) {
            String msg = e.getCause().getMessage();
            msg = msg.replace("ERRO","" );
            throw new JavaException(ServiceUtils.NAO_FOI_POSSIVEL_REALIZAR_ACAO+msg, HttpStatus.NOT_ACCEPTABLE.value());
        }

    }

    @Override
    public void aplicarTaxaRendimento(Long id) {
        try {
            jdbcTemplate.update(SqlUtils.SQL_APLICAR_TAXA_RENDIMENTO,id);
        } catch (Exception e) {
            String msg = e.getCause().getMessage();
            msg = msg.replace("ERRO","" );
            throw new JavaException(ServiceUtils.NAO_FOI_POSSIVEL_REALIZAR_ACAO+msg, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }


}
