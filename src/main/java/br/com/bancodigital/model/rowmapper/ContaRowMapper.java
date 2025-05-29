package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.entity.Conta;
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
        conta.setId(rs.getLong("id_conta"));
        conta.setAgencia(rs.getLong("agencia"));
        conta.setChavePix(rs.getString("chave_pix"));
        conta.setNumero(rs.getLong("numero"));
        conta.setSaldo(rs.getDouble("saldo"));
        conta.setSenha(rs.getLong("senha"));
        conta.setTipoConta(TipoConta.fromInt(rs.getInt("tipo_conta")));
        return conta;
    }
}
