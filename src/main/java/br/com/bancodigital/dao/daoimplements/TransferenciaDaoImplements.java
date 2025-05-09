package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.TransferenciaDao;
import br.com.bancodigital.dao.utils.SqlUtils;
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
            jdbcTemplate.update(SqlUtils.SQL_TRANSFERENCIA_SAVE,
                    transferencia.getId(),
                    transferencia.getIdContaOrigem(),
                    transferencia.getIdContaDestino(),
                    transferencia.getValor());
            return transferencia;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar transferência: " + e.getMessage());
        }
    }
}
