package br.com.bancodigital.dao.daoextends;

import br.com.bancodigital.dao.interfaces.ContaDao;
import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.rowmapper.ContaRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class ContaDaoExtends implements ContaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ContaRowMapper contaRowMapper;

    @Override
    public void save(Conta conta) {
        try{
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("numero", conta.getNumero())
                .addValue("agencia", conta.getAgencia())
                .addValue("senha", conta.getSenha())
                .addValue("saldo", conta.getSaldo())
                .addValue("id_cliente", conta.getCliente().getId())
                .addValue("chave_pix", conta.getChavePix())
                .addValue("tipo_conta", conta.getTipoConta().name());
        jdbcTemplate.update(SqlUtils.SQL_CONTA_INSERT, sqlParameterSource);
    }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
        jdbcTemplate.update(SqlUtils.SQL_CONTA_DELETE, id);
    }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Conta> findById(Long id) {
        try {
            Conta conta = jdbcTemplate.queryForObject(SqlUtils.SQL_CONTA_FIND_BY_ID, contaRowMapper, id);
            return Optional.of(conta);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Conta> findByChavePix(String chavePix) {
        try {
            Conta conta = jdbcTemplate.queryForObject(SqlUtils.SQL_CONTA_FIND_BY_PIX, contaRowMapper, chavePix);
            return Optional.of(conta);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Conta> findByAgenciaAndNumero(Long agencia, Long numero) {
        try {
            Conta conta = jdbcTemplate.queryForObject(SqlUtils.SQL_CONTA_FIND_BY_AGENCIA_NUMERO, contaRowMapper, agencia, numero);
            return Optional.of(conta);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
