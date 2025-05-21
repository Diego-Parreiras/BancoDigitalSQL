package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.TransferenciaDao;
import br.com.bancodigital.constantutils.SqlUtils;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Transferencia;
import br.com.bancodigital.constantutils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
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
            throw new JavaException(ServiceUtils.ERRO_AO_SALVAR_TRANSFERENCIA, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }
}
