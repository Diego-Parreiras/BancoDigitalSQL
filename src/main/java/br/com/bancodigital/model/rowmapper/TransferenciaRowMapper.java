package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.Transferencia;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class TransferenciaRowMapper implements RowMapper<Transferencia> {
    @Override
    public Transferencia mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transferencia transferencia = new Transferencia();
        transferencia.setId(rs.getLong("id"));
        transferencia.setIdContaOrigem(rs.getLong("id_conta_origem"));
        transferencia.setIdContaDestino(rs.getLong("id_conta_destino"));
        transferencia.setValor(rs.getDouble("valor"));
        transferencia.setDataTransferencia(rs.getTimestamp("data_transferencia").toLocalDateTime());
        return transferencia;
    }
}
