package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.CartaoDeCredito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class CartaoDeCreditoRowMapper implements RowMapper<CartaoDeCredito> {
    @Override
    public CartaoDeCredito mapRow(ResultSet rs, int rowNum) throws SQLException {
       CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(rs.getLong("id"));
        cartaoDeCredito.setNumero(rs.getLong("numero"));
        cartaoDeCredito.setAtivoOuNao(rs.getBoolean("ativo_ou_nao"));
        cartaoDeCredito.setSenha(rs.getLong("senha"));
        cartaoDeCredito.setCvv(rs.getLong("cvv"));
        cartaoDeCredito.setLimiteCredito(rs.getDouble("limite_credito"));
        cartaoDeCredito.setFatura(rs.getDouble("fatura"));
        return cartaoDeCredito;
    }
}
