package br.com.bancodigital.dao.daoimplements;

import br.com.bancodigital.dao.interfaces.TransferenciaDao;
import br.com.bancodigital.constantutils.SqlUtils;
import br.com.bancodigital.exception.JavaException;
import br.com.bancodigital.model.entity.Transferencia;
import br.com.bancodigital.constantutils.ServiceUtils;
import br.com.bancodigital.model.rowmapper.TransferenciaRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransferenciaDaoImplements implements TransferenciaDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransferenciaRowMapper transferenciaRowMapper ;
    @Override
    public Transferencia save(Transferencia transferencia) {
        try {
            Transferencia t = jdbcTemplate.queryForObject(SqlUtils.SQL_TRANSFERENCIA_SAVE, transferenciaRowMapper,
                    transferencia.getIdContaOrigem(),
                    transferencia.getIdContaDestino(),
                    transferencia.getValor());
            return transferencia;
        } catch (Exception e) {
            System.out.println("=== ERRO REAL: " + e.getClass().getSimpleName() + " ===");
            e.printStackTrace();
            throw new JavaException(ServiceUtils.ERRO_AO_SALVAR_TRANSFERENCIA, HttpStatus.NOT_ACCEPTABLE.value());
        }
    }
}
