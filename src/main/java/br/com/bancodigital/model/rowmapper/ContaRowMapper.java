package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.Conta;
import br.com.bancodigital.model.enuns.TipoConta;
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
        conta.setNumero(rs.getLong("numero"));
        conta.setAgencia(rs.getLong("agencia"));
        conta.setSenha(rs.getLong("senha"));
        conta.setSaldo(rs.getDouble("saldo"));
        conta.setChavePix(rs.getString("chave-pix"));
        conta.setTipoConta(TipoConta.fromInt(rs.getInt("tipo_conta")));

        return conta;
    }
}
