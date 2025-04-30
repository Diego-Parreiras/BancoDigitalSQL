package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.Conta;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ContaRowMapper implements RowMapper<Conta> {
    @Override
    public Conta mapRow(ResultSet rs, int rowNum) throws SQLException {
      Conta conta = new Conta();
      conta.setId(rs.getLong("id"));
      conta.setAgencia(rs.getLong("agencia"));
      conta.setNumero(rs.getLong("numero"));
      conta.setSaldo(rs.getDouble("saldo"));
      return conta;
    }
}
