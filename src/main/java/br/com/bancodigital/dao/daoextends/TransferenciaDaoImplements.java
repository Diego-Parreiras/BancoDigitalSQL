package br.com.bancodigital.dao.daoextends;

import br.com.bancodigital.dao.interfaces.TransferenciaDao;
import br.com.bancodigital.model.Transferencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TransferenciaDaoImplements implements TransferenciaDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Transferencia save(Transferencia transferencia) {
        try {
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id_conta_origem", transferencia.getIdContaOrigem())
                    .addValue("id_conta_destino", transferencia.getIdContaDestino())
                    .addValue("valor", transferencia.getValor());
            jdbcTemplate.update(SqlUtils.SQL_TRANSFERENCIA_SAVE, sqlParameterSource);
            return transferencia;
        } catch (Exception e) {
            return null;
        }
    }
}
