package br.com.bancodigital.model.rowmapper;

import br.com.bancodigital.model.CartaoDeDebito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class CartaoDeDebitoRowMapper implements RowMapper<CartaoDeDebito> {
    @Override
    public CartaoDeDebito mapRow(ResultSet rs, int rowNum) throws SQLException {
        CartaoDeDebito cartaoDeDebito = new CartaoDeDebito();
        cartaoDeDebito.setId(rs.getLong("id"));
        cartaoDeDebito.setNumero(rs.getLong("numero"));
        cartaoDeDebito.setAtivoOuNao(rs.getBoolean("ativo_ou_nao"));
        cartaoDeDebito.setSenha(rs.getLong("senha"));
        cartaoDeDebito.setCvv(rs.getLong("cvv"));
        return cartaoDeDebito;
    }
}
